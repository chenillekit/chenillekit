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

package org.chenillekit.access;

import org.apache.tapestry5.test.AbstractIntegrationTestSuite;
import org.testng.annotations.Test;

/**
 *
 * @version $Id: TestAccessIntegration.java 380 2008-12-30 10:21:52Z mlusetti $
 */
public class TestAccessIntegration2 extends AbstractIntegrationTestSuite
{
	
	public TestAccessIntegration2()
	{
		super("src/test/webapp");
	}
	
	@Test
    public void base_url_text_present() throws Exception
    {
        open(BASE_URL);
        
        assertTextPresent("Restricted");
        assertTextPresent("UnRestricted");
    }
	
	@Test
    public void un_restrcited_page() throws Exception
    {
        open(BASE_URL);
        
        clickAndWait("link=UnRestricted");
        
        assertTextPresent("everybody has access");
    }
	
	@Test
	public void restricted() throws Exception
	{
		open(BASE_URL);
		
		clickAndWait("link=Restricted");
		
		assertTextPresent("Login Page");
	}
	
	@Test
	public void restricted_with_context() throws Exception
	{
		open("/restrictedpage/yes/present");
		
		type("chenillekitUsername", "root");
		type("chenillekitPassword", "banane");
		
		clickAndWait("chenillekitLoginSubmit");
		
		assertTextPresent("Context: yes present");
		
		open(BASE_URL);
		
		clickAndWait("link=Logout");
		
		assertTextPresent("Logout Page");
		
		assertTextPresent("User logged in: NO");
	}
	
	@Test
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
		
		open(BASE_URL);
		
		clickAndWait("link=Logout");
		
		assertTextPresent("Logout Page");
		
		assertTextPresent("User logged in: NO");
	}
	
	@Test
	public void action_link_rights_restricted() throws Exception
	{
		open(BASE_URL);
        
        clickAndWait("link=UnRestricted");
        
        clickAndWait("link=only admin");
        
        assertTextPresent("Login Page");
	}
	
	@Test
	public void action_link_rights_restricted_on_event() throws Exception
	{
		open(BASE_URL);
        
        clickAndWait("link=UnRestricted");
        
        clickAndWait("link=only admin handled by OnEvent annotation");
        
        assertTextPresent("Login Page");
	}
	
	@Test
	public void action_with_context_restricted() throws Exception
	{
		open(BASE_URL);
		
		clickAndWait("link=UnRestricted");
		
		clickAndWait("link=only admin with context");
		
		assertTextPresent("Login Page");
		
		type("chenillekitUsername", "root");
		type("chenillekitPassword", "banane");
		
		clickAndWait("chenillekitLoginSubmit");
		
		assertTextPresent("[first, second, third, forth]");
		
		open(BASE_URL);
		
		clickAndWait("link=Logout");
		
		assertTextPresent("Logout Page");
		
		assertTextPresent("User logged in: NO");
	}
	
	@Test
	public void do_login_and_logout() throws Exception
	{
		open(BASE_URL);
		
		clickAndWait("link=Invisible");
		
		assertTextPresent("Login Page");
		
		type("chenillekitUsername", "root");
		type("chenillekitPassword", "banane");
		
		clickAndWait("chenillekitLoginSubmit");
		
		assertTextPresent("You should be able to not see me!");
		
		clickAndWait("link=Back to start");
		
		clickAndWait("link=Logout");
		
		assertTextPresent("Logout Page");
		
		assertTextPresent("User logged in: NO");
	}
	
	@Test
	public void not_enough_rights() throws Exception
	{
		open(BASE_URL);
		
		clickAndWait("link=NotEnoughRights");
		
		assertTextPresent("Login Page");
		
		type("chenillekitUsername", "dummy");
		type("chenillekitPassword", "pere");
		
		clickAndWait("chenillekitLoginSubmit");
		
		assertTextPresent("Login Page");
		
		clickAndWait("link=Back to start");
		
		clickAndWait("link=Logout");
		
		assertTextPresent("Logout Page");
		
		assertTextPresent("User logged in: NO");
	}

}
