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

package org.chenillekit.demo.pages;

import java.util.Arrays;
import java.util.List;

import org.apache.tapestry5.annotations.Component;
import org.apache.tapestry5.corelib.components.ActionLink;

/**
 * ChenilleKit demo application start page.
 */
public class Start
{
	@Component(parameters = {"context=context"})
	private ActionLink actionLink;

	void onActivate(List<String> context)
	{
		System.err.println(Arrays.toString(context.toArray()));
	}

	void onActionFromActionLink(List<String> context)
	{
		System.err.println("onActionFromActionLink: " + Arrays.toString(context.toArray()));
	}

	public List<String> getContext()
	{
		return Arrays.asList("first", "second", "third", "forth");
	}
}
