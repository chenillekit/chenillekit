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
 * SQL string helper class.
 *
 * @author <a href="mailto:shomburg@hsofttec.com">S.Homburg</a>
 * @version $Id$
 */
public class SQLString
{
    private boolean _whereAdded = false;
    private boolean _groupAdded = false;
    private boolean _orderAdded = false;
    private final String _sqlString;
    private static final String _trailingLineFeed = System.getProperty("line.separator");
    private String _orderClause = "";
    private String _whereClause = "";
    private String _orClause = "";
    private String _groupClause = "";
    private String _fromClause = "";

    public SQLString(String sqlString)
    {
        _sqlString = sqlString;
    }

    public void addWhereClause(String whereClause)
    {
        _whereClause += (!_whereAdded ? " WHERE " : " AND ") + whereClause + _trailingLineFeed;
        _whereAdded = true;
    }

    public void addOrClause(String orClause)
    {
        _orClause += (!_whereAdded ? " WHERE " : " OR ") + orClause + _trailingLineFeed;
        _whereAdded = true;
    }

    public void setOrderClause(String orderClause)
    {
        _orderClause = " ORDER BY " + orderClause;
        _orderAdded = true;
    }

    public void addOrderField(String orderField)
    {
        _orderClause += (!_orderAdded ? " ORDER BY " : ", ") + orderField;
        _orderAdded = true;
    }

    public void addGroupField(String groupField)
    {
        _groupClause += (!_groupAdded ? " GROUP BY " : ", ") + groupField;
        _groupAdded = true;
    }

    public void addFromClause(String fromClause)
    {
        _fromClause += " " + fromClause;
    }

    public boolean isWhereAdded()
    {
        return _whereAdded;
    }

    public String toString()
    {
        return _sqlString + _fromClause + _whereClause + _orClause + _groupClause + _orderClause;
    }
}
