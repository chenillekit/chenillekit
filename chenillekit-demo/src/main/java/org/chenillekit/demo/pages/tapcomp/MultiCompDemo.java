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

package org.chenillekit.demo.pages.tapcomp;

import java.util.Date;

import org.apache.tapestry5.annotations.Component;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.corelib.components.Form;
import org.apache.tapestry5.corelib.components.TextField;

import org.chenillekit.demo.components.LeftSideMenu;
import org.chenillekit.tapestry.core.components.ClickSubmit;
import org.chenillekit.tapestry.core.components.ColorPicker;
import org.chenillekit.tapestry.core.components.DateSelector;
import org.chenillekit.tapestry.core.components.LinkSubmit;

/**
 * @version $Id$
 */
public class MultiCompDemo
{
    @Persist(value = "flash")
    @Property
    private String name;

    @Persist(value = "flash")
    @Property
    private String city;

    @Persist(value = "flash")
    @Property
    private String color;

    @Persist
    @Property
    private Date dateValue;

    @Component(parameters = {"menuName=demo"})
    private LeftSideMenu menu;

    @Component
    private Form form1;

    @Component(parameters = {"value=name"})
    private TextField inputName;

    @Component(parameters = {"element=img"})
    private ClickSubmit clickSubmit;

    @Component
    private Form form2;

    @Component(parameters = {"value=city"})
    private TextField inputCity;

    @Component(parameters = {"value=color"})
    private ColorPicker inputColor;

    @Component
    private LinkSubmit linkSubmit;

	@Component
	private Form form3;

    @Component(parameters = {"value=dateValue"})
    private DateSelector dateSelector;
}