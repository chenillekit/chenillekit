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

package org.chenillekit.demo.pages.tapcomp;

import org.apache.tapestry5.annotations.Component;
import org.apache.tapestry5.annotations.OnEvent;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.corelib.components.Form;
import org.apache.tapestry5.json.JSONObject;

import org.chenillekit.demo.components.LeftSideMenu;
import org.chenillekit.tapestry.core.components.AjaxCheckbox;

/**
 * @version $Id$
 */
public class AjaxCheckboxDemo
{
    @Persist
    @Property
    private boolean selected;

    @Component(parameters = {"menuName=demo"})
    private LeftSideMenu menu;

    @Component
    private Form form;

    @Component(parameters = {"value=selected"})
    private AjaxCheckbox ajaxCheckbox;

    @OnEvent(component = "ajaxCheckbox", value = "checkboxclicked")
    public JSONObject onChangeEvent(boolean value)
    {
        JSONObject json = new JSONObject();
        json.put("result", value);
        return json;
    }
}