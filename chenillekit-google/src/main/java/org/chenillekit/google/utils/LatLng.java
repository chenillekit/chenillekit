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
public class LatLng
{
    private String _latitude;
    private String _longitude;

    public LatLng(String latitude, String longitude)
    {
        _latitude = latitude;
        _longitude = longitude;
    }

    public String getLatitude()
    {
        return _latitude;
    }

    public void setLatitude(String latitude)
    {
        _latitude = latitude;
    }

    public String getLongitude()
    {
        return _longitude;
    }

    public void setLongitude(String longitude)
    {
        _longitude = longitude;
    }
}
