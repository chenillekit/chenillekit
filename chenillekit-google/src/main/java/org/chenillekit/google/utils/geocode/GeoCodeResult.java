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

package org.chenillekit.google.utils.geocode;

import java.util.List;

import org.apache.tapestry5.ioc.internal.util.CollectionFactory;

import org.chenillekit.google.utils.JSONArray;
import org.chenillekit.google.utils.JSONException;
import org.chenillekit.google.utils.JSONObject;

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

    private String name;
    private Status status;
    private List<Placemark> placemarks = CollectionFactory.newList();

    public GeoCodeResult(JSONObject geoCodeResult)
    {
        buildFromJSON(geoCodeResult);
    }

    private void buildFromJSON(JSONObject json)
    {
        try
        {
            name = json.getString("name");
            status = new Status(json.getJSONObject("Status"));

            if (json.has("Placemark"))
            {
                JSONArray jsonArray = json.getJSONArray("Placemark");
                for (int i = 0; i < jsonArray.length(); i++)
                    placemarks.add(new Placemark(jsonArray.getJSONObject(i)));
            }
        }
        catch (JSONException e)
        {
            throw new RuntimeException(e);
        }
    }

    public String getName()
    {
        return name;
    }

    public Status getStatus()
    {
        return status;
    }

    public List<Placemark> getPlacemarks()
    {
        return placemarks;
    }
}