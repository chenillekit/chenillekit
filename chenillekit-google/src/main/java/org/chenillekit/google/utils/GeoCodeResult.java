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

package org.chenillekit.google.utils;

/**
 * hold the result codes from gmap.
 *
 * @author <a href="mailto:homburgs@gmail.com">S.Homburg</a>
 * @version $Id$
 */
public class GeoCodeResult
{
    // No errors occurred; the address was successfully parsed and its geocode has been returned. (Since 2.55)
    public static int G_GEO_SUCCESS = 200;

    // A directions request could not be successfully parsed. (Since 2.81)
    public static int G_GEO_BAD_REQUEST = 400;

    // A geocoding or directions request could not be successfully processed, yet the exact reason for the failure
    // is not known. (Since 2.55)
    public static int G_GEO_SERVER_ERROR = 500;

    // The HTTP q parameter was either missing or had no value. For geocoding requests, this means that an empty address was
    // specified as input. For directions requests, this means that no query was specified in the input. (Since 2.81)
    public static int G_GEO_MISSING_QUERY = 601;

    // Synonym for G_GEO_MISSING_QUERY. (Since 2.55)
    public static int G_GEO_MISSING_ADDRESS = 601;

    // No corresponding geographic location could be found for the specified address. This may be due to the fact that the
    // address is relatively new, or it may be incorrect. (Since 2.55)
    public static int G_GEO_UNKNOWN_ADDRESS = 602;

    // The geocode for the given address or the route for the given directions query cannot be returned due to
    // legal or contractual reasons. (Since 2.55)
    public static int G_GEO_UNAVAILABLE_ADDRESS = 603;

    // The GDirections object could not compute directions between the points mentioned in the query. This is usually because
    // there is no route available between the two points, or because we do not have data for routing in that region. (Since 2.81)
    public static int G_GEO_UNKNOWN_DIRECTIONS = 604;

    // The given key is either invalid or does not match the domain for which it was given. (Since 2.55)
    public static int G_GEO_BAD_KEY = 610;

    public static int G_GEO_TOO_MANY_QUERIES = 620;

    private int httpResult;

    /**
     * <ul>
     * <li>0 - Unknown location. (Since 2.59)</li>
     * <li>1 - Country level accuracy. (Since 2.59)</li>
     * <li>2 - Region (state, province, prefecture, etc.) level accuracy. (Since 2.59)</li>
     * <li>3 - Sub-region (county, municipality, etc.) level accuracy. (Since 2.59)</li>
     * <li>4 - Town (city, village) level accuracy. (Since 2.59)</li>
     * <li>5 - Post code (zip code) level accuracy. (Since 2.59)</li>
     * <li>6 - Street level accuracy. (Since 2.59)</li>
     * <li>7 - Intersection level accuracy. (Since 2.59)</li>
     * <li>8 - Address level accuracy. (Since 2.59)</li>
     * <li>9 - Premise (building name, property name, shopping center, etc.) level accuracy. (Since 2.105)</li>
     * </ul>
     */
    private int accuracy;

    private float latitude;
    private float longitude;

    public GeoCodeResult()
    {
    }

    public GeoCodeResult(int httpResult, int accuracy, float latitude, float longitude)
    {
        this.httpResult = httpResult;
        this.accuracy = accuracy;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public int getHttpResult()
    {
        return httpResult;
    }

    public void setHttpResult(int httpResult)
    {
        this.httpResult = httpResult;
    }

    public int getAccuracy()
    {
        return accuracy;
    }

    public void setAccuracy(int accuracy)
    {
        this.accuracy = accuracy;
    }

    public float getLatitude()
    {
        return latitude;
    }

    public void setLatitude(float latitude)
    {
        this.latitude = latitude;
    }

    public float getLongitude()
    {
        return longitude;
    }

    public void setLongitude(float longitude)
    {
        this.longitude = longitude;
    }
}