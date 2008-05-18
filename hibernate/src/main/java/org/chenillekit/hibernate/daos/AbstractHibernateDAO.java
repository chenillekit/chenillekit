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

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

import org.apache.tapestry.hibernate.HibernateSessionManager;
import org.apache.tapestry.ioc.internal.util.Defense;

import org.hibernate.Criteria;
import org.hibernate.LockMode;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Order;
import org.hibernate.type.Type;

import org.slf4j.Logger;
import org.chenillekit.hibernate.utils.QueryParameter;

/**
 * abstract hibernate based data access object.
 *
 * @author <a href="mailto:homburgs@gmail.com">S.Homburg</a>
 * @version $Id$
 */
public abstract class AbstractHibernateDAO<T, ID extends Serializable> implements GenericDAO<T, ID>
{
    private Logger _logger;
    private Class<T> _persistentClass;
    private HibernateSessionManager _hsm;

    @SuppressWarnings("unchecked")
    public AbstractHibernateDAO()
    {
        _persistentClass = (Class<T>) ((java.lang.reflect.ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
    }

    /**
     * set the logging facility.
     *
     * @param logger logging facility
     */
    public void setLogger(Logger logger)
    {
        _logger = logger;
    }

    /**
     * get the logging facility.
     *
     * @return logging facility
     */
    public Logger getLogger()
    {
        Defense.notNull(_logger, "logger");
        return _logger;
    }

    /**
     * set hibernate session manager.
     *
     * @param hsm hibernate session manager.
     */
    public void setSessionManager(HibernateSessionManager hsm)
    {
        _hsm = hsm;
    }

    /**
     * commit all database changes.
     */
    public void commit()
    {
        _hsm.commit();
    }

    /**
     * rollback all database changes.
     */
    public void rollback()
    {
        _hsm.abort();
    }

    /**
     * get class type of entity.
     *
     * @return class type
     */
    public Class<T> getPersistentClass()
    {
        return _persistentClass;
    }

    /**
     * Force this session to flush. Must be called at the end of a
     * unit of work, before commiting the transaction and closing the
     * session (depending on {@link #setFlushMode flush-mode},
     * {@link org.hibernate.Transaction#commit()} calls this method).
     * <p/>
     * <i>Flushing</i> is the process of synchronizing the underlying persistent
     * store with persistable state held in memory.
     */
    public void flush()
    {
        _hsm.getSession().flush();
    }

    /**
     * Completely clear the session. Evict all loaded instances and cancel all pending
     * saves, updates and deletions. Do not close open iterators or instances of
     * <tt>ScrollableResults</tt>.
     */
    public void clear()
    {
        _hsm.getSession().clear();
    }

    /**
     * retrieve a list of entities by criteria.
     *
     * @param criterions array of criterions
     *
     * @return list of entities
     */
    @SuppressWarnings("unchecked")
    protected List<T> findByCriteria(Criterion... criterions)
    {
        Criteria crit = _hsm.getSession().createCriteria(getPersistentClass());

        for (Criterion c : criterions)
            crit.add(c);

        return crit.list();
    }

    /**
     * methode executes before entity retieved.
     * this implementation do nothing.
     *
     * @param id
     */
    public void postDoRetrieve(ID id)
    {
    }

    /**
     * retrieve a entity by his primary key.
     *
     * @param id   primary key
     * @param lock true sets LockMode.UPGRADE
     *
     * @return the entity.
     */
    @SuppressWarnings("unchecked")
    public T doRetrieve(ID id, boolean lock)
    {
        T entity;
        Defense.notNull(id, "id");

        if (lock)
            entity = (T) _hsm.getSession().load(getPersistentClass(), id, LockMode.UPGRADE);
        else
            entity = (T) _hsm.getSession().load(getPersistentClass(), id);

        return entity;
    }

    /**
     * methode executes after entity retieved.
     * this implementation do nothing.
     *
     * @param id
     */
    public void preDoRetrieve(ID id)
    {
    }

    /**
     * retrieve all entities.
     *
     * @return list of entities.
     */
    public List<T> findAll()
    {
        return findByCriteria();
    }

    /**
     * retrieve all entities sorted by <em>sortFields</em>.
     *
     * @param sortFields sort by fields
     *
     * @return all entities
     */
    public List<T> findAll(String... sortFields)
    {
        Criteria criteria = _hsm.getSession().createCriteria(getPersistentClass());

        for (String sortField : sortFields)
            criteria.addOrder(Order.asc(sortField));

        return criteria.list();
    }

    /**
     * retieve entites by HQL query.
     *
     * @param queryString the query to find entities.
     * @param parameters  the (optional) parameters for the query.
     *
     * @return list of entities
     */
    @SuppressWarnings("unchecked")
    public List<T> findByQuery(String queryString, QueryParameter... parameters)
    {
        return findByQuery(queryString, 0, 0, parameters);
    }

    /**
     * retieve entites by HQL query.
     *
     * @param queryString the query to fin entities.
     * @param offset      record number where start to read.
     * @param limit       amount of records to read.
     * @param parameters  the (optional) parameters for the query.
     *
     * @return list of entities
     */
    @SuppressWarnings("unchecked")
    public List<T> findByQuery(String queryString, int offset, int limit, QueryParameter... parameters)
    {
        Query query = _hsm.getSession().createQuery(queryString);
        for (QueryParameter parameter : parameters)
        {
            if (parameter.getParameterValue() instanceof Collection)
                query.setParameterList(parameter.getParameterName(), (Collection) parameter.getParameterValue());
            else
                query.setParameter(parameter.getParameterName(), parameter.getParameterValue());
        }

        if (limit > 0)
            query.setMaxResults(limit);

        if (offset > 0)
            query.setFirstResult(offset);

        if (_logger.isDebugEnabled())
            _logger.debug(query.getQueryString());

        return query.list();
    }

    /**
     * retieve entites by SQL query.
     *
     * @param queryString the query to find entities.
     *
     * @return list of entities
     */
    public List<T> findBySQLQuery(String queryString)
    {
        SQLQuery sqlQuery = _hsm.getSession().createSQLQuery(queryString);

        if (_logger.isDebugEnabled())
            _logger.debug(sqlQuery.getQueryString());

        return sqlQuery.list();
    }


    /**
     * wir holen uns die Anzahl der Entitaeten, die den uebergebenen <em>queryString</em> entsprechen.
     *
     * @param queryString the query to fin entities.
     * @param parameters  the (optional) parameters for the query.
     *
     * @return Anzahl der gefundenen Entitaeten.
     */
    @SuppressWarnings("unchecked")
    public Object countByQuery(String queryString, QueryParameter... parameters)
    {
        Query query = _hsm.getSession().createQuery(queryString);
        for (QueryParameter parameter : parameters)
        {
            if (parameter.getParameterValue() instanceof Collection)
                query.setParameterList(parameter.getParameterName(), (Collection) parameter.getParameterValue());
            else
                query.setParameter(parameter.getParameterName(), parameter.getParameterValue());
        }

        return query.uniqueResult();
    }

    /**
     * sends a query that retrieve an aggregate or group result.
     *
     * @param queryString the query to count entities.
     * @param parameters  the (optional) parameters for the query.
     *
     * @return aggregate or group result
     */
    public Object aggregateOrGroup(String queryString, QueryParameter... parameters)
    {
        Query query = _hsm.getSession().createQuery(queryString);
        for (QueryParameter parameter : parameters)
        {
            if (parameter.getParameterValue() instanceof Collection)
                query.setParameterList(parameter.getParameterName(), (Collection) parameter.getParameterValue());
            else
                query.setParameter(parameter.getParameterName(), parameter.getParameterValue());
        }

        boolean returnCollection = false;
        Type[] types = query.getReturnTypes();
        for (Type type : types)
        {
            if (type.isCollectionType())
                returnCollection = true;
        }

        return returnCollection ? query.list() : query.uniqueResult();
    }

    /**
     * methode executes before entity saved.
     * this implementation do nothing.
     *
     * @param entity
     */
    public void postDoSave(T entity)
    {
    }

    /**
     * Either {@link #save(Object)} or {@link #update(Object)} the given
     * instance, depending upon resolution of the unsaved-value checks (see the
     * manual for discussion of unsaved-value checking).
     * <p/>
     * This operation cascades to associated instances if the association is mapped
     * with <tt>cascade="save-update"</tt>.
     *
     * @param entity a transient or detached instance containing new or updated state
     *
     * @return entity
     */
    public T doSave(T entity)
    {
        preDoSave(entity);
        _hsm.getSession().saveOrUpdate(entity);
        postDoSave(entity);

        return entity;
    }

    /**
     * methode executes after entity saved.
     * this implementation do nothing.
     *
     * @param entity
     */
    public void preDoSave(T entity)
    {
    }

    /**
     * methode executes before entity deleted.
     * this implementation do nothing.
     *
     * @param entity
     */
    public void postDoDelete(T entity)
    {
    }

    /**
     * Remove a persistent instance from the datastore. The argument may be
     * an instance associated with the receiving <tt>Session</tt> or a transient
     * instance with an identifier associated with existing persistent state.
     * This operation cascades to associated instances if the association is mapped
     * with <tt>cascade="delete"</tt>.
     *
     * @param entity the instance to be removed
     */
    public void doDelete(T entity)
    {
        preDoDelete(entity);
        _hsm.getSession().delete(entity);
        postDoDelete(entity);
    }

    /**
     * Remove a persistent instance from the datastore. The <b>object</b> argument may be
     * an instance associated with the receiving <tt>Session</tt> or a transient
     * instance with an identifier associated with existing persistent state.
     * This operation cascades to associated instances if the association is mapped
     * with <tt>cascade="delete"</tt>.
     *
     * @param primaryKey the primary key of entity
     */
    public void doDelete(ID primaryKey)
    {
        doDelete(doRetrieve(primaryKey, false));
    }

    /**
     * methode executes after entity deleted.
     * this implementation do nothing.
     *
     * @param entity
     */
    public void preDoDelete(T entity)
    {
    }

    /**
     * Re-read the state of the given instance from the underlying database. It is
     * inadvisable to use this to implement long-running sessions that span many
     * business tasks. This method is, however, useful in certain special circumstances.
     * For example
     * <ul>
     * <li>where a database trigger alters the object state upon insert or update
     * <li>after executing direct SQL (eg. a mass update) in the same session
     * <li>after inserting a <tt>Blob</tt> or <tt>Clob</tt>
     * </ul>
     *
     * @param entity a persistent or detached instance
     */
    public T doRefresh(T entity)
    {
        _hsm.getSession().refresh(entity);
        return entity;
    }

    /**
     * Enable the named filter for this current session.
     *
     * @param filterName The name of the filter to be enabled.
     */
    public void enableFilter(String filterName)
    {
        _hsm.getSession().enableFilter(filterName);
    }

    /**
     * Disable the named filter for the current session.
     *
     * @param filterName The name of the filter to be disabled.
     */
    public void disableFilter(String filterName)
    {
        _hsm.getSession().disableFilter(filterName);
    }
}
