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

package org.chenillekit.access.integration.app3;

import org.apache.tapestry5.test.SeleniumTestCase;

import org.testng.annotations.Test;

/**
 * @version $Id: AccessIntegration.java 685 2010-08-03 16:54:50Z homburgs $
 */
@Test
public class AccessIntegration3 extends SeleniumTestCase
{
	public void root_restricted() throws Exception
	{
		openBaseURL();
		
		assertTextPresent("Login Page");
		
		loginAs("root", "banane");
		
		clickAndWait("link=Restricted");
		
		assertTextPresent("Has Access");
		
		logout();
		
		assertTextPresent("User logged in: NO");
	}

	private void loginAs(String userName, String password)
	{
		type("chenillekitUsername", userName);
		type("chenillekitPassword", password);
		clickAndWait("//input[@id='chenillekitLoginSubmit']");
	}

	private void logout()
	{
		openBaseURL();

		clickAndWait("link=Logout");

		assertTextPresent("Logout Page");

		assertTextPresent("User logged in: NO");
	}
}
