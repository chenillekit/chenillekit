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

import org.apache.tapestry5.dom.Document;
import org.apache.tapestry5.dom.Element;
import org.apache.tapestry5.test.PageTester;

import org.testng.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

/**
 * @author <a href="mailto:homburgs@googlemail.com">shomburg</a>
 * @version $Id: TestComponentIntegration.java 54 2008-05-25 21:42:29Z homburgs@gmail.com $
 */
public class TestAccessIntegration extends Assert
{
    private PageTester pageTester;

    @BeforeTest
    public void initializeTests()
    {
        String appPackage = "org.chenillekit.access";
        String appName = "TestAppWithRoot";
        pageTester = new PageTester(appPackage, appName, "src/test/webapp");
    }

    @Test
    public void test_restricted()
    {
        Document doc = pageTester.renderPage("Start");
        Element link = doc.getElementById("Restricted");

        doc = pageTester.clickLink(link);
        Element element = doc.getElementById("has_access");
        assertEquals(element.getChildMarkup(), "Has Access");
    }

    @Test
    public void test_unRestricted()
    {
        Document doc = pageTester.renderPage("Start");
        Element link = doc.getElementById("UnRestricted");

        doc = pageTester.clickLink(link);
        Element element = doc.getElementById("has_access");
        assertEquals(element.getChildMarkup(), "everybody has access");
    }

    @Test
    public void test_notEnoughRights()
    {
        Document doc = pageTester.renderPage("Start");
        Element link = doc.getElementById("NotEnoughRights");

        doc = pageTester.clickLink(link);
        Element element = doc.getElementById("login_message");
        assertEquals(element.getChildMarkup(), "Login Page");
    }
}
