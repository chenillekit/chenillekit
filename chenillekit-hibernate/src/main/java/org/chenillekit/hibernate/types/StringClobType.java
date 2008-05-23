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

package org.chenillekit.hibernate.types;

import java.io.Serializable;
import java.sql.Clob;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;

import org.hibernate.HibernateException;
import org.hibernate.usertype.UserType;

/**
 * @author <a href="mailto:homburgs@gmail.com">S.Homburg</a>
 * @version $Id$
 */
public class StringClobType implements UserType
{

    public StringClobType()
    {
    }

    public int[] sqlTypes()
    {
        return new int[]{Types.CLOB};
    }

    public Class returnedClass()
    {
        return String.class;
    }

    public boolean equals(Object x, Object y)
    {
        return (x == y) || (x != null && y != null && x.equals(y));
    }

    public Object nullSafeGet(ResultSet rs, String[] names, Object owner)
            throws HibernateException, SQLException
    {
        Clob clob = rs.getClob(names[0]);
        if (clob != null)
            return clob.getSubString(1, (int) clob.length());

        return "";
    }

    public void nullSafeSet(PreparedStatement st, Object value, int index)
            throws HibernateException, SQLException
    {
        st.setAsciiStream(index, new java.io.ByteArrayInputStream(((String) value).getBytes()), ((String) value).length());
    }

    public Object deepCopy(Object value)
    {
        if (value == null) return null;
        return (String) value;
    }

    public boolean isMutable()
    {
        return false;
    }

    /**
     * Get a hashcode for the instance, consistent with persistence "equality"
     */
    public int hashCode(Object x) throws HibernateException
    {
        return x.hashCode();
    }

    /**
     * Transform the object into its cacheable representation. At the very least this
     * method should perform a deep copy if the type is mutable. That may not be enough
     * for some implementations, however; for example, associations must be cached as
     * identifier values. (optional operation)
     *
     * @param value the object to be cached
     *
     * @return a cachable representation of the object
     *
     * @throws org.hibernate.HibernateException
     *
     */
    public Serializable disassemble(Object value) throws HibernateException
    {
        return (Serializable) deepCopy(value);
    }

    /**
     * Reconstruct an object from the cacheable representation. At the very least this
     * method should perform a deep copy if the type is mutable. (optional operation)
     *
     * @param cached the object to be cached
     * @param owner  the owner of the cached object
     *
     * @return a reconstructed object from the cachable representation
     *
     * @throws org.hibernate.HibernateException
     *
     */
    public Object assemble(Serializable cached, Object owner) throws HibernateException
    {
        return deepCopy(cached);
    }

    /**
     * During merge, replace the existing (target) value in the entity we are merging to
     * with a new (original) value from the detached entity we are merging. For immutable
     * objects, or null values, it is safe to simply return the first parameter. For
     * mutable objects, it is safe to return a copy of the first parameter. For objects
     * with component values, it might make sense to recursively replace component values.
     *
     * @param original the value from the detached entity being merged
     * @param target   the value in the managed entity
     *
     * @return the value to be merged
     */
    public Object replace(Object original, Object target, Object owner) throws HibernateException
    {
        return deepCopy(original);
    }
}
