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

import org.hibernate.Session;

import org.chenillekit.hibernate.daos.AddressDAO;
import org.chenillekit.hibernate.daos.AddressDAOHibernate;
import org.chenillekit.hibernate.daos.UserDAO;
import org.chenillekit.hibernate.daos.UserDAOHibernate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This is a factory for the test DAOs.
 *
 * @author <a href="mailto:homburgs@gmail.com">S.Homburg</a>
 * @version $Id$
 */
public class TestDAOFactory extends AbstractHibernateDAOFactory
{
    private static Logger logger = LoggerFactory.getLogger(TestDAOFactory.class);

    public TestDAOFactory(Session session)
    {
        super(logger, session);
    }

    public UserDAO getUserDAO()
    {
        return (UserDAO) instantiateDAO(UserDAOHibernate.class);
    }

    public AddressDAO getAddressDAO()
    {
        return (AddressDAO) instantiateDAO(AddressDAOHibernate.class);
    }
}
