/*
 * Apache License
 * Version 2.0, January 2004
 * http://www.apache.org/licenses/
 *
 * Copyright 2008-2009 by chenillekit.org
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 */

package org.chenillekit.access.internal;

import org.apache.tapestry5.EventContext;
import org.apache.tapestry5.internal.EmptyEventContext;
import org.apache.tapestry5.ioc.test.TestBase;
import org.testng.annotations.Test;

/**
 * Testing internal utilities
 *  
 * @id $id$
 */
public class ChenilleKitAccessInternalUtilsTest extends TestBase
{
	private final static String[] ADMINS = { "ADMINS", "ADMINISTRATORS" };
	private final static String[] USERS = { "USERS", "NORMAL_USERS" };
	private final static String[] ALL = { "ADMINS", "ADMINISTRATORS", "USERS", "NORMAL_USERS" };
	
	
	@Test
	public void null_context_as_string()
	{
		String res = ChenillekitAccessInternalUtils.getContextAsString(null);
		
		assertEquals(res, "");
	}
	
	@Test
	public void empty_context_as_string()
	{
		String res = ChenillekitAccessInternalUtils.getContextAsString(new EmptyEventContext());
		
		assertEquals(res, "");
	}
	
	@Test
	public void context_as_string()
	{
		EventContext context = newMock(EventContext.class);
		
		expect(context.getCount()).andReturn(3);
		
		expect(context.get(String.class, 0)).andReturn("UNO");
		expect(context.get(String.class, 1)).andReturn("DUE");
		expect(context.get(String.class, 2)).andReturn("TRE");
		
		replay();
		
		String result = ChenillekitAccessInternalUtils.getContextAsString(context);
		
		verify();
		
		assertEquals(result, "UNO####DUE####TRE####");
	}
	
	@Test
	public void required_groups_with_null_required()
	{
		assertTrue(ChenillekitAccessInternalUtils.hasUserRequiredGroup(ADMINS, null));
	}
	
	@Test
	public void required_groups_with_null_required_and_groups()
	{
		assertTrue(ChenillekitAccessInternalUtils.hasUserRequiredGroup(null, null));
	}
	
	@Test
	public void required_groups_with_null_groups()
	{
		assertFalse(ChenillekitAccessInternalUtils.hasUserRequiredGroup(null, USERS));
	}
	
	@Test
	public void required_groups_ok()
	{
		assertTrue(ChenillekitAccessInternalUtils.hasUserRequiredGroup(ALL, ADMINS));
	}
	
	@Test
	public void required_groups_ko()
	{
		assertFalse(ChenillekitAccessInternalUtils.hasUserRequiredGroup(USERS, ADMINS));
	}
	
	@Test
	public void string_array_as_string_null()
	{
		String expected = "";
		String actual = ChenillekitAccessInternalUtils.getStringArrayAsString(null);
		
		assertEquals(actual, expected);
	}
	
	@Test
	public void string_array_as_string()
	{
		String expected = "ADMINS,ADMINISTRATORS";
		String actual = ChenillekitAccessInternalUtils.getStringArrayAsString(ADMINS);
		
		assertEquals(actual, expected);
	}
	
	@Test
	public void string_as_string_array_null()
	{
		String[] expected = new String[0];
		String[] actual = ChenillekitAccessInternalUtils.getStringAsStringArray(null);
		
		assertEquals(actual, expected);
	}
	
	@Test
	public void string_as_string_array()
	{
		String[] expected = USERS;
		String[] actual = ChenillekitAccessInternalUtils.getStringAsStringArray("USERS,NORMAL_USERS");
		
		assertEquals(actual, expected);
	}

}
