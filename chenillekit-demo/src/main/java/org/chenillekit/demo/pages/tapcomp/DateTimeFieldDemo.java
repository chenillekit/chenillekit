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

import java.util.Date;

import org.apache.tapestry5.annotations.Component;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.corelib.components.Form;

import org.chenillekit.demo.components.LeftSideMenu;
import org.chenillekit.tapestry.core.components.DateTimeField;

/**
 * @version $Id$
 */
public class DateTimeFieldDemo
{
    @Persist
    @Property
    private Date actualDate1;

    @Persist
    @Property
    private Date actualDate2;

    @Persist
    @Property
    private Date actualDate3;

    @Persist
    @Property
    private Date actualDate4;

    @Component(parameters = {"menuName=demo"})
    private LeftSideMenu menu;

    @Component
    private Form form;

    @Component(parameters = {"value=actualDate1", "datePattern=dd-MM-yyyy HH:mm"})
    private DateTimeField dateTimeField1;

    @Component(parameters = {"value=actualDate2", "lenient=false"})
    private DateTimeField dateTimeField2;

    @Component(parameters = {"value=actualDate3", "timePicker=true", "datePattern=MM/dd/yyyy HH:mm"})
    private DateTimeField dateTimeField3;

    @Component(parameters = {"value=actualDate4", "datePicker=false", "timePicker=true", "datePattern=HH:mm"})
    private DateTimeField dateTimeField4;
}