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
import org.apache.tapestry5.services.javascript.JavaScriptSupport;

/**
 * a expand-/pin-able side panel.
 *
 * @version $Id$
 */
@SupportsInformalParameters
@Import(library = {"../Chenillekit.js", "../Cookie.js", "SidePanel.js"}, stylesheet = {"SidePanel.css"})
public class SidePanel implements ClientElement
{
	/**
	 * The id used to generate a page-unique client-side identifier for the component. If a component renders multiple
	 * times, a suffix will be appended to the to id to ensure uniqueness.
	 */
	@Parameter(value = "prop:componentResources.id", defaultPrefix = BindingConstants.LITERAL)
	private String clientId;

	/**
	 * if the panel height should be dynamic, you can place
	 * here the id  of the html element, on wich deliver size
	 * to the panel.
	 */
	@Parameter(required = false, defaultPrefix = BindingConstants.LITERAL)
	private String sizeElement;

	@Environmental
	private JavaScriptSupport javascriptSupport;

	@Inject
	private ComponentResources resources;

	private String assignedClientId;

	void setupRender()
	{
		assignedClientId = javascriptSupport.allocateClientId(clientId);
	}

	void beginRender(MarkupWriter writer)
	{
		writer.element("div", "id", getClientId(), "class", "ck_sidepanel");
		resources.renderInformalParameters(writer);
		writer.element("div", "class", "ck_sidepanel-panel");
		writer.element("div", "class", "ck_sidepanel-toggler");
		writer.end();
		writer.end();
		writer.element("div", "class", "ck_sidepanel-content", "style", "display: none;");
		writer.element("div", "class", "ck_sidepanel-pinner-bar");
		writer.element("div", "class", "ck_sidepanel-pinner");
		writer.end();
		writer.end();
	}

	void afterRender(MarkupWriter writer)
	{
		writer.end();
		writer.end();
		javascriptSupport.addScript("new Ck.SidePanel('%s','%s');", getClientId(), sizeElement);
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
