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
 * @author <a href="mailto:homburgs@googlemail.com">shomburg</a>
 * @version $Id$
 */
public class TestComponentIntegration extends AbstractIntegrationTestSuite
{
    /**
     * Initializes the suite using {@link #DEFAULT_WEB_APP_ROOT}.
     */
    public TestComponentIntegration()
    {
        super("src/test/webapp");
    }

    @Test
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
}
