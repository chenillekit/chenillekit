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

package org.chenillekit.access.services;

/**
 *
 * @version $Id$
 */
public interface AuthRedirectService extends AuthService<String>
{
	/**
	 * User authentication.
	 *
	 * @param userName name of the user
	 * @param password users password
	 * @return page to forwar to on successful authentication
	 */
	String doAuthenticate(String userName, String password);

	String getReturnPage();

	void setReturnPage( String returnPage );
}
