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

package org.chenillekit.template;

import org.apache.tapestry5.ioc.MappedConfiguration;
import org.apache.tapestry5.ioc.Resource;
import org.apache.tapestry5.ioc.annotations.SubModule;
import org.apache.tapestry5.ioc.internal.util.ClasspathResource;

import freemarker.template.Configuration;
import org.chenillekit.template.services.impl.FreeMarkerServiceImpl;
import org.chenillekit.template.services.impl.VelocityServiceImpl;

/**
 * @author <a href="mailto:homburgs@gmail.com">S.Homburg</a>
 * @version $Id$
 */
@SubModule(value = {ChenilleKitTemplateModule.class})
public class ChenilleKitTemplateTestModule
{
    /**
     * wo liegen die Entities fuer das Masterdata5 Modul.
     *
     * @param configuration
     */
    public static void contributeVelocityService(MappedConfiguration<String, Resource> configuration)
    {
        configuration.add(VelocityServiceImpl.CONFIG_RESOURCE_KEY, new ClasspathResource("velocity.properties"));
    }

    /**
     * wo liegen die Entities fuer das Masterdata5 Modul.
     *
     * @param configuration
     */
    public static void contributeFreeMarkerService(MappedConfiguration<String, Configuration> configuration)
    {
        freemarker.template.Configuration config = new freemarker.template.Configuration();
        config.setDateFormat("dd.MM.yyyy");
        config.setClassForTemplateLoading(config.getClass(), "/");
        configuration.add(FreeMarkerServiceImpl.CONFIG_RESOURCE_KEY, config);
    }
}
