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

package org.chenillekit.access.services;

import org.chenillekit.access.WebSessionUser;

/**
 * @id $id$
 *
 */
public interface AuthenticationServiceFilter
{
	/**
	 * 
	 * @param userName
	 * @param password
	 * @param delegate
	 * @return
	 */
	WebSessionUser<?> doAuthenticate(String userName, String password, AuthenticationService delegate);

	/**
	 * check for user is authenticated.
	 */
	boolean isAuthenticate(AuthenticationService delegate);
}
