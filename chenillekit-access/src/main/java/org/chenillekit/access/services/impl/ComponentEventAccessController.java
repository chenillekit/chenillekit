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

import org.apache.tapestry5.EventConstants;
import org.apache.tapestry5.internal.EmptyEventContext;
import org.apache.tapestry5.ioc.services.SymbolSource;
import org.apache.tapestry5.services.ComponentEventRequestFilter;
import org.apache.tapestry5.services.ComponentEventRequestHandler;
import org.apache.tapestry5.services.ComponentEventRequestParameters;
import org.chenillekit.access.ChenilleKitAccessConstants;
import org.chenillekit.access.services.AccessValidator;

/**
 * @author <a href="mailto:mlusetti@gmail.com">M.Lusetti</a>
 * @version $Id$
 */
public class ComponentEventAccessController implements ComponentEventRequestFilter
{
	private final AccessValidator accessValidator;
	private final String loginPage;
	
	public ComponentEventAccessController(AccessValidator accessValidator, SymbolSource symbols)
	{
		this.accessValidator = accessValidator;
		this.loginPage = symbols.valueForSymbol(ChenilleKitAccessConstants.LOGIN_PAGE); 
	}

	public void handle(ComponentEventRequestParameters parameters,
			ComponentEventRequestHandler handler) throws IOException
	{
		if (accessValidator.hasAccess(parameters.getActivePageName(), parameters.getNestedComponentId(), parameters.getEventType()))
			handler.handle(parameters);
		else
			handler.handle(getLoginComponentParameters());
				
	}
	
	private ComponentEventRequestParameters getLoginComponentParameters()
	{
		ComponentEventRequestParameters parameters = new ComponentEventRequestParameters(loginPage, loginPage,
				"", EventConstants.ACTIVATE, new EmptyEventContext(),new EmptyEventContext());
		
		
		return parameters;
	}

}
