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

import java.util.List;

import org.chenillekit.google.utils.GoogleGeoCode;
import org.chenillekit.google.utils.LatLng;


/**
 * This service let you ues some Google Maps services in your application.
 *
 * @author <a href="mailto:homburgs@googlemail.com">S.Homburg</a>
 * @version $Id: GoogleMapService.java 367 2008-02-06 09:59:36Z homburgs $
 */
public interface GoogleMapService
{
    /**
     * get the geo code from google map service for address.
     *
     * @param street  the street
     * @param country the country
     * @param state   the state
     * @param zipCode the zip code
     * @param city    the city
     *
     * @return list of google resonsed values
     */
    List<GoogleGeoCode> getGeoCode(String street, String country, String state, String zipCode, String city);

    /**
     * get the latitude and longitude from geo code as string array.
     *
     * @param goggleMapResponse the response from google map service as list
     * @param position          get the list element at position <em>position</em>
     *
     * @return the latitude and longitude
     */
    LatLng getLatLag(List<GoogleGeoCode> goggleMapResponse, int position);

    /**
     * get the access key for your map service.
     *
     * @return the access key
     */
    String getAccessKey();
}
