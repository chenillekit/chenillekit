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

package org.chenillekit.hibernate.tests;

import java.util.Date;
import java.util.List;

import org.chenillekit.hibernate.AbstractHibernateTest;
import org.chenillekit.hibernate.tests.daos.UserDAO;
import org.chenillekit.hibernate.tests.entities.Address;
import org.chenillekit.hibernate.tests.entities.Pseudonym;
import org.chenillekit.hibernate.tests.entities.User;
import org.chenillekit.hibernate.utils.QueryParameter;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

/**
 * @version $Id$
 */
public class TestUserDAO extends AbstractHibernateTest
{
    @BeforeTest
    public void setup()
    {
        super.setup();
    }

    @Test
    public void test_persist_user_entity()
    {
        UserDAO userDAO = registry.getService(UserDAO.class);

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
        user.getPseudonyms().add(new Pseudonym(user, "jolli"));
        user.getPseudonyms().add(new Pseudonym(user, "trugoy"));
        user.getPseudonyms().add(new Pseudonym(user, "hombi"));

        System.err.println("test_persist_user_entity: " + userDAO);
        userDAO.doSave(user);
    }

    @Test(dependsOnMethods = {"test_persist_user_entity"})
    public void test_find_user_entity()
    {
        UserDAO userDAO = registry.getService(UserDAO.class);
        List entityList = userDAO.findByQuery("FROM User WHERE loginName = :loginName",
                                              QueryParameter.newInstance("loginName", "homburgs"));

        assertEquals(entityList.size(), 1);
    }

    @Test(dependsOnMethods = {"test_persist_user_entity"})
    public void test_remove_pseudonym_entity()
    {
        UserDAO userDAO = registry.getService(UserDAO.class);
        List<User> entityList = userDAO.findByQuery("FROM User WHERE loginName = :loginName",
                                              QueryParameter.newInstance("loginName", "homburgs"));

        for (User user : entityList)
        {
            for (Pseudonym pseudonym : user.getPseudonyms())
            {
                user.getPseudonyms().remove(pseudonym);
                break;
            }
            userDAO.doSave(user);
            assertEquals(user.getPseudonyms().size(), 2);
        }
    }

    @Test(dependsOnMethods = {"test_persist_user_entity"})
    public void test_group_by_loginname()
    {
        UserDAO userDAO = registry.getService(UserDAO.class);
        String result = (String) userDAO.aggregateOrGroup("SELECT loginName FROM User GROUP BY loginName");

        assertEquals(result, "homburgs");
    }

    @Test(dependsOnMethods = {"test_persist_user_entity"})
    public void test_max_id()
    {
        UserDAO userDAO = registry.getService(UserDAO.class);
        long result = (Long) userDAO.aggregateOrGroup("SELECT MAX(id) FROM User");

        assertEquals(result, 1);
    }
}
