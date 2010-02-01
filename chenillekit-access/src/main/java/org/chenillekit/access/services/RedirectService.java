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

package org.chenillekit.access.services;

import org.apache.tapestry5.EventContext;
import org.apache.tapestry5.services.ComponentEventRequestParameters;
import org.apache.tapestry5.services.PageRenderRequestParameters;
import org.apache.tapestry5.services.Response;

import java.io.IOException;

/**
 * Service used to redirect incoming requests where they belong and they
 * can have access.
 *
 * @version $Id$
 */
public interface RedirectService
{
    /**
     * Redirect the user to the specified page with the specified context
     *
     * @param pageName the logical page name where to redirect the user
     * @param context  the context associated
     * @throws IOException exception thrown during response redirect (eventually)
     */
    public void redirectTo(String pageName, EventContext context) throws IOException;

    /**
     * send a http response code to the user.
     *
     * @param httpStatusCode the http response code
     * @param message        the respons message
     * @throws IOException exception thrown during response redirect (eventually)
     */
    public void sendHttpStatusCode(int httpStatusCode, String message) throws IOException;

    /**
     * Remember the {@link PageRenderRequestParameters} to use in a future
     * {@link Response} redirect after an eventually successful login.
     *
     * @param params the {@link PageRenderRequestParameters} to remember
     */
    public void rememberPageRenderParameter(PageRenderRequestParameters params);

    /**
     * @param params
     */
    public void rememberComponentEventParameters(ComponentEventRequestParameters params);
}
