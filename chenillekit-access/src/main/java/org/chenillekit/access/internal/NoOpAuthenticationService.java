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

package org.chenillekit.access.internal;

import org.chenillekit.access.ChenilleKitAccessException;
import org.chenillekit.access.WebSessionUser;
import org.chenillekit.access.services.AuthenticationService;

/**
 * A simple no-op service used as a terminator for the authentication service chain
 * when no other authenticator has validate user's credentials.
 *
 * @version $Id$
 */
public class NoOpAuthenticationService implements AuthenticationService
{

	/* (non-Javadoc)
	 * @see org.chenillekit.access.services.AuthenticationService#doAuthenticate(java.lang.String, java.lang.String)
	 */
	public WebSessionUser doAuthenticate(String userName, String password)
	{
//		throw new ChenilleKitAccessException("Unable to find a valid authentication service for " + userName);
		
		return null;
	}

}
