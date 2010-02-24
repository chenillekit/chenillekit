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

package org.chenillekit.access.integration.app1.pages;

import org.apache.tapestry5.annotations.Component;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.ioc.annotations.Inject;

import org.slf4j.Logger;


/**
 * Login page. The user is given three login attempts, or else the browser session will need to be closed.
 *
 * @version $Id$
 */
public class Login
{
	private static final int MAX_LOGIN_ATTEMPTS = 3;

	@SuppressWarnings("unused")
	@Inject
	private Logger logger;

	@Persist
	private int loginAttempts;

	@Component
	private org.chenillekit.access.components.Login login; 

	final public boolean isLoginAllowed()
	{
		return loginAttempts < MAX_LOGIN_ATTEMPTS;
	}

	void onActivate()
	{
	}
}
