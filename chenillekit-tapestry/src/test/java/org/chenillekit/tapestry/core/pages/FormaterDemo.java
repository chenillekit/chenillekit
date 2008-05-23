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

import java.util.Date;

import org.apache.tapestry5.annotations.Component;
import org.apache.tapestry5.annotations.Property;

import org.chenillekit.tapestry.core.components.DateFormat;

/**
 * @author <a href="mailto:homburgs@gmail.com">shomburg</a>
 * @version $Id$
 */
public class FormaterDemo
{
    @Property(write = false)
    private Date _dateValue;

    @Property
    private String _textAreaValue = "this field is sizable in vertical and horizontal direction";

    @Property(write = false)
    private String _patternUS = "dd/MM/yyyy";

    @Property(write = false)
    private String _patternDE = "dd.MM.yyyy";

    void setupRender()
    {
        _dateValue = new Date(1);
    }

    @Component(parameters = {"value=dateValue", "pattern=prop:patternDE", "bodyPosition=1"})
    private DateFormat _dateFormat1;

    @Component(parameters = {"value=dateValue", "pattern=prop:patternUS", "bodyPosition=2"})
    private DateFormat _dateFormat2;

    @Component(parameters = {"value=dateValue", "pattern=prop:patternUS", "bodyPosition=0"})
    private DateFormat _dateFormat3;
}