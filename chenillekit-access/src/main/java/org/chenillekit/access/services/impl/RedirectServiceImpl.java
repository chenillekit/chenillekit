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

/**
 *
 */
package org.chenillekit.access.services.impl;

import java.io.IOException;

import org.apache.tapestry5.EventContext;
import org.apache.tapestry5.services.ComponentEventRequestParameters;
import org.apache.tapestry5.services.ContextPathEncoder;
import org.apache.tapestry5.services.Cookies;
import org.apache.tapestry5.services.PageRenderRequestParameters;
import org.apache.tapestry5.services.Response;

import org.chenillekit.access.ChenilleKitAccessConstants;
import org.chenillekit.access.internal.ChenillekitAccessInternalUtils;
import org.chenillekit.access.services.RedirectService;
import org.slf4j.Logger;

/**
 * @author massimo
 */
public class RedirectServiceImpl implements RedirectService
{
    private final Cookies cookies;

    private final Logger logger;
    private final ContextPathEncoder contextPathEncoder;

    private final Response response;

    /**
     * @param contextPathEncoder
     * @param response
     */
    public RedirectServiceImpl(Logger logger, Cookies cookies, ContextPathEncoder contextPathEncoder, Response response)
    {
        this.logger = logger;
        this.contextPathEncoder = contextPathEncoder;
        this.response = response;
        this.cookies = cookies;
    }

    /* (non-Javadoc)
      * @see org.chenillekit.access.services.RedirectService#redirectTo(java.lang.String, org.apache.tapestry5.EventContext)
      */

    public void redirectTo(String pageName, EventContext context) throws IOException
    {
        String contextPath = contextPathEncoder.encodeIntoPath(context);

        String redirectURL = response.encodeRedirectURL(String.format("%s/%s", pageName, contextPath));
		if (logger.isDebugEnabled())
			logger.debug("redirect to: {}", redirectURL);

        response.sendRedirect(redirectURL);
    }

    /*
      * (non-Javadoc)
      * @see org.chenillekit.access.services.RedirectService#putPageRenderParameter(java.lang.String, org.apache.tapestry5.services.PageRenderRequestParameters)
      */

    public void rememberPageRenderParameter(PageRenderRequestParameters params)
    {
        String successfulLogin = cookies.readCookieValue(ChenilleKitAccessConstants.LOGIN_SUCCESSFUL_COOKIE_NAME);

        if (successfulLogin == null || !successfulLogin.equals(ChenilleKitAccessConstants.LOGIN_SUCCESSFUL_COOKIE_NAME_KO))
        {
            cookies.writeCookieValue(ChenilleKitAccessConstants.REMEMBERED_ACTIVE_PAGE_NAME,
                    params.getLogicalPageName());
            cookies.writeCookieValue(ChenilleKitAccessConstants.REMEMBERED_ACTIVATION_CONTEXT,
                    ChenillekitAccessInternalUtils.getContextAsString(params.getActivationContext()));
            cookies.writeCookieValue(ChenilleKitAccessConstants.REMEMBERED_PARAMS_TYPE,
                    ChenilleKitAccessConstants.REMEMBERED_PARAMS_TYPE_PAGERENDER_VALUE);
        }
    }

    /* (non-Javadoc)
      * @see org.chenillekit.access.services.RedirectService#rememberComponentEventParameters(org.apache.tapestry5.services.ComponentEventRequestParameters)
      */

    public void rememberComponentEventParameters(
            ComponentEventRequestParameters params)
    {
        String successfulLogin = cookies.readCookieValue(ChenilleKitAccessConstants.LOGIN_SUCCESSFUL_COOKIE_NAME);

        if (successfulLogin == null || !successfulLogin.equals(ChenilleKitAccessConstants.LOGIN_SUCCESSFUL_COOKIE_NAME_KO))
        {
            if (logger.isDebugEnabled())
                logger.debug("{}/{}/{}/{}/{}/{}", new Object[]{params.getActivePageName(), params.getContainingPageName(),
                        params.getEventType(), params.getNestedComponentId(),
                        ChenillekitAccessInternalUtils.getContextAsString(params.getPageActivationContext()),
                        ChenillekitAccessInternalUtils.getContextAsString(params.getEventContext())});

            cookies.writeCookieValue(ChenilleKitAccessConstants.REMEMBERED_ACTIVE_PAGE_NAME,
                    params.getActivePageName());
            cookies.writeCookieValue(ChenilleKitAccessConstants.REMEMBERED_CONTAINING_PAGE_NAME,
                    params.getContainingPageName());
            cookies.writeCookieValue(ChenilleKitAccessConstants.REMEMBERED_EVENT_TYPE,
                    params.getEventType());
            cookies.writeCookieValue(ChenilleKitAccessConstants.REMEMBERED_NESTED_COMPONENT_ID,
                    params.getNestedComponentId());
            cookies.writeCookieValue(ChenilleKitAccessConstants.REMEMBERED_ACTIVATION_CONTEXT,
                    ChenillekitAccessInternalUtils.getContextAsString(params.getPageActivationContext()));
            cookies.writeCookieValue(ChenilleKitAccessConstants.REMEMBERED_EVENT_CONTEXT,
                    ChenillekitAccessInternalUtils.getContextAsString(params.getEventContext()));
            cookies.writeCookieValue(ChenilleKitAccessConstants.REMEMBERED_PARAMS_TYPE,
                    ChenilleKitAccessConstants.REMEMBERED_PARAMS_TYPE_ACTIONEVENT_VALUE);
        }
    }

    /**
     * send a http response code to the user.
     *
     * @param httpStatusCode the http response code
     * @param message        the respons message
     * @throws java.io.IOException exception thrown during response redirect (eventually)
     */
    public void sendHttpStatusCode(int httpStatusCode, String message) throws IOException
    {
        response.sendError(httpStatusCode, message);
    }
}
