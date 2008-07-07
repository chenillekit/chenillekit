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

import org.apache.tapestry5.annotations.Component;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Property;

import org.chenillekit.tapestry.core.components.Hidden;

/**
 * @author <a href="mailto:homburgs@gmail.com">shomburg</a>
 * @version $Id$
 */
public class HiddenDemo
{
    @Persist
    @Property
    private String hiddenValue1 = "Blub";

    @Persist
    @Property
    private Long hiddenValue2 = 200l;

    @Persist
    @Property
    private Float hiddenValue3 = 123.456f;

    @Component(parameters = {"value=hiddenValue1"})
    private Hidden hidden1;

    @Component(parameters = {"value=hiddenValue2"})
    private Hidden hidden2;

    @Component(parameters = {"value=hiddenValue3"})
    private Hidden hidden3;
}