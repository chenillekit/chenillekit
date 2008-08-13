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

package org.chenillekit.core.services.impl;

import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.XMLConfiguration;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.tapestry5.ioc.Resource;
import org.apache.tapestry5.ioc.internal.util.Defense;

import org.chenillekit.core.services.ConfigurationService;

/**
 * @author <a href="mailto:homburgs@gmail.com">S.Homburg</a>
 * @version $Id$
 */
public class ConfigurationServiceImpl implements ConfigurationService
{
    /**
     * get the configuration from the named resource.
     *
     * @param configurationResource the configuration resource
     *
     * @return the configuration
     */
    public Configuration getConfiguration(Resource configurationResource)
    {
        Configuration configuration;

        Defense.notNull(configurationResource, "configurationResource");

        if (!configurationResource.exists())
            throw new RuntimeException(String.format("configuration resource '%s' not found", configurationResource.toString()));

        try
        {
            if (configurationResource.getFile().endsWith(".xml"))
                configuration = new XMLConfiguration(configurationResource.toURL());
            else if (configurationResource.getFile().endsWith(".properties"))
                configuration = new PropertiesConfiguration(configurationResource.toURL());
            else
                throw new RuntimeException(String.format("cant resolve configuration type of resource '%s'", configurationResource.toString()));
        }
        catch (ConfigurationException e)
        {
            throw new RuntimeException(e);
        }

        return configuration;
    }
}
