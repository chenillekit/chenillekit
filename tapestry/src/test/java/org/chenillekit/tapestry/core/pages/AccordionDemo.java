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

import org.apache.tapestry.annotation.Component;
import org.apache.tapestry.annotation.Property;

import org.chenillekit.tapestry.core.components.Accordion;

/**
 * @author <a href="mailto:homburgs@gmail.com">shomburg</a>
 * @version $Id$
 */
public class AccordionDemo
{
    @Property
    private String[] _subjects = {"Subject 1", "Subject 2", "Subject 3", "Subject 4", "Subject 5"};

    @Property
    private String[] _details = {"Detail 1", "Detail 2", "Detail 3", "Detail 4", "Detail 5"};

    @Component(parameters = {"subjects=subjects", "details=details"})
    private Accordion _accordion;
}