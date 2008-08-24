/*
 * Apache License
 * Version 2.0, January 2004
 * http://www.apache.org/licenses/
 *
 * Copyright 1996-2008 by Sven Homburg
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 */
package org.chenillekit.google.utils.geocode;

import org.chenillekit.google.utils.JSONObject;
import org.chenillekit.google.utils.JSONException;

/**
 * @author <a href="mailto:homburgs@gmail.com">S.Homburg</a>
 * @version $Id$
 */
public class LatLng
{
    private double latitude;
    private double longitude;

    public LatLng(JSONObject json)
    {
        buildFromJSON(json);
    }

    private void buildFromJSON(JSONObject json)
    {
        try
        {
            longitude = json.getJSONArray("coordinates").getDouble(0);
            latitude = json.getJSONArray("coordinates").getDouble(1);
        }
        catch (JSONException e)
        {
            throw new RuntimeException(e);
        }
    }

    public double getLatitude()
    {
        return latitude;
    }

    public void setLatitude(double latitude)
    {
        this.latitude = latitude;
    }

    public double getLongitude()
    {
        return longitude;
    }

    public void setLongitude(double longitude)
    {
        this.longitude = longitude;
    }
}
