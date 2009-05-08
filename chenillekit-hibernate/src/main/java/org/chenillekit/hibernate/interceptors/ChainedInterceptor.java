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

package org.chenillekit.hibernate.interceptors;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.hibernate.CallbackException;
import org.hibernate.EmptyInterceptor;
import org.hibernate.Interceptor;
import org.hibernate.Transaction;
import org.hibernate.type.Type;

/**
 * Implementation of the Hibernate <code>Interceptor</code>
 * interface that allows the chaining of several different
 * instances of the same interface.
 *
 * @version $Id$
 */
public class ChainedInterceptor extends EmptyInterceptor
{
    private static final long serialVersionUID = -2793570429597347233L;
    private HashMap<String, Interceptor> interceptors = new HashMap<String, Interceptor>();

    /**
     * Constructor
     */
    public ChainedInterceptor()
    {
        super();
    }

    /**
     * add a new interceptor.
     *
     * @param key
     * @param interceptor
     */
    public void addInterceptor(String key, Interceptor interceptor)
    {
        interceptors.put(key, interceptor);
    }

    public Interceptor getInterceptor(String key)
    {
        return interceptors.get(key);
    }

    /**
     * Returns an array containing the instances of the <code>Interceptor</code>
     * interface that are chained within this interceptor.
     *
     * @return map of interceptors
     */
    public HashMap<String, Interceptor> getInterceptors()
    {
        return interceptors;
    }

    public void removeAllInterceptors()
    {
        interceptors.clear();
    }

    public void onDelete(Object entity, Serializable id, Object[] state, String[] propertyNames, Type[] types)
    {
        for (Object o : interceptors.entrySet())
        {
            Map.Entry entry = (Map.Entry) o;
            Interceptor interceptor = (Interceptor) entry.getValue();
            interceptor.onDelete(entity, id, state, propertyNames, types);
        }
    }

    public boolean onLoad(Object entity, Serializable id, Object[] state, String[] propertyNames, Type[] types)
    {
        boolean result = false;
        for (Object o : interceptors.entrySet())
        {
            Map.Entry entry = (Map.Entry) o;
            Interceptor interceptor = (Interceptor) entry.getValue();
            if (interceptor.onLoad(entity, id, state, propertyNames, types))
                result = true;
        }
        return result;
    }

    public void postFlush(Iterator entities)
    {
        for (Object o : interceptors.entrySet())
        {
            Map.Entry entry = (Map.Entry) o;
            Interceptor interceptor = (Interceptor) entry.getValue();
            interceptor.postFlush(entities);
        }
    }

    public void preFlush(Iterator entities)
    {
        for (Object o : interceptors.entrySet())
        {
            Map.Entry entry = (Map.Entry) o;
            Interceptor interceptor = (Interceptor) entry.getValue();
            interceptor.preFlush(entities);
        }
    }

    public Boolean isTransient(Object entity)
    {
        boolean result = false;
        for (Object o : interceptors.entrySet())
        {
            Map.Entry entry = (Map.Entry) o;
            Interceptor interceptor = (Interceptor) entry.getValue();
            if (interceptor.isTransient(entity))
                result = true;
        }
        return result;
    }

    public void afterTransactionBegin(Transaction tx)
    {
        for (Object o : interceptors.entrySet())
        {
            Map.Entry entry = (Map.Entry) o;
            Interceptor interceptor = (Interceptor) entry.getValue();
            interceptor.afterTransactionBegin(tx);
        }
    }

    public void afterTransactionCompletion(Transaction tx)
    {
        for (Object o : interceptors.entrySet())
        {
            Map.Entry entry = (Map.Entry) o;
            Interceptor interceptor = (Interceptor) entry.getValue();
            interceptor.afterTransactionCompletion(tx);
        }
    }

    public void beforeTransactionCompletion(Transaction tx)
    {
        for (Object o : interceptors.entrySet())
        {
            Map.Entry entry = (Map.Entry) o;
            Interceptor interceptor = (Interceptor) entry.getValue();
            interceptor.beforeTransactionCompletion(tx);
        }
    }

    public String onPrepareStatement(String sql)
    {
        for (Object o : interceptors.entrySet())
        {
            Map.Entry entry = (Map.Entry) o;
            Interceptor interceptor = (Interceptor) entry.getValue();
            sql = interceptor.onPrepareStatement(sql);
        }

        return sql;
    }


    /**
     * Called before an object is saved. The interceptor may modify the <tt>state</tt>, which will be used for
     * the SQL <tt>INSERT</tt> and propagated to the persistent object.
     *
     * @return <tt>true</tt> if the user modified the <tt>state</tt> in any way.
     */
    public boolean onSave(Object entity, Serializable id, Object[] state, String[] propertyNames, Type[] types) throws CallbackException
    {
        boolean result = false;
        for (Object o : interceptors.entrySet())
        {
            Map.Entry entry = (Map.Entry) o;
            Interceptor interceptor = (Interceptor) entry.getValue();
            if (interceptor.onSave(entity, id, state, propertyNames, types))
                result = true;
        }
        return result;
    }

    /**
     * Called before a flush
     */
    public boolean onFlushDirty(Object entity, Serializable id, Object[] currentState, Object[] previousState, String[] propertyNames, Type[] types)
    {
        boolean result = false;
        for (Object o : interceptors.entrySet())
        {
            Map.Entry entry = (Map.Entry) o;
            Interceptor interceptor = (Interceptor) entry.getValue();
            if (interceptor.onFlushDirty(entity, id, currentState, previousState, propertyNames, types))
                result = true;
        }
        return result;
    }
}
