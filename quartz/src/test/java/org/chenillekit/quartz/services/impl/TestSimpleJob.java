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

package org.chenillekit.quartz.services.impl;

import org.chenillekit.quartz.AbstractIOCTest;
import org.chenillekit.quartz.ChenilleKitQuartzModule;
import org.chenillekit.quartz.services.QuartzSchedulerManager;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Test;

/**
 * @author <a href="mailto:homburgs@googlemail.com">shomburg</a>
 * @version $Id$
 */
public class TestSimpleJob extends AbstractIOCTest
{
    @BeforeSuite
    public final void setup_registry()
    {
        super.setup_registry(ChenilleKitQuartzModule.class);
    }

    @Test
    public void test_simple_job()
    {
        QuartzSchedulerManager manager = getService(QuartzSchedulerManager.class);
    }
}
