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

package org.chenillekit.access.pages;

import org.apache.tapestry5.ComponentResources;
import org.apache.tapestry5.annotations.Component;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.corelib.components.PasswordField;
import org.apache.tapestry5.corelib.components.TextField;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.chenillekit.access.services.AuthRedirectService;
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

	@Inject
	private AuthRedirectService authService;

	@Persist
	@Property
	private String userName;

	@Property
	private String password;

	@SuppressWarnings("unused")
	@Component(parameters = {"value=userName"})
	private TextField inputUserName;

	@SuppressWarnings("unused")
	@Component(parameters = {"value=password"})
	private PasswordField inputPassword;

	@Inject
	private ComponentResources resources;

	@Persist
	private int loginAttempts;

	final public boolean isLoginAllowed()
	{
		return loginAttempts < MAX_LOGIN_ATTEMPTS;
	}

	final public String onSuccess()
	{
		loginAttempts++;
		String page = authService.doAuthenticate( userName, password );
		if ( null != page ) resources.discardPersistentFieldChanges();
		return page;
	}

	void onActivate()
	{

	}
}
