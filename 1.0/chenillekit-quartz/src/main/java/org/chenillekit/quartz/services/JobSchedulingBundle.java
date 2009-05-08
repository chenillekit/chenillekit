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

import org.quartz.JobDetail;
import org.quartz.Trigger;

/**
 * job detail/trigger bundle.
 *
 * @version $Id$
 */
@SuppressWarnings({"JavaDoc"})
public interface JobSchedulingBundle
{
    /**
     * get the scheduler id.
     * <p/>
     * may be null
     */
    String getSchedulerId();

    /**
     * get the job detail.
     */
    JobDetail getJobDetail();

    /**
     * get the trigger.
     */
    Trigger getTrigger();
}
