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

import org.chenillekit.google.ChenilleKitGoogleTestModule;
import org.chenillekit.google.utils.GMapLocation;
import org.chenillekit.google.utils.GeoCodeResult;
import org.chenillekit.google.utils.GeoCodeResultList;
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
        GeoCodeResultList geoCodes = new GeoCodeResultList();
        geoCodes.setMinAccuracyForGoodMatch(6);
        googleMapService.getGeoCode(geoCodes, "Neue Wollkämmereistrasse 2", "DE", "", "21220", "Hamburg");

        for (GeoCodeResult geoCode : geoCodes)
        {
            if (geoCode.getHttpResult() == GeoCodeResult.G_GEO_SUCCESS)
                System.err.println(String.format("Latitude/Longitude: %s/%s", geoCode.getLatitude(),
                                                 geoCode.getLongitude()));
        }

        assertTrue(geoCodes.size() > 0);
    }

    @Test
    public void test_geocoding_holder_exist()
    {
        GeoCodeResultList geoCodes = new GeoCodeResultList();
        GMapLocation location = new GMapLocation("Neue Wollkämmereistrasse 2", "DE", "", "21220", "Hamburg");
        googleMapService.getGeoCode(geoCodes, location);

        for (GeoCodeResult geoCode : geoCodes)
        {
            if (geoCode.getHttpResult() == GeoCodeResult.G_GEO_SUCCESS)
                System.err.println(String.format("Latitude/Longitude: %s/%s", geoCode.getLatitude(),
                                                 geoCode.getLongitude()));
        }

        assertTrue(geoCodes.size() > 0);
    }

    @Test
    public void test_geocoding_notexist()
    {
        GeoCodeResultList geoCodes = new GeoCodeResultList();
        googleMapService.getGeoCode(geoCodes, "Neue Wollstrasse 2", "DE", "", "21220", "Hamburg");

        for (GeoCodeResult geoCode : geoCodes)
        {
            if (geoCode.getHttpResult() == GeoCodeResult.G_GEO_SUCCESS)
                System.err.println(String.format("Latitude/Longitude: %s/%s", geoCode.getLatitude(),
                                                 geoCode.getLongitude()));
        }

        assertTrue(geoCodes.goodMatches() == 0);
    }
}
