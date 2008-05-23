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
import org.apache.tapestry5.annotations.Property;

import org.chenillekit.tapestry.core.components.Equals;

/**
 * @author <a href="mailto:homburgs@gmail.com">shomburg</a>
 * @version $Id$
 */
public class EqualsDemo
{
    @Property
    private String leftValue = "testLeft";

    @Property
    private String rightValue = "testRight";

    @Component(parameters = {"left=prop:leftValue", "right=prop:rightValue"})
    private Equals equals1;

    @Component(parameters = {"left=prop:leftValue", "right=prop:rightValue", "negate=true"})
    private Equals equals2;
}