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

import org.apache.tapestry5.annotations.Component;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Property;

import org.chenillekit.demo.components.LeftSideMenu;

/**
 * @version $Id$
 */
public class RatingFieldDemo
{
    @Component(parameters = {"menuName=demo"})
    private LeftSideMenu menu;

    @Persist
    @Property
    private String ratingValue1;

    @Persist
    @Property
    private int ratingValue2;

    @Persist
    @Property
    private String ratingValue3;
}
