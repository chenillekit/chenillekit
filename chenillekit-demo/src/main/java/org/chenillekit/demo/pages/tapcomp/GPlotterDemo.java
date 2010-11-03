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

package org.chenillekit.demo.pages.tapcomp;

import java.util.List;

import org.apache.tapestry5.annotations.Component;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.corelib.components.ActionLink;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.ioc.internal.util.CollectionFactory;
import org.apache.tapestry5.services.Request;

import org.chenillekit.demo.components.LeftSideMenu;
import org.chenillekit.demo.utils.GMapAddress;
import org.chenillekit.google.services.GoogleGeoCoder;
import org.chenillekit.google.utils.geocode.GeoCodeResult;
import org.chenillekit.google.utils.geocode.LatLng;
import org.chenillekit.google.utils.geocode.Placemark;
import org.chenillekit.tapestry.core.components.GPlotter;

/**
 * @version $Id$
 */
public class GPlotterDemo
{
	/**
	 * inject our google map service.
	 */
	@Inject
	private GoogleGeoCoder geoCoder;

	/**
	 * Request object for information on current request.
	 */
	@Inject
	private Request request;

	@Component(parameters = {"menuName=demo"})
	private LeftSideMenu menu;

	@Component(parameters = {"errorCallbackFunction=literal:GMapErrorHandler",
			"lat=prop:center.latitude",
			"lng=prop:center.longitude"})
	private GPlotter gPlotter;

	@Component()
	private ActionLink plotLink;

	@Persist
	@Property(write = false)
	private List<GMapAddress> addressList;

	@Persist
	@Property
	private GMapAddress selectedAddress;

	@Persist
	@Property
	private GeoCodeResult geoCodeResult;

	@Property
	private GMapAddress address;

	@Property
	private Placemark placemark;

	void setupRender()
	{
		if (addressList == null)
		{
			addressList = CollectionFactory.newList();
			addressList.add(new GMapAddress("Howard", "OR", "USA", "", "Portland"));
			addressList.add(new GMapAddress("Sven", "", "DE", "21220", "Seevetal"));
			addressList.add(new GMapAddress("Massimo", "", "IT", "", "Modena"));
			addressList.add(new GMapAddress("Alejandro", "", "", "", "", "sierras argentina"));
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
				geoCodeResult = geoCoder.getGeoCode(request.getLocale(), selectedAddress.getStreet(),
													selectedAddress.getCountry(), selectedAddress.getState(), selectedAddress.getZipCode(),
													selectedAddress.getCity());
			}
		}
	}

	public String getInfoWindowHtml()
	{
		return "<b>" + " SOME LABEL " + "</b>" + /*selectedAddress.getStreet() +*/ "<br/>" + selectedAddress.getCountry() +
				"<br/> (" + selectedAddress.getState() + ") - " + selectedAddress.getZipCode() + " " +
				selectedAddress.getCity();
	}

	public String getGPlotterId()
	{
		return gPlotter.getClientId();
	}

	public LatLng getCenter()
	{
		if (geoCodeResult.getPlacemarks().size() > 0)
		{
			return geoCodeResult.getPlacemarks().get(0).getLatLng();
		}
		else
		{
			return new LatLng(41.387918, 2.169929);
		}
	}
}
