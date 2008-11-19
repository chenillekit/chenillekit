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

package org.chenillekit.access.services.impl;

import org.apache.tapestry5.ioc.annotations.Scope;
import org.chenillekit.access.WebSessionUser;
import org.chenillekit.access.services.WebSessionUserService;

/**
 * Service which stores the WebSessionUser and makes it available to the application and other services.
 *
 * @version $Id$
 */
@Scope( value = "perthread" )
public class WebSessionUserServiceImpl implements WebSessionUserService
{
	private WebSessionUser user;

	public WebSessionUser getUser()
	{
		return user;
	}

	public void setUser( WebSessionUser user )
	{
		this.user = user;
	}
}
