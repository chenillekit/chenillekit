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

package org.chenillekit.reports;

import java.util.Map;

import org.apache.tapestry5.ioc.Resource;

import org.chenillekit.reports.services.JasperReportsService;
import org.chenillekit.reports.services.impl.JasperReportsServiceImpl;
import org.slf4j.Logger;

/**
 * @author <a href="mailto:homburgs@gmail.com">S.Homburg</a>
 * @version $Id$
 */
public class ChenilleKitReportsModule
{
    /**
     * initialize the jasperreports service.
     *
     * @param logger        the logging service
     * @param configuration jasperreports configuration
     *
     * @return the jasperreports service
     */
    public static JasperReportsService buildJasperReportsService(Logger logger, Map<String, Resource> configuration)
    {
        return new JasperReportsServiceImpl(logger, configuration.get(JasperReportsService.CONFIG_RESOURCE_KEY));
    }
}
