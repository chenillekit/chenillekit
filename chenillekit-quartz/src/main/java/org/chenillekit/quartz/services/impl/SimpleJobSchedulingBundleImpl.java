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

import org.apache.tapestry5.ioc.internal.util.Defense;

import org.chenillekit.quartz.services.JobSchedulingBundle;
import org.quartz.JobDetail;
import org.quartz.Trigger;

/**
 * a simple implementation of a job detail/trigger bundle.
 *
 * @version $Id$
 */
public class SimpleJobSchedulingBundleImpl implements JobSchedulingBundle
{
    private JobDetail jobDetail;
    private Trigger trigger;
    private String schedulerId;

    public SimpleJobSchedulingBundleImpl(JobDetail jobDetail, Trigger trigger)
    {
        this(null, jobDetail, trigger);
    }

    public SimpleJobSchedulingBundleImpl(String schedulerId, JobDetail jobDetail, Trigger trigger)
    {
        Defense.notNull(jobDetail, "jobDetail");
        Defense.notNull(trigger, "trigger");

        this.schedulerId = schedulerId;
        this.jobDetail = jobDetail;
        this.trigger = trigger;
    }

    /**
     * get the scheduler id.
     * <p/>
     * may be null
     */
    public String getSchedulerId()
    {
        return schedulerId;
    }

    /**
     * get the job detail.
     */
    public JobDetail getJobDetail()
    {
        return jobDetail;
    }

    /**
     * get the trigger.
     */
    public Trigger getTrigger()
    {
        return trigger;
    }
}
