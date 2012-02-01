/*
 * Apache License
 * Version 2.0, January 2004
 * http://www.apache.org/licenses/
 *
 * Copyright 2008-2012 by chenillekit.org
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
import org.apache.tapestry5.EventContext;
import org.apache.tapestry5.alerts.AlertManager;
import org.apache.tapestry5.internal.EmptyEventContext;
import org.apache.tapestry5.ioc.Messages;
import org.apache.tapestry5.ioc.services.SymbolSource;
import org.apache.tapestry5.services.ApplicationStateManager;
import org.apache.tapestry5.services.ComponentEventRequestParameters;
import org.apache.tapestry5.services.ComponentRequestFilter;
import org.apache.tapestry5.services.ComponentRequestHandler;
import org.apache.tapestry5.services.PageRenderRequestParameters;

import org.chenillekit.access.ChenilleKitAccessConstants;
import org.chenillekit.access.WebSessionUser;
import org.chenillekit.access.services.AccessValidator;
import org.chenillekit.access.services.RedirectService;
import org.slf4j.Logger;

/**
 * It the main responsable for checking every incoming request, beeing for a
 * page render or a component action, to grant the current user has rights to
 * access the reuqested resource.
 *
 * @version $Id$
 */
public class ComponentRequestAccessFilter implements ComponentRequestFilter
{
	private final SymbolSource symbols;
	private final Logger logger;

	private final AccessValidator accessValidator;
	private final ApplicationStateManager stateManager;
	private final AlertManager alertManager;
	private final Messages messages;

	private final String loginPage;
	private final String fallbackPage;

	private final RedirectService redirect;

	private final PageRenderRequestParameters loginPageRenderParameters;
	private final PageRenderRequestParameters fallbackPageRenderParameters;

	private final EventContext emptyEventContext;

	/**
	 * @param accessValidator
	 * @param symbols
	 * @param logger
	 */
	public ComponentRequestAccessFilter(AccessValidator accessValidator,
										SymbolSource symbols,
										Logger logger,
										RedirectService redirect,
										ApplicationStateManager stateManager,
										AlertManager alertManager,
										Messages messages)
	{
		this.symbols = symbols;
		this.logger = logger;
		this.accessValidator = accessValidator;
		this.stateManager = stateManager;
		this.alertManager = alertManager;
		this.messages = messages;
		this.loginPage = symbols.valueForSymbol(ChenilleKitAccessConstants.LOGIN_PAGE);

		String tempFallbackPage;
		try
		{
			tempFallbackPage = symbols.valueForSymbol(ChenilleKitAccessConstants.FALLBACK_PAGE);
		}
		catch (RuntimeException ex)
		{
			if (logger.isWarnEnabled())
				logger.warn("fallback page not set [{}], user fall back to '{}' if he has no access",
						ChenilleKitAccessConstants.FALLBACK_PAGE, loginPage);

			tempFallbackPage = this.loginPage;
		}

		this.fallbackPage = tempFallbackPage;
		this.redirect = redirect;
		this.emptyEventContext = new EmptyEventContext();

		this.loginPageRenderParameters = new PageRenderRequestParameters(this.loginPage, emptyEventContext, false);
		this.fallbackPageRenderParameters = new PageRenderRequestParameters(this.fallbackPage, emptyEventContext, false);
	}

	/**
	 * @see org.apache.tapestry5.services.ComponentRequestFilter#handleComponentEvent(org.apache.tapestry5.services.ComponentEventRequestParameters, org.apache.tapestry5.services.ComponentRequestHandler)
	 */
	public void handleComponentEvent(ComponentEventRequestParameters parameters,
									 ComponentRequestHandler handler) throws IOException
	{
		boolean isUserLoggedIn = stateManager.exists(WebSessionUser.class);

		if (accessValidator.hasAccessToComponentEvent(parameters))
		{
			if (logger.isDebugEnabled())
				logger.debug("User can access event '{}' on page '{}'", parameters.getEventType(), parameters.getActivePageName());

			handler.handleComponentEvent(parameters);
		}
		else
		{
			if (logger.isDebugEnabled())
				logger.debug("User has not enough rights to access event '{}' on  page '{}'", parameters.getEventType(), parameters.getActivePageName());

			ComponentEventRequestParameters event = null;
			String accessDeniedAction = symbols.valueForSymbol(ChenilleKitAccessConstants.ACCESS_DENIED_ACTION);

			if (!isUserLoggedIn && accessDeniedAction.equalsIgnoreCase(ChenilleKitAccessConstants.JUMP_TO_LOGIN_PAGE))
			{
				redirect.rememberComponentEventParameters(parameters);
				event = new ComponentEventRequestParameters(this.fallbackPage,
						this.fallbackPage, "", EventConstants.ACTIVATE, emptyEventContext, emptyEventContext);
			}
			else if (isUserLoggedIn || accessDeniedAction.equalsIgnoreCase(ChenilleKitAccessConstants.TRIGGER_EVENT))
			{
				event = new ComponentEventRequestParameters(parameters.getActivePageName(),
						parameters.getActivePageName(), parameters.getNestedComponentId(),
						ChenilleKitAccessConstants.EVENT_NOT_ENOUGH_ACCESS_RIGHTS, emptyEventContext, emptyEventContext);
			}
			else if (accessDeniedAction.equalsIgnoreCase(ChenilleKitAccessConstants.SEND_ERROR_401))
			{
				redirect.sendHttpStatusCode(401, "the request requires authentication or more access rights!");
				return;
			}

			handler.handleComponentEvent(event);
		}
	}

	/**
	 * @see org.apache.tapestry5.services.ComponentRequestFilter#handlePageRender(org.apache.tapestry5.services.PageRenderRequestParameters, org.apache.tapestry5.services.ComponentRequestHandler)
	 */
	public void handlePageRender(PageRenderRequestParameters parameters,
								 ComponentRequestHandler handler) throws IOException
	{
		if (accessValidator.hasAccessToPageRender(parameters))
		{
			handler.handlePageRender(parameters);
		}
		else
		{
			PageRenderRequestParameters requestParameters;
			boolean isUserLoggedIn = stateManager.exists(WebSessionUser.class);

			if (logger.isDebugEnabled())
				logger.debug("User hasn't rights to access {} page", parameters.getLogicalPageName());

			redirect.rememberPageRenderParameter(parameters);

			if (isUserLoggedIn)
			{
				requestParameters = fallbackPageRenderParameters;
				alertManager.warn(messages.get("not-allowed-to-access-this-page"));
			}
			else
				requestParameters = loginPageRenderParameters;

			if (logger.isDebugEnabled())
				logger.debug("User fallback to page {}", requestParameters.getLogicalPageName());

			handler.handlePageRender(requestParameters);
		}
	}
}
