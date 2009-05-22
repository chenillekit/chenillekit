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
 *
 */

package org.chenillekit.access.pages;

import java.util.Arrays;
import java.util.List;

import org.apache.tapestry5.annotations.InjectPage;
import org.apache.tapestry5.annotations.OnEvent;
import org.chenillekit.access.annotations.Restricted;

/**
 *
 * @version $Id$
 */
public class UnRestrictedPage
{
	@InjectPage
	private Invisible invisible;
	
	private List<String> context = null;
	
	
	void onActivate(List<String> context)
	{
		this.context = context;
	}
	
	
	@Restricted(groups = { "ADMINS" })
	Object onActionFromTestRights()
	{
		return invisible;
	}

	@Restricted(groups = { "ADMINS" })
	@OnEvent(component = "testRightsOnEvent")
	Object thisThrowRuntimeException()
	{
		return invisible;
	}
	
	@Restricted(groups = { "ADMINS" })
	void onActionFromTestRightsContext(List<String> context)
	{
		
	}
	
	public List<String> getActionContext()
	{
		return Arrays.asList("first", "second", "third", "forth");
	}
	
	public String getContextIfPresent()
	{
		return this.context == null || this.context.isEmpty() ?
					"NO CONTEXT" :
						Arrays.toString(context.toArray());
	}
}