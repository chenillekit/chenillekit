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
package org.chenillekit.access.services.impl;

import java.io.IOException;

import org.apache.tapestry5.EventConstants;
import org.apache.tapestry5.internal.EmptyEventContext;
import org.apache.tapestry5.ioc.services.SymbolSource;
import org.apache.tapestry5.services.ComponentEventRequestParameters;
import org.apache.tapestry5.services.ComponentRequestFilter;
import org.apache.tapestry5.services.ComponentRequestHandler;
import org.apache.tapestry5.services.ContextValueEncoder;
import org.apache.tapestry5.services.Cookies;
import org.apache.tapestry5.services.PageRenderRequestParameters;
import org.chenillekit.access.ChenilleKitAccessConstants;
import org.chenillekit.access.internal.ChenillekitAccessInternalUtils;
import org.chenillekit.access.services.AccessValidator;
import org.slf4j.Logger;

/**
 * 
 * @version $Id: AccessValidatorImpl.java 380 2008-12-30 10:21:52Z mlusetti $
 */
public class ComponentRequestAccessFilter implements ComponentRequestFilter
{
	private final Logger logger;
	private final AccessValidator accessValidator;
	private final String loginPage;
	private final ContextValueEncoder valueEncoder;

	private final Cookies cookies;
	
	public ComponentRequestAccessFilter(AccessValidator accessValidator,
			SymbolSource symbols, Logger logger,
			ContextValueEncoder valueEncoder,
			Cookies cookies)
	{
		this.logger = logger;
		this.accessValidator = accessValidator;
		this.loginPage = symbols.valueForSymbol(ChenilleKitAccessConstants.LOGIN_PAGE);
		this.cookies = cookies;
		this.valueEncoder = valueEncoder;
	}

	/**
	 * 
	 * @return
	 */
	private PageRenderRequestParameters getLoginPageParameters()
	{
		PageRenderRequestParameters parameters = new PageRenderRequestParameters(loginPage, new EmptyEventContext());

		return parameters;
	}
	
	/**
	 * 
	 * @return
	 */
	private ComponentEventRequestParameters getLoginComponentParameters()
	{
		ComponentEventRequestParameters parameters = new ComponentEventRequestParameters(loginPage, loginPage,
				"", EventConstants.ACTIVATE, new EmptyEventContext(),new EmptyEventContext());

		return parameters;
	}
	

	/* (non-Javadoc)
	 * @see org.apache.tapestry5.services.ComponentRequestFilter#handleComponentEvent(org.apache.tapestry5.services.ComponentEventRequestParameters, org.apache.tapestry5.services.ComponentRequestHandler)
	 */
	public void handleComponentEvent(ComponentEventRequestParameters parameters,
			ComponentRequestHandler handler) throws IOException
	{
		if (accessValidator.hasAccess(parameters.getActivePageName(), parameters.getNestedComponentId(), parameters.getEventType()))
			handler.handleComponentEvent(parameters);
		else
			handler.handleComponentEvent(getLoginComponentParameters());
	}

	/* (non-Javadoc)
	 * @see org.apache.tapestry5.services.ComponentRequestFilter#handlePageRender(org.apache.tapestry5.services.PageRenderRequestParameters, org.apache.tapestry5.services.ComponentRequestHandler)
	 */
	public void handlePageRender(PageRenderRequestParameters parameters,
			ComponentRequestHandler handler) throws IOException
	{
//		String previousPage = cookies.readCookieValue(ChenilleKitAccessConstants.REQUESTED_PAGENAME_COOKIE);
//		String previousContext = cookies.readCookieValue(ChenilleKitAccessConstants.REQUESTED_EVENTCONTEXT_COOKIE);
//
//		if (previousPage != null)
//		{
//			EventContext context = ChenillekitAccessInternalUtils.getContextFromString(valueEncoder, previousContext);
//			currentParameters = new PageRenderRequestParameters(previousPage, context);
//
//			cookies.removeCookieValue(ChenilleKitAccessConstants.REQUESTED_EVENTCONTEXT_COOKIE);
//			cookies.removeCookieValue(ChenilleKitAccessConstants.REQUESTED_PAGENAME_COOKIE);
//		}
//		else if ( !accessValidator.hasAccess(parameters.getLogicalPageName(), null, null) )
		if ( !accessValidator.hasAccess(parameters.getLogicalPageName(), null, null) )
		{
			if (logger.isDebugEnabled())
				logger.debug("User hasn't rights to access " + parameters.getLogicalPageName()  + " page");
			
			cookies.writeCookieValue(ChenilleKitAccessConstants.REQUESTED_PAGENAME_COOKIE, parameters.getLogicalPageName());
			cookies.writeCookieValue(ChenilleKitAccessConstants.REQUESTED_EVENTCONTEXT_COOKIE, ChenillekitAccessInternalUtils.getContextAsString((parameters.getActivationContext())));

			handler.handlePageRender(getLoginPageParameters());
		}
		else
		{
			handler.handlePageRender(parameters);
		}
	}

}
