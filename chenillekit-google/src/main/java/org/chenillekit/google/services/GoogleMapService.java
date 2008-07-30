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

import org.chenillekit.google.utils.GMapLocation;
import org.chenillekit.google.utils.GeoCodeResultList;


/**
 * This service let you ues some Google Maps services in your application.
 *
 * @author <a href="mailto:homburgs@googlemail.com">S.Homburg</a>
 * @version $Id: GoogleMapService.java 367 2008-02-06 09:59:36Z homburgs $
 */
public interface GoogleMapService
{
    public static String CONFIG_KEY = "/src/test/conf/gmap.properties";
    public static String GOOGLE_KEY_FILE = "google.keyfile";
    public static String GOOGLE_TIMEOUT = "google.timeout";
    public static String GOOGLE_PROXY = "google.proxy";

    /**
     * get the geo code from google map service for address.
     *
     * @param gMapLocation location holder
     */
    GeoCodeResultList getGeoCode(GMapLocation gMapLocation);

    /**
     * get the geo code from google map service for address.
     *
     * @param geoCodes     empty list for geo codes received from google maps
     * @param gMapLocation location holder
     */
    void getGeoCode(GeoCodeResultList geoCodes, GMapLocation gMapLocation);

    /**
     * get the geo code from google map service for address.
     *
     * @param geoCodes empty list for geo codes received from google maps
     * @param street   the street
     * @param country  the country
     * @param state    the state
     * @param zipCode  the zip code
     * @param city     the city
     */
    void getGeoCode(GeoCodeResultList geoCodes, String street, String country, String state, String zipCode, String city);

    /**
     * get the access key for your map service.
     *
     * @return the access key
     */
    String getAccessKey();
}
