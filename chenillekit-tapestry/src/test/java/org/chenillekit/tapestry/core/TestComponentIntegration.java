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

package org.chenillekit.tapestry.core;

import org.apache.tapestry5.test.AbstractIntegrationTestSuite;

import org.testng.annotations.Test;

/**
 * @version $Id$
 */
public class TestComponentIntegration extends AbstractIntegrationTestSuite
{
    /**
     * Initializes the suite using {@link #DEFAULT_WEB_APP_ROOT}.
     */
    public TestComponentIntegration()
    {
        super("src/test/webapp", "*firefox /usr/lib/firefox/firefox-2-bin");
    }

    @Test(enabled = false)
    public void test_accordion() throws InterruptedException
    {
        open(BASE_URL);

        start("Accordion");
        waitForPageToLoad("5000");
        click("xpath=//div[@id='accordion_toggle_1']");
        Thread.sleep(2000);
        assertEquals(getElementHeight("xpath=//div[@id='accordion_content_0']"), 0);
        assertEquals(getElementHeight("xpath=//div[@id='accordion_content_1']"), 20);

        click("xpath=//div[@id='accordion_toggle_3']");
        Thread.sleep(2000);
        assertEquals(getElementHeight("xpath=//div[@id='accordion_content_3']"), 20);
        assertEquals(getElementHeight("xpath=//div[@id='accordion_content_1']"), 0);
    }

    @Test
    public void test_datetimefield() throws InterruptedException
    {
        open(BASE_URL);

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
        open(BASE_URL);

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
        open(BASE_URL);

        start("Contains");
        waitForPageToLoad("5000");

        assertEquals(getText("xpath=//div[@id='test1']"), "list contains 'test1'");
        assertEquals(getText("xpath=//div[@id='test4']"), "list doesnt contains 'test6'");
    }

    @Test
    public void test_equals() throws InterruptedException
    {
        open(BASE_URL);

        start("Equals");
        waitForPageToLoad("5000");

        assertEquals(getText("xpath=//div[@id='test2']"), "'testLeft' dont equals 'testRight'");
        assertEquals(getText("xpath=//div[@id='test3']"), "'testLeft' dont equals 'testRight'");
    }

    @Test(enabled = false)
    public void test_fieldset() throws InterruptedException
    {
        String style;

        open(BASE_URL);

        start("FieldSet");
        waitForPageToLoad("5000");
        click("xpath=//fieldset[@id='fieldSet1']//legend");
        Thread.sleep(2000);
        style = getAttribute("xpath=//fieldset[@id='fieldSet1']//legend[@class='ck_fieldset_content']@style");
        assertEquals(style, "width: 375px;");

        click("xpath=//fieldset[@id='fieldSet2']//legend");
        Thread.sleep(2000);
        style = getAttribute("xpath=//fieldset[@id='fieldSet1']@style");
        assertEquals(style, "xxxx");
    }

    @Test
    public void test_element() throws InterruptedException
    {
        open(BASE_URL);

        start("Element");
        waitForPageToLoad("5000");

        assertEquals(getText("xpath=//strong[@id='element']"), "BlaBla");
    }

    @Test
    public void test_hidden() throws InterruptedException
    {
        open(BASE_URL);

        start("Hidden");
        waitForPageToLoad("5000");

        this.runScript("document.form.hidden1.value = 'BlaBla';");
        this.runScript("document.form.hidden2.value = '200';");
        this.runScript("document.form.hidden3.value = '200.12';");
        clickAndWait(SUBMIT);

        assertEquals(getText("xpath=//strong[@id='hiddenResult1']"), "BlaBla");
        assertEquals(getText("xpath=//strong[@id='hiddenResult2']"), "200");
        assertEquals(getText("xpath=//strong[@id='hiddenResult3']"), "200.12");
    }

    @Test
    public void test_inplace() throws InterruptedException
    {
        open(BASE_URL);

        start("InPlace");
        waitForPageToLoad("5000");

        click("xpath=//input[@id='inPlaceCheckbox']");
//        click("xpath=//span[@id='inPlaceEditor']");
//        type("xpath=//form[@id='inPlaceEditor-inplaceeditor']//input", "BlaBla");
//        Thread.sleep(5000);
//        click("xpath=//form[@id='inPlaceEditor-inplaceeditor']//input[@class='editor_ok_button']");
        Thread.sleep(1000);

        assertEquals(getText("xpath=//strong[@id='inPlaceCheckboxValue']"), "checked");
//        assertEquals(getText("xpath=//span[@id='inPlaceEditor']"), "BlaBla");
    }

    @Test
    public void test_thumbnail()
    {
        open(BASE_URL);

        start("ThumbNail");
        waitForPageToLoad("5000");
        captureScreenshot("test_thumbnail.png");
        assertAttribute("xpath=//img@src", "thumbnails/2373045207");

        click("xpath=//img[@id='thumbNail']");
        assertAttribute("xpath=//img[@id='thumbNail']@src", "assets/images/sven.jpg");

        mouseOut("xpath=//img[@id='thumbNail']");
        assertAttribute("xpath=//img[@id='thumbNail']@src", "http://localhost:9999/thumbnails/2373045207");
    }

    @Test
    public void test_onevent() throws InterruptedException
    {
        open(BASE_URL);

        start("OnEvent");
        waitForPageToLoad("5000");
        select("xpath=//select[@id='select1']", "BLACK");
        select("xpath=//select[@id='select1']", "GREEN");
        Thread.sleep(1000);

        assertEquals(getText("xpath=//div[@id='result1']"), "GREEN");
    }

    @Test
    public void test_tabset()
    {
        open(BASE_URL);

        start("TabSet");
        waitForPageToLoad("5000");

        click("xpath=//span[@id='stuff2']");

        assertTrue(this.isVisible("xpath=//div[@id='contentZone']"));
    }

    @Test(enabled = false)
    public void test_slideshow()
    {
        open(BASE_URL);

        start("SlideShow");
        waitForPageToLoad("5000");

        assertTrue(this.isVisible("xpath=//img[@class='ck-slide']"));
    }

	@Test
	public void test_uriasset()
	{
		open(BASE_URL);

		start("UriAsset");
		waitForPageToLoad("5000");

		assertEquals(this.getAttribute("xpath=//a[@id='test1']@href"), "uri/http%3A%2F%2Fwww.google.com");

		click("xpath=//a[@id='test1']");
		waitForPageToLoad("5000");

		assertEquals(this.getTitle(), "Google");
	}
}
