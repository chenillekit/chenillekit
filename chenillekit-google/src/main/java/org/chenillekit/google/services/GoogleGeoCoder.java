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

package org.chenillekit.google.services;

import java.util.Locale;

import org.chenillekit.google.utils.GeoCodeLocation;
import org.chenillekit.google.utils.geocode.GeoCodeResult;

/**
 * This service let you ues some Google Maps services in your application.
 *
 * @version $Id: GoogleMapService.java 367 2008-02-06 09:59:36Z homburgs $
 */
public interface GoogleGeoCoder extends GoogleService
{
	/**
	 * get the geo code from google map service for address.
	 *
	 * @param geoCodeLocation location holder
	 */
	GeoCodeResult getGeoCode(GeoCodeLocation geoCodeLocation);

	/**
	 * get the geo code from google map service for address.
	 *
	 * @param street  the street
	 * @param country the country
	 * @param state   the state
	 * @param zipCode the zip code
	 * @param city	the city
	 */
	GeoCodeResult getGeoCode(String street, String country, String state, String zipCode, String city);

	/**
	 * get the geo code from google map service for address.
	 *
	 * @param locale
	 * @param street  the street
	 * @param country the country
	 * @param state   the state
	 * @param zipCode the zip code
	 * @param city	the city
	 */
	GeoCodeResult getGeoCode(Locale locale, String street, String country, String state, String zipCode, String city);
}
