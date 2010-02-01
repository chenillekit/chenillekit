/*
 * Apache License
 * Version 2.0, January 2004
 * http://www.apache.org/licenses/
 *
 * Copyright 2009 by chenillekit.org
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 */

package org.chenillekit.tapestry.core.pages;

import org.apache.tapestry5.dom.Document;
import org.apache.tapestry5.dom.Element;
import org.apache.tapestry5.test.PageTester;

import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * @author <a href="mailto:homburgs@googlemail.com">sven</a>
 * @version $Id$
 */
public class TestUriAssetPage extends Assert
{
	@Test
	public void test1()
	{
		String appPackage = "org.chenillekit.tapestry.core";
		String appName = "Test";
		PageTester tester = new PageTester(appPackage, appName, "src/test/webapp");

		Document doc = tester.renderPage("UriAssetDemo");
		Element image = doc.getElementById("test1");
		assertEquals(image.getAttribute("src"), "/foo/uri/http%3A%2F%2Fwww.heise.de%2Fct%2Fmotive%2Fimage%2F1476%2Fp800_en.jpg");

	}
}
