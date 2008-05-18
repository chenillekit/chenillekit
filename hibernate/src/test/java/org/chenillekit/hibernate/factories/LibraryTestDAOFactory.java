/*
 * Apache License
 * Version 2.0, January 2004
 * http://www.apache.org/licenses/
 *
 * Copyright 1996-2008 by Sven Homburg
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 */

package org.chenillekit.hibernate.factories;

import org.apache.tapestry.hibernate.HibernateSessionManager;

import org.chenillekit.hibernate.daos.AddressDAO;
import org.chenillekit.hibernate.daos.AddressDAOHibernate;
import org.chenillekit.hibernate.daos.UserDAO;
import org.chenillekit.hibernate.daos.UserDAOHibernate;
import org.slf4j.Logger;

/**
 * This is a factory for the test DAOs.
 *
 * @author <a href="mailto:homburgs@gmail.com">S.Homburg</a>
 * @version $Id$
 */
public class LibraryTestDAOFactory extends AbstractHibernateDAOFactory
{
    public LibraryTestDAOFactory(Logger serviceLog, HibernateSessionManager sessionManager)
    {
        super(serviceLog, sessionManager);
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
