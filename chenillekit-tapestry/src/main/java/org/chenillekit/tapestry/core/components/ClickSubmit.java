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
import org.apache.tapestry5.annotations.Mixin;
import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.annotations.SupportsInformalParameters;
import org.apache.tapestry5.corelib.mixins.RenderInformals;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.services.javascript.JavascriptSupport;

/**
 * ClickSubmit allows arbitrary page (DOM) elements to submit a form.
 *
 * @version $Id$
 */
@SupportsInformalParameters
@Import(library = {"../Chenillekit.js", "ClickSubmit.js"})
public class ClickSubmit implements ClientElement
{
	@Inject
	private ComponentResources resources;

	@Environmental
	private JavascriptSupport javascriptSupport;

	@Mixin
	private RenderInformals renderInformals;

	@Parameter(value = "prop:componentResources.elementName", defaultPrefix = BindingConstants.LITERAL)
	private String element;

	/**
	 * The client-side id.
	 */
	private String clientId;

	/**
	 * Tapestry render phase method.
	 * Initialize temporary instance variables here.
	 */
	void setupRender()
	{
		clientId = javascriptSupport.allocateClientId(resources.getId());
	}

	/**
	 * Tapestry render phase method.
	 * Start a tag here, end it in afterRender
	 *
	 * @param writer the markup writer
	 */
	void beginRender(MarkupWriter writer)
	{
		writer.element(element, "id", getClientId());
	}

	/**
	 * Tapestry render phase method. End a tag here.
	 *
	 * @param writer the markup writer
	 */
	void afterRender(MarkupWriter writer)
	{
		writer.end();
		javascriptSupport.addScript("new Ck.ClickSubmit('%s');", getClientId());
	}

	public String getClientId()
	{
		return clientId;
	}
}
