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

package org.chenillekit.access.pages;

import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.annotations.SessionState;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.services.Request;
import org.apache.tapestry5.services.Session;

import org.chenillekit.access.WebSessionUser;

/**
 * Page which logs the user out of the application.
 *
 * @version $Id$
 */
public class Logout
{
	@Inject
	private Request request;

	@SuppressWarnings("unused")
	@SessionState
	private WebSessionUser user;
	@Property
	private boolean userExists;

	final public void beginRender()
	{
		// logout
		Session session = request.getSession(false);

		if (session != null)
			session.invalidate();
	}

	public String getUserLoggedIn()
	{
		if (userExists)
		{
			return "YES";
		}
		else
		{
			return "NO";
		}

	}
}
