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

import java.util.HashMap;
import java.util.Map;

import org.apache.tapestry5.dom.Document;
import org.apache.tapestry5.dom.Element;
import org.apache.tapestry5.test.PageTester;
import org.testng.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

/**
 *
 * @version $Id$
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

//    FIXME this test fail cause i don't have a user logged in
//	@Test
//	public void test_restricted()
//	{
//		Document doc = pageTester.renderPage("Start");
//		Element link = doc.getElementById("Restricted");
//
//		doc = pageTester.clickLink(link);
//		Element element = doc.getElementById("has_access");
//
//		assertEquals(element.getChildMarkup(), "Has Access");
//	}

	// FIXME this test fail cause i don't have a user logged in
//    @Test
//    public void test_restricted_rolevalue()
//    {
//        Document doc = pageTester.renderPage("Start");
//        Element link = doc.getElementById("Restricted");
//
//        doc = pageTester.clickLink(link);
//        Element element = doc.getElementById("role_meta_value");
//
//        assertEquals(element.getChildMarkup(), "2");
//    }

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

	@Test
	public void test_restrictedActionLink()
	{
		Document doc = pageTester.renderPage("UnRestrictedPage");
		Element link = doc.getElementById("testRights");

		doc = pageTester.clickLink(link);
	}

	@Test
	public void test_restrictedActionLinkOnEvent()
	{
		Document doc = pageTester.renderPage("UnRestrictedPage");
		Element link = doc.getElementById("testRightsOnEvent");

		doc = pageTester.clickLink(link);
	}

	@Test
	public void test_login()
	{
		Document doc = pageTester.renderPage("NotEnoughRights");

		Element element = doc.getElementById("login_message");
		assertEquals(element.getChildMarkup(), "Login Page");

		Element form = doc.getElementById("form");
		Map<String, String> fieldValues = new HashMap<String, String>();
		fieldValues.put("inputUserName", "root");
		fieldValues.put("inputPassword", "banane");
		doc = pageTester.submitForm(form, fieldValues);
	}
}
