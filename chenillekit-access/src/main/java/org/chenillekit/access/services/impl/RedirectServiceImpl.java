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

/**
 * @author massimo
 *
 */
public class RedirectServiceImpl implements RedirectService
{
	private final Cookies cookies;
	
	private final ContextPathEncoder contextPathEncoder;
	
	private final Response response;
	
	/**
	 * 
	 * @param contextPathEncoder
	 * @param response
	 */
	public RedirectServiceImpl(Cookies cookies, ContextPathEncoder contextPathEncoder, Response response)
	{
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
		
		String redirectURL = response.encodeRedirectURL(new String(pageName + "/" + contextPath));
		
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
			cookies.writeCookieValue(ChenilleKitAccessConstants.REMEMBERED_ACTIVE_PAGE_NAME,
						params.getActivePageName());
			System.out.println("@@@@");
			System.out.println(params.getActivePageName());
			cookies.writeCookieValue(ChenilleKitAccessConstants.REMEMBERED_CONTAINING_PAGE_NAME,
					params.getContainingPageName());
			System.out.println("@@@@");
			System.out.println(params.getContainingPageName());
			cookies.writeCookieValue(ChenilleKitAccessConstants.REMEMBERED_EVENT_TYPE,
					params.getEventType());
			System.out.println("@@@@");
			System.out.println(params.getEventType());
			cookies.writeCookieValue(ChenilleKitAccessConstants.REMEMBERED_NESTED_COMPONENT_ID,
					params.getNestedComponentId());
			System.out.println("@@@@");
			System.out.println(params.getNestedComponentId());
			cookies.writeCookieValue(ChenilleKitAccessConstants.REMEMBERED_ACTIVATION_CONTEXT,
						ChenillekitAccessInternalUtils.getContextAsString(params.getPageActivationContext()));
			System.out.println("@@@@");
			System.out.println(ChenillekitAccessInternalUtils.getContextAsString(params.getPageActivationContext()));
			cookies.writeCookieValue(ChenilleKitAccessConstants.REMEMBERED_EVENT_CONTEXT,
					ChenillekitAccessInternalUtils.getContextAsString(params.getEventContext()));
			System.out.println("@@@@");
			System.out.println(ChenillekitAccessInternalUtils.getContextAsString(params.getEventContext()));
			cookies.writeCookieValue(ChenilleKitAccessConstants.REMEMBERED_PARAMS_TYPE,
					ChenilleKitAccessConstants.REMEMBERED_PARAMS_TYPE_ACTIONEVENT_VALUE);
		}
	}

}
