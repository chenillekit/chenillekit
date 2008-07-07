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

package org.chenillekit.google.utils;

/**
 * @author <a href="mailto:homburgs@gmail.com">S.Homburg</a>
 * @version $Id$
 */
public class GoogleGeoCode
{
    private int _httpStatusCode;
    private int _accuracy;
    private LatLng _latLng;

    public GoogleGeoCode(int httpStatusCode, int accuracy, String latitude, String longitude)
    {
        _httpStatusCode = httpStatusCode;
        _accuracy = accuracy;
        _latLng = new LatLng(latitude, longitude);
    }

    public int getHttpStatusCode()
    {
        return _httpStatusCode;
    }

    public int getAccuracy()
    {
        return _accuracy;
    }

    public LatLng getLatLng()
    {
        return _latLng;
    }
}
