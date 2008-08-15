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

package org.chenillekit.demo.pages;

import org.apache.tapestry5.annotations.Component;
import org.apache.tapestry5.annotations.Mixins;
import org.apache.tapestry5.annotations.OnEvent;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.corelib.components.Select;
import org.apache.tapestry5.json.JSONObject;

import org.chenillekit.demo.components.LeftSideMenu;

/**
 * @author <a href="mailto:homburgs@gmail.com">shomburg</a>
 * @version $Id$
 */
public class OnEventDemo
{
    @Component(parameters = {"menuName=demo"})
    private LeftSideMenu menu;

    @Property
    private String selected1;

    @Component(parameters = {"value=selected1", "model=literal:silver, orange, yellow, green, blue, black", "event=change",
            "onCompleteCallback=literal:onCompleteFunction"})
    @Mixins({"ck/OnEvent"})
    private Select select1;

    @OnEvent(component = "select1", value = "change")
    public JSONObject onChangeEvent(String value)
    {
        JSONObject json = new JSONObject();
        json.put("color", value);
        return json;
    }
}