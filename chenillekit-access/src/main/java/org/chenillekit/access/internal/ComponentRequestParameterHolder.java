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

package org.chenillekit.access.internal;

import org.apache.tapestry5.EventContext;
import org.apache.tapestry5.services.ComponentEventRequestParameters;
import org.apache.tapestry5.services.PageRenderRequestParameters;

/**
* Simple wrapper around {@link PageRenderRequestParameters} and {@link ComponentEventRequestParameters}.
* It let you store either one of the two and give you same method's signature to
* retrieve the page name and the context 
* 
* @version $Id: AuthenticationService.java 380 2008-12-30 10:21:52Z mlusetti $
*/
public final class ComponentRequestParameterHolder
{
	private final ComponentEventRequestParameters componentEvent;
	private final PageRenderRequestParameters pageRender;
	
	public ComponentRequestParameterHolder(PageRenderRequestParameters pageRender)
	{
		this.componentEvent = null;
		this.pageRender = pageRender;
	}
	
	public ComponentRequestParameterHolder(ComponentEventRequestParameters componentEvent)
	{
		this.componentEvent = componentEvent;
		this.pageRender = null;
	}
	
	public String getPageName()
	{
		return componentEvent != null ?
					componentEvent.getActivePageName() :
					pageRender.getLogicalPageName();
	}
	
	public EventContext getEventContext()
	{
		return componentEvent != null ?
					componentEvent.getEventContext() :
					pageRender.getActivationContext();
	}
	
	public boolean isPageRender()
	{
		return pageRender != null ? true : false;
	}
	
	public boolean isComponentEvent()
	{
		return componentEvent != null ? true : false;
	}
}
