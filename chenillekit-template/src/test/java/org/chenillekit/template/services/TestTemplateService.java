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

package org.chenillekit.template.services;

import java.io.ByteArrayOutputStream;
import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.tapestry5.ioc.Registry;
import org.apache.tapestry5.ioc.RegistryBuilder;
import org.apache.tapestry5.ioc.internal.util.ClasspathResource;

import org.chenillekit.core.streams.StringInputStream;
import org.chenillekit.template.ChenilleKitTemplateTestModule;
import org.testng.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

/**
 * @version $Id$
 */
public class TestTemplateService extends Assert
{
    private Registry _registry;

    @BeforeTest
    public void beforeTest()
    {
        RegistryBuilder builder = new RegistryBuilder();
        builder.add(ChenilleKitTemplateTestModule.class);
        _registry = builder.build();
        _registry.performRegistryStartup();
    }

    @Test
    public void test_velocity_service()
    {
        TemplateService service = _registry.getService("VelocityService", TemplateService.class);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        Map<String, Serializable> parameterMap = new HashMap<String, Serializable>();
        parameterMap.put("user_name", "Athur Dent");
        parameterMap.put("login_tries", 3);
        parameterMap.put("block_date", new Date());
        parameterMap.put("sysadm_email", "Zaphod.Beeblebrox@beteigeuze.moon");

        service.mergeDataWithResource(new ClasspathResource("test1.vm"), baos, parameterMap);

        String result = baos.toString();
        System.err.println(result);

        assertTrue(result.contains("tried 3"));
        assertTrue(result.contains("Athur Dent"));
        assertFalse(result.contains("block_date"));
    }

    @Test
    public void test_velocity_service_stream()
    {
        TemplateService service = _registry.getService("VelocityService", TemplateService.class);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        Map<String, Serializable> parameterMap = new HashMap<String, Serializable>();
        parameterMap.put("var", "fine short");

        service.mergeDataWithStream(new StringInputStream("this is a ${var} test string"), baos, parameterMap);

        String result = baos.toString();
        System.err.println(result);

        assertTrue(result.contains("fine short"));
    }

    @Test
    public void test_freemarker_service()
    {
        TemplateService service = _registry.getService("FreeMarkerService", TemplateService.class);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        Map<String, Serializable> parameterMap = new HashMap<String, Serializable>();
        parameterMap.put("user_name", "Athur Dent");
        parameterMap.put("login_tries", 3);
        parameterMap.put("block_date", new Date());
        parameterMap.put("sysadm_email", "Zaphod.Beeblebrox@beteigeuze.moon");

        service.mergeDataWithResource(new ClasspathResource("test1.fmt"), baos, parameterMap);

        String result = baos.toString();
        System.err.println(result);

        assertTrue(result.contains("tried 3"));
        assertTrue(result.contains("Athur Dent"));
        assertFalse(result.contains("block_date"));
    }
}
