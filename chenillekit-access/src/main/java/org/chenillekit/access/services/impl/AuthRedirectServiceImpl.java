/*
 *  Apache License
 *  Version 2.0, January 2004
 *  http://www.apache.org/licenses/
 *
 *  Copyright 2008 by chenillekit.org
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 */

package org.chenillekit.access.services.impl;

import org.chenillekit.access.WebSessionUser;
import org.chenillekit.access.services.AppServerLoginService;
import org.chenillekit.access.services.AuthRedirectService;
import org.chenillekit.access.services.AuthService;
import org.chenillekit.access.services.WebSessionUserService;
import org.slf4j.Logger;

/**
 *
 * @version $Id$
 */
public class AuthRedirectServiceImpl implements AuthRedirectService
{
	private final Logger logger;
	private AuthService<WebSessionUser> baseAuthService;
	private WebSessionUserService userService;
	private String returnPage;
	private AppServerLoginService appServerLoginService;

	public AuthRedirectServiceImpl(final Logger logger,
				final AuthService<WebSessionUser> baseAuthService,
				final WebSessionUserService userService,
				final AppServerLoginService appServerLoginService)
	{
		this.logger = logger;
		this.baseAuthService = baseAuthService;
		this.userService = userService;
		this.appServerLoginService = appServerLoginService;
	}

	public String getReturnPage()
	{
		return returnPage;
	}

	public void setReturnPage( String returnPage )
	{
		this.returnPage = returnPage;
	}

	/**
	 * User authentication.
	 *
	 * @param userName name of the user
	 * @param password users password
	 */
	public String doAuthenticate(String userName, String password)
	{
		if (logger.isDebugEnabled()) logger.trace("doAthenticate user : " + userName);

		WebSessionUser user = baseAuthService.doAuthenticate( userName, password );

		if ( null == user ) return null;

		if (logger.isDebugEnabled()) logger.trace("user was found, return to page : " + returnPage);
		userService.setUser( user );
		appServerLoginService.appServerLogin( user );
		return returnPage;
	}
}
