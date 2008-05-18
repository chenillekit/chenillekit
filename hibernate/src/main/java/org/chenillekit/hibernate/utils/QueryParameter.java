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

package org.chenillekit.hibernate.utils;

/**
 * @author <a href="mailto:homburgs@gmail.com">S.Homburg</a>
 * @version $Id$
 */
public class QueryParameter
{
    private String _parameterName;
    private Object _parameterValue;

    public QueryParameter(String parameterName, Object parameterValue)
    {
        _parameterName = parameterName;
        _parameterValue = parameterValue;
    }

    public String getParameterName()
    {
        return _parameterName;
    }

    public Object getParameterValue()
    {
        return _parameterValue;
    }

    public static QueryParameter newInstance(String parameterName, Object parameterValue)
    {
        return new QueryParameter(parameterName, parameterValue);
    }
}
