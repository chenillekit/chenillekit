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

import org.apache.tapestry5.Asset;
import org.apache.tapestry5.annotations.Component;
import org.apache.tapestry5.annotations.Path;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.ioc.annotations.Inject;

import org.chenillekit.demo.components.LeftSideMenu;
import org.chenillekit.tapestry.core.components.ThumbNail;

/**
 * @version $Id$
 */
public class ThumbnailDemo
{
    @Component(parameters = {"menuName=demo"})
    private LeftSideMenu menu;

    @Inject
    @Path("../../assets/images/sven.jpg")
    @Property
    private Asset myAsset;

    @Component(parameters = {"asset=prop:myAsset", "thumbHeight=50", "onClickAction=true"})
    private ThumbNail thumbNail;
}