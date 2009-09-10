/*
 * Apache License
 * Version 2.0, January 2004
 * http://www.apache.org/licenses/
 *
 * Copyright 2008-2009 by chenillekit.org
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
import org.chenillekit.access.services.AuthenticationServiceFilter;

/**
 * @id $id$
 *
 */
public class UserEqualPassCheck implements AuthenticationServiceFilter
{

	/* (non-Javadoc)
	 * @see org.chenillekit.access.services.AuthenticationServiceFilter#doAuthenticate(java.lang.String, java.lang.String, org.chenillekit.access.services.AuthenticationService)
	 */
	public WebSessionUser doAuthenticate(String userName, String password,
			AuthenticationService delegate)
	{
		// Just to show how the pipeline can operate
		if (userName.equalsIgnoreCase(password))
		{
			return null;
		}
		
		return delegate.doAuthenticate(userName, password);
	}

}
