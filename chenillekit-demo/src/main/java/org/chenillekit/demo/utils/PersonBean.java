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

package org.chenillekit.demo.utils;

/**
 * @author <a href="mailto:homburgs@googlemail.com">homburgs</a>
 * @version $Id$
 */
public class PersonBean
{
    int _id;
    String _name;
    String _city;

    public PersonBean(int id, String name, String city)
    {
        _id = id;
        _name = name;
        _city = city;
    }

    public int getId()
    {
        return _id;
    }

    public String getName()
    {
        return _name;
    }

    public void setName(String name)
    {
        _name = name;
    }

    public String getCity()
    {
        return _city;
    }

    public void setCity(String city)
    {
        _city = city;
    }
}
