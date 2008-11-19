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

import org.chenillekit.access.WebSessionUser;
import org.chenillekit.access.services.AuthService;
import org.chenillekit.access.utils.DummyUser;
import org.chenillekit.access.utils.RootUser;

/**
 * ...
 *
 * @version $Id$
 */
public class UserAuthServiceImpl implements AuthService<WebSessionUser>
{
	public WebSessionUser doAuthenticate( String userName, String password )
	{
		if ( "root".equals( userName ) ) return new RootUser();
		if ( "dummy".equals( userName ) ) return new DummyUser();
		return null;
	}
}
