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

/**
 *
 */
package org.chenillekit.ldap.tests;

import netscape.ldap.LDAPConnection;
import org.chenillekit.ldap.ChenilleKitLDAPTestModule;
import org.chenillekit.ldap.services.internal.LDAPSource;
import org.chenillekit.ldap.services.internal.WriteService;
import org.chenillekit.ldap.services.internal.WriteServiceImpl;
import org.chenillekit.test.AbstractTestSuite;
import org.easymock.classextension.EasyMock;
import org.slf4j.Logger;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

/**
 *
 *
 */
public class WriteServiceTest extends AbstractTestSuite
{
	private WriteService writeService;

	private WriteServiceImpl writeServiceImpl;
	private Logger mockLogger;
	private LDAPSource mockSource;

	@BeforeTest
    public final void setup_registry()
    {
        super.setup_registry(ChenilleKitLDAPTestModule.class);
        writeService = registry.getService(WriteService.class);
    }

	@BeforeMethod
	public final void setup_mocks()
	{
		mockLogger = EasyMock.createMock(Logger.class);
		mockSource = EasyMock.createMock(LDAPSource.class);

		writeServiceImpl = new WriteServiceImpl(mockLogger, mockSource);
	}

	@Test
	public void add_entry() throws Exception
	{
		LDAPConnection mockConn = EasyMock.createMock(LDAPConnection.class);

		EasyMock.expect(mockSource.openSession()).andReturn(mockConn);

		EasyMock.replay(mockSource);

		writeServiceImpl.addEntry("uid=duffy_duck", null);

		EasyMock.verify(mockSource);
	}

	@Test
	public void delete_entry_npe() throws Exception
	{
		try
		{
			mockSource.openSession().delete("uid=null");

			writeServiceImpl.deleteEntry("uid=null");
		}
		catch (NullPointerException npe)
		{
			return;
		}

		fail();
	}
}
