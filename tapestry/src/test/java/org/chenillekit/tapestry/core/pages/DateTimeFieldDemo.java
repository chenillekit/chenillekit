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

package org.chenillekit.tapestry.core.pages;

import java.util.Date;

import org.apache.tapestry5.annotations.Component;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.corelib.components.Form;

import org.chenillekit.tapestry.core.components.DateTimeField;

/**
 * @author <a href="mailto:homburgs@gmail.com">shomburg</a>
 * @version $Id$
 */
public class DateTimeFieldDemo
{
    @Persist
    @Property
    private Date _actualDate1;

    @Persist
    @Property
    private Date _actualDate2;

    @Persist
    @Property
    private Date _actualDate3;

    @Component
    private Form _form;

    @Component(parameters = {"value=actualDate1", "datePattern=dd-MM-yyyy HH:mm"})
    private DateTimeField _dateTimeField1;

    @Component(parameters = {"value=actualDate2"})
    private DateTimeField _dateTimeField2;

    @Component(parameters = {"value=actualDate3"})
    private DateTimeField _dateTimeField3;
}