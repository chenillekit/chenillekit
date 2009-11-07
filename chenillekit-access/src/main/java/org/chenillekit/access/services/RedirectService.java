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

import java.io.IOException;

import org.apache.tapestry5.EventContext;
import org.apache.tapestry5.services.ComponentEventRequestParameters;
import org.apache.tapestry5.services.PageRenderRequestParameters;
import org.apache.tapestry5.services.Response;

/**
* Service used to redirect incoming requests where they belong and they
* can have access.
* 
* @version $Id: AuthenticationService.java 380 2008-12-30 10:21:52Z mlusetti $
*/
public interface RedirectService
{
	/**
	 * Redirect the user to the specified page with the specified context
	 * 
	 * @param pageName the logical page name where to redirect the user 
	 * @param context the context associated
	 * @throws IOException exception thrown during response redirect (eventually)
	 */
	public void redirectTo(String pageName, EventContext context) throws IOException;
	
	/**
	 * Remember the {@link PageRenderRequestParameters} to use in a future
	 * {@link Response} redirect after an eventually successful login.
	 * 
	 * @param params the {@link PageRenderRequestParameters} to remember
	 */
	public void rememberPageRenderParameter(PageRenderRequestParameters params);
	
	/**
	 * 
	 * @param params
	 */
	public void rememberComponentEventParameters(ComponentEventRequestParameters params);
}
