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

package org.chenillekit.tapestry.core.base;

import java.util.List;

import org.apache.tapestry5.ClientElement;
import org.apache.tapestry5.ComponentEventCallback;
import org.apache.tapestry5.ComponentResources;
import org.apache.tapestry5.Link;
import org.apache.tapestry5.annotations.Environmental;
import org.apache.tapestry5.annotations.Import;
import org.apache.tapestry5.annotations.InjectContainer;
import org.apache.tapestry5.annotations.MixinAfter;
import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.internal.util.Holder;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.services.Request;
import org.apache.tapestry5.services.javascript.JavascriptSupport;

/**
 * @version $Id$
 */
@Import(library = {"../Chenillekit.js", "../components/CkOnEvents.js"})
@MixinAfter
abstract public class AbstractEventMixin implements EventMixin
{
	private static String EVENT_NAME = "internalEvent";

	private static String PARAM_NAME = "value";

	@Inject
	private Request request;

	@Inject
	private ComponentResources resources;

	@InjectContainer
	private ClientElement clientElement;

	/**
	 * the javascript callback function (optional).
	 * function has one parameter: the response text
	 */
	@Parameter(required = false, defaultPrefix = "literal")
	private String onCompleteCallback;

	/**
	 * <a href="http://www.prototypejs.org/api/event/stop">Event.stop</a>
	 */
	@Parameter(required = false, defaultPrefix = "literal")
	private boolean stop;

	/**
	 * The context for the link (optional parameter). This list of values will be converted into strings and included in
	 * the URI. The strings will be coerced back to whatever their values are and made available to event handler
	 * methods.
	 */
	@Parameter
	private List<?> context;

	@Environmental
	private JavascriptSupport javascriptSupport;

	private Object[] contextArray;

	/**
	 * get the conext parameter(s)
	 *
	 * @return conext parameter(s)
	 */
	public List<?> getContext()
	{
		return context;
	}

	void setupRender()
	{
		contextArray = context == null ? new Object[0] : context.toArray();
	}

	void afterRender()
	{
		Link link = resources.createEventLink(EVENT_NAME, contextArray);
		String id = clientElement.getClientId();

		String jsString = "new Ck.OnEvent('%s', '%s', %b, '%s', '%s');";
		String callBackString = resources.isBound("onCompleteCallback") ? onCompleteCallback : "";
		boolean doStop = resources.isBound("stop") && stop;

		javascriptSupport.addScript(jsString, getEventName(), id, doStop, link.toAbsoluteURI(), callBackString);
	}

	Object onInternalEvent()
	{
		String input = request.getParameter(PARAM_NAME);

		final Holder<Object> valueHolder = Holder.create();

		ComponentEventCallback callback = new ComponentEventCallback<Object>()
		{
			public boolean handleResult(Object result)
			{
				valueHolder.put(result);
				return true;
			}
		};

		resources.triggerEvent(getEventName(), new Object[]{input}, callback);

		return valueHolder.get();
	}
}
