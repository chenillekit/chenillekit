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
import org.apache.tapestry5.services.ApplicationStateManager;
import org.apache.tapestry5.services.ComponentEventRequestParameters;
import org.apache.tapestry5.services.ComponentRequestFilter;
import org.apache.tapestry5.services.ComponentRequestHandler;
import org.apache.tapestry5.services.PageRenderRequestParameters;
import org.chenillekit.access.ChenilleKitAccessConstants;
import org.chenillekit.access.services.AccessValidator;
import org.chenillekit.access.services.RedirectService;
import org.slf4j.Logger;

/**
 * It the main responsable for checking every incoming request, beeing for a
 * page render or a component action, to grant the current user has rights to
 * access the reuqested resource.
 * 
 * @version $Id: AccessValidatorImpl.java 380 2008-12-30 10:21:52Z mlusetti $
 */
public class ComponentRequestAccessFilter implements ComponentRequestFilter
{
	private final Logger logger;
	
	private final AccessValidator accessValidator;
	
	private final String loginPage;
	
	private final RedirectService redirect;
		
	private final PageRenderRequestParameters loginPageRenderParameters;
	private final ComponentEventRequestParameters loginComponentEventParameters;

	/**
	 * 
	 * @param accessValidator
	 * @param symbols
	 * @param logger
	 * @param valueEncoder
	 * @param cookies
	 */
	public ComponentRequestAccessFilter(AccessValidator accessValidator,
			SymbolSource symbols, Logger logger, RedirectService redirect,ApplicationStateManager stateManager)
	{
		this.logger = logger;
		this.accessValidator = accessValidator;
		this.loginPage = symbols.valueForSymbol(ChenilleKitAccessConstants.LOGIN_PAGE);
		this.redirect = redirect;
		
		this.loginPageRenderParameters = new PageRenderRequestParameters(this.loginPage,
												new EmptyEventContext());
		
		this.loginComponentEventParameters = new ComponentEventRequestParameters(this.loginPage,
												this.loginPage, "", EventConstants.ACTIVATE,
												new EmptyEventContext(),new EmptyEventContext());
	}
	
	/* (non-Javadoc)
	 * @see org.apache.tapestry5.services.ComponentRequestFilter#handleComponentEvent(org.apache.tapestry5.services.ComponentEventRequestParameters, org.apache.tapestry5.services.ComponentRequestHandler)
	 */
	public void handleComponentEvent(ComponentEventRequestParameters parameters,
			ComponentRequestHandler handler) throws IOException
	{
		if ( accessValidator.hasAccess(parameters.getActivePageName(),
				parameters.getNestedComponentId(), parameters.getEventType()) )
		{
			handler.handleComponentEvent(parameters);
		}
		else
		{
			if (logger.isDebugEnabled())
				logger.debug("User hasn't rights to access " +  parameters.getEventType()
						+ " event on " + parameters.getActivePageName()  + " page");
			
			redirect.rememberComponentEventParameters(parameters);
			
			handler.handleComponentEvent(loginComponentEventParameters);
		}
	}

	/* (non-Javadoc)
	 * @see org.apache.tapestry5.services.ComponentRequestFilter#handlePageRender(org.apache.tapestry5.services.PageRenderRequestParameters, org.apache.tapestry5.services.ComponentRequestHandler)
	 */
	public void handlePageRender(PageRenderRequestParameters parameters,
			ComponentRequestHandler handler) throws IOException
	{
		if ( accessValidator.hasAccess(parameters.getLogicalPageName(), null, null) )
		{
			handler.handlePageRender(parameters);
		}
		else
		{
			if (logger.isDebugEnabled())
				logger.debug("User hasn't rights to access " + parameters.getLogicalPageName()  + " page");
			
			redirect.rememberPageRenderParameter(parameters);
			
			handler.handlePageRender(loginPageRenderParameters);
		}
	}

}
