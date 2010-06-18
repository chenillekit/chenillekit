/*
 * Apache License
 * Version 2.0, January 2004
 * http://www.apache.org/licenses/
 *
 * Copyright 2008-2010 by chenillekit.org
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 */

package org.chenillekit.google.services;

import java.util.Locale;

import org.chenillekit.google.ChenilleKitGoogleTestModule;
import org.chenillekit.google.utils.GeoCodeLocation;
import org.chenillekit.google.utils.geocode.GeoCodeResult;
import org.chenillekit.test.AbstractTestSuite;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

/**
 * @version $Id$
 */
public class TestGeoCoding extends AbstractTestSuite
{
    private GoogleGeoCoder googleGeoCoder;

    @DataProvider(name = "location_exists")
    public Object[][] location_exists()
    {
        return new Object[][]{
                {new GeoCodeLocation("Ohlendorfer Stieg", "DE", "", "21220", "Seevetal")},
        };
    }

    @DataProvider(name = "location_not_exists")
    public Object[][] location_not_exists()
    {
        return new Object[][]{
                {new GeoCodeLocation("Ohxxxxxrfer Stieg", "DE", "", "21220", "Seevetal")},
        };
    }

    @DataProvider(name = "country_city")
    public Object[][] country_city()
    {
        return new Object[][]{
                {new GeoCodeLocation("", "DE", "", "", "Hamburg")},
        };
    }

    @DataProvider(name = "city")
    public Object[][] city()
    {
        return new Object[][]{
                {new GeoCodeLocation(Locale.ENGLISH, "", "", "", "", "Hanover")},
        };
    }

    @DataProvider(name = "localeIsNull")
    public Object[][] localeIsNull()
    {
        return new Object[][]{
                {new GeoCodeLocation(null, "Ohlendorfer Stieg", "DE", "", "21220", "Seevetal")},
        };
    }

    @BeforeClass
    public final void setup_registry()
    {
        super.setup_registry(ChenilleKitGoogleTestModule.class);
        googleGeoCoder = registry.getService(GoogleGeoCoder.class);
    }

    @Test(dataProvider = "location_exists")
    public void test_location_exist(GeoCodeLocation geo1)
    {
        GeoCodeResult result = googleGeoCoder.getGeoCode(geo1);
        assertTrue(result.getStatus().getCode() == GeoCodeResult.G_GEO_SUCCESS);
    }

    @Test(dataProvider = "country_city")
    public void test_city_exist(GeoCodeLocation geo1)
    {
        GeoCodeResult result = googleGeoCoder.getGeoCode(geo1);
        assertTrue(result.getStatus().getCode() == GeoCodeResult.G_GEO_SUCCESS &&
                result.getPlacemarks().get(0).getAddressDetails().getAccuracy() == 4);
    }

    @Test(dataProvider = "city")
    public void test_2city_exist(GeoCodeLocation geo1)
    {
        GeoCodeResult result = googleGeoCoder.getGeoCode(geo1);
        assertTrue(result.getStatus().getCode() == GeoCodeResult.G_GEO_SUCCESS &&
                result.getPlacemarks().get(0).getAddressDetails().getAccuracy() == 4);
    }

    @Test(dataProvider = "location_not_exists")
    public void test_location_notexist(GeoCodeLocation geo1)
    {
        GeoCodeResult result = googleGeoCoder.getGeoCode(geo1);
        assertTrue(result.getPlacemarks().get(0).getAddressDetails().getAccuracy() == 5);
    }

    @Test(dataProvider = "location_exists")
    public void test_latlng(GeoCodeLocation geo1)
    {
        GeoCodeResult result = googleGeoCoder.getGeoCode(geo1);
        assertEquals(result.getPlacemarks().get(0).getLatLng().getLatitude(), 53.368533);
        assertEquals(result.getPlacemarks().get(0).getLatLng().getLongitude(), 10.030249);
    }

    @Test(dataProvider = "localeIsNull")
    public void test_localeIsNull(GeoCodeLocation geo1)
    {
        GeoCodeResult result = googleGeoCoder.getGeoCode(geo1);
        assertEquals(result.getPlacemarks().get(0).getLatLng().getLatitude(), 53.368533);
        assertEquals(result.getPlacemarks().get(0).getLatLng().getLongitude(), 10.030249);
    }

}
