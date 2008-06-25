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

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.File;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.tapestry5.ioc.Registry;
import org.apache.tapestry5.ioc.RegistryBuilder;
import org.apache.tapestry5.ioc.internal.util.ClasspathResource;

import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.JasperPrint;
import org.chenillekit.reports.ChenilleKitReportsTestModule;
import org.chenillekit.reports.utils.ExportFormat;
import org.testng.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

/**
 * @author <a href="mailto:homburgs@googlemail.com">shomburg</a>
 * @version $Id$
 */
public class TestReportsService extends Assert
{
    private Registry _registry;

    @BeforeTest
    public void beforeTest()
    {
        RegistryBuilder builder = new RegistryBuilder();
        builder.add(ChenilleKitReportsTestModule.class);
        _registry = builder.build();
        _registry.performRegistryStartup();
    }

    @Test
    public void test_simple_report() throws FileNotFoundException
    {
        ReportsService service = _registry.getService(ReportsService.class);

        Map parameterMap = new HashMap(10);
        parameterMap.put("testString", "this is a test string");
        parameterMap.put("myDate", new Date());

        File pdfFile = new File("./target/test_simple_report.pdf");
        if (pdfFile.exists())
            pdfFile.delete();

        FileOutputStream fos = new FileOutputStream(pdfFile);

        service.fillAndExport(new ClasspathResource("TestReport.jrxml"), ExportFormat.PDF, parameterMap, new JREmptyDataSource(), fos);

        assertTrue(pdfFile.exists());
    }

    @Test
    public void test_append_report() throws FileNotFoundException
    {
        ReportsService service = _registry.getService(ReportsService.class);

        Map parameterMap = new HashMap(10);
        parameterMap.put("testString", "this is a test string for page 1");
        parameterMap.put("myDate", new Date());

        File pdfFile = new File("./target/test_append_report.pdf");
        if (pdfFile.exists())
            pdfFile.delete();

        FileOutputStream fos = new FileOutputStream(pdfFile);

        JasperPrint jasperPrint = null;
        jasperPrint = service.fillReport(jasperPrint, new ClasspathResource("TestReport.jrxml"), parameterMap, new JREmptyDataSource());

        parameterMap.put("testString", "this is a test string for page 2");
        parameterMap.put("myDate", new Date());

        jasperPrint = service.fillReport(jasperPrint, new ClasspathResource("TestReport.jrxml"), parameterMap, new JREmptyDataSource());

        service.export(jasperPrint, ExportFormat.PDF, fos);

        assertTrue(pdfFile.exists());
    }
}
