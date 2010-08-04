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

package org.chenillekit.access.components;

import org.apache.tapestry5.annotations.Component;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.annotations.SessionState;
import org.apache.tapestry5.corelib.components.Form;
import org.apache.tapestry5.ioc.Messages;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.ioc.annotations.Local;
import org.apache.tapestry5.services.Cookies;
import org.chenillekit.access.ChenilleKitAccessConstants;
import org.chenillekit.access.WebSessionUser;
import org.chenillekit.access.services.AuthenticationService;

/**
 * Login component
 *
 * @version $Id$
 */
public class Login
{
	@SuppressWarnings("unused")
	@SessionState
	private WebSessionUser<?> webSessionUser;

	@Inject
	private Messages messages;

	@Inject
	private Cookies cookies;

	@Inject
	@Local
	private AuthenticationService authenticationService;

	@Component
	private Form chenillekitLoginForm;

	@Property
	private String username;

	@Property
	private String password;

	private WebSessionUser<?> tmpUser;

	void onValidateForm()
	{
		tmpUser = authenticationService.doAuthenticate(username, password);

		if (tmpUser == null)
			chenillekitLoginForm.recordError(messages.format(ChenilleKitAccessConstants.NOT_AUTHENTICATED_ERROR_MESSAGE, username));
	}

	void onFailure()
	{
		cookies.writeCookieValue(ChenilleKitAccessConstants.LOGIN_SUCCESSFUL_COOKIE_NAME, "KO", 300);
	}

	void onSuccess()
	{
		webSessionUser = tmpUser;

		cookies.writeCookieValue(ChenilleKitAccessConstants.LOGIN_SUCCESSFUL_COOKIE_NAME, "OK", 300);
	}

}
