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

import org.apache.tapestry5.EventContext;
import org.apache.tapestry5.ioc.services.TypeCoercer;
import org.apache.tapestry5.services.ComponentEventRequestParameters;
import org.apache.tapestry5.services.ComponentRequestFilter;
import org.apache.tapestry5.services.ComponentRequestHandler;
import org.apache.tapestry5.services.Cookies;
import org.apache.tapestry5.services.PageRenderRequestParameters;
import org.chenillekit.access.ChenilleKitAccessConstants;
import org.chenillekit.access.internal.ChenillekitAccessInternalUtils;
import org.chenillekit.access.services.RedirectService;

/**
* Filter to catch request coming in after a successfull login which should
* be redirected to the actual request made before the login request.
* 
* @version $Id$
*/
public class CookieRedirectAccessFilter implements ComponentRequestFilter
{
	private final Cookies cookies;
	
	private final RedirectService redirect;
	
	private final TypeCoercer coercer;
	
	/**
	 * Default main construction with injection fields.
	 * 
	 * @param cookies {@link Cookies} services from Tapestry5
	 * @param response {@link Response} shadow servide from Tapestry5
	 * @param redirect {@link RedirectService} to apply redirections
	 */
	public CookieRedirectAccessFilter(Cookies cookies,
				RedirectService redirect, TypeCoercer coercer)
	{
		this.cookies = cookies;
		this.redirect = redirect;
		this.coercer = coercer;
	}
	
	/**
	 * Remove all the cookies used
	 */
	private void removeAllCookies()
	{
		cookies.removeCookieValue(ChenilleKitAccessConstants.REMEMBERED_ACTIVATION_CONTEXT);
		cookies.removeCookieValue(ChenilleKitAccessConstants.REMEMBERED_ACTIVE_PAGE_NAME);
		cookies.removeCookieValue(ChenilleKitAccessConstants.REMEMBERED_CONTAINING_PAGE_NAME);
		cookies.removeCookieValue(ChenilleKitAccessConstants.REMEMBERED_EVENT_CONTEXT);
		cookies.removeCookieValue(ChenilleKitAccessConstants.REMEMBERED_EVENT_TYPE);
		cookies.removeCookieValue(ChenilleKitAccessConstants.REMEMBERED_NESTED_COMPONENT_ID);
		cookies.removeCookieValue(ChenilleKitAccessConstants.REMEMBERED_PARAMS_TYPE);
	}
	
	/**
	 * 
	 * @return
	 * @throws IOException
	 */
	private boolean handleRememberedParameters() throws IOException
	{
		String rememberedType = cookies.readCookieValue(ChenilleKitAccessConstants.REMEMBERED_PARAMS_TYPE);
		
		if (rememberedType == null)
			return false;
		
		String activePageName = cookies.readCookieValue(ChenilleKitAccessConstants.REMEMBERED_ACTIVE_PAGE_NAME);
		String containingPageName = cookies.readCookieValue(ChenilleKitAccessConstants.REMEMBERED_CONTAINING_PAGE_NAME);
		String eventType = cookies.readCookieValue(ChenilleKitAccessConstants.REMEMBERED_EVENT_TYPE);
		String nestedComponentId = cookies.readCookieValue(ChenilleKitAccessConstants.REMEMBERED_NESTED_COMPONENT_ID);
		String activationContextString = cookies.readCookieValue(ChenilleKitAccessConstants.REMEMBERED_ACTIVATION_CONTEXT);
		String eventContextString = cookies.readCookieValue(ChenilleKitAccessConstants.REMEMBERED_EVENT_CONTEXT);
		
		EventContext activationContext = ChenillekitAccessInternalUtils.getContextFromString(coercer, activationContextString);
		EventContext eventContext = ChenillekitAccessInternalUtils.getContextFromString(coercer, eventContextString);
		
		removeAllCookies();
		
		if (rememberedType.equals(ChenilleKitAccessConstants.REMEMBERED_PARAMS_TYPE_PAGERENDER_VALUE))
		{
			redirect.redirectTo(activePageName, activationContext);
		}
		else if (rememberedType.equals(ChenilleKitAccessConstants.REMEMBERED_PARAMS_TYPE_ACTIONEVENT_VALUE))
		{
			// XXX This one is missing stuff and is not completely right!
			redirect.redirectTo(activePageName, eventContext);
		}
		else
		{
			throw new IllegalStateException("Remembered request type unknown: " + rememberedType);
		}
		
		
		
		return true;
	}

	/* (non-Javadoc)
	 * @see org.apache.tapestry5.services.ComponentRequestFilter#handleComponentEvent(org.apache.tapestry5.services.ComponentEventRequestParameters, org.apache.tapestry5.services.ComponentRequestHandler)
	 */
	public void handleComponentEvent(
			ComponentEventRequestParameters parameters,
			ComponentRequestHandler handler) throws IOException
	{
		// We don't need to interfere here since we do redirect just after a successful
		// login from our own component which after all do a page render redirect
		handler.handleComponentEvent(parameters);
	}

	/* (non-Javadoc)
	 * @see org.apache.tapestry5.services.ComponentRequestFilter#handlePageRender(org.apache.tapestry5.services.PageRenderRequestParameters, org.apache.tapestry5.services.ComponentRequestHandler)
	 */
	public void handlePageRender(PageRenderRequestParameters parameters,
			ComponentRequestHandler handler) throws IOException
	{
		String successfulLogin = cookies.readCookieValue(ChenilleKitAccessConstants.LOGIN_SUCCESSFUL_COOKIE_NAME);
		
		if (successfulLogin != null && successfulLogin.equals(ChenilleKitAccessConstants.LOGIN_SUCCESSFUL_COOKIE_NAME_OK))
		{
			// We have just done a successfull login
			cookies.removeCookieValue(ChenilleKitAccessConstants.LOGIN_SUCCESSFUL_COOKIE_NAME);
			
			if ( !handleRememberedParameters() )
			{
				// We don't have the hook for stored parameters so proceed...
				handler.handlePageRender(parameters);
			}
		}
		else
		{
			// Normally we should continue down the pipeline...
			handler.handlePageRender(parameters);
		}
	}

}
