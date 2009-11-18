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

import org.apache.tapestry5.hibernate.HibernateConfigurer;
import org.apache.tapestry5.ioc.OrderedConfiguration;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.ioc.annotations.Symbol;
import org.chenillekit.hibernate.services.impl.PropertiesHibernateConfigurer;

import java.io.File;

/**
 * @version $Id$
 */
public class ChenillekitHibernateModule
{
    /**
     * Add the properties configurer.
     */
    public static void contributeHibernateSessionSource(OrderedConfiguration<HibernateConfigurer> config,
                                                        @Inject @Symbol("hibernate.properties") String propertiesFilename)
    {
        if (propertiesFilename != null)
        {
            if (!new File(propertiesFilename).exists())
                throw new RuntimeException(String.format("cant read hibernate properties '%s'", propertiesFilename));

            config.add("Properties", new PropertiesHibernateConfigurer(propertiesFilename));
        }
    }
}
