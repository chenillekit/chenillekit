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

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.apache.tapestry.ioc.internal.util.Defense;

import org.chenillekit.hibernate.daos.GenericDAO;

/**
 * abstract data access object factory.
 *
 * @author <a href="mailto:homburgs@gmail.com">S.Homburg</a>
 * @version $Id$
 */
public abstract class AbstractDAOFactory implements GenericDAOFactory
{
    /**
     * get a DAO by the entity class.
     *
     * @param entityClass the entity class
     */
    public GenericDAO getDAO(Class entityClass)
    {
        return getDAO(getShortClassName(entityClass));
    }

    /**
     * get a DAO by the entity class name.
     *
     * @param entityClassName the entity class name
     *
     * @return generic dao
     */
    public GenericDAO getDAO(String entityClassName)
    {
        try
        {
            Method method = getClass().getMethod("get" + entityClassName + "DAO");
            return (GenericDAO) method.invoke(this);
        }
        catch (NoSuchMethodException e)
        {
            throw new RuntimeException(e.getLocalizedMessage(), e);
        }
        catch (IllegalAccessException e)
        {
            throw new RuntimeException(e.getLocalizedMessage(), e);
        }
        catch (InvocationTargetException e)
        {
            throw new RuntimeException(e.getLocalizedMessage(), e);
        }
    }

    private String getShortClassName(Class clasz)
    {
        Defense.notNull(clasz, "clasz");
        String className = clasz.getName();

        int lastPointPos = className.lastIndexOf('.');
        if (lastPointPos > 0)
            className = className.substring(lastPointPos + 1);

        return className;
    }

}
