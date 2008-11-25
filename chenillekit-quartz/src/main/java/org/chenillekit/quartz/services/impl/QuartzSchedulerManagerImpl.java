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

import java.util.Collection;
import java.util.List;

import org.apache.tapestry5.ioc.internal.util.CollectionFactory;

import org.chenillekit.quartz.services.JobSchedulingBundle;
import org.chenillekit.quartz.services.QuartzSchedulerManager;
import org.quartz.CronTrigger;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SchedulerFactory;
import org.quartz.Trigger;
import org.slf4j.Logger;

/**
 * manages the Quartz schedulers.
 * <p/>
 * adding all configured job scheduling bundles to the default or named scheduler.
 *
 * @version $Id$
 */
public class QuartzSchedulerManagerImpl implements QuartzSchedulerManager
{
    private final SchedulerFactory schedulerFactory;
    private final Logger logger;

    public QuartzSchedulerManagerImpl(Logger logger,
                                      SchedulerFactory schedulerFactory,
                                      Collection<JobSchedulingBundle> jobSchedulingBundles)
    {
        this.logger = logger;
        this.schedulerFactory = schedulerFactory;

        try
        {
            for (JobSchedulingBundle jobSchedulingBundle : jobSchedulingBundles)
                addBundleToScheduler(jobSchedulingBundle);

            List<Scheduler> schedulers = CollectionFactory.newList(this.schedulerFactory.getAllSchedulers());
            for (Scheduler scheduler : schedulers)
                scheduler.start();
        }
        catch (SchedulerException e)
        {
            throw new RuntimeException(e);
        }
    }

    /**
     * get the default scheduler.
     *
     * @return the default scheduler
     */
    public Scheduler getScheduler()
    {
        return getScheduler(null);
    }

    /**
     * get a scheduler by his id.
     * <p/>
     * if <em>schedulerId</em> is null or length == 0, than returns the default scheduler.
     *
     * @param schedulerId id of the scheduler
     *
     * @return a named scheduler
     */
    public Scheduler getScheduler(String schedulerId)
    {
        Scheduler scheduler;

        try
        {
            if (schedulerId == null || schedulerId.length() == 0)
                scheduler = schedulerFactory.getScheduler();
            else
                scheduler = schedulerFactory.getScheduler(schedulerId);

            return scheduler;
        }
        catch (SchedulerException e)
        {
            throw new RuntimeException(e);
        }
    }

    /**
     * shutdown all schedulers.
     */
    public void shutdown()
    {
        try
        {
            List<Scheduler> schedulers = CollectionFactory.newList(schedulerFactory.getAllSchedulers());
            for (Scheduler scheduler : schedulers)
                shutdown(scheduler.getSchedulerName());
        }
        catch (SchedulerException e)
        {
            throw new RuntimeException(e);
        }
    }

    /**
     * shutdown a scheduler by his id.
     *
     * @param schedulerId id of the scheduler
     */
    public void shutdown(String schedulerId)
    {
        try
        {
            Scheduler scheduler = schedulerFactory.getScheduler(schedulerId);
            scheduler.shutdown();
        }
        catch (SchedulerException e)
        {
            throw new RuntimeException(e);
        }
    }

    /**
     * adding a job detail/trigger bundle to the named or default scheduler.
     *
     * @param jobSchedulingBundle the job detail/trigger bundle
     */
    @SuppressWarnings({"JavaDoc"})
    private void addBundleToScheduler(JobSchedulingBundle jobSchedulingBundle) throws SchedulerException
    {
        JobDetail jobDetail = jobSchedulingBundle.getJobDetail();
        Trigger trigger = jobSchedulingBundle.getTrigger();
        String schedulerId = jobSchedulingBundle.getSchedulerId();

        Scheduler scheduler = getScheduler(schedulerId);

        if (schedulerId == null || schedulerId.length() == 0)
            schedulerId = "default";

        if (trigger != null)
        {

            if (logger.isInfoEnabled())
            {
                String triggerName = trigger.getName();
                if (trigger instanceof CronTrigger)
                    triggerName += " (" + ((CronTrigger)trigger).getCronExpression() + ")";

                logger.info("schedule job '{}' with trigger '{}' to scheduler '{}'",
                            new Object[]{jobDetail.getName(), triggerName, schedulerId});
            }

            scheduler.scheduleJob(jobDetail, trigger);
        }
        else
        {
            if (logger.isInfoEnabled())
                logger.info("add job '{}' to scheduler '{}'", jobDetail.getName(), schedulerId);

            scheduler.addJob(jobDetail, true);
        }
    }
}
