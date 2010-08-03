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

package org.chenillekit.access.integration.app1;

import org.apache.tapestry5.test.SeleniumTestCase;
import org.testng.annotations.Test;

/**
 * @version $Id$
 */
@Test
public class AccessIntegration extends SeleniumTestCase
{
    public void base_url_text_present() throws Exception
    {
        openBaseURL();

        assertTextPresent("Restricted");
        assertTextPresent("UnRestricted");
    }

    public void un_restrcited_page() throws Exception
    {
        openBaseURL();

        clickAndWait("link=UnRestricted");

        assertTextPresent("everybody has access");
    }

	public void restricted() throws Exception
	{
		openBaseURL();

		clickAndWait("link=Restricted");

		assertTextPresent("Login Page");
	}

	public void restricted_with_context() throws Exception
	{
		open("/restrictedpage/yes/present");

		type("chenillekitUsername", "root");
		type("chenillekitPassword", "banane");

		clickAndWait("chenillekitLoginSubmit");

		open("/restrictedpage/yes/present");

		assertTextPresent("Context: yes present");

		openBaseURL();

		clickAndWait("link=Logout");

		assertTextPresent("Logout Page");

		assertTextPresent("User logged in: NO");

	}

	public void restricted_with_context_error_failed_first_login() throws Exception
	{
		open("/restrictedpage/yes/present");

		type("chenillekitUsername", "rot");
		type("chenillekitPassword", "banane");

		clickAndWait("chenillekitLoginSubmit");

		assertTextPresent("Login Page");

		type("chenillekitUsername", "root");
		type("chenillekitPassword", "banane");

		clickAndWait("chenillekitLoginSubmit");

		assertTextPresent("Context: yes present");

		openBaseURL();

		clickAndWait("link=Logout");

		assertTextPresent("Logout Page");

		assertTextPresent("User logged in: NO");
	}

	public void action_link_rights_restricted() throws Exception
	{
		openBaseURL();

        clickAndWait("link=UnRestricted");

        clickAndWait("link=only admin");

        assertTextPresent("Login Page");
	}

	public void action_link_rights_role_restricted_login() throws Exception
	{
		openBaseURL();

        clickAndWait("link=UnRestricted");

        clickAndWait("link=only role 10");

        assertTextPresent("Login Page");
	}

	public void action_link_rights_role_restricted_invisible() throws Exception
	{
        openBaseURL();

		clickAndWait("link=UnRestricted");

		clickAndWait("link=only role 10");

		assertTextPresent("Login Page");

		type("chenillekitUsername", "root");
		type("chenillekitPassword", "banane");

		clickAndWait("//input[@id='chenillekitLoginSubmit']");

		assertTextPresent("You should be able to not see me!");

		openBaseURL();

		clickAndWait("link=Logout");

		assertTextPresent("Logout Page");

		assertTextPresent("User logged in: NO");

	}

	public void action_link_rights_restricted_on_event() throws Exception
	{
		openBaseURL();

        clickAndWait("link=UnRestricted");

        clickAndWait("link=only admin handled by OnEvent annotation");

        assertTextPresent("Login Page");
	}

	public void action_with_context_restricted() throws Exception
	{
		openBaseURL();

		clickAndWait("link=UnRestricted");

		clickAndWait("link=only admin with context");

		assertTextPresent("Login Page");

		type("chenillekitUsername", "root");
		type("chenillekitPassword", "banane");

		clickAndWait("//input[@id='chenillekitLoginSubmit']");

		assertTextPresent("[first, second, third, forth]");

		openBaseURL();

		clickAndWait("link=Logout");

		assertTextPresent("Logout Page");

		assertTextPresent("User logged in: NO");
	}

	public void do_login_and_logout() throws Exception
	{
		openBaseURL();

		assertTextPresent("not authenticated");

		clickAndWait("link=Invisible");

		assertTextPresent("Login Page");

		type("chenillekitUsername", "root");
		type("chenillekitPassword", "banane");

		clickAndWait("//input[@id='chenillekitLoginSubmit']");

		assertTextPresent("You should be able to not see me!");

		clickAndWait("link=Back to start");

		assertTextPresent("is authenticated");

		clickAndWait("link=Logout");

		assertTextPresent("Logout Page");

		assertTextPresent("User logged in: NO");
	}

	public void not_enough_rights() throws Exception
	{
		openBaseURL();

		clickAndWait("link=NotEnoughRights");

		assertTextPresent("Login Page");

		type("chenillekitUsername", "dummy");
		type("chenillekitPassword", "pere");

		clickAndWait("//input[@id='chenillekitLoginSubmit']");

		assertTextPresent("Login Page");

		clickAndWait("link=Back to start");

		clickAndWait("link=Logout");

		assertTextPresent("Logout Page");

		assertTextPresent("User logged in: NO");
	}

	public void equal_check() throws Exception
	{
		openBaseURL();

		clickAndWait("link=Invisible");

		assertTextPresent("Login Page");

		type("chenillekitUsername", "root");
		type("chenillekitPassword", "root");

		clickAndWait("//input[@id='chenillekitLoginSubmit']");

		assertTextPresent("Login Page");
	}

	public void test_managedrestricted()
	{
		openBaseURL();

		clickAndWait("link=ManagedRestricted");

		assertTextPresent("Login Page");

		type("chenillekitUsername", "dummy");
		type("chenillekitPassword", "pere");

		clickAndWait("//input[@id='chenillekitLoginSubmit']");

		assertTextPresent("Login Page");

		type("chenillekitUsername", "root");
		type("chenillekitPassword", "banane");

		clickAndWait("//input[@id='chenillekitLoginSubmit']");

		assertTextPresent("Manageable Restricted Page");

		clickAndWait("firstEventLink");

		assertTextPresent("event1 triggered");

		clickAndWait("secondEventLink");

		assertTextPresent("you have no access");

		clickAndWait("firstActionLink");

		assertTextPresent("action1 triggered");

		clickAndWait("secondActionLink");

		assertTextPresent("you have no access");

		clickAndWait("thirdActionLink");

		assertTextPresent("action3 triggered");

		assertTextPresent("Hey Dude From Action 3");

		clickAndWait("thirdEventLink");

		assertTextPresent("event3 triggered");

		assertTextPresent("Hey Dude From Event 3");

		openBaseURL();

		clickAndWait("link=Logout");

		assertTextPresent("Logout Page");

		assertTextPresent("User logged in: NO");
	}

	public void test_managedrestricted_loggedin_but_no_access()
	{
		openBaseURL();

		clickAndWait("link=ManagedRestricted2");

		assertTextPresent("Login Page");

		type("chenillekitUsername", "root");
		type("chenillekitPassword", "banane");

		clickAndWait("//input[@id='chenillekitLoginSubmit']");

		assertTextPresent("Logged in but no access");

		clickAndWait("firstEventLink");

		assertTextPresent("event1 triggered");

		clickAndWait("secondEventLink");

		assertTextPresent("you have no access");

		openBaseURL();

		clickAndWait("link=Logout");

		assertTextPresent("Logout Page");

		assertTextPresent("User logged in: NO");

		openBaseURL();

		clickAndWait("link=ManagedRestricted2");

		assertTextPresent("Login Page");

		openBaseURL();

		clickAndWait("link=Logout");

		assertTextPresent("Logout Page");

		assertTextPresent("User logged in: NO");
	}
}
