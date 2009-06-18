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
import org.apache.tapestry5.annotations.Mixins;
import org.apache.tapestry5.annotations.OnEvent;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.annotations.Retain;
import org.apache.tapestry5.corelib.components.Checkbox;
import org.apache.tapestry5.corelib.components.EventLink;

import org.chenillekit.demo.components.LeftSideMenu;

/**
 * @version $Id$
 */
public class YuiComponentsDemo
{
	@Retain
	@Property
	private boolean checkboxValue;

	@Component(parameters = {"menuName=demo"})
	private LeftSideMenu menu;

	@Component(parameters = {"event=doNothing"})
	@Mixins("ck/yui/Button")
	private EventLink eventLink;

	@Component(parameters = {"value=checkboxValue", "Button.label=literal:Checkbox"})
	@Mixins("ck/yui/Button")
	private Checkbox checkbox;

	@OnEvent(value = "doNothing")
	public void doNothing()
	{

	}
}