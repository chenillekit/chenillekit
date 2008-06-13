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

package org.chenillekit.secure.services.impl;

import java.io.IOException;

import org.apache.tapestry5.runtime.Component;
import org.apache.tapestry5.services.ApplicationStateManager;
import org.apache.tapestry5.services.ComponentClassResolver;
import org.apache.tapestry5.services.ComponentSource;
import org.apache.tapestry5.services.Dispatcher;
import org.apache.tapestry5.services.Request;
import org.apache.tapestry5.services.Response;

import org.chenillekit.secure.annotations.SecuredPage;
import org.chenillekit.secure.services.AuthService;

/**
 * @author <a href="mailto:homburgs@gmail.com">S.Homburg</a>
 * @version $Id$
 */
public class AccessController implements Dispatcher
{
    private final static String LOGIN_PAGE = "/Start";

    private ApplicationStateManager stateManager;
    private final ComponentClassResolver resolver;
    private final ComponentSource componentSource;
    private final AuthService authService;

    public AccessController(ApplicationStateManager stateManager,
                            ComponentClassResolver resolver,
                            ComponentSource componentSource,
                            AuthService authService)
    {
        this.stateManager = stateManager;
        this.resolver = resolver;
        this.componentSource = componentSource;
        this.authService = authService;
    }

    /**
     * Analyzes the incoming request and performs an appropriate operation for each.
     *
     * @param request  the request object
     * @param response the response object
     *
     * @return true if a response was delivered, false if the servlet container should be allowed to handle the request
     */
    public boolean dispatch(Request request, Response response) throws IOException
    {
        /**
         * We need to get the Tapestry page requested by the user.
         * So we parse the path extracted from the request
         */
        String path = request.getPath();
        if (path.equals(""))
            return false;

        int nextslashx = path.length();
        String pageName;

        while (true)
        {
            pageName = path.substring(1, nextslashx);
            if (!pageName.endsWith("/") && resolver.isPageName(pageName))
                break;
            nextslashx = path.lastIndexOf('/', nextslashx - 1);
            if (nextslashx <= 1)
                return false;
        }

        return checkAccess(pageName, request, response);
    }

    /**
     * Check the rights of the user for the page requested
     *
     * @param pageName name of the page
     * @param request  the request object
     * @param response the response object
     *
     * @return if true then leave the chain
     *
     * @throws java.io.IOException
     */
    private boolean checkAccess(String pageName, Request request, Response response) throws IOException
    {
        boolean hasAccess = true;

        /**
         * Is the requested page private ?
         */
        Component page = componentSource.getPage(pageName);
        Boolean privatePage = page.getClass().isAnnotationPresent(SecuredPage.class);

        if (privatePage)
        {
            hasAccess = false;

            /**
             * Is the user already authentified ?
             */
            // TODO: some more brain stuff
        }

        /**
         * This page can't be requested by a non authentified user => we redirect him on the signon page
         */
        if (!hasAccess)
        {
            response.sendRedirect(request.getContextPath() + LOGIN_PAGE);
            return true; //Make sure to leave the chain
        }

        return false;
    }
}
