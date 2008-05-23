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
import java.util.Date;

import org.hibernate.EmptyInterceptor;
import org.hibernate.type.Type;

import org.chenillekit.hibernate.entities.Audit;
import org.chenillekit.hibernate.entities.Auditable;
import org.slf4j.Logger;

/**
 * inserts audit informations into an entity object that implements @see Auditable.
 *
 * @author <a href="mailto:homburgs@gmail.com">S.Homburg</a>
 * @version $Id$
 */
public class AuditInterceptor extends EmptyInterceptor
{
    private static final long serialVersionUID = -1136936304886987684L;
    private String _userName = "unknown";
    private Logger _logger;

    public AuditInterceptor(Logger logger)
    {
        super();
        _logger = logger;
    }

    public void setUser(String userName)
    {
        _userName = userName;
    }


    public String getUserName()
    {
        return _userName;
    }

    public Logger getLogger()
    {
        return _logger;
    }

    /**
     * Called before an object is saved. The interceptor may modify the <tt>state</tt>, which will be used for
     * the SQL <tt>INSERT</tt> and propagated to the persistent object.
     *
     * @return <tt>true</tt> if the user modified the <tt>state</tt> in any way.
     */
    public boolean onSave(Object entity, Serializable id, Object[] currentState, String[] propertyNames, Type[] types)
    {
        boolean modified = false;

        if (entity instanceof Auditable)
        {
            Audit audit = getAudit(entity);

            audit.setCreated(new Date());
            audit.setCreatedBy(getUserName());

            for (int i = 0; i < types.length; i++)
            {
                Type type = types[i];
                if (type.getReturnedClass() == Audit.class)
                    currentState[i] = audit;
            }

            modified = true;

            if (_logger.isDebugEnabled())
                _logger.debug("set audit informations '{}' for '{}'", audit.toString(), entity.toString());
        }
        return modified;
    }

    /**
     * Called when an object is detected to be dirty, during a flush. The interceptor may modify the detected
     * <tt>currentState</tt>, which will be propagated to both the database and the persistent object.
     * Note that not all flushes end in actual synchronization with the database, in which case the
     * new <tt>currentState</tt> will be propagated to the object, but not necessarily (immediately) to
     * the database. It is strongly recommended that the interceptor <b>not</b> modify the <tt>previousState</tt>.
     *
     * @return <tt>true</tt> if the user modified the <tt>currentState</tt> in any way.
     */
    public boolean onFlushDirty(Object entity, Serializable id, Object[] currentState, Object[] previousState, String[] propertyNames, Type[] types)
    {
        boolean modified = false;

        if (entity instanceof Auditable)
        {
            Audit audit = getAudit(entity);

            audit.setUpdated(new Date());
            audit.setUpdatedBy(getUserName());

            for (int i = 0; i < types.length; i++)
            {
                Type type = types[i];
                if (type.getReturnedClass() == Audit.class)
                    currentState[i] = audit;
            }

            modified = true;

            if (_logger.isDebugEnabled())
                _logger.debug("set audit informations '{}' for '{}'", audit.toString(), entity.toString());
        }
        return modified;
    }

    /**
     * check for instantiated audit object.
     *
     * @param entity instantiated audit object
     *
     * @return audit object attached to entity
     */
    protected Audit getAudit(Object entity)
    {
        Audit audit = ((Auditable) entity).getAudit();
        if (audit == null)
        {
            audit = new Audit();
            ((Auditable) entity).setAudit(audit);
        }

        return audit;
    }
}
