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

import org.chenillekit.hibernate.daos.AbstractHibernateDAO;
import org.hibernate.Session;
import org.slf4j.Logger;

/**
 * abstract hibernate based data access object factory.
 *
 * @author <a href="mailto:homburgs@gmail.com">S.Homburg</a>
 * @version $Id$
 */
public class AbstractHibernateDAOFactory extends AbstractDAOFactory
{
    protected Logger _serviceLog;
    protected Session session;

    public AbstractHibernateDAOFactory(Logger serviceLog, Session session)
    {
        this.session = session;
        _serviceLog = serviceLog;
    }

    protected AbstractHibernateDAO instantiateDAO(Class daoClass)
    {
        try
        {
            AbstractHibernateDAO dao = (AbstractHibernateDAO) daoClass.newInstance();
            dao.setLogger(_serviceLog);
            dao.setSession(session);
            return dao;
        }
        catch (Exception ex)
        {
            throw new RuntimeException("Can not instantiate DAO: " + daoClass, ex);
        }
    }
}
