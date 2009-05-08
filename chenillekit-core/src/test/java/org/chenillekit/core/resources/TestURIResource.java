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

package org.chenillekit.core.resources;

import java.io.File;
import java.net.MalformedURLException;

import org.apache.tapestry5.ioc.Resource;

import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * @author <a href="mailto:shomburg@depot120.dpd.de">S.Homburg</a>
 * @version $Id$
 */
public class TestURIResource extends Assert
{
	@Test
	public void httpURI()
	{
		Resource resource = new URIResource("http://www.google.com/index.html");

		assertEquals(resource.getFile(), "/index.html");
		assertEquals(resource.getPath(), "http://www.google.com/index.html");
		assertEquals(resource.getFolder(), "");
	}

	@Test
	public void ftpURI()
	{
		Resource resource = new URIResource("ftp://ftp.uni-kassel.de/pub/Index.txt");

		assertEquals(resource.getFile(), "Index.txt");
		assertEquals(resource.getPath(), "ftp://ftp.uni-kassel.de/pub/Index.txt");
		assertEquals(resource.getFolder(), "/pub");
	}

	@Test
	public void existsURI()
	{
		Resource resource = new URIResource("http://www.google.com/index.html");
		assertTrue(resource.exists());
	}

	@Test
	public void notExistsURI()
	{
		Resource resource = new URIResource("http://www.google.com/index_xx.html");
		assertFalse(resource.exists());
	}

	@Test
	public void fileURI() throws MalformedURLException
	{
		Resource resource = new URIResource(new File("./pom.xml"));
		assertTrue(resource.exists());
		assertEquals(resource.getFile(), "pom.xml");
	}
}
