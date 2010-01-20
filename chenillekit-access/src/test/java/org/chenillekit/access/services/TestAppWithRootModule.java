/*
 *  Apache License
 *  Version 2.0, January 2004
 *  http://www.apache.org/licenses/
 *
 *  Copyright 2008 by chenillekit.org
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 */

package org.chenillekit.access.services;

import org.apache.tapestry5.SymbolConstants;
import org.apache.tapestry5.ioc.MappedConfiguration;
import org.apache.tapestry5.ioc.OrderedConfiguration;
import org.apache.tapestry5.ioc.ServiceBinder;
import org.apache.tapestry5.ioc.annotations.SubModule;
import org.chenillekit.access.ChenilleKitAccessConstants;
import org.chenillekit.access.ChenilleKitAccessModule;
import org.chenillekit.access.services.impl.NoOpAppServerLoginService;
import org.chenillekit.access.services.impl.UserAuthServiceImpl;
import org.chenillekit.access.services.impl.UserEqualPassCheck;

/**
 * @version $Id$
 */
@SubModule(ChenilleKitAccessModule.class)
public class TestAppWithRootModule
{
    /**
     * Bind extra services.
     *
     * @param binder object to bind services to
     */
    public static void bind(ServiceBinder binder)
    {
        binder.bind(AppServerLoginService.class, NoOpAppServerLoginService.class);
    }

    /**
     * @param configuration
     */
    public static void contributeAuthenticationService(OrderedConfiguration<AuthenticationServiceFilter> configuration)
    {
        configuration.add("SAMPLE", new UserAuthServiceImpl());
        configuration.add("EQUALTEST", new UserEqualPassCheck(), "before:*");
    }

    /**
     * @param configuration
     */
    public static void contributeApplicationDefaults(MappedConfiguration<String, String> configuration)
    {
        configuration.add(ChenilleKitAccessConstants.LOGIN_PAGE, "login");
        configuration.add(ChenilleKitAccessConstants.ACCESS_DENIED_ACTION, ChenilleKitAccessConstants.TRIGGER_EVENT);
        configuration.add(SymbolConstants.PRODUCTION_MODE, "false");
    }

}
