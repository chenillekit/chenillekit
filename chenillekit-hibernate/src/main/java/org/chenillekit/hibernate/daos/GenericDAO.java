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
import java.util.List;

import org.chenillekit.hibernate.utils.QueryParameter;

/**
 * a generic data access object.
 *
 * @author <a href="mailto:homburgs@gmail.com">S.Homburg</a>
 * @version $Id$
 */
public interface GenericDAO<T, ID extends Serializable>
{
    /**
     * get class type of entity.
     *
     * @return class type
     */
    Class<T> getPersistentClass();

    /**
     * retrieve all entities.
     *
     * @return list of entities.
     */
    List<T> findAll();

    /**
     * retrieve all entities sorted by <em>sortFields</em>.
     *
     * @param sortFields sort by fields
     *
     * @return all entities
     */
    List<T> findAll(String... sortFields);

    /**
     * retieve entites by query.
     *
     * @param queryString the query to find entities.
     * @param parameters  the (optional) parameters for the query.
     *
     * @return list of entities
     */
    List<T> findByQuery(String queryString, QueryParameter... parameters);

    /**
     * retieve entites by query.
     *
     * @param queryString the query to fin entities.
     * @param offset      record number where start to read.
     * @param limit       amount of records to read.
     * @param parameters  the (optional) parameters for the query.
     *
     * @return list of entities
     */
    List<T> findByQuery(String queryString, int offset, int limit, QueryParameter... parameters);

    /**
     * sends a COUNT(*) query to database.
     *
     * @param queryString the query to count entities.
     * @param parameters  the (optional) parameters for the query.
     *
     * @return
     */
    Object countByQuery(String queryString, QueryParameter... parameters);

    /**
     * sends a query that retrieve an aggregate or group result.
     *
     * @param queryString the query to count entities.
     * @param parameters  the (optional) parameters for the query.
     *
     * @return aggregate or group result
     */
    Object aggregateOrGroup(String queryString, QueryParameter... parameters);

    /**
     * methode executes before entity retieved.
     *
     * @param id
     */
    void postDoRetrieve(ID id);

    /**
     * retrieve a entity by his primary key.
     *
     * @param id   primary key
     * @param lock true sets LockMode.UPGRADE
     *
     * @return the entity.
     */
    T doRetrieve(ID id, boolean lock);

    /**
     * methode executes after entity retieved.
     *
     * @param id
     */
    void preDoRetrieve(ID id);

    /**
     * methode executes before entity saved.
     *
     * @param entity
     */
    void postDoSave(T entity);

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
    T doSave(T entity);

    /**
     * methode executes after entity saved.
     *
     * @param entity
     */
    void preDoSave(T entity);

    /**
     * methode executes before entity deleted.
     *
     * @param entity
     */
    void postDoDelete(T entity);

    /**
     * Remove a persistent instance from the datastore. The argument may be
     * an instance associated with the receiving <tt>Session</tt> or a transient
     * instance with an identifier associated with existing persistent state.
     * This operation cascades to associated instances if the association is mapped
     * with <tt>cascade="delete"</tt>.
     *
     * @param entity the instance to be removed
     */
    void doDelete(T entity);

    /**
     * Remove a persistent instance from the datastore. The argument may be
     * an instance associated with the receiving <tt>Session</tt> or a transient
     * instance with an identifier associated with existing persistent state.
     * This operation cascades to associated instances if the association is mapped
     * with <tt>cascade="delete"</tt>.
     *
     * @param primaryKey the primary key of entity
     */
    void doDelete(ID primaryKey);

    /**
     * methode executes after entity deleted.
     *
     * @param entity
     */
    void preDoDelete(T entity);

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
    T doRefresh(T entity);

    /**
     * Enable the named filter.
     *
     * @param filterName The name of the filter to be enabled.
     */
    public void enableFilter(String filterName);

    /**
     * Disable the named filter.
     *
     * @param filterName The name of the filter to be disabled.
     */
    public void disableFilter(String filterName);

}
