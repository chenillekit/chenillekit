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
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.corelib.components.Form;

import org.chenillekit.demo.components.LeftSideMenu;
import org.chenillekit.tapestry.core.components.Slider;

/**
 * @author <a href="mailto:homburgs@gmail.com">shomburg</a>
 * @version $Id$
 */
public class SliderDemo
{
    @Component(parameters = {"menuName=demo"})
    private LeftSideMenu menu;

    @Persist
    @Property
    private int slider1Value = 10;

    @Persist
    @Property
    private float slider2Value = 12.4f;

    @Component
    private Form form1;

    @Component(parameters = {"value=slider1Value"})
    private Slider slider1;

    @Component(parameters = {"value=slider2Value"})
    private Slider slider2;
}