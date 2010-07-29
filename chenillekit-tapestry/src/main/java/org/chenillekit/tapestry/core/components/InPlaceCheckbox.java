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
import org.apache.tapestry5.EventConstants;
import org.apache.tapestry5.EventContext;
import org.apache.tapestry5.Link;
import org.apache.tapestry5.MarkupWriter;
import org.apache.tapestry5.annotations.Environmental;
import org.apache.tapestry5.annotations.Import;
import org.apache.tapestry5.annotations.Mixin;
import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.annotations.SupportsInformalParameters;
import org.apache.tapestry5.corelib.mixins.DiscardBody;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.json.JSONObject;
import org.apache.tapestry5.services.javascript.JavaScriptSupport;

import java.util.List;

/**
 * a "just in place" checkbox component that dont must emmbedded in a form.
 * sends an event after click named "checkboxclicked".
 *
 * @version $Id$
 */
@SupportsInformalParameters
@Import(library = {"../Chenillekit.js", "InPlaceCheckbox.js"})
public class InPlaceCheckbox implements ClientElement
{
	public static final String EVENT_NAME = "clicked";

	/**
	 * If true, then the field will render out with a disabled attribute (to turn off client-side
	 * behavior). Further, a disabled field ignores any value in the request when the form is
	 * submitted.
	 */
	@Parameter(value = "false", defaultPrefix = BindingConstants.PROP)
	private boolean disabled;

	/**
	 * The id used to generate a page-unique client-side identifier for the component. If a
	 * component renders multiple times, a suffix will be appended to the to id to ensure
	 * uniqueness. The uniqued value may be accessed via the
	 * {@link #getClientId() clientId property}.
	 */
	@Parameter(value = "prop:componentResources.id", defaultPrefix = BindingConstants.LITERAL)
	private String clientId;

	/**
	 * The value to read or update.
	 */
	@Parameter(required = true, defaultPrefix = BindingConstants.PROP, allowNull = false)
	private boolean value;

	/**
	 * the javascript callback function (optional).
	 * function has one parameter: the response text
	 */
	@Parameter(required = false, defaultPrefix = BindingConstants.LITERAL)
	private String onCompleteCallback;

	/**
	 * the javascript callback function (optional).
	 * function has one parameter: the response text
	 */
	@Parameter(required = false, defaultPrefix = BindingConstants.PROP)
	private List<?> context;

	@Mixin
	private DiscardBody discardBody;

	/**
	 * For blocks, messages, crete actionlink, trigger event.
	 */
	@Inject
	private ComponentResources resources;

	/**
	 * RenderSupport to get unique client side id.
	 */
	@Environmental
	private JavaScriptSupport javascriptSupport;

	private String assignedClientId;

	private Object[] contextArray;

	void setupRender()
	{
		assignedClientId = javascriptSupport.allocateClientId(clientId);
		contextArray = context == null ? new Object[0] : context.toArray();
	}

	/**
	 * Tapestry render phase method.
	 * Start a tag here, end it in afterRender
	 *
	 * @param writer the markup writer
	 */
	void beginRender(MarkupWriter writer)
	{
		writer.element("input",
					   "type", "checkbox",
					   "id", getClientId(),
					   "checked", value ? "checked" : null);

		resources.renderInformalParameters(writer);
	}

	/**
	 * Tapestry render phase method. End a tag here.
	 *
	 * @param writer the markup writer
	 */
	void afterRender(MarkupWriter writer)
	{
		writer.end(); // input

		Link link = resources.createEventLink(EventConstants.ACTION, contextArray);
		String ajaxString = "new Ck.InPlaceCheckbox('%s', '%s'";

		if (onCompleteCallback != null)
			ajaxString += ",'" + onCompleteCallback + "'";

		ajaxString += ");";

		javascriptSupport.addScript(ajaxString, getClientId(), link.toAbsoluteURI());
	}

	JSONObject onAction(EventContext context)
	{
		Object[] eventContextArray = new Object[context.getCount()];
		for (int x = 0; x < context.getCount(); x++)
		{
			if (x < context.getCount() - 1)
				eventContextArray[x] = context.get(String.class, x);
			else
				eventContextArray[x] = context.get(Boolean.class, x);
		}

		resources.triggerEvent(EVENT_NAME, eventContextArray, null);

		return new JSONObject().put("value", eventContextArray[context.getCount() - 1]);
	}

	/**
	 * Returns a unique id for the element. This value will be unique for any given rendering of a page. This value is
	 * intended for use as the id attribute of the client-side element, and will be used with any DHTML/Ajax related
	 * JavaScript.
	 */
	public String getClientId()
	{
		return assignedClientId;
	}
}