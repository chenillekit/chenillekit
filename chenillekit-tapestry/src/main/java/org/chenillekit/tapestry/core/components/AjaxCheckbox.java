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

import org.apache.tapestry5.ComponentEventCallback;
import org.apache.tapestry5.ComponentResources;
import org.apache.tapestry5.EventContext;
import org.apache.tapestry5.Link;
import org.apache.tapestry5.MarkupWriter;
import org.apache.tapestry5.annotations.Environmental;
import org.apache.tapestry5.annotations.IncludeJavaScriptLibrary;
import org.apache.tapestry5.annotations.OnEvent;
import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.corelib.components.Checkbox;
import org.apache.tapestry5.internal.util.Holder;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.services.javascript.JavascriptSupport;

/**
 * A "ajaxed" <a href="http://tapestry.apache.org/t5components/tapestry-core/component-parameters.html#orgapachetapestrycorelibcomponentscheckbox">Checkbox</a> component.
 *
 * @version $Id$
 */
@IncludeJavaScriptLibrary(value = {"../Chenillekit.js", "AjaxCheckbox.js"})
public class AjaxCheckbox extends Checkbox
{
	/**
	 * The value of this constant is {@value}.
	 */
	public static final String EVENT_NAME = "checkboxclicked";

	/**
	 * The value of this constant is {@value}.
	 */
	private static final String INTERNAL_EVENT_NAME = "internal_clicked";

	/**
	 * the javascript callback function (optional).
	 * function has the response text as parameter
	 */
	@Parameter(required = false, defaultPrefix = "literal")
	private String onCompleteCallback;

	/**
	 * The context for the link (optional parameter). This list of values will be converted into strings and included in
	 * the URI. The strings will be coerced back to whatever their values are and made available to event handler
	 * methods.
	 */
	@Parameter
	private Object[] context;

	@Inject
	private ComponentResources resources;

	/**
	 * RenderSupport to get unique client side id.
	 */
	@Environmental
	private JavascriptSupport javascriptSupport;


	/**
	 * Mixin afterRender phrase occurs after the component itself. This is where we write the &lt;div&gt;
	 * element and the JavaScript.
	 *
	 * @param writer
	 */
	void afterRender(MarkupWriter writer)
	{
		String event = INTERNAL_EVENT_NAME;
		String ajaxString = "new Ck.AjaxCheckbox('%s', '%s'";

		if (onCompleteCallback != null)
		{
			ajaxString += ",'" + onCompleteCallback + "'";
			event = EVENT_NAME;
		}

		ajaxString += ");";

		Link link = resources.createEventLink(event, context);

		javascriptSupport.addScript(ajaxString, getClientId(), link.toAbsoluteURI());
	}

	@OnEvent(value = AjaxCheckbox.INTERNAL_EVENT_NAME)
	Object checkboxClicked(EventContext context)
	{
		final Holder<Object> valueHolder = Holder.create();

		ComponentEventCallback callback = new ComponentEventCallback<Object>()
		{
			public boolean handleResult(Object result)
			{
				valueHolder.put(result);
				return true;
			}
		};

		resources.triggerContextEvent(AjaxCheckbox.EVENT_NAME, context, callback);

		return valueHolder.get();
	}
}
