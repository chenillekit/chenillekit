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

import org.apache.tapestry5.ioc.MappedConfiguration;
import org.apache.tapestry5.ioc.OrderedConfiguration;
import org.apache.tapestry5.ioc.Resource;
import org.apache.tapestry5.ioc.annotations.SubModule;
import org.apache.tapestry5.ioc.internal.util.ClasspathResource;

import org.chenillekit.quartz.services.JobSchedulingBundle;
import org.chenillekit.quartz.services.impl.SimpleJobSchedulingBundleImpl;
import org.quartz.JobDetail;
import org.quartz.Trigger;
import org.quartz.TriggerUtils;

/**
 * @author <a href="mailto:homburgs@googlemail.com">shomburg</a>
 * @version $Id$
 */
@SubModule(value = {ChenilleKitQuartzModule.class})
public class ChenilleKitQuartzTestModule
{
    /**
     * contribute the <a href="http://www.opensymphony.com/quartz/">job scheduling system</a>.
     *
     * @param configuration configuration map
     */
    public static void contributeSchedulerFactory(MappedConfiguration<String, Resource> configuration)
    {
        configuration.add("quartz.properties", new ClasspathResource("scheduler.properties"));
    }

    public static void contributeQuartzSchedulerManager(OrderedConfiguration<JobSchedulingBundle> configuration)
    {
        JobDetail myTestDetail = new JobDetail("MyTestJob", "MyTestGroup", MyTestJob.class);
        Trigger myTestTrigger = TriggerUtils.makeSecondlyTrigger();
        myTestTrigger.setName("MyTestTrigger");

        configuration.add("MyFirstJob", new SimpleJobSchedulingBundleImpl(myTestDetail, myTestTrigger));
    }
}
