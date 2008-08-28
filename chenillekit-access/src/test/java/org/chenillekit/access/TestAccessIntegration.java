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
 * @author <a href="mailto:homburgs@googlemail.com">shomburg</a>
 * @version $Id: TestComponentIntegration.java 54 2008-05-25 21:42:29Z homburgs@gmail.com $
 */
public class TestAccessIntegration extends AbstractIntegrationTestSuite
{
    /**
     * Initializes the suite using {@link #DEFAULT_WEB_APP_ROOT}.
     */
    public TestAccessIntegration()
    {
        super("src/test/webapp");
    }

    @Test
    public void test_restricted()
    {
        start("Restricted");
        waitForPageToLoad("5000");

        assertEquals(getText("xpath=//span[@id='login_message']"), "Login Page");
    }

    @Test
    public void test_not_enough_rights()
    {
        start("NotEnoughRights");
        waitForPageToLoad("5000");

        assertEquals(getText("xpath=//span[@id='login_message']"), "Login Page");
    }
    
    @Test
    public void test_not_enough_rights_action_link()
    {
        start("NotEnoughRights.noway");
        waitForPageToLoad("5000");

        assertEquals(getText("xpath=//span[@id='login_message']"), "Login Page");
//        assertEquals(getText("xpath=//span[@id='has_access']"), "Don't have access");
    }
    
    @Test
    public void test_invalidate_session_user_rights()
    {
        start("Invalidate", "Restricted");
        waitForPageToLoad("5000");

        assertEquals(getText("xpath=//span[@id='login_message']"), "Login Page");
    }
}
