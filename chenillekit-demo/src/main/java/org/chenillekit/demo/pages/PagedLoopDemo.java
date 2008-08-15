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
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.ioc.annotations.Inject;

import org.chenillekit.demo.components.LeftSideMenu;
import org.chenillekit.demo.data.Track;
import org.chenillekit.demo.services.MusicLibrary;
import org.chenillekit.tapestry.core.components.PagedLoop;

/**
 * @author <a href="mailto:homburgs@gmail.com">shomburg</a>
 * @version $Id$
 */
public class PagedLoopDemo
{
    @Component(parameters = {"menuName=demo"})
    private LeftSideMenu menu;

    @Property
    @Inject
    private MusicLibrary musicLibrary;

    @Property
    private Track track;

    @Component(parameters = {"source=musicLibrary.tracks", "value=track"})
    private PagedLoop pagedLoop;
}