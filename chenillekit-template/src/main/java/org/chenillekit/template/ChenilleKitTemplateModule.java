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

import java.util.Map;

import org.apache.tapestry5.ioc.Resource;
import org.apache.tapestry5.ioc.annotations.Marker;

import org.chenillekit.template.services.TemplateService;
import org.chenillekit.template.services.Velocity;
import org.chenillekit.template.services.FreeMarker;
import org.chenillekit.template.services.impl.VelocityServiceImpl;
import org.chenillekit.template.services.impl.FreeMarkerServiceImpl;
import org.slf4j.Logger;
import freemarker.template.Configuration;

/**
 * @author <a href="mailto:homburgs@gmail.com">S.Homburg</a>
 * @version $Id$
 */
public class ChenilleKitTemplateModule
{
    /**
     * contribute template service based on <a href="http://velocity.apache.org">Velocity</a> framework.
     *
     * @param logger        the logging service
     * @param configuration velocity configuration
     *
     * @return the Velocity based template service
     */
    @Marker(Velocity.class)
    public static TemplateService buildVelocityService(Logger logger, Map<String, Resource> configuration)
    {
        return new VelocityServiceImpl(logger, configuration.get(VelocityServiceImpl.CONFIG_RESOURCE_KEY));
    }

    /**
     * contribute template service based on <a href="http://freemarker.sourceforge.net">FreeMarker</a> framework.
     *
     * @param logger        the logging service
     * @param configuration velocity configuration
     *
     * @return the FreeMarker based template service
     */
    @Marker(FreeMarker.class)
    public static TemplateService buildFreeMarkerService(Logger logger, Map<String, Configuration> configuration)
    {
        return new FreeMarkerServiceImpl(logger, configuration.get(FreeMarkerServiceImpl.CONFIG_RESOURCE_KEY));
    }
}
