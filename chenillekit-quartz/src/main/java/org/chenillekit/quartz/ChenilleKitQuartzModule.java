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

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.tapestry5.ioc.Resource;
import org.apache.tapestry5.ioc.annotations.EagerLoad;
import org.apache.tapestry5.ioc.internal.util.ClasspathResource;
import org.apache.tapestry5.ioc.internal.util.CollectionFactory;
import org.apache.tapestry5.ioc.services.RegistryShutdownHub;
import org.apache.tapestry5.ioc.services.RegistryShutdownListener;

import org.chenillekit.quartz.services.JobSchedulingBundle;
import org.chenillekit.quartz.services.QuartzSchedulerManager;
import org.chenillekit.quartz.services.impl.QuartzSchedulerManagerImpl;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SchedulerFactory;
import org.quartz.impl.StdSchedulerFactory;
import org.slf4j.Logger;

/**
 * @author <a href="mailto:homburgs@googlemail.com">shomburg</a>
 * @version $Id$
 */
public class ChenilleKitQuartzModule
{
    /**
     * bind the <a href="http://www.opensymphony.com/quartz/">Quartz</a> scheduler factory.
     * <p/>
     * first we look for configuration contribution. if not exists (is null) we try to access
     * the quartz.properties in classpath.
     *
     * @param shutdownHub the shutdown hub
     *
     * @return scheduler factory
     */
    public static SchedulerFactory buildSchedulerFactory(Logger logger,
                                                         RegistryShutdownHub shutdownHub,
                                                         Map<String, Resource> contributions)
    {
        if (logger.isInfoEnabled())
            logger.info("initialize scheduler factory");

        try
        {
            Resource resource = contributions.get(ChenilleKitQuartsConstants.CONFIG_RESOURCE_KEY);
            if (resource == null)
                resource = new ClasspathResource("/" + ChenilleKitQuartsConstants.CONFIG_RESOURCE_KEY);

            if (!resource.exists())
                throw new RuntimeException(String.format("Quartz properties resource '%s' doesnt exists!", resource));

            InputStream in = resource.openStream();
            Properties prop = new Properties();
            prop.load(in);

            final SchedulerFactory factory = new StdSchedulerFactory(prop);

            shutdownHub.addRegistryShutdownListener(new RegistryShutdownListener()
            {
                /**
                 * Invoked when the registry shuts down, giving services a chance to perform any final operations. Service
                 * implementations should not attempt to invoke methods on other services (via proxies) as the service proxies may
                 * themselves be shutdown.
                 */
                public void registryDidShutdown()
                {
                    try
                    {
                        List<Scheduler> schedulers = CollectionFactory.newList(factory.getAllSchedulers());
                        for (Scheduler scheduler : schedulers)
                            scheduler.shutdown();
                    }
                    catch (SchedulerException e)
                    {
                        throw new RuntimeException(e);
                    }
                }
            });

            return factory;
        }
        catch (IOException ioe)
        {
            throw new RuntimeException(ioe);
        }
        catch (SchedulerException se)
        {
            throw new RuntimeException(se);
        }
    }

    /**
     * bind the <a href="http://www.opensymphony.com/quartz/">Quartz</a> based scheduler manager.
     *
     * @param schedulerFactory     the scheduler factory
     * @param jobSchedulingBundles list of job detail and trigger bundles
     *
     * @return scheduler manager
     */
    @EagerLoad
    public static QuartzSchedulerManager buildQuartzSchedulerManager(Logger logger, final SchedulerFactory schedulerFactory,
                                                                     final List<JobSchedulingBundle> jobSchedulingBundles)
    {
        return new QuartzSchedulerManagerImpl(logger, schedulerFactory, jobSchedulingBundles);
    }
}
