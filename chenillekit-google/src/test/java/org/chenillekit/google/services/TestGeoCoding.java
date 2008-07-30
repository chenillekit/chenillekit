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

package org.chenillekit.google.services;

import java.util.List;

import org.apache.tapestry5.ioc.internal.util.CollectionFactory;

import org.chenillekit.google.ChenilleKitGoogleTestModule;
import org.chenillekit.google.utils.GoogleGeoCode;
import org.chenillekit.test.AbstractTestSuite;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Test;

/**
 * @author <a href="mailto:shomburg@hsofttec.com">S.Homburg</a>
 * @version $Id$
 */
public class TestGeoCoding extends AbstractTestSuite
{
    private GoogleMapService googleMapService;

    @BeforeSuite
    public final void setup_registry()
    {
        super.setup_registry(ChenilleKitGoogleTestModule.class);
        googleMapService = registry.getService(GoogleMapService.class);
    }

    @Test
    public void test_geocoding_exist()
    {
        List<GoogleGeoCode> geoCodes = CollectionFactory.newList();
        int errorCode = googleMapService.getGeoCode(geoCodes, "Neue Wollkämmereistrasse 2", "DE", "", "21220", "Hamburg");

        for (GoogleGeoCode geoCode : geoCodes)
        {
            System.err.println(String.format("Latitude/Longitude: %s/%s", geoCode.getLatLng().getLatitude(),
                                             geoCode.getLatLng().getLongitude()));
        }

        assertTrue(geoCodes.size() > 0 && errorCode == 200);
    }

    @Test
    public void test_geocoding_notexist()
    {
        List<GoogleGeoCode> geoCodes = CollectionFactory.newList();
        int errorCode = googleMapService.getGeoCode(geoCodes, "Neue Wollstrasse 2", "DE", "", "21220", "Hamburg");

        for (GoogleGeoCode geoCode : geoCodes)
        {
            System.err.println(String.format("Latitude/Longitude: %s/%s", geoCode.getLatLng().getLatitude(),
                                             geoCode.getLatLng().getLongitude()));
        }

        assertTrue(errorCode == 602);
    }
}
