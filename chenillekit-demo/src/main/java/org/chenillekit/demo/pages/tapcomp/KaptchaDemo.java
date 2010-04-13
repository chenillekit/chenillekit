/*
 * Apache License
 * Version 2.0, January 2004
 * http://www.apache.org/licenses/
 *
 * Copyright 2008-2010 by chenillekit.org
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
import org.chenillekit.tapestry.core.components.Kaptcha;

/**
 * @version $Id$
 */
public class KaptchaDemo
{
	@Persist
	@Property
	private boolean inputValue;

	@Component(parameters = {"menuName=literal:demo"})
	private LeftSideMenu menu;

	@Component
	private Form kaptchaForm;

	@Component(parameters = {"value=inputValue"})
	private Kaptcha kaptchaComponent;

	public boolean onValidateForm()
	{
		boolean goodValidation = true;

		if (!inputValue)
		{
			kaptchaForm.recordError("kaptcha value not equals the user input!");
			goodValidation = false;
		}

		return goodValidation;
	}
}