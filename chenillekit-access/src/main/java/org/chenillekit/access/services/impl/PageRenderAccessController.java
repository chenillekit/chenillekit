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
import org.slf4j.Logger;

/**
 * @author <a href="mailto:mlusetti@gmail.com">M.Lusetti</a>
 * @version $Id$
 */
public class PageRenderAccessController implements PageRenderRequestFilter
{
	private final Logger logger;
	private final AccessValidator accessValidator;
	private final String loginPage;
	
	public PageRenderAccessController(AccessValidator accessValidator,
								SymbolSource symbols, Logger logger)
	{
		this.logger = logger;
		this.accessValidator = accessValidator;
		this.loginPage = symbols.valueForSymbol(ChenilleKitAccessConstants.LOGIN_PAGE);
		
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
			logger.info("User has rights to access " + parameters.getLogicalPageName()  + " page");
			handler.handle(parameters);
		}
		else
		{
			logger.info("User hasn't rights to access " + parameters.getLogicalPageName()  + " page");
			handler.handle(getLoginPageParameters());
		}
		
	}
	
	
	private PageRenderRequestParameters getLoginPageParameters()
	{
		PageRenderRequestParameters parameters = new PageRenderRequestParameters(loginPage, new EmptyEventContext());
		
		return parameters;
	}

}
