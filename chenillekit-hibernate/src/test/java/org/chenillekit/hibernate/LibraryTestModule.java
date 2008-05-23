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

package org.chenillekit.hibernate;

import org.apache.tapestry5.hibernate.HibernateConfigurer;
import org.apache.tapestry5.hibernate.HibernateSessionManager;
import org.apache.tapestry5.internal.InternalConstants;
import org.apache.tapestry5.ioc.Configuration;
import org.apache.tapestry5.ioc.MappedConfiguration;
import org.apache.tapestry5.ioc.OrderedConfiguration;
import org.apache.tapestry5.ioc.ServiceBinder;

import org.hibernate.Interceptor;

import org.chenillekit.hibernate.factories.GenericDAOFactory;
import org.chenillekit.hibernate.factories.LibraryTestDAOFactory;
import org.chenillekit.hibernate.interceptors.AuditInterceptor;
import org.chenillekit.hibernate.services.impl.HibernateInterceptorConfigurer;
import org.slf4j.Logger;

/**
 * @author <a href="mailto:homburgs@gmail.com">S.Homburg</a>
 * @version $Id$
 */
public class LibraryTestModule
{
    public void contributeApplicationDefaults(MappedConfiguration<String, String> configuration)
    {
        configuration.add(InternalConstants.TAPESTRY_ALIAS_MODE_SYMBOL, "only_testing");
        configuration.add(InternalConstants.TAPESTRY_APP_PACKAGE_PARAM, "only_testing");
    }

    public static GenericDAOFactory buildLibraryTestDAOFactory(Logger serviceLog, HibernateSessionManager sessionManager)
    {
        return new LibraryTestDAOFactory(serviceLog, sessionManager);
    }

    /**
     * wo liegen die Entities fuer das Masterdata5 Modul.
     *
     * @param configuration
     */
    public static void contributeHibernateEntityPackageManager(Configuration<String> configuration)
    {
        configuration.add("org.chenillekit.hibernate.entities");
    }

    /**
     * Adds the following configurers: <dl> <dt>Default</dt> <dd>Performs default hibernate configuration</dd>
     * <dt>PackageName</dt> <dd>Loads entities by package name</dd> </ul>
     *
     * @param config
     * @param interceptor
     */
    public static void contributeHibernateSessionSource(OrderedConfiguration<HibernateConfigurer> config,
                                                        Interceptor interceptor)
    {
        config.add("Interceptor", new HibernateInterceptorConfigurer(interceptor));
    }

    public static void bind(ServiceBinder binder)
    {
        binder.bind(Interceptor.class, AuditInterceptor.class);
    }
}
