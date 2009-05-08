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
 *
 */

package org.chenillekit.google.utils.geocode;

import org.chenillekit.google.utils.JSONException;
import org.chenillekit.google.utils.JSONObject;

/**
 * @version $Id$
 */
public class Placemark
{
    private AddressDetails addressDetails;
    private LatLng latLng;
    private String address;
    private String id;

    public Placemark(JSONObject json)
    {
        buildFromJSON(json);
    }

    private void buildFromJSON(JSONObject json)
    {
        try
        {
            address = json.getString("address");
            id = json.getString("id");
            addressDetails = new AddressDetails(json.getJSONObject("AddressDetails"));
            latLng = new LatLng(json.getJSONObject("Point"));
        }
        catch (JSONException e)
        {
            throw new RuntimeException(e);
        }
    }

    public AddressDetails getAddressDetails()
    {
        return addressDetails;
    }

    public String getAddress()
    {
        return address;
    }

    public String getId()
    {
        return id;
    }

    public LatLng getLatLng()
    {
        return latLng;
    }
}
