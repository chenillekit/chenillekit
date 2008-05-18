package org.chenillekit.hibernate.services.impl;

import org.apache.tapestry.hibernate.HibernateConfigurer;

import org.hibernate.Interceptor;
import org.hibernate.cfg.Configuration;

/**
 * @author <a href="mailto:homburgs@googlemail.com">shomburg</a>
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
