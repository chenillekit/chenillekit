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
import org.apache.tapestry5.annotations.Property;

import org.chenillekit.demo.components.LeftSideMenu;

/**
 * @version $Id$
 */
public class FormatterDemo
{
    @Component(parameters = {"menuName=demo"})
    private LeftSideMenu menu;

    @Property(write = false)
    private Date dateValue;

    @Property
    private String textAreaValue = "this field is sizable in vertical and horizontal direction";

    @Property(write = false)
    private String patternUS = "dd/MM/yyyy";

    @Property(write = false)
    private String patternDE = "dd.MM.yyyy";

    void setupRender()
    {
        dateValue = new Date();
    }
}