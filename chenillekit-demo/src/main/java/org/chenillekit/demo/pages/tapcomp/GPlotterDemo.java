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

package org.chenillekit.demo.pages.tapcomp;

import java.util.List;

import org.apache.tapestry5.RenderSupport;
import org.apache.tapestry5.annotations.Component;
import org.apache.tapestry5.annotations.Environmental;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.corelib.components.ActionLink;
import org.apache.tapestry5.ioc.internal.util.CollectionFactory;

import org.chenillekit.demo.components.LeftSideMenu;
import org.chenillekit.demo.utils.GMapAddress;
import org.chenillekit.tapestry.core.components.GPlotter;

/**
 * @version $Id$
 */
public class GPlotterDemo
{
	@Component(parameters = {"menuName=demo"})
	private LeftSideMenu menu;

	@Component(parameters = {"errorCallbackFunction=literal:GMapErrorHandler",
			"street=literal:",
			"country=prop:selectedAddress.country",
			"state=prop:selectedAddress.state",
			"zipCode=prop:selectedAddress.zipCode",
			"city=prop:selectedAddress.city"})
	private GPlotter gPlotter;

	@Component()
	private ActionLink plotLink;

	@Environmental
	private RenderSupport renderSupport;

	@Persist
	@Property(write = false)
	private List<GMapAddress> addressList;

	@Persist
	@Property
	private GMapAddress selectedAddress;

	@Property
	private GMapAddress address;

	void setupRender()
	{
		if (addressList == null)
		{
			addressList = CollectionFactory.newList();
			addressList.add(new GMapAddress("Howard", "OR", "USA", "", "Portland"));
			addressList.add(new GMapAddress("Sven", "", "DE", "21220", "Seevetal"));
			addressList.add(new GMapAddress("Massimo", "", "IT", "", "Modena"));
			addressList.add(new GMapAddress("Florian", "", "CO", "", "Cali"));
			addressList.add(new GMapAddress("NoBody", "", "MOON", "", "Nowhere"));
		}
	}

	public void onActionFromPlotLink(String name)
	{
		for (GMapAddress a_addressList : addressList)
		{
			if (a_addressList.getName().equalsIgnoreCase(name))
			{
				selectedAddress = a_addressList;
				System.err.println("Selected: " + a_addressList.getName());
			}
		}
	}
}