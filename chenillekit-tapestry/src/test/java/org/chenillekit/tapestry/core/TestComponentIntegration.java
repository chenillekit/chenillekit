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

package org.chenillekit.tapestry.core;

import org.apache.tapestry5.test.SeleniumTestCase;
import org.testng.annotations.Test;

/**
 * @version $Id$
 */
@Test
public class TestComponentIntegration extends SeleniumTestCase
{
	public static final String SUBMIT = "//input[@type='submit']";

	public void test_accordion()
	{
		openBaseURL();

		clickAndWait("link=Accordion");
		waitForPageToLoad("5000");
		click("xpath=//div[@id='accordion_toggle_1']");
//		Thread.sleep(2000);

		assertEquals(getAttribute("xpath=//div[@id='accordion_content_0']@style"), "display: none;");
		assertEquals(getAttribute("xpath=//div[@id='accordion_content_1']@style"), "overflow: hidden;");
//
//		click("xpath=//div[@id='accordion_toggle_3']");
//		Thread.sleep(2000);
//		assertEquals(getAttribute("xpath=//div[@id='accordion_content_3']@style"), "overflow: hidden;");
//		assertEquals(getAttribute("xpath=//div[@id='accordion_content_1']@style"), "overflow: hidden; display: none;");
	}

	@Test
	public void test_datetimefield() throws InterruptedException
	{
		openBaseURL();

		start("DateTimeField");
		waitForPageToLoad("5000");

		type("xpath=//input[@id='dateTimeField1']", "10-12-2008 15:12");
		type("xpath=//input[@id='dateTimeField2']", "11/31/2007");
		clickAndWait(SUBMIT);

		assertEquals(getValue("xpath=//input[@id='dateTimeField1']"), "10-12-2008 15:12");
		assertEquals(getValue("xpath=//input[@id='dateTimeField2']"), "12/01/2007");
		assertEquals(getValue("xpath=//input[@id='dateTimeField3']"), "");
	}

	@Test
	public void test_formater() throws InterruptedException
	{
		openBaseURL();

		start("Formater");
		waitForPageToLoad("5000");

		assertEquals(getText("xpath=//div[@id='test1']"), "today is 01.01.1970");
		assertEquals(getText("xpath=//div[@id='test2']"), "01/01/1970 is an US formated date");
		assertEquals(getText("xpath=//div[@id='test3']"), "01/01/1970");
		assertEquals(getText("xpath=//span[@id='test4']"), "this ist a very long...");
		assertEquals(getText("xpath=//span[@id='test5']"), "this ist a...");
		assertEquals(getText("xpath=//span[@id='test6']"), "...ery long test string");
		assertEquals(getText("xpath=//span[@id='test7']"), "555-685458-0");
		assertEquals(getText("xpath=//span[@id='test8']"), "555-6854__-_");
	}

	@Test
	public void test_contains() throws InterruptedException
	{
		openBaseURL();

		start("Contains");
		waitForPageToLoad("5000");

		assertEquals(getText("xpath=//div[@id='test1']"), "list contains 'test1'");
		assertEquals(getText("xpath=//div[@id='test4']"), "list doesnt contains 'test6'");
	}

	@Test
	public void test_equals() throws InterruptedException
	{
		openBaseURL();

		start("Equals");
		waitForPageToLoad("5000");

		assertEquals(getText("xpath=//div[@id='test2']"), "'testLeft' dont equals 'testRight'");
		assertEquals(getText("xpath=//div[@id='test3']"), "'testLeft' dont equals 'testRight'");
	}

	@Test
	public void test_fieldset() throws InterruptedException
	{
		String style;

		openBaseURL();

		start("FieldSet");
		waitForPageToLoad("5000");
		click("xpath=//fieldset[@id='fieldSet1']//legend");
		Thread.sleep(2000);
		assertEquals(getAttribute("xpath=//fieldset[@id='fieldSet1']//div[@class='ck_fieldset_content']@style"), "overflow: visible;");

		click("xpath=//fieldset[@id='fieldSet2']//legend");
		Thread.sleep(2000);
		assertEquals(getAttribute("xpath=//fieldset[@id='fieldSet2']//div[@class='ck_fieldset_content']@style"), "overflow: visible; display: none;");
	}

	@Test
	public void test_element() throws InterruptedException
	{
		openBaseURL();

		start("Element");
		waitForPageToLoad("5000");

		assertEquals(getText("xpath=//strong[@id='element']"), "BlaBla");
	}

	@Test(enabled = false)
	public void test_hidden() throws InterruptedException
	{
		openBaseURL();

		start("Hidden");
		waitForPageToLoad("5000");

		float floatValue = 200.12f;

		this.runScript("$(hidden1).value = 'BlaBla';");
		this.runScript("$(hidden2).value = '200';");
		this.runScript("$(hidden3).value = '200,21';");
		Thread.sleep(10000);
		clickAndWait(SUBMIT);

		assertEquals(getText("xpath=//strong[@id='hiddenResult1']"), "BlaBla");
		assertEquals(getText("xpath=//strong[@id='hiddenResult2']"), "200");
		assertEquals(getText("xpath=//strong[@id='hiddenResult3']"), "200.21");
	}

	@Test
	public void test_inplace() throws InterruptedException
	{
		openBaseURL();

		start("InPlace");
		waitForPageToLoad("5000");

		click("xpath=//input[@id='inPlaceCheckbox']");
//        click("xpath=//span[@id='inPlaceEditor']");
//		Thread.sleep(1000);
//        type("xpath=//form[@id='inPlaceEditor-inplaceeditor']//input", "BlaBla");
//        click("xpath=//form[@id='inPlaceEditor-inplaceeditor']//input[@class='editor_ok_button']");
		Thread.sleep(1000);

		assertEquals(getText("xpath=//strong[@id='inPlaceCheckboxValue']"), "checked");
//        assertEquals(getText("xpath=//span[@id='inPlaceEditor']"), "BlaBla");
	}

	@Test
	public void test_onevent() throws InterruptedException
	{
		openBaseURL();

		start("OnEvent");
		waitForPageToLoad("5000");
		select("xpath=//select[@id='select1']", "BLACK");
		select("xpath=//select[@id='select1']", "GREEN");
		Thread.sleep(2000);

		assertEquals(getText("xpath=//div[@id='result1']"), "GREEN");
	}


	@Test
	public void test_button()
	{
		openBaseURL();

		start("Button");
//		waitforPageToLoad("5000");

		click("xpath=//button[@id='theButton']");

		waitForPageToLoad("5000");

		assertEquals(getText("xpath=//div[@id='testButton']"), "Are you happy?");
	}


	@Test
	public void test_thumbnail()
	{
		openBaseURL();

		start("ThumbNail");
		waitForPageToLoad("5000");
		String iconUrl = getAttribute("xpath=//img@src");

		click("xpath=//img[@id='thumbNail']");
		assertAttribute("xpath=//img[@id='thumbNail']@src", "/assets/ctx/1.0-test/assets/images/sven.jpg");

		mouseOut("xpath=//img[@id='thumbNail']");
		assertAttribute("xpath=//img[@id='thumbNail']@src", "http://localhost:9999" + iconUrl);
	}

	@Test
	public void test_tabset()
	{
		openBaseURL();

		start("TabSet");
		waitForPageToLoad("5000");

		click("xpath=//span[@id='stuff2']");

		assertTrue(this.isVisible("xpath=//div[@id='contentZone']"));
	}

	@Test
	public void test_slideshow()
	{
		openBaseURL();

		start("SlideShow");
		waitForPageToLoad("5000");

		assertTrue(this.isVisible("xpath=//img[@class='ck-slide']"));
	}

	@Test
	public void test_uriasset()
	{
		openBaseURL();

		start("UriAsset");
		waitForPageToLoad("5000");

		assertEquals(this.getAttribute("xpath=//img[@id='test1']@src"), "uri/http%3A%2F%2Fwww.heise.de%2Fct%2Fmotive%2Fimage%2F1476%2Fp800_de.jpg");
	}
}
