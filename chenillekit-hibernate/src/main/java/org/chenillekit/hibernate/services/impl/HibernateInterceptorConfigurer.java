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

package org.chenillekit.hibernate.services.impl;

import org.apache.tapestry5.hibernate.HibernateConfigurer;

import org.hibernate.Interceptor;
import org.hibernate.cfg.Configuration;

/**
 * @version $Id$
 */
public class HibernateInterceptorConfigurer implements HibernateConfigurer
{
    private Interceptor _interceptor;

    public HibernateInterceptorConfigurer(Interceptor interceptor)
    {
        _interceptor = interceptor;
    }

    /**
     * Passed the configuration so as to make changes.
     */
    public void configure(Configuration configuration)
    {
        configuration.setInterceptor(_interceptor);
    }
}
