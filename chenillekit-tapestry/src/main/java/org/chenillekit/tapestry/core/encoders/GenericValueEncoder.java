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

package org.chenillekit.tapestry.core.encoders;

import java.util.List;

import org.apache.tapestry5.ValueEncoder;
import org.apache.tapestry5.ioc.internal.util.CollectionFactory;
import org.apache.tapestry5.ioc.services.PropertyAccess;

/**
 * @version $Id$
 */
public class GenericValueEncoder<T> implements ValueEncoder<T>
{
    private List<T> _objectList;
    private final PropertyAccess _access;
    private String _valueFieldName = null;

    public GenericValueEncoder(List<T> objectList)
    {
        this(objectList, null, null);
    }

    public GenericValueEncoder(List<T> objectList, String valueFieldName, PropertyAccess propertyAccess)
    {
        assert objectList != null;

        // more carfully i think, so we copy the object list
        _objectList = CollectionFactory.newList(objectList);
        _valueFieldName = valueFieldName;
        _access = propertyAccess;
    }

    public String toClient(T serverValue)
    {
        return getServerValueAsString(serverValue);
    }

    public T toValue(String clientValue)
    {
        T serverValue = null;

        for (T obj : _objectList)
        {
            String value = getServerValueAsString(obj);
            if (value.equals(clientValue))
            {
                serverValue = obj;
                break;
            }
        }

        return serverValue;
    }

    private String getServerValueAsString(T serverValue)
    {
        String clientValue = "";

        if (_valueFieldName != null && _access != null)
            clientValue = String.valueOf(_access.get(serverValue, _valueFieldName));
        else
            clientValue = String.valueOf(serverValue);

        return clientValue;
    }
}
