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

package org.chenillekit.hibernate.daos;

import java.util.Date;
import java.util.List;

import org.apache.tapestry5.hibernate.HibernateModule;
import org.apache.tapestry5.hibernate.HibernateSessionManager;
import org.apache.tapestry5.ioc.Registry;
import org.apache.tapestry5.ioc.RegistryBuilder;
import org.apache.tapestry5.services.TapestryModule;

import org.hibernate.SQLQuery;

import org.chenillekit.hibernate.LibraryTestModule;
import org.chenillekit.hibernate.entities.Address;
import org.chenillekit.hibernate.entities.User;
import org.chenillekit.hibernate.factories.GenericDAOFactory;
import org.chenillekit.hibernate.utils.QueryParameter;
import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

/**
 * @author <a href="mailto:homburgs@gmail.com">S.Homburg</a>
 * @version $Id$
 */
public class TestUserDAO extends Assert
{
    private Registry _registry;

    @BeforeTest
    public void beforeTest()
    {
        RegistryBuilder builder = new RegistryBuilder();
        builder.add(TapestryModule.class, HibernateModule.class, LibraryTestModule.class);
        _registry = builder.build();
        _registry.performRegistryStartup();
    }

    @Test
    public void persist_user_entity()
    {
        GenericDAOFactory daoFactory = _registry.getService(GenericDAOFactory.class);
        UserDAO userDAO = (UserDAO) daoFactory.getDAO(User.class);

        User user = new User("homburgs", "password");
        user.setLastLogin(new Date());
        user.setActive(true);

        Address address = new Address();
        address.setName1("Sven");
        address.setName1("Homburg");
        address.setStreet("WhereEver");
        address.setCity("SmallTown");
        address.setZip("11111");
        address.setEmail("homburgs@gmail.com");

        user.setAddress(address);

        userDAO.doSave(user);
    }

    @Test(dependsOnMethods = {"persist_user_entity"})
    public void find_user_entity()
    {
        GenericDAOFactory daoFactory = _registry.getService(GenericDAOFactory.class);
        UserDAO userDAO = (UserDAO) daoFactory.getDAO(User.class);

        List entityList = userDAO.findByQuery("FROM User WHERE loginName = :loginName",
                                              QueryParameter.newInstance("loginName", "homburgs"));


        assertEquals(entityList.size(), 1);
    }

    @Test(dependsOnMethods = {"persist_user_entity"})
    public void group_by_loginname()
    {
        GenericDAOFactory daoFactory = _registry.getService(GenericDAOFactory.class);
        UserDAO userDAO = (UserDAO) daoFactory.getDAO(User.class);

        String result = (String) userDAO.aggregateOrGroup("SELECT loginName FROM User GROUP BY loginName");

        assertEquals(result, "homburgs");
    }

    @Test(dependsOnMethods = {"persist_user_entity"})
    public void max_id()
    {
        GenericDAOFactory daoFactory = _registry.getService(GenericDAOFactory.class);
        UserDAO userDAO = (UserDAO) daoFactory.getDAO(User.class);

        long result = (Long) userDAO.aggregateOrGroup("SELECT MAX(id) FROM User");

        assertEquals(result, 1);
    }

    @AfterTest
    public void afterTest()
    {
        _registry.cleanupThread();
        HibernateSessionManager manager = _registry.getService(HibernateSessionManager.class);
        SQLQuery query = manager.getSession().createSQLQuery("SHUTDOWN");
        query.executeUpdate();
    }
}
