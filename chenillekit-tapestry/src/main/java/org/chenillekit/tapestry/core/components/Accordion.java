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
import org.apache.tapestry5.services.Environment;
import org.apache.tapestry5.services.javascript.JavaScriptSupport;

import java.util.List;

/**
 * accordion component.
 *
 * @version $Id$
 */
@SupportsInformalParameters
@Import(library = {"../Chenillekit.js", "Accordion.js"}, stylesheet = {"Accordion.css"})
public class Accordion implements ClientElement
{
	/**
	 * The id used to generate a page-unique client-side identifier for the component. If a component renders multiple
	 * times, a suffix will be appended to the to id to ensure uniqueness.
	 */
	@Parameter(value = "prop:componentResources.id", defaultPrefix = BindingConstants.LITERAL)
	private String clientId;

	/**
	 * array of strings to show as panels subject.
	 */
	@Parameter(required = true)
	private List<?> subjects;

	/**
	 * array of strings to show as details text.
	 */
	@Parameter(required = true)
	private List<?> details;

	/**
	 * output raw markup to the client if true.
	 */
	@Parameter(value = "false", required = false)
	private boolean renderDetailsRaw;

	/**
	 * duration of slide animation.
	 */
	@Parameter(value = "0.2", required = false)
	private String duration;

	@Inject
	private ComponentResources resources;

	@Environmental
	private JavaScriptSupport javascriptSupport;

	@Inject
	private Environment environment;

	private String assignedClientId;

	void setupRender()
	{
		assignedClientId = javascriptSupport.allocateClientId(clientId);
	}

	void beginRender(MarkupWriter writer)
	{
		writer.element("div", "id", getClientId());
		resources.renderInformalParameters(writer);


		Object[] subjectsArray = subjects.toArray();
		Object[] detailsArray = details.toArray();

		for (int i = 0; i < subjectsArray.length; i++)
		{
			String subject = subjectsArray[i].toString();
			String detail = "";
			if (detailsArray.length >= i + 1)
				detail = detailsArray[i].toString();

			writer.element("div", "id", getClientId() + "_toggle_" + i, "class", "ck_accordionToggle");
			writer.write(subject);
			writer.end();

			writer.element("div", "id", getClientId() + "_content_" + i, "class", "ck_accordionContent", "style", "display: none;");

			writer.element("div");
			if (renderDetailsRaw)
				writer.writeRaw(detail);
			else
				writer.write(detail);
			writer.end(); // overViewContent

			writer.end(); // overViewContent
		}
	}

	void afterRender(MarkupWriter writer)
	{
		writer.end(); // main div
		javascriptSupport.addScript("new Ck.Accordion('%s', {duration: %s});", getClientId(), duration);
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
