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

package org.chenillekit.core.tests;

import java.util.List;

import org.apache.commons.configuration.Configuration;
import org.apache.tapestry5.ioc.Resource;
import org.apache.tapestry5.ioc.internal.util.ClasspathResource;

import org.chenillekit.core.ChenilleKitCoreTestModule;
import org.chenillekit.core.services.ConfigurationService;
import org.chenillekit.test.AbstractTestSuite;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

/**
 * @version $Id$
 */
public class TestConfigurationService extends AbstractTestSuite
{
    private ConfigurationService service;

    @BeforeTest
    public void setup_registry()
    {
        super.setup_registry(ChenilleKitCoreTestModule.class);
        service = registry.getService(ConfigurationService.class);
    }

    @Test
    public void test1()
    {
        Resource configResource = new ClasspathResource("test.properties");
        Configuration testConfiguration = service.getConfiguration(configResource);

        assertNotNull(testConfiguration);
        assertEquals(testConfiguration.getString("test.value1"), "test1");
        assertEquals(testConfiguration.getInt("test.value2"), 3);
        assertEquals(testConfiguration.getStringArray("test.value3"), new String[]{"test1", "test2", "test3", "test4"});
        assertEquals(testConfiguration.getString("test.value4"), System.getProperty("java.vendor"));
    }

    @Test
    public void test2()
    {
        Resource configResource = new ClasspathResource("test.xml");
        Configuration testConfiguration = service.getConfiguration(configResource);

        String backColor = testConfiguration.getString("colors.background");
        String textColor = testConfiguration.getString("colors.text");
        String linkNormal = testConfiguration.getString("colors.link[@normal]");
        String defColor = testConfiguration.getString("colors.default");
        int rowsPerPage = testConfiguration.getInt("rowsPerPage");
        List buttons = testConfiguration.getList("buttons.name");

        assertNotNull(testConfiguration);
        assertEquals(backColor, "#808080");
        assertEquals(textColor, "#000000");
        assertEquals(linkNormal, "#000080");
        assertEquals(defColor, "#008000");
        assertEquals(rowsPerPage, 15);
        assertEquals(buttons.size(), 3);
    }

    @Test(expectedExceptions = {RuntimeException.class})
    public void test3()
    {
        Resource configResource = new ClasspathResource("test.xm");
        service.getConfiguration(configResource);
    }

    @Test(expectedExceptions = {RuntimeException.class}, enabled = false)
    public void test4()
    {
        Resource configResource = new ClasspathResource("test.ini");
        Configuration configuration = service.getConfiguration(configResource);
        Configuration subConfiguration = configuration.subset("Mail");

        assertEquals(subConfiguration.getInt("MAPI"), 1);
    }
}
