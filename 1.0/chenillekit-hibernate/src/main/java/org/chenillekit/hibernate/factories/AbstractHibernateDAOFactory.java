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

package org.chenillekit.hibernate.factories;

import java.lang.reflect.Constructor;

import org.hibernate.Session;

import org.chenillekit.hibernate.daos.AbstractHibernateDAO;
import org.slf4j.Logger;

/**
 * abstract hibernate based data access object factory.
 *
 * @version $Id$
 */
public class AbstractHibernateDAOFactory extends AbstractDAOFactory
{
    protected Logger logger;
    protected Session session;

    public AbstractHibernateDAOFactory(final Logger logger, final Session session)
    {
        this.logger = logger;
        this.session = session;
    }

    protected AbstractHibernateDAO instantiateDAO(Class daoClass)
    {
        try
        {
            Constructor constructor = daoClass.getConstructor(Logger.class, Session.class);
            return (AbstractHibernateDAO) constructor.newInstance(logger, session);
        }
        catch (Exception ex)
        {
            throw new RuntimeException("cant create DAO: " + daoClass, ex);
        }
    }
}
