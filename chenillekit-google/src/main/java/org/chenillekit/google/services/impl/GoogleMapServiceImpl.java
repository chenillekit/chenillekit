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
import java.io.UnsupportedEncodingException;
import java.net.ConnectException;
import java.net.URLEncoder;
import java.util.List;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.MultiThreadedHttpConnectionManager;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.tapestry5.ioc.Resource;
import org.apache.tapestry5.ioc.internal.util.CollectionFactory;
import org.apache.tapestry5.ioc.internal.util.Defense;

import au.com.bytecode.opencsv.CSVReader;
import org.chenillekit.google.services.GoogleMapService;
import org.chenillekit.google.utils.GoogleGeoCode;
import org.chenillekit.google.utils.LatLng;
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
    private Logger _sysLogger;

    /**
     * our proxy configuration
     */
    private ProxyConfig _proxyConfig;

    /**
     * your personal google map key.
     */
    private Resource _googleMapKey;

    /**
     * the client engine.
     */
    private HttpClient _httpClient;

    /**
     * timeout for service request.
     */
    private int _timeout;

    /**
     * standard constructor.
     *
     * @param syslog       system logger
     * @param googleMapKey file that holds your google map key
     * @param proxyConfig  the proxy configuration (may be null)
     * @param timeout      timeout for service request (default 30000 ms.)
     */
    public GoogleMapServiceImpl(Logger syslog, Resource googleMapKey, ProxyConfig proxyConfig, int timeout)
    {
        _sysLogger = syslog;

        Defense.notNull(googleMapKey, "googleMapKey");
        Defense.notNull(timeout, "timeout");

        _googleMapKey = googleMapKey;
        _proxyConfig = proxyConfig;
        _timeout = timeout > 0 ? timeout : 30000;

        // configure Commons-HTTP
        _httpClient = createHttpClient();
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

        try
        {
            br = new BufferedReader(new InputStreamReader(_googleMapKey.toURL().openStream()));
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
     * @param street  the street
     * @param country the country
     * @param zipCode the zip code
     * @param city    the city
     *
     * @return xml stream
     */
    public List<GoogleGeoCode> getGeoCode(String street, String country, String state, String zipCode, String city)
    {
//        Defense.notNull(street, "street");
//        Defense.notNull(country, "country");
//        Defense.notNull(state, "state");
//        Defense.notNull(zipCode, "zipCode");
//        Defense.notNull(city, "city");

        GetMethod getMethod = null;
        List<GoogleGeoCode> goggleMapResponse = CollectionFactory.newList();

        try
        {
            String queryString = String.format("q=%s,%s,%s,%s,%s&output=csv&key=%s",
                                               getEncodedString(street),
                                               getEncodedString(country),
                                               getEncodedString(state),
                                               getEncodedString(zipCode),
                                               getEncodedString(city),
                                               getAccessKey());

            if (_sysLogger.isDebugEnabled())
                _sysLogger.debug(String.format("send query '%s' to google maps", queryString));

            getMethod = new GetMethod("http://maps.google.com");
            getMethod.setPath("/maps/geo");
            getMethod.setQueryString(queryString);


            int iGetResultCode = _httpClient.executeMethod(getMethod);

            if (iGetResultCode == 200)
            {
                List valuesList = new CSVReader(new InputStreamReader(getMethod.getResponseBodyAsStream()), ',').readAll();
                for (Object aValuesList : valuesList)
                {
                    String[] values = (String[]) aValuesList;

                    if (values[0].equals("602"))
                    {
                        if (_sysLogger.isWarnEnabled())
                            _sysLogger.warn(String.format("GoogleMapService receives error code '%s'", values[0]));
                        break;
                    }

                    int resultCode = Integer.parseInt(values[0]);
                    int accuracy = Integer.parseInt(values[1]);
                    String latitude = values[2];
                    String longitude = values[3];

                    goggleMapResponse.add(new GoogleGeoCode(resultCode, accuracy,
                                                            resultCode != 200 ? null : latitude,
                                                            resultCode != 200 ? null : longitude));
                }
            }
            else
            {
                if (_sysLogger.isWarnEnabled())
                    _sysLogger.warn("GoogleMapService receives an error: '%s' with code '%d'", getMethod.getStatusText(),
                                    getMethod.getStatusCode());
            }

            return goggleMapResponse;
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
     * get the geo code from google map service for address.
     *
     * @param goggleMapResponse the response from google map service as list
     * @param position          get the list element at position <em>position</em>
     *
     * @return the latitude an longitude
     */
    public LatLng getLatLag(List<GoogleGeoCode> goggleMapResponse, int position)
    {
        if (position > goggleMapResponse.size())
            return null;

        if (goggleMapResponse.get(position).getHttpStatusCode() != 200)
            return null;

        return goggleMapResponse.get(position).getLatLng();
    }

    /**
     * create the http client.
     *
     * @return the http client
     */
    private HttpClient createHttpClient()
    {
        HttpClient httpClient = new HttpClient(new MultiThreadedHttpConnectionManager());

        if (_proxyConfig != null)
            httpClient.getHostConfiguration().setProxy(_proxyConfig.getHost(), _proxyConfig.getPort());

        httpClient.getHttpConnectionManager().getParams().setConnectionTimeout(_timeout);

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
