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

package org.chenillekit.access;

/**
 *
 * @version $Id$
 */
public class WebSessionUser
{
	private final int roleWeight;
	private final String[] groups;
	
	public WebSessionUser(int roleWeight, String... groups)
	{
		this.roleWeight = roleWeight;
		this.groups = groups;
	}
	
	/**
	 * get the role ids.
	 *
	 * @return role ids
	 */
	public int getRoleWeight()
	{
		return this.roleWeight;
	}

	/**
	 * get the group names.
	 *
	 * @return group names
	 */
	public String[] getGroups()
	{
		return this.groups;
	}
}
