/*
 * Apache License
 * Version 2.0, January 2004
 * http://www.apache.org/licenses/
 *
 * Copyright 2008-2010 by chenillekit.org
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 */

package org.chenillekit.hibernate.tests.daos;

import org.hibernate.Session;

import org.chenillekit.hibernate.daos.AbstractHibernateDAO;
import org.chenillekit.hibernate.tests.entities.User;
import org.slf4j.Logger;

/**
 * @version $Id$
 */
public class UserDAOHibernate extends AbstractHibernateDAO<User, Long> implements UserDAO
{
    public UserDAOHibernate(Logger logger, Session session)
    {
        super(logger, session);
    }
}
