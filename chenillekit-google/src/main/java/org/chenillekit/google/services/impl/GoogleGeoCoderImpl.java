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

package org.chenillekit.google.services.impl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Proxy;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;
import java.util.Locale;

import org.chenillekit.google.services.GoogleGeoCoder;
import org.chenillekit.google.utils.GeoCodeLocation;
import org.chenillekit.google.utils.JSONException;
import org.chenillekit.google.utils.JSONObject;
import org.chenillekit.google.utils.geocode.GeoCodeResult;
import org.slf4j.Logger;

/**
 * This service let you ues some Google Maps services in your application.
 *
 * @version $Id: GoogleMapServiceImpl.java 388 2008-02-07 10:19:22Z homburgs $
 */
public class GoogleGeoCoderImpl extends AbstractGoogleService implements GoogleGeoCoder
{
    private final Logger logger;

    /**
     * standard constructor.
     *
     * @param logger         system logger
     */
    public GoogleGeoCoderImpl(Logger logger, String googleKey, int timeout, String referer, String proxy)
    {
        super(logger, googleKey, timeout, referer, proxy);
        this.logger = logger;
    }

    /**
     * get the geo code from google map service for address.
     *
     * @param geoCodeLocation location holder
     */
    public GeoCodeResult getGeoCode(GeoCodeLocation geoCodeLocation)
    {
        return getGeoCode(geoCodeLocation.getStreet(), geoCodeLocation.getCountry(),
                          geoCodeLocation.getState(), geoCodeLocation.getZipCode(), geoCodeLocation.getCity());
    }

    /**
     * get the geo code from google map service for address.
     *
     * @param street  the street
     * @param country the country
     * @param state   the state
     * @param zipCode the zip code
     * @param city    the city
     */
    public GeoCodeResult getGeoCode(String street, String country, String state, String zipCode, String city)
    {
        return getGeoCode(null, street, country, state, zipCode, city);
    }

    /**
     * get the geo code from google map service for address.
     *
     * @param street  the street
     * @param country the country
     * @param state   the state
     * @param zipCode the zip code
     * @param city    the city
     */
    public GeoCodeResult getGeoCode(Locale locale, String street, String country, String state, String zipCode, String city)
    {
        GeoCodeResult geoCodeResult;

        if (locale == null)
            locale = Locale.getDefault();

        try
        {
            String queryString = String.format("%shttp://maps.google.com/maps/geo?q=%s,%s,%s,%s,%s&key=%s&gl=%s&output=json",
                                               getProxy(),
                                               getEncodedString(street),
                                               getEncodedString(country),
                                               getEncodedString(state),
                                               getEncodedString(zipCode),
                                               getEncodedString(city),
                                               getKey(),
                                               locale.getLanguage());

            if (logger.isDebugEnabled())
                logger.debug(String.format("send query '%s' to google maps", queryString));


            URL url = new URL(queryString);
            List<Proxy> proxies = getProxySelector().select(url.toURI());
            URLConnection connection = url.openConnection(proxies.get(0));
            connection.setConnectTimeout(getTimeout());
            connection.setUseCaches(false);
            connection.addRequestProperty("Referer", getReferer());

            String line;
            StringBuilder builder = new StringBuilder();
            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            while ((line = reader.readLine()) != null)
                builder.append(line);

            JSONObject jsonObject = new JSONObject(builder.toString());

            if (logger.isDebugEnabled())
                logger.debug("JSON result: {}", jsonObject.toString(1));

            geoCodeResult = new GeoCodeResult(jsonObject);

            if (geoCodeResult.getStatus().getCode() != 200)
            {
                if (logger.isWarnEnabled())
                    logger.warn("GoogleMapService receives an error code '{}'", geoCodeResult.getStatus().getCode());
            }

            return geoCodeResult;
        }
        catch (IOException e)
        {
            throw new RuntimeException(e);
        }
        catch (URISyntaxException e)
        {
            throw new RuntimeException(e);
        }
        catch (JSONException e)
        {
            throw new RuntimeException(e);
        }
    }
}
