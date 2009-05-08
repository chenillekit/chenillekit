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

package org.chenillekit.quartz.services;

import org.quartz.Scheduler;

/**
 * manages the Quartz schedulers.
 *
 * @version $Id$
 */
public interface QuartzSchedulerManager
{
    /**
     * get the default scheduler.
     *
     * @return the default scheduler
     */
    Scheduler getScheduler();

    /**
     * get a named scheduler.
     *
     * @param schedulerId id of the scheduler
     *
     * @return a named scheduler
     */
    Scheduler getScheduler(String schedulerId);

    /**
     * shutdown all scheduler.
     */
    void shutdown();

    /**
     * shutdown a named scheduler.
     *
     * @param schedulerId id of the scheduler
     */
    void shutdown(String schedulerId);
}
