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

import org.chenillekit.tapestry.core.components.ThumbNail;

/**
 * @version $Id$
 */
public class ThumbNailDemo
{
    @Component(parameters = {"asset=context:assets/images/sven.jpg", "thumbHeight=80", "onClickAction=true"})
    private ThumbNail _thumbNail;
}