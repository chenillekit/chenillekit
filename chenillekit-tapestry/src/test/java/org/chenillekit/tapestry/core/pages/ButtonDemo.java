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

import org.apache.tapestry5.annotations.Property;


/**
 * @version $Id$
 */
public class ButtonDemo
{
	@Property
	private String clicked;
	
	
	void onActivate(String clicked)
	{
		this.clicked = clicked;
	}
	
	String onPassivate()
	{
		return this.clicked;
	}
	
	
	Object onClickedFromTheButton()
	{
		this.clicked = "Are you happy?";
		
		return this;
	}
	
	Object onClickedFromTheConfirmButton()
	{
		this.clicked = "I'm sure you are happy";
		
		return this;
	}
	
}