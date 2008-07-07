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

package org.chenillekit.scripting.services;

import java.util.HashMap;

import org.apache.tapestry5.ioc.Registry;
import org.apache.tapestry5.ioc.RegistryBuilder;
import org.apache.tapestry5.ioc.internal.util.ClasspathResource;

import org.chenillekit.scripting.ChenilleKitScriptingTestModule;
import org.testng.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

/**
 * Testcase for the BeanShellService.
 *
 * @author <a href="mailto:homburgs@googlemail.com">shomburg</a>
 */
public class TestBSFService extends Assert
{
    private Registry _registry;

    @BeforeTest
    public void beforeTest()
    {
        RegistryBuilder builder = new RegistryBuilder();
        builder.add(ChenilleKitScriptingTestModule.class);
        _registry = builder.build();
        _registry.performRegistryStartup();
    }

    @Test
    public void groovy_without_predefined()
    {
        ScriptingService service = _registry.getService(ScriptingService.class);
        Object value = service.eval("groovy", "def x = \"Hello World\"; println \"GroovyShell's greeting: \" + x; return x;");

        assertEquals(value, "Hello World");
    }

    @Test
    public void groovy_with_predefined()
    {
        ScriptingService service = _registry.getService(ScriptingService.class);

        HashMap<String, Object> predefineds = new HashMap<String, Object>();
        predefineds.put("x", "Hello World, I am predefined");

        Object value = service.eval("groovy", "x = bsf.lookupBean('x'); println(\"GroovyShell's greeting: \" + x); return x;", predefineds);

        assertEquals(value, "Hello World, I am predefined");
    }

    @Test
    public void groovy_file()
    {
        ScriptingService service = _registry.getService(ScriptingService.class);

        Object value = service.eval(new ClasspathResource("test1.groovy"));

        assertEquals(value, "Hello World from 'test1.groovy' script");
    }

    @Test
    public void groovy_with_predefined_file()
    {
        ScriptingService service = _registry.getService(ScriptingService.class);

        HashMap<String, Object> predefineds = new HashMap<String, Object>();
        predefineds.put("registry", _registry);
        Object value = service.eval(new ClasspathResource("test2.groovy"), predefineds);

        assertEquals(value, "Hello World from 'test2.groovy' script");
    }

    @Test
    public void beanshell_without_predefined()
    {
        ScriptingService service = _registry.getService(ScriptingService.class);
        Object value = service.eval("beanshell", "x = \"Hello World\"; System.out.println(\"BeanShell's greeting: \" + x); return x;");

        assertEquals(value, "Hello World");
    }

    @Test
    public void beanshell_with_predefined()
    {
        ScriptingService service = _registry.getService(ScriptingService.class);

        HashMap<String, Object> predefineds = new HashMap<String, Object>();
        predefineds.put("x", "Hello World, I am predefined");

        Object value = service.eval("beanshell", "x = bsf.lookupBean(\"x\"); System.out.println(\"BeanShell's greeting: \" + x); return x;", predefineds);

        assertEquals(value, "Hello World, I am predefined");
    }

    @Test
    public void beanshell_file()
    {
        ScriptingService service = _registry.getService(ScriptingService.class);

        Object value = service.eval(new ClasspathResource("test1.bsh"));

        assertEquals(value, "Hello World from 'test1.bsh' script");
    }

    @Test
    public void beanshell_with_predefined_file()
    {
        ScriptingService service = _registry.getService(ScriptingService.class);

        HashMap<String, Object> predefineds = new HashMap<String, Object>();
        predefineds.put("registry", _registry);
        Object value = service.eval(new ClasspathResource("test2.bsh"), predefineds);

        assertEquals(value, "Hello World from 'test2.bsh' script");
    }
}