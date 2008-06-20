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

import org.apache.tapestry5.ioc.MappedConfiguration;
import org.apache.tapestry5.services.ApplicationStateContribution;
import org.apache.tapestry5.services.ApplicationStateCreator;

import org.chenillekit.access.utils.WebUser;

/**
 * @author <a href="mailto:shomburg@hsofttec.com">S.Homburg</a>
 * @version $Id$
 */
public class TestAppModule
{
    public static void contributeASOs(MappedConfiguration<Class, ApplicationStateContribution> configuration)
    {
        ApplicationStateCreator<WebUser> creator = new ApplicationStateCreator<WebUser>()
        {
            public WebUser create()
            {
                return new WebUser(1, "root", new int[]{1, 2}, new String[]{"superusers"});
            }
        };

        configuration.add(WebUser.class, new ApplicationStateContribution("session", creator));
    }

    public static void contributeAccessControllerDispatcher(MappedConfiguration<String, Class> configuration)
    {
        configuration.add("webuser.implementation", WebUser.class);
    }
}
