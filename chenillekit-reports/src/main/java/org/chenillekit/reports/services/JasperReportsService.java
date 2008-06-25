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

package org.chenillekit.reports.services;

import java.io.OutputStream;
import java.util.Map;

import org.apache.tapestry5.ioc.Resource;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JasperPrint;
import org.chenillekit.reports.utils.JasperExportFormat;

/**
 * reporting tool that has the ability to deliver rich content to the screen or printer or into PDF, HTML, XLS, CSV and XML files
 * based <a href="http://jasperforge.org/sf/projects/jasperreports">jasperreports</a>.
 *
 * @author <a href="mailto:homburgs@gmail.com">S.Homburg</a>
 * @version $Id$
 */
public interface JasperReportsService
{
    public final String CONFIG_RESOURCE_KEY = "jasperreports.properties";

    /**
     * export the report.
     *
     * @param inputResource the input resource (report template file ".jrxml")
     * @param format        the output format
     * @param parameterMap  the parameter map
     * @param dataSource    the datasource, maybe null or empty.
     * @param outputStream  the output stream
     */
    void fillAndExport(Resource inputResource, JasperExportFormat format, Map parameterMap, JRDataSource dataSource, OutputStream outputStream);

    /**
     * export the report.
     *
     * @param jasperPrint  represents a page-oriented document that can be viewed, printed or exported to other formats.
     * @param format       the output format
     * @param outputStream the output stream
     */
    void export(JasperPrint jasperPrint, JasperExportFormat format, OutputStream outputStream);

    /**
     * Fills the report design loaded from the supplied input resource and returns the generated report object.
     *
     * @param inputResource the input resource (report template file ".jrxml")
     * @param parameterMap  the parameter map
     *
     * @return document that can be viewed, printed or exported to other formats.
     */
    JasperPrint fillReport(Resource inputResource, Map parameterMap);

    /**
     * Fills the report design loaded from the supplied input resource and returns the generated report object.
     *
     * @param inputResource the input resource (report template file ".jrxml")
     * @param parameterMap  the parameter map
     * @param dataSource    the datasource, maybe null or empty.
     *
     * @return document that can be viewed, printed or exported to other formats.
     */
    JasperPrint fillReport(Resource inputResource, Map parameterMap, JRDataSource dataSource);

    /**
     * Fills the report design loaded from the supplied input resource and returns the generated report object.
     * <p/>
     * if parameter <em>jasperPrint<em> not null, the data filled into <em>jasperPrint</em>
     * instead of returns a new jasper print object.
     *
     * @param jasperPrint   represents a page-oriented document that can be viewed, printed or exported to other formats.
     * @param inputResource the input resource (report template file ".jrxml")
     * @param parameterMap  the parameter map
     * @param dataSource    the datasource, maybe null or empty.
     *
     * @return document that can be viewed, printed or exported to other formats.
     */
    JasperPrint fillReport(JasperPrint jasperPrint, Resource inputResource, Map parameterMap, JRDataSource dataSource);
}
