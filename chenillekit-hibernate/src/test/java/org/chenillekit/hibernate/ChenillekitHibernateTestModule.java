/*
 * Apache License
 * Version 2.0, January 2004
 * http://www.apache.org/licenses/
 *
 * Copyright 2009 by chenillekit.org
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 */

package org.chenillekit.hibernate;

import org.apache.tapestry5.hibernate.HibernateCoreModule;
import org.apache.tapestry5.ioc.MappedConfiguration;
import org.apache.tapestry5.ioc.ServiceBinder;
import org.apache.tapestry5.ioc.annotations.SubModule;
import org.chenillekit.hibernate.daos.UserDAO;
import org.chenillekit.hibernate.daos.UserDAOHibernate;

/**
 * @version $Id$
 */
@SubModule({HibernateCoreModule.class, ChenillekitHibernateModule.class})
public class ChenillekitHibernateTestModule
{
    public static void bind(ServiceBinder binder)
    {
        binder.bind(UserDAO.class, UserDAOHibernate.class);
    }

    public void contributeApplicationDefaults(MappedConfiguration<String, String> configuration)
    {
        configuration.add("hibernate.properties", "./etc/hibernate.properties");
    }
}
