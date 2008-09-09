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

import org.chenillekit.demo.components.LeftSideMenu;
import org.chenillekit.tapestry.core.components.Window;

/**
 * @author <a href="mailto:homburgs@gmail.com">shomburg</a>
 * @version $Id$
 */
public class WindowDemo
{
    @Component(parameters = {"menuName=demo"})
    private LeftSideMenu menu;

    @Component(parameters = {"style=bluelighting", "show=false", "title=literal:Window 1", "width=300", "height=200"})
    private Window window1;

    @Component(parameters = {"style=mac_os_x", "show=false", "title=literal:Window 2", "width=300", "height=200"})
    private Window window2;

    @Component(parameters = {"style=nuncio", "show=false", "title=literal:Window 3", "width=300", "height=200"})
    private Window window3;

    @Component(parameters = {"style=spread", "show=false", "title=literal:Window 4", "width=300", "height=200"})
    private Window window4;

    @Component(parameters = {"style=alphacube", "show=false", "title=literal:Window 5", "width=300", "height=200"})
    private Window window5;

    @Component(parameters = {"style=dialog", "show=false", "title=literal:Window 6", "width=300", "height=200"})
    private Window window6;
}