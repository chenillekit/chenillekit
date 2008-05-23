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

package org.chenillekit.quartz;

import org.apache.tapestry5.ioc.Registry;
import org.apache.tapestry5.ioc.RegistryBuilder;

import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterSuite;

/**
 * @author <a href="mailto:homburgs@gmail.com">S.Homburg</a>
 * @version $Id$
 */
abstract public class AbstractIOCTest extends Assert
{
    private static Registry registry;

    public void setup_registry(Class... moduleClasses)
    {
        RegistryBuilder builder = new RegistryBuilder();

        builder.add(moduleClasses);
        registry = builder.build();

        registry.performRegistryStartup();
    }

    @AfterMethod
    public final void cleanup_registry()
    {
        registry.cleanupThread();
    }

    @AfterSuite
    public final void shutdown_registry()
    {
        registry.shutdown();
    }

    public <T> T getService(Class<T> serviceClass)
    {
        return registry.getService(serviceClass);
    }
}
