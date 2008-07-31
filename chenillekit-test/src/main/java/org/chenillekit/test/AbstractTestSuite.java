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

package org.chenillekit.test;

import org.apache.tapestry5.ioc.Registry;
import org.apache.tapestry5.ioc.RegistryBuilder;

import org.testng.Assert;
import org.testng.annotations.AfterSuite;

/**
 * @author <a href="mailto:homburgs@googlemail.com">shomburg</a>
 * @version $Id$
 */
public class AbstractTestSuite extends Assert
{
    protected static Registry registry;

    public void setup_registry(Class... moduleClasses)
    {
        RegistryBuilder builder = new RegistryBuilder();

        builder.add(moduleClasses);
        registry = builder.build();

        registry.performRegistryStartup();
    }

    public final void shutdown()
    {
        throw new UnsupportedOperationException("No registry shutdown until @AfterSuite.");
    }

    @AfterSuite
    public final void shutdown_registry()
    {
        registry.cleanupThread();
        registry.shutdown();
        registry = null;
    }
}
