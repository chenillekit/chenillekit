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

import org.apache.tapestry5.ioc.OrderedConfiguration;
import org.apache.tapestry5.ioc.annotations.SubModule;
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
	 * @param configuration
	 */
    public static void contributeQuartzSchedulerManager(OrderedConfiguration<JobSchedulingBundle> configuration)
    {
        JobDetail myTestDetail = new JobDetail("MyTestJob", "MyTestGroup", MyTestJob.class);
        Trigger myTestTrigger = TriggerUtils.makeSecondlyTrigger();
        myTestTrigger.setName("MyTestTrigger");

        configuration.add("MyFirstJob", new SimpleJobSchedulingBundleImpl(myTestDetail, myTestTrigger));
    }
}
