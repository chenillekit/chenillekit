/**
 * 
 */
package org.chenillekit.access.services.impl;

import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.apache.tapestry5.EventContext;
import org.apache.tapestry5.services.ComponentEventRequestParameters;
import org.apache.tapestry5.services.ContextPathEncoder;
import org.apache.tapestry5.services.Cookies;
import org.apache.tapestry5.services.PageRenderRequestParameters;
import org.apache.tapestry5.services.Response;
import org.chenillekit.access.ChenilleKitAccessConstants;
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
	
	private final ConcurrentMap<String, ComponentEventRequestParameters> requestedEventParamter =
		new ConcurrentHashMap<String, ComponentEventRequestParameters>();
	
	private final ConcurrentMap<String, PageRenderRequestParameters> requestedPageParameter =
		new ConcurrentHashMap<String, PageRenderRequestParameters>();
	
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
		
		String redirectURL = response.encodeRedirectURL(new String(pageName + contextPath));
		
		response.sendRedirect(redirectURL);
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.chenillekit.access.services.RedirectService#getComponentEventParamter(java.lang.String)
	 */
	public ComponentEventRequestParameters removeComponentEventParamter(String ckAccessId)
	{
		return requestedEventParamter.remove(ckAccessId);
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.chenillekit.access.services.RedirectService#getPageRenderParamter(java.lang.String)
	 */
	public PageRenderRequestParameters removePageRenderParamter(String ckAccessId)
	{
		return requestedPageParameter.remove(ckAccessId);
	}

	/*
	 * (non-Javadoc)
	 * @see org.chenillekit.access.services.RedirectService#putComponentEventParameter(java.lang.String, org.apache.tapestry5.services.ComponentEventRequestParameters)
	 */
	public void rememberComponentEventParameter(String ckAccessId, ComponentEventRequestParameters params)
	{
		requestedEventParamter.put(ckAccessId, params);
		
		cookies.writeCookieValue(ChenilleKitAccessConstants.ACCESS_ID_COOKIE_NAME, ckAccessId);
	}

	/*
	 * (non-Javadoc)
	 * @see org.chenillekit.access.services.RedirectService#putPageRenderParameter(java.lang.String, org.apache.tapestry5.services.PageRenderRequestParameters)
	 */
	public void rememberPageRenderParameter(String ckAccessId, PageRenderRequestParameters params)
	{
		requestedPageParameter.put(ckAccessId, params);
		
		cookies.writeCookieValue(ChenilleKitAccessConstants.ACCESS_ID_COOKIE_NAME, ckAccessId);
	}
	
	

}
