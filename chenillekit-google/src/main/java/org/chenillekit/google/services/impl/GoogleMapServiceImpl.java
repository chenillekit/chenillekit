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
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.ConnectException;
import java.net.URLEncoder;
import java.util.List;

import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.MultiThreadedHttpConnectionManager;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.tapestry5.ioc.Resource;
import org.apache.tapestry5.ioc.internal.util.Defense;

import au.com.bytecode.opencsv.CSVReader;
import org.chenillekit.google.services.GoogleMapService;
import org.chenillekit.google.utils.GMapLocation;
import org.chenillekit.google.utils.GeoCodeResult;
import org.chenillekit.google.utils.GeoCodeResultList;
import org.chenillekit.google.utils.ProxyConfig;
import org.slf4j.Logger;

/**
 * This service let you ues some Google Maps services in your application.
 *
 * @author <a href="mailto:homburgs@googlemail.com">S.Homburg</a>
 * @version $Id: GoogleMapServiceImpl.java 388 2008-02-07 10:19:22Z homburgs $
 */
public class GoogleMapServiceImpl implements GoogleMapService
{
    private final Logger logger;
    private final Resource configResource;

    /**
     * our proxy configuration
     */
    private ProxyConfig proxyConfig;

    /**
     * your personal google map key.
     */
    private File googleKeyResource;

    /**
     * the client engine.
     */
    private HttpClient httpClient;

    /**
     * timeout for service request.
     */
    private int timeout;

    /**
     * standard constructor.
     *
     * @param logger         system logger
     * @param configResource file that holds your google map setup
     */
    public GoogleMapServiceImpl(Logger logger, Resource configResource)
    {
        Defense.notNull(configResource, "configResource");

        this.configResource = configResource;
        this.logger = logger;

        if (!this.configResource.exists())
            throw new RuntimeException(String.format("config resource '%s' not found!", this.configResource.toString()));

        initService(configResource);
    }

    /**
     * read and check all service parameters.
     */
    private void initService(Resource configResource)
    {
        try
        {
            Configuration configuration = new PropertiesConfiguration(configResource.toURL());

            String googleKeyFileName = configuration.getString("google.keyfile");
            if (googleKeyFileName.length() == 0)
                throw new RuntimeException("the property 'google.keyfile' is not set!");

            /**
             * do we need a proxy?
             */
            if (configuration.getString("google.proxy.server").length() > 0)
            {
                proxyConfig = new ProxyConfig(configuration.getString("google.proxy.server"),
                                              configuration.getInt("google.proxy.port", 3128),
                                              configuration.getString("google.proxy.user"),
                                              configuration.getString("google.proxy.password"));
            }

            googleKeyResource = new File(googleKeyFileName);
            timeout = configuration.getInt("google.timeout", 30000);

            // configure Commons-HTTP
            httpClient = createHttpClient();
        }
        catch (ConfigurationException e)
        {
            throw new RuntimeException(e);
        }
    }

    /**
     * get the access key for your map service.
     * <p/>
     * reads a simple ASCII file until finds a line that not starts with the '#' sign
     * and line is not empty.
     * <p/>
     * sample
     * <pre>
     * #
     * # this is the google map key
     * #
     * <p/>
     * ABQIAA ... your key ... KwBzBXU3z7Bk5UOzahnw
     * <p/>
     * </pre>
     *
     * @return the access key
     */
    public String getAccessKey()
    {
        BufferedReader br = null;

        if (!googleKeyResource.exists())
            throw new RuntimeException("the named google key file does not exists: '" + googleKeyResource.toString() + "'!");

        try
        {
            br = new BufferedReader(new InputStreamReader(googleKeyResource.toURL().openStream()));
            String lastReadedLine;
            while ((lastReadedLine = br.readLine()) != null)
            {
                if (!lastReadedLine.trim().startsWith("#") && lastReadedLine.trim().length() > 0)
                    break;
            }
            return lastReadedLine;
        }
        catch (IOException e)
        {
            throw new RuntimeException(e);
        }
        finally
        {
            try
            {
                if (br != null)
                    br.close();
            }
            catch (IOException e)
            {
                throw new RuntimeException(e);
            }
        }
    }

    /**
     * get the geo code from google map service for address.
     *
     * @param gMapLocation location holder
     */
    public GeoCodeResultList getGeoCode(GMapLocation gMapLocation)
    {
        GeoCodeResultList geoCodes = new GeoCodeResultList();

        getGeoCode(geoCodes, gMapLocation.getStreet(), gMapLocation.getCountry(),
                   gMapLocation.getState(), gMapLocation.getZipCode(), gMapLocation.getCity());

        return geoCodes;
    }

    /**
     * get the geo code from google map service for address.
     *
     * @param geoCodes     empty list for geo codes received from google maps
     * @param gMapLocation location holder
     */
    public void getGeoCode(GeoCodeResultList geoCodes, GMapLocation gMapLocation)
    {
        getGeoCode(geoCodes, gMapLocation.getStreet(), gMapLocation.getCountry(),
                   gMapLocation.getState(), gMapLocation.getZipCode(), gMapLocation.getCity());
    }

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
    public void getGeoCode(GeoCodeResultList geoCodes,
                           String street, String country, String state, String zipCode, String city)
    {
        Defense.notNull(geoCodes, "geoCodes");
        GetMethod getMethod = null;

        try
        {
            String queryString = String.format("q=%s,%s,%s,%s,%s&output=csv&key=%s",
                                               getEncodedString(street),
                                               getEncodedString(country),
                                               getEncodedString(state),
                                               getEncodedString(zipCode),
                                               getEncodedString(city),
                                               getAccessKey());

            if (logger.isDebugEnabled())
                logger.debug(String.format("send query '%s' to google maps", queryString));

            getMethod = new GetMethod("http://maps.google.com");
            getMethod.setPath("/maps/geo");
            getMethod.setQueryString(queryString);


            if (httpClient.executeMethod(getMethod) == 200)
            {
                List valuesList = new CSVReader(new InputStreamReader(getMethod.getResponseBodyAsStream()), ',').readAll();
                for (Object aValuesList : valuesList)
                {
                    String[] values = (String[]) aValuesList;

                    GeoCodeResult geoCodeResult = new GeoCodeResult(Integer.parseInt(values[0]),
                                                                    Integer.parseInt(values[1]),
                                                                    Float.parseFloat(values[2]),
                                                                    Float.parseFloat(values[3]));

                    geoCodes.add(geoCodeResult);
                }
            }
            else
            {
                if (logger.isWarnEnabled())
                    logger.warn("GoogleMapService receives an error: '%s' with code '%d'", getMethod.getStatusText(),
                                getMethod.getStatusCode());
            }
        }
        catch (HttpException e)
        {
            throw new RuntimeException(e);
        }
        catch (ConnectException e)
        {
            throw new RuntimeException(e);
        }
        catch (IOException e)
        {
            throw new RuntimeException(e);
        }
        finally
        {
            if (getMethod != null)
                getMethod.releaseConnection();
        }
    }

    /**
     * create the http client.
     *
     * @return the http client
     */
    private HttpClient createHttpClient()
    {
        HttpClient httpClient = new HttpClient(new MultiThreadedHttpConnectionManager());

        if (proxyConfig != null)
            httpClient.getHostConfiguration().setProxy(proxyConfig.getHost(), proxyConfig.getPort());

        httpClient.getHttpConnectionManager().getParams().setConnectionTimeout(timeout);

        return httpClient;
    }

    /**
     * encode string if not null.
     */
    private String getEncodedString(String source) throws UnsupportedEncodingException
    {
        if (source == null)
            return "";

        return URLEncoder.encode(source.replace(' ', '+'), "UTF-8");
    }
}
