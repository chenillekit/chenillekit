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
import org.apache.tapestry5.ioc.annotations.Marker;

import org.chenillekit.reports.annotations.ChenilleKitReports;
import org.chenillekit.reports.services.ReportsService;
import org.chenillekit.reports.services.impl.ReportsServiceImpl;
import org.slf4j.Logger;

/**
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
    @Marker(ChenilleKitReports.class)
    public static ReportsService buildReportsService(Logger logger, Map<String, Resource> configuration)
    {
        return new ReportsServiceImpl(logger, configuration.get(ReportsService.CONFIG_RESOURCE_KEY));
    }
}
