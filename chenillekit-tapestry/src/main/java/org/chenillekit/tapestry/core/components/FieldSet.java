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
import org.apache.tapestry5.ComponentResources;
import org.apache.tapestry5.MarkupWriter;
import org.apache.tapestry5.annotations.Environmental;
import org.apache.tapestry5.annotations.Import;
import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.annotations.SupportsInformalParameters;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.services.javascript.JavascriptSupport;

/**
 * closeable fieldset.
 *
 * @version $Id$
 */
@SupportsInformalParameters
@Import(library = {"../Chenillekit.js", "FieldSet.js"}, stylesheet = {"FieldSet.css"})
public class FieldSet
{
	/**
	 * let the Fieldset initialy displayed as closed/open,
	 */
	@Parameter(value = "false", required = false)
	private boolean closed;

	/**
	 * The id used to generate a page-unique client-side identifier for the component. If a component renders multiple
	 * times, a suffix will be appended to the to id to ensure uniqueness. The uniqued value may be accessed via the
	 * {@link #getClientId() clientId property}.
	 */
	@Parameter(value = "prop:componentResources.id", defaultPrefix = BindingConstants.LITERAL)
	private String clientId;

	private String assignedClientId;

	@Environmental
	private JavascriptSupport javascriptSupport;

	@Inject
	private ComponentResources resources;

	void setupRender()
	{
		// By default, use the component id as the (base) client id. If the clientid
		// parameter is bound, then that is the value to use.
		// Often, these controlName and _clientId will end up as the same value. There are many
		// exceptions, including a form that renders inside a loop, or a form inside a component
		// that is used multiple times.
		assignedClientId = javascriptSupport.allocateClientId(clientId);
	}

	void beginRender(MarkupWriter writer)
	{
		writer.element("fieldset", "id", getClientId(), "class", "ck_fieldset");
		resources.renderInformalParameters(writer);
	}

	void afterRender(MarkupWriter writer)
	{
		writer.end();

		javascriptSupport.addScript("var %s = new Ck.FieldSet('%s', %s);", getClientId(), getClientId(), closed);
	}

	public String getClientId()
	{
		return assignedClientId;
	}
}
