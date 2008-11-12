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

import org.chenillekit.demo.components.LeftSideMenu;
import org.chenillekit.tapestry.core.components.Editor;

/**
 * @version $Id$
 */
public class EditorDemo
{
	@Component(parameters = {"menuName=demo"})
	private LeftSideMenu menu;

	@Persist
	@Property
	private String editor1Value;

	@Persist
	@Property
	private String editor2Value;

	@Component(parameters = {"value=editor1Value"})
	private Editor editor1;

	@Component(parameters = {"value=editor2Value",
			"customConfiguration=asset:classpath:org/chenillekit/demo/assets/js/myEditorConfig.js",
			"toolbarSet=MyToolbar"})
	private Editor editor2;

}