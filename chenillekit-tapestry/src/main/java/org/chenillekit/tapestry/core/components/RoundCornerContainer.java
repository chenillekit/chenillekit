/*
 * Apache License
 * Version 2.0, January 2004
 * http://www.apache.org/licenses/
 *
 * Copyright 2008-2010 by chenillekit.org
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 */

package org.chenillekit.tapestry.core.components;

import org.apache.tapestry5.BindingConstants;
import org.apache.tapestry5.ClientElement;
import org.apache.tapestry5.ComponentResources;
import org.apache.tapestry5.MarkupWriter;
import org.apache.tapestry5.annotations.Environmental;
import org.apache.tapestry5.annotations.Import;
import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.annotations.SupportsInformalParameters;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.json.JSONObject;
import org.apache.tapestry5.services.javascript.JavaScriptSupport;

/**
 * displays a colored container with rounded corners without pics or any mozilla-css goodies.
 *
 * @version $Id$
 */
@SupportsInformalParameters
@Import(library = {"../Chenillekit.js", "RoundCornerContainer.js"}, stylesheet = {"RoundCornerContainer.css"})
public class RoundCornerContainer implements ClientElement
{
	public final static String RENDER_BOTH = "both";
	public final static String RENDER_TOP = "top";
	public final static String RENDER_BOTTOM = "bottom";

	/**
	 * parameter let component know, wich part of the container should rendered.
	 * possible values arr "both" by default, "top" or "bottom".
	 */
	@Parameter(value = RENDER_BOTH, required = false, defaultPrefix = BindingConstants.LITERAL)
	private String renderPart;

	/**
	 * The id used to generate a page-unique client-side identifier for the component. If a component renders multiple
	 * times, a suffix will be appended to the to id to ensure uniqueness.
	 */
	@Parameter(value = "prop:componentResources.id", defaultPrefix = BindingConstants.LITERAL)
	private String clientId;

	/**
	 * the corners background color.
	 */
	@Parameter(value = "#FFFFFF", required = false, defaultPrefix = BindingConstants.LITERAL)
	private String bgcolor;

	/**
	 * the corners foreground color.
	 */
	@Parameter(value = "#9BD1FA", required = false, defaultPrefix = BindingConstants.LITERAL)
	private String fgcolor;

	/**
	 * the corners radius size (maybe "small" or "big")
	 */
	@Parameter(value = "", required = false, defaultPrefix = BindingConstants.LITERAL)
	private String size;

	@Inject
	private ComponentResources resources;

	@Environmental
	private JavaScriptSupport javascriptSupport;

	private String assignedClientId;

	void setupRender()
	{
		assignedClientId = javascriptSupport.allocateClientId(clientId);
	}

	void beginRender(MarkupWriter writer)
	{
		writer.element("div", "id", getClientId());
		resources.renderInformalParameters(writer);
	}

	void afterRender(MarkupWriter writer)
	{
		writer.end();

		JSONObject options = new JSONObject();
		options.put("clientId", getClientId());
		options.put("bgcolor", bgcolor);
		options.put("color", fgcolor);
		options.put("size", size);
		options.put("render", renderPart);

		javascriptSupport.addInitializerCall("ckroundcornercontainer", options);
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