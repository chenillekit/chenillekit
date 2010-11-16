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

package org.chenillekit.core.services.impl;

import org.apache.commons.configuration.AbstractConfiguration;
import org.apache.commons.configuration.CombinedConfiguration;
import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.DatabaseConfiguration;
import org.apache.commons.configuration.HierarchicalINIConfiguration;
import org.apache.commons.configuration.JNDIConfiguration;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.commons.configuration.SystemConfiguration;
import org.apache.commons.configuration.XMLConfiguration;
import org.apache.commons.configuration.plist.PropertyListConfiguration;
import org.apache.tapestry5.ioc.Resource;
import org.chenillekit.core.services.ConfigurationService;

import javax.naming.Context;
import javax.sql.DataSource;

/**
 * @version $Id$
 * @deprecated to be removed in future release (soon)
 */
@Deprecated
public class ConfigurationServiceImpl implements ConfigurationService
{
    /**
     * get the configuration from the named resource.
     * <p/>
     * the system properties merged auto. into the returned configuration
     *
     * @param configurationResource the configuration resource
     *
     * @return the configuration
     */
    public Configuration getConfiguration(Resource configurationResource)
    {
        return getConfiguration(configurationResource, true);
    }

    /**
     * get the configuration from the named resource.
     *
     * @param configurationResource the configuration resource
     * @param mergeWithSysProps     merge the configuration resource with system properties
     *
     * @return the configuration
     */
    public Configuration getConfiguration(Resource configurationResource, boolean mergeWithSysProps)
    {
        Configuration configuration;

        assert configurationResource != null;

        if (!configurationResource.exists())
            throw new RuntimeException(String.format("configuration resource '%s' not found", configurationResource.toString()));

        try
        {
            if (configurationResource.getFile().endsWith(".xml"))
                configuration = new XMLConfiguration(configurationResource.toURL());
            else if (configurationResource.getFile().endsWith(".properties"))
                configuration = new PropertiesConfiguration(configurationResource.toURL());
            else if (configurationResource.getFile().endsWith(".plist"))
                configuration = new PropertyListConfiguration(configurationResource.toURL());
            else if (configurationResource.getFile().endsWith(".plist"))
                configuration = new PropertyListConfiguration(configurationResource.toURL());
            else if (configurationResource.getFile().endsWith(".ini"))
                configuration = new HierarchicalINIConfiguration(configurationResource.toURL());
            else
                throw new RuntimeException(String.format("cant resolve configuration type of resource '%s'", configurationResource.toString()));

            if (mergeWithSysProps)
            {
                CombinedConfiguration mergedConfiguration = new CombinedConfiguration();
                mergedConfiguration.addConfiguration((AbstractConfiguration) configuration);
                mergedConfiguration.addConfiguration((AbstractConfiguration) getConfiguration());

                configuration = mergedConfiguration;
            }
        }
        catch (ConfigurationException e)
        {
            throw new RuntimeException(e);
        }

        return configuration;
    }

    /**
     * get the configuration from JNDI context.
     *
     * @param context the JNDI context
     *
     * @return the configuration
     */
    public Configuration getConfiguration(Context context)
    {
        assert context != null;
        return new JNDIConfiguration(context);
    }

    /**
     * get the configuration from system (JVM).
     *
     * @return the configuration
     */
    public Configuration getConfiguration()
    {
        return new SystemConfiguration();
    }

    /**
     * Build a configuration from a table containing multiple configurations.
     *
     * @param datasource  the datasource to connect to the database
     * @param table       the name of the table containing the configurations
     * @param nameColumn  the column containing the name of the configuration
     * @param keyColumn   the column containing the keys of the configuration
     * @param valueColumn the column containing the values of the configuration
     * @param name        the name of the configuration
     */
    public Configuration getConfiguration(DataSource datasource, String table, String nameColumn,
                                          String keyColumn, String valueColumn, String name)
    {
        return new DatabaseConfiguration(datasource, table, nameColumn, keyColumn, valueColumn, name);
    }
}
