/*
 * Apache License
 * Version 2.0, January 2004
 * http://www.apache.org/licenses/
 *
 * Copyright 2010 by chenillekit.org
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
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 * @version $Id$
 */
public class TestAccessIntegration3 extends Assert
{
    private PageTester pageTester;

    @BeforeMethod
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
        Element link = doc.getElementById("ManagedRestrictedPage");
        doc = pageTester.clickLink(link);

        Element element = doc.find("html/head/title");
        assertEquals(element.getChildMarkup(), "Login Page");
    }
}