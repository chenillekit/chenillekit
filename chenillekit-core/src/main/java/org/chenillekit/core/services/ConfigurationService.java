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

package org.chenillekit.core.services;

import javax.naming.Context;
import javax.sql.DataSource;

import org.apache.commons.configuration.Configuration;
import org.apache.tapestry5.ioc.Resource;

/**
 * @version $Id$
 */
public interface ConfigurationService
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
    Configuration getConfiguration(Resource configurationResource);

    /**
     * get the configuration from the named resource.
     *
     * @param configurationResource the configuration resource
     * @param mergeWithSysProps     merge the configuration resource with system properties
     *
     * @return the configuration
     */
    Configuration getConfiguration(Resource configurationResource, boolean mergeWithSysProps);

    /**
     * get the configuration from JNDI context.
     *
     * @param context the JNDI context
     *
     * @return the configuration
     */
    Configuration getConfiguration(Context context);

    /**
     * get the configuration from system (JVM).
     *
     * @return the configuration
     */
    Configuration getConfiguration();

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
    Configuration getConfiguration(DataSource datasource, String table, String nameColumn,
                                   String keyColumn, String valueColumn, String name);
}
