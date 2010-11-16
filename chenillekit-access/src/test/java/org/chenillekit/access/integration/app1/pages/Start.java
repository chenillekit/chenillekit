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

package org.chenillekit.access.integration.app1.pages;

import java.util.Collections;
import java.util.List;

import org.apache.tapestry5.annotations.Component;
import org.apache.tapestry5.annotations.SessionState;
import org.apache.tapestry5.corelib.components.If;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.ioc.annotations.Local;
import org.apache.tapestry5.ioc.internal.util.CollectionFactory;
import org.chenillekit.access.WebSessionUser;
import org.chenillekit.access.services.AuthenticationService;

/**
 *
 * @version $Id$
 */
public class Start
{
	@Inject
	@Local
	private AuthenticationService authenticationService;

	@SessionState @SuppressWarnings("unused")
	private WebSessionUser<?> webSessionUser;

	@Component(parameters = {"test=authenticated", "negate=true"})
	@SuppressWarnings("unused")
	private If ifNotAuthenticated;

	@Component(parameters = {"test=authenticated"})
	@SuppressWarnings("unused")
	private If ifAuthenticated;

	public static class Item implements Comparable<Item>
	{
		private final String _pageName;
		private final String _label;
		private final String _description;

		public Item(String pageName, String label, String description)
		{
			_pageName = pageName;
			_label = label;
			_description = description;
		}

		public String getPageName()
		{
			return _pageName;
		}

		public String getLabel()
		{
			return _label;
		}

		public String getDescription()
		{
			return _description;
		}

		public int compareTo(Item o)
		{
			return _label.compareTo(o._label);
		}
	}

	private static final List<Item> ITEMS = CollectionFactory.newList(
			new Item("RestrictedPage", "Restricted", "tests Restricted page"),
			new Item("SecureRestrictedPage", "SecureRestricted", "Secure Restricted page"),
			new Item("UnRestrictedPage", "UnRestricted", "tests UnRestricted page"),
			new Item("RestrictedTextField", "RestrictedTextField", "tests RestrictedTextField page"),
			new Item("NotEnoughRights", "NotEnoughRights", "test Restricted page without rights to access"),
			new Item("Invisible", "Invisible", "Invisible page"),
			new Item("Logout", "Logout", "Logout user"),
			new Item("ManagedRestrictedPage", "ManagedRestricted", "test ManagedRestricted page"),
			new Item("ManagedRestrictedPage2", "ManagedRestricted2", "test logged in user but no access page"),
			new Item("Login", "Login", "Login access form")
	);

	static
	{
		Collections.sort(ITEMS);
	}

	private Item _item;

	public List<Item> getItems()
	{
		return ITEMS;
	}

	public Item getItem()
	{
		return _item;
	}

	public void setItem(Item item)
	{
		_item = item;
	}

	public boolean isAuthenticated()
	{
		return authenticationService.isAuthenticate();
	}
}
