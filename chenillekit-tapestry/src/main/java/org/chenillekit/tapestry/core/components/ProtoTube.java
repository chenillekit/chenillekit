/*
 * Apache License
 * Version 2.0, January 2004
 * http://www.apache.org/licenses/
 *
 * Copyright 2008 by chenillekit.org
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 */

package org.chenillekit.tapestry.core.components;

import java.util.List;

import org.apache.tapestry5.BindingConstants;
import org.apache.tapestry5.ClientElement;
import org.apache.tapestry5.ComponentResources;
import org.apache.tapestry5.MarkupWriter;
import org.apache.tapestry5.annotations.Environmental;
import org.apache.tapestry5.annotations.IncludeJavaScriptLibrary;
import org.apache.tapestry5.annotations.IncludeStylesheet;
import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.annotations.SupportsInformalParameters;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.json.JSONObject;
import org.apache.tapestry5.services.javascript.JavascriptSupport;

import org.chenillekit.tapestry.core.utils.ProtoTubeIdHolder;

/**
 * ProtoTube can be used to embed YouTube videos and the respective thumbnails in pages of any web site.
 * <p/>
 * This component generates HTML to embed a player to show the video in other Web site pages. The component
 * may also generate HTML to display thumbnail preview of the videos and display video in a overlay viewer.
 * ProtoTube is unobtrusive: when javascript is turned off, the page still delivers its core functionality
 * and is just displayed the link to the Video page on the YouTube site.
 * <p/>
 * Would seem to be compatible with IE6, IE7, IE8, Firefox 3.09, Safari 3.2.2, Opera 9.61.
 * <p/>
 * <strong>Configuration Options</strong>
 * <ul>
 * <li>overlay: default true, if false embed the video player directly</li>
 * </ul>
 * <p/>
 * <strong>Overlay and image preview options:</strong>
 * <ul>
 * <li>duration: overlay appare/fade effect duration (default 0.5)</li>
 * <li>opacity: lightbox overlay opacity (default 0.8)</li>
 * <li>ImagePreview: show the video thumbs (default true)</li>
 * <li>imageID: choose between different image previews: value: 0 to 3 (0 = 320x240 jpg , {1,2,3} = 130x97 jpg, default 1)</li>
 * </ul>
 * <p/>
 * <strong>Player options</strong>
 * <ul>
 * <li>playerWidth: default 425px</li>
 * <li>playerHeight: default 350px</li>
 * <li>fs: 1, allow fullscreen button (default 1, 0 disallow fullscreen)</li>
 * <li>autoplay: allowed value: 1,0 (default 0)</li>
 * <li>loop: allowed value: 1,0 (default 0)</li>
 * <li>hd: Setting to 1 enables High Definition playback by default , if is available; values: 0 or 1 (default 0)</li>
 * <li>showinfo: Setting to 0 causes the player to not display information like the video title and rating before the video starts playing; values: 0 or 1 (default 1)</li>
 * <li>rel: show related video at end; values: 0 or 1 (default 1)</li>
 * </ul>
 *
 * @version $Id$
 */
@SupportsInformalParameters
@IncludeJavaScriptLibrary(value = {"prototube/prototube.js", "prototube/swfobject.js",
		"${tapestry.scriptaculous}/effects.js"})
@IncludeStylesheet(value = {"prototube/prototube.css"})
public class ProtoTube implements ClientElement
{
	/**
	 * The id used to generate a page-unique client-side identifier for the component. If a component renders multiple
	 * times, a suffix will be appended to the to id to ensure uniqueness.
	 */
	@Parameter(value = "prop:componentResources.id", defaultPrefix = BindingConstants.LITERAL)
	private String clientId;

	/**
	 * list of ProtoTubeIdHolders that contains display information for the YouTube videos.
	 */
	@Parameter(required = true, defaultPrefix = BindingConstants.PROP)
	private List<ProtoTubeIdHolder> youtubeIds;

	@Environmental
	private JavascriptSupport javascriptSupport;

	@Inject
	private ComponentResources resources;

	private String assignedClientId;

	/**
	 * Tapestry render phase method.
	 * Initialize temporary instance variables here.
	 */
	void setupRender()
	{
		assignedClientId = javascriptSupport.allocateClientId(clientId);
	}


	/**
	 * Tapestry render phase method.
	 * Start a tag here, end it in afterRender
	 *
	 * @param writer the markup writer
	 */
	void beginRender(MarkupWriter writer)
	{
		writer.element("div", "id", getClientId());
		resources.renderInformalParameters(writer);

		for (ProtoTubeIdHolder youtubeId : youtubeIds)
		{
			writer.element("a", "href", youtubeId.getId(), "title", youtubeId.getTitle());
			writer.end();
		}

		writer.end();
	}


	void afterRender(MarkupWriter writer)
	{
		JSONObject config = new JSONObject();

		config.put("hd", 1);
		config.put("showinfo", 1);

		//
		// Let subclasses do more.
		//
		configure(config);

		javascriptSupport.addScript("$$('div#%s a').each( function(el) {new ProtoTube(el, %s);});", getClientId(), config);
	}

	/**
	 * Invoked to allow subclasses to further configure the parameters passed to this component's javascript
	 * options. Subclasses may override this method to configure additional features of the ProtoTube.
	 * <p/>
	 * This implementation does nothing.
	 * <p/>
	 * <strong>Configuration Options</strong>
	 * <ul>
	 * <li>overlay: default true, if false embed the video player directly</li>
	 * </ul>
	 * <p/>
	 * <strong>Overlay and image preview options:</strong>
	 * <ul>
	 * <li>duration: overlay appare/fade effect duration (default 0.5)</li>
	 * <li>opacity: lightbox overlay opacity (default 0.8)</li>
	 * <li>ImagePreview: show the video thumbs (default true)</li>
	 * <li>imageID: choose between different image previews: value: 0 to 3 (0 = 320x240 jpg , {1,2,3} = 130x97 jpg, default 1)</li>
	 * </ul>
	 * <p/>
	 * <strong>Player options</strong>
	 * <ul>
	 * <li>playerWidth: default 425px</li>
	 * <li>playerHeight: default 350px</li>
	 * <li>fs: 1, allow fullscreen button (default 1, 0 disallow fullscreen)</li>
	 * <li>autoplay: allowed value: 1,0 (default 0)</li>
	 * <li>loop: allowed value: 1,0 (default 0)</li>
	 * <li>hd: Setting to 1 enables High Definition playback by default , if is available; values: 0 or 1 (default 0)</li>
	 * <li>showinfo: Setting to 0 causes the player to not display information like the video title and rating before the video starts playing; values: 0 or 1 (default 1)</li>
	 * <li>rel: show related video at end; values: 0 or 1 (default 1)</li>
	 * </ul>
	 *
	 * @param config parameters object
	 */
	protected void configure(JSONObject config)
	{
	}

	/**
	 * Returns a unique id for the element. This value will be unique for any given rendering of a
	 * page. This value is intended for use as the id attribute of the client-side element, and will
	 * be used with any DHTML/Ajax related JavaScript.
	 */
	public String getClientId()
	{
		return assignedClientId;
	}
}