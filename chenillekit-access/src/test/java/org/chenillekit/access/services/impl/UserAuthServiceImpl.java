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
import org.chenillekit.access.services.AuthenticationService;

/**
 * Just for tests {@link AuthenticationService}
 *
 * @version $Id$
 */
public class UserAuthServiceImpl implements AuthenticationService
{
	public WebSessionUser doAuthenticate( String userName, String password )
	{	
		if ( "root".equals( userName ) )
		{
			return new TestWebSessionUser("root", 10);
		}
		if ( "dummy".equals( userName ) )
		{
			return new TestWebSessionUser("dummy", 0);
		}
		return null;
	}
}
