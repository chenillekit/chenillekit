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

package org.chenillekit.hibernate.services.impl;

import org.apache.tapestry5.hibernate.HibernateConfigurer;
import org.hibernate.cfg.Configuration;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

/**
 *
 * @version $Id$
 */
public class PropertiesHibernateConfigurer implements HibernateConfigurer
{
    private final String propertiesFileName;

    public PropertiesHibernateConfigurer(String propertiesFileName)
    {
        this.propertiesFileName = propertiesFileName;
    }

    /**
     * Passed the configuration so as to make changes.
     */
    public void configure(Configuration configuration)
    {
        Properties properties = new Properties();

        try
        {
            properties.load(new FileInputStream(propertiesFileName));
            configuration.addProperties(properties);
            configuration.configure();
        }
        catch (IOException e)
        {
            throw new RuntimeException(e);
        }
    }
}
