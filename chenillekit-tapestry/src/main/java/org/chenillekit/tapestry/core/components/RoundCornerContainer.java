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

import org.apache.tapestry5.BindingConstants;
import org.apache.tapestry5.ClientElement;
import org.apache.tapestry5.ComponentResources;
import org.apache.tapestry5.MarkupWriter;
import org.apache.tapestry5.RenderSupport;
import org.apache.tapestry5.annotations.Environmental;
import org.apache.tapestry5.annotations.IncludeJavaScriptLibrary;
import org.apache.tapestry5.annotations.IncludeStylesheet;
import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.annotations.SupportsInformalParameters;
import org.apache.tapestry5.ioc.annotations.Inject;

/**
 * displays a colored container with rounded corners without pics or any mozilla-css goodies.
 *
 * @author <a href="mailto:homburgs@gmail.com">S.Homburg</a>
 * @version $Id$
 */
@SupportsInformalParameters
@IncludeJavaScriptLibrary(value = {"../Chenillekit.js", "RoundCornerContainer.js"})
@IncludeStylesheet(value = {"RoundCornerContainer.css"})
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
	private RenderSupport pageRenderSupport;

	private String _assignedClientId;

	void setupRender()
	{
		_assignedClientId = pageRenderSupport.allocateClientId(clientId);
	}

	void beginRender(MarkupWriter writer)
	{
		writer.element("div", "id", getClientId());
		resources.renderInformalParameters(writer);
	}

	void afterRender(MarkupWriter writer)
	{
		writer.end();
		pageRenderSupport.addScript("var %s = new Ck.Rounded('%s', '%s', '%s', '%s', '%s');",
									getClientId(), getClientId(), bgcolor, fgcolor, size, renderPart);
		pageRenderSupport.addScript("%s.round();", getClientId());
	}

	/**
	 * Returns a unique id for the element. This value will be unique for any given rendering of a
	 * page. This value is intended for use as the id attribute of the client-side element, and will
	 * be used with any DHTML/Ajax related JavaScript.
	 */
	public String getClientId()
	{
		return _assignedClientId;
	}
}