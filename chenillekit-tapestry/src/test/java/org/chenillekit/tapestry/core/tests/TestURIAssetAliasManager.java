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

package org.chenillekit.tapestry.core.tests;

import java.util.HashMap;
import java.util.Map;

import org.apache.tapestry5.services.Request;

import org.chenillekit.tapestry.core.services.URIAssetAliasManager;
import org.chenillekit.tapestry.core.services.impl.URIAssetAliasManagerImpl;
import org.easymock.EasyMock;
import org.testng.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

/**
 * @version $Id$
 */
public class TestURIAssetAliasManager extends Assert
{
	private URIAssetAliasManager aliasManager;

	@BeforeTest
	public void initializeURIAssetAliasManager()
	{
		Request request = (Request) EasyMock.createMock(Request.class);
		EasyMock.expect(request.getContextPath()).andReturn("testcontext");
		EasyMock.replay(request);
		Map<String, String> configuration = new HashMap<String, String>();
		configuration.put("root_path", "file:///");
		configuration.put("home_path", "file://home/sven");
		aliasManager = new URIAssetAliasManagerImpl(request, configuration);
	}

	@Test
	public void existingURI()
	{
		String clientUrl = aliasManager.toClientURL("file://home/sven/test.txt");
		assertEquals(clientUrl, "testcontext/uri/home_path/test.txt");

		String path = aliasManager.toResourcePath("/uri/root_path/test.asc");
		assertEquals(path, "file:///test.asc");
	}
}
