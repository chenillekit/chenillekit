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
import org.apache.tapestry5.corelib.components.PageLink;

/**
 * ChenilleKit demo application start page.
 */
public class Start
{
    @Component(parameters = {"page=PagedLoopDemo"})
    private PageLink pagedLoopDemo;

    @Component(parameters = {"page=AccordionDemo"})
    private PageLink accordionDemo;
}
