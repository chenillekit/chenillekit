/*
 * Apache License
 * Version 2.0, January 2004
 * http://www.apache.org/licenses/
 *
 * Copyright 2008-2010 by chenillekit.org
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 */

package org.chenillekit.access.integration.app3.services;

import org.apache.tapestry5.SymbolConstants;
import org.apache.tapestry5.ioc.MappedConfiguration;
import org.apache.tapestry5.ioc.OrderedConfiguration;
import org.apache.tapestry5.ioc.annotations.SubModule;
import org.apache.tapestry5.services.ApplicationStateManager;

import org.chenillekit.access.ChenilleKitAccessConstants;
import org.chenillekit.access.ChenilleKitAccessModule;
import org.chenillekit.access.integration.app1.services.impl.UserAuthServiceImpl;
import org.chenillekit.access.services.AuthenticationServiceFilter;

/**
 * @version $Id: TestAppWithRootModule.java 685 2010-08-03 16:54:50Z homburgs $
 */
@SubModule(ChenilleKitAccessModule.class)
public class TestAppWithRootModule
{
	/**
	 * @param configuration
	 */
	public static void contributeAuthenticationService(OrderedConfiguration<AuthenticationServiceFilter> configuration,
													   ApplicationStateManager stateManager)
	{
		configuration.add("SAMPLE", new UserAuthServiceImpl(stateManager));
	}

	/**
	 * @param configuration
	 */
	public static void contributeApplicationDefaults(MappedConfiguration<String, String> configuration)
	{
		configuration.add(ChenilleKitAccessConstants.LOGIN_PAGE, "login");
		configuration.add(ChenilleKitAccessConstants.FALLBACK_PAGE, "start");
		configuration.add(ChenilleKitAccessConstants.ACCESS_DENIED_ACTION, ChenilleKitAccessConstants.JUMP_TO_LOGIN_PAGE);
		configuration.add(ChenilleKitAccessConstants.HAS_ACCESS_IF_NORESTRICTION_EVEN_NOT_LOGGEDIN, "false");
		configuration.add(ChenilleKitAccessConstants.RESTRICTED_PAGE_ROLE, "-1");
		configuration.add(SymbolConstants.PRODUCTION_MODE, "false");
	}
}
