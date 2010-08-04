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

package org.chenillekit.access.integration.app1.services.impl;

import org.apache.tapestry5.services.ApplicationStateManager;
import org.chenillekit.access.WebSessionUser;
import org.chenillekit.access.services.AuthenticationService;
import org.chenillekit.access.services.AuthenticationServiceFilter;

/**
 * Just for tests {@link AuthenticationService}
 *
 * @version $Id$
 */
public class UserAuthServiceImpl implements AuthenticationServiceFilter
{
	final private ApplicationStateManager stateManager;

	public UserAuthServiceImpl(ApplicationStateManager stateManager)
	{
		this.stateManager = stateManager;
	}

	/* (non-Javadoc)
	 * @see org.chenillekit.access.services.AuthenticationServiceFilter#doAuthenticate(java.lang.String, java.lang.String, org.chenillekit.access.services.AuthenticationService)
	 */

	public WebSessionUser<?> doAuthenticate(String userName, String password,
										 AuthenticationService delegate)
	{
		if ("root".equals(userName))
		{
			return new TestWebSessionUser("root", 10, "admin");
		}
		if ("dummy".equals(userName))
		{
			return new TestWebSessionUser("dummy", 0, "dummy");
		}
		return delegate.doAuthenticate(userName, password);
	}

	/**
	 * check for user is authenticated.
	 */
	public boolean isAuthenticate(AuthenticationService delegate)
	{
		WebSessionUser<?> webSessionUser = stateManager.getIfExists(WebSessionUser.class);
		return webSessionUser != null && delegate.isAuthenticate();
	}
}
