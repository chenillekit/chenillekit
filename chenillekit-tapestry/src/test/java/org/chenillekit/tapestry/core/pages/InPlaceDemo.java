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

import org.chenillekit.tapestry.core.components.InPlaceCheckbox;
import org.chenillekit.tapestry.core.components.InPlaceEditor;

/**
 * @author <a href="mailto:homburgs@gmail.com">shomburg</a>
 * @version $Id$
 */
public class InPlaceDemo
{
    @Property
    private boolean inPlaceCheckboxValue;

    @Component(parameters = {"value=inPlaceCheckboxValue", "onCompleteCallback=onCheckboxClicked"})
    private InPlaceCheckbox inPlaceCheckbox;

    @Property
    private String inPlaceEditorValue;

    @Component(parameters = {"value=inPlaceEditorValue"})
    private InPlaceEditor inPlaceEditor;
}