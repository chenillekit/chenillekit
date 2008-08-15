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

import org.chenillekit.tapestry.core.components.SlidingPanel;
import org.chenillekit.demo.components.LeftSideMenu;

/**
 * @author <a href="mailto:homburgs@gmail.com">shomburg</a>
 * @version $Id$
 */
public class SlidingPanelDemo
{
    @Component(parameters = {"menuName=demo"})
    private LeftSideMenu menu;
    
    @Component(parameters = {"subject=Panel 1"})
    private SlidingPanel panel1;

    @Component(parameters = {"subject=Panel 2", "options={duration:0.2}", "closed=true"})
    private SlidingPanel panel2;
}

