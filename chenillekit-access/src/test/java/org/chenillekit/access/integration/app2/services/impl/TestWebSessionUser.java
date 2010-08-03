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

/**
 *
 */
package org.chenillekit.access.integration.app2.services.impl;

import org.chenillekit.access.WebSessionUser;

/**
 * @author massimo
 *
 */
public class TestWebSessionUser implements WebSessionUser<String>
{
	private static final long serialVersionUID = -2806629129033079130L;

	private final String name;
	private final int roleWeight;
	private final String[] groups;


	public TestWebSessionUser(String name, int roleWeigth, String... groups)
	{
		this.name = name;
		this.roleWeight = roleWeigth;
		this.groups = groups;
	}

	/* (non-Javadoc)
	 * @see org.chenillekit.access.WebSessionUser#getGroups()
	 */
	public String[] getGroups()
	{
		return this.groups;
	}

	/* (non-Javadoc)
	 * @see org.chenillekit.access.WebSessionUser#getName()
	 */
	public String getName()
	{
		return this.name;
	}

	/* (non-Javadoc)
	 * @see org.chenillekit.access.WebSessionUser#getRoleWeight()
	 */
	public int getRoleWeight()
	{
		return this.roleWeight;
	}

	/*
	 * (non-Javadoc)
	 * @see org.chenillekit.access.WebSessionUser#getUserId()
	 */
	public String getUserId()
	{
		return Integer.toString(0);

	}

	/* (non-Javadoc)
	 * @see org.chenillekit.access.WebSessionUser#getType()
	 */
	public Class<String> getType()
	{
		return String.class;
	}

}
