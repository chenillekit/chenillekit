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

import org.apache.tapestry5.EventConstants;
import org.apache.tapestry5.EventContext;
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

import java.io.IOException;

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

    private final String loginPage;

    private final RedirectService redirect;

    private final PageRenderRequestParameters loginPageRenderParameters;

    private final EventContext emptyEventContext;

    /**
     * @param accessValidator
     * @param symbols
     * @param logger
     */
    public ComponentRequestAccessFilter(AccessValidator accessValidator,
                                        SymbolSource symbols, Logger logger, RedirectService redirect, ApplicationStateManager stateManager)
    {
        this.symbols = symbols;
        this.logger = logger;
        this.accessValidator = accessValidator;
        this.loginPage = symbols.valueForSymbol(ChenilleKitAccessConstants.LOGIN_PAGE);
        this.redirect = redirect;
        this.emptyEventContext = new EmptyEventContext();

        this.loginPageRenderParameters = new PageRenderRequestParameters(this.loginPage, emptyEventContext, false);
    }

    /**
     * @see org.apache.tapestry5.services.ComponentRequestFilter#handleComponentEvent(org.apache.tapestry5.services.ComponentEventRequestParameters, org.apache.tapestry5.services.ComponentRequestHandler)
     */
    public void handleComponentEvent(ComponentEventRequestParameters parameters,
                                     ComponentRequestHandler handler) throws IOException
    {
        if (accessValidator.hasAccess(parameters.getActivePageName(),
                parameters.getNestedComponentId(), parameters.getEventType()))
        {
            handler.handleComponentEvent(parameters);
        }
        else
        {
            if (logger.isDebugEnabled())
                logger.debug("User hasn't rights to access " + parameters.getEventType()
                        + " event on " + parameters.getActivePageName() + " page");

            ComponentEventRequestParameters event = null;
            String accessDeniedAction = symbols.valueForSymbol(ChenilleKitAccessConstants.ACCESS_DENIED_ACTION);

            if (accessDeniedAction.equalsIgnoreCase(ChenilleKitAccessConstants.JUMP_TO_LOGIN_PAGE))
            {
                redirect.rememberComponentEventParameters(parameters);
                event = new ComponentEventRequestParameters(this.loginPage,
                        this.loginPage, "", EventConstants.ACTIVATE, emptyEventContext, emptyEventContext);
            }
            else if (accessDeniedAction.equalsIgnoreCase(ChenilleKitAccessConstants.TRIGGER_EVENT))
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
        if (accessValidator.hasAccess(parameters.getLogicalPageName(), null, null))
        {
            handler.handlePageRender(parameters);
        }
        else
        {
            if (logger.isDebugEnabled())
                logger.debug("User hasn't rights to access " + parameters.getLogicalPageName() + " page");

            redirect.rememberPageRenderParameter(parameters);

            handler.handlePageRender(loginPageRenderParameters);
        }
    }

}
