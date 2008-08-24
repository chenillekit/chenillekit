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

import java.io.Serializable;
import java.util.Locale;

/**
 * simple class to hold a location for geocoding.
 *
 * @author <a href="mailto:homburgs@gmail.com">S.Homburg</a>
 * @version $Id$
 */
public class GeoCodeLocation implements Serializable
{
    private Locale locale;
    private String street;
    private String country;
    private String state;
    private String zipCode;
    private String city;

    public GeoCodeLocation()
    {
        this.locale = Locale.getDefault();
    }

    public GeoCodeLocation(String street, String country, String state, String zipCode, String city)
    {
        this.locale = Locale.getDefault();
        this.street = street;
        this.country = country;
        this.state = state;
        this.zipCode = zipCode;
        this.city = city;
    }

    public GeoCodeLocation(final Locale locale, final String street, final String country, final String state,
                           final String zipCode, final String city)
    {
        this.locale = locale;
        this.street = street;
        this.country = country;
        this.state = state;
        this.zipCode = zipCode;
        this.city = city;
    }

    public String getStreet()
    {
        return street != null ? street : "";
    }

    public void setStreet(String street)
    {
        this.street = street;
    }

    public String getCountry()
    {
        return country != null ? country : "";
    }

    public void setCountry(String country)
    {
        this.country = country;
    }

    public String getState()
    {
        return state != null ? state : "";
    }

    public void setState(String state)
    {
        this.state = state;
    }

    public String getZipCode()
    {
        return zipCode != null ? zipCode : "";
    }

    public void setZipCode(String zipCode)
    {
        this.zipCode = zipCode;
    }

    public String getCity()
    {
        return city != null ? city : "";
    }

    public void setCity(String city)
    {
        this.city = city;
    }

    public Locale getLocale()
    {
        return locale;
    }

    public void setLocale(Locale locale)
    {
        this.locale = locale;
    }
}