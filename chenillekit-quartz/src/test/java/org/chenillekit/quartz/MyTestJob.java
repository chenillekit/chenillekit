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

import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

/**
 * @author <a href="mailto:shomburg@hsofttec.com">S.Homburg</a>
 * @version $Id$
 */
public class MyTestJob implements Job
{
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException
    {
    	JobDataMap map = jobExecutionContext.getJobDetail().getJobDataMap();
    	String testValue = map.getString(ChenilleKitQuartzTestModule.TEST_STRING_KEY);
    	
    	DummyService dummy = (DummyService) map.get(ChenilleKitQuartzTestModule.DUMMY_SERVICE);
    	
    	dummy.runMePlease(testValue);
    	
        jobExecutionContext.setResult(String.format("Greetings from %s", this.getClass().getName()));
    }
}
