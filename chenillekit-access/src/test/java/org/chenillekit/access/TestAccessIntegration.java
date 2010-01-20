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
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.Map;

/**
 * @version $Id$
 */
public class TestAccessIntegration extends Assert
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
    public void test_unRestricted()
    {
        Document doc = pageTester.renderPage("Start");
        Element link = doc.getElementById("UnRestricted");

        doc = pageTester.clickLink(link);
        Element element = doc.getElementById("has_access");
        assertEquals(element.getChildMarkup(), "everybody has access");

        Map<String, String> paramMap = new HashMap<String, String>();
        paramMap.put("testInput", "Sven");
        Element submit = doc.getElementById("restrictedSubmit");
        doc = pageTester.clickSubmit(submit, paramMap);
        element = doc.getElementById("error");
        assertTrue(element.getChildMarkup().contains("onNotEnoughAccessRights"));
    }

    @Test
    public void test_restrictedTextField()
    {
        Document doc = pageTester.renderPage("Start");
        Element link = doc.getElementById("RestrictedTextField");

        doc = pageTester.clickLink(link);
        Element element1 = doc.getElementById("simpleTextField1");
        assertNotNull(element1);

        Element element2 = doc.getElementById("simpleTextField2");
        assertNotNull(element2);
    }
}
