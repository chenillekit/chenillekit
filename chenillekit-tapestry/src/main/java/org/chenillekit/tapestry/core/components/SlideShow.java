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
import org.apache.tapestry5.MarkupWriter;
import org.apache.tapestry5.annotations.Environmental;
import org.apache.tapestry5.annotations.Import;
import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.json.JSONObject;
import org.apache.tapestry5.services.javascript.JavascriptSupport;

/**
 * A component for creating a slide show from a generic group of HTML
 * elements on the client side.
 *
 * @version $Id$
 */
@Import(library = {"../Chenillekit.js", "SlideShow.js"}, stylesheet = "SlideShow.css")
public class SlideShow implements ClientElement
{
	/**
	 * Sets the amount of time (in seconds) each slide will be displayed.
	 */
	@Parameter(value = "2")
	private int interval;

	/**
	 * The slide transition object.
	 */
	@Parameter(defaultPrefix = BindingConstants.LITERAL, value = "Ck.SlideShow.Tx.Crossfade")
	private String transition;

	// (in development)
	@Parameter(value = "true")
	private boolean controls;

	/**
	 * Determines if the slide show will start over after displaying the final slide.
	 */
	@Parameter(value = "false")
	private boolean loop;

	/**
	 * Determines if the slide show will pause when the mouse hovers over it.
	 */
	@Parameter(value = "true")
	private boolean pauseOnHover;

	/**
	 * calculates the component size by the size of the biggest image.
	 */
	@Parameter(value = "true")
	private boolean calculateElementSize;

	@Parameter(value = "prop:componentResources.id", defaultPrefix = BindingConstants.LITERAL)
	private String clientId;

	@Environmental
	private JavascriptSupport javascriptSupport;

	private String assignedClientId;

	void setupRender()
	{
		assignedClientId = javascriptSupport.allocateClientId(clientId);
	}

	void beginRender(final MarkupWriter writer)
	{
		JSONObject jsConfig = new JSONObject();
		jsConfig.put("interval", interval);
		jsConfig.put("transition", transition);
		jsConfig.put("controls", controls);
		jsConfig.put("loop", loop);
		jsConfig.put("pauseOnHover", pauseOnHover);
		jsConfig.put("calculateElementSize", calculateElementSize);
		javascriptSupport.addScript("new Ck.SlideShow('%s', %s);", getClientId(), jsConfig);
	}

	public String getClientId()
	{
		return assignedClientId;
	}

}