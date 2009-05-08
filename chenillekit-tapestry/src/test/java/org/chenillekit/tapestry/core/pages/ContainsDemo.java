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
 *
 */

package org.chenillekit.tapestry.core.pages;

import java.util.List;

import org.apache.tapestry5.annotations.Component;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.ioc.internal.util.CollectionFactory;

import org.chenillekit.tapestry.core.components.Contains;

/**
 * @version $Id$
 */
public class ContainsDemo
{
    @Property
    private List<String> list;

    @Component(parameters = {"value=literal:test3", "list=prop:list"})
    private Contains contains1;

    @Component(parameters = {"value=literal:test6", "list=prop:list"})
    private Contains contains2;

    /**
     * Tapestry render phase method.
     * Initialize temporary instance variables here.
     */
    void setupRender()
    {
        list = CollectionFactory.newList();
        list.add("test1");
        list.add("test2");
        list.add("test3");
        list.add("test4");
        list.add("test5");
    }
}