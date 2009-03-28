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
	 * 
	 * @param ckAccessId
	 * @param params
	 */
	public void rememberPageRenderParameter(String ckAccessId, PageRenderRequestParameters params);
	
	/**
	 * 
	 * @param ckAccessId
	 * @return
	 */
	public PageRenderRequestParameters removePageRenderParamter(String ckAccessId);
	
	/**
	 * 
	 * @param chAccessId
	 * @param params
	 */
	public void rememberComponentEventParameter(String ckAccessId, ComponentEventRequestParameters params);
	
	/**
	 * 
	 * @param ckAccessId
	 * @return
	 */
	public ComponentEventRequestParameters removeComponentEventParamter(String ckAccessId);
}
