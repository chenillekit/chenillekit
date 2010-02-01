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
import org.apache.tapestry5.corelib.components.Form;

import org.chenillekit.demo.components.LeftSideMenu;
import org.chenillekit.tapestry.core.components.RatingField;

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
    private Integer ratingValue2;

    @Persist
    @Property
    private String ratingValue3;

	@Component
	private Form form;

	@Component(parameters = {"source=list:1,2,3,4,5,6,7,8,9,10", "value=ratingValue1"})
	private RatingField ratingField1;

	@Component(parameters = {"source=list:1,2,3,4,5", "value=ratingValue2"})
	private RatingField ratingField2;

	@Component(parameters = {"source=list:'bad','nice','good','excelent'", "value=ratingValue3"})
	private RatingField ratingField3;
}
