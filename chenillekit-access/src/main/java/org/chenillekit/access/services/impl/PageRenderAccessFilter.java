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

import org.apache.tapestry5.internal.EmptyEventContext;
import org.apache.tapestry5.ioc.services.SymbolSource;
import org.apache.tapestry5.services.PageRenderRequestFilter;
import org.apache.tapestry5.services.PageRenderRequestHandler;
import org.apache.tapestry5.services.PageRenderRequestParameters;
import org.chenillekit.access.ChenilleKitAccessConstants;
import org.chenillekit.access.services.AccessValidator;
import org.chenillekit.access.services.AuthRedirectService;
import org.slf4j.Logger;

/**
 *
 * @version $Id$
 */
public class PageRenderAccessFilter implements PageRenderRequestFilter
{
	private final Logger logger;
	private final AccessValidator accessValidator;
	private final String loginPage;
	private final AuthRedirectService authRedirectService;

	public PageRenderAccessFilter(AccessValidator accessValidator,
								SymbolSource symbols, Logger logger, AuthRedirectService authRedirectService )
	{
		this.logger = logger;
		this.accessValidator = accessValidator;
		this.loginPage = symbols.valueForSymbol(ChenilleKitAccessConstants.LOGIN_PAGE);
		this.authRedirectService = authRedirectService;
	}

	/*
	 * (non-Javadoc)
	 * @see org.apache.tapestry5.services.PageRenderRequestFilter#handle(org.apache.tapestry5.services.PageRenderRequestParameters, org.apache.tapestry5.services.PageRenderRequestHandler)
	 */
	public void handle(PageRenderRequestParameters parameters,
			PageRenderRequestHandler handler) throws IOException
	{
		if (accessValidator.hasAccess(parameters.getLogicalPageName(), null, null))
		{
			if (logger.isDebugEnabled())
				logger.debug("User has rights to access " + parameters.getLogicalPageName()  + " page");
			handler.handle(parameters);
		}
		else
		{
			if (logger.isDebugEnabled())
				logger.debug("User hasn't rights to access " + parameters.getLogicalPageName()  + " page");
			authRedirectService.setReturnPage( parameters.getLogicalPageName() );
			if (logger.isDebugEnabled()) logger.debug("set return page to " + parameters.getLogicalPageName());
			handler.handle(getLoginPageParameters());
		}

	}


	private PageRenderRequestParameters getLoginPageParameters()
	{
		PageRenderRequestParameters parameters = new PageRenderRequestParameters(loginPage, new EmptyEventContext());

		return parameters;
	}

}
