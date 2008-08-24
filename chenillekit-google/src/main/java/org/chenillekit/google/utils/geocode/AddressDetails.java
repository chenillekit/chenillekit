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
 *
 */

package org.chenillekit.google.utils.geocode;

import org.chenillekit.google.utils.JSONException;
import org.chenillekit.google.utils.JSONObject;

/**
 * @author <a href="mailto:homburgs@googlemail.com">shomburg</a>
 * @version $Id$
 */
public class AddressDetails
{
    private int accuracy;
    private String administrativeAreaName;
    private String dependentLocalityName;
    private String postalCodeNumber;
    private String thoroughfareName;
    private String localityName;
    private String subAdministrativeAreaName;
    private String countryNameCode;

    public AddressDetails(JSONObject json)
    {
        buildFromJSON(json);
    }

    private void buildFromJSON(JSONObject json)
    {
        try
        {
            accuracy = json.getInt("Accuracy");
            administrativeAreaName = json.getJSONObject("Country").getJSONObject("AdministrativeArea").getString("AdministrativeAreaName");
            countryNameCode = json.getJSONObject("Country").getString("CountryNameCode");

            JSONObject subAdministrativeArea = json.getJSONObject("Country").getJSONObject("AdministrativeArea").getJSONObject("SubAdministrativeArea");
            JSONObject locality = subAdministrativeArea.getJSONObject("Locality");

            if (locality.has("DependentLocality"))
            {
                JSONObject dependentLocality = locality.getJSONObject("DependentLocality");
                dependentLocalityName = dependentLocality.getString("DependentLocalityName");
                postalCodeNumber = dependentLocality.getJSONObject("PostalCode").getString("PostalCodeNumber");
                thoroughfareName = dependentLocality.getJSONObject("Thoroughfare").getString("ThoroughfareName");
            }
            
            localityName = locality.getString("LocalityName");
            subAdministrativeAreaName = subAdministrativeArea.getString("SubAdministrativeAreaName");
        }
        catch (JSONException e)
        {
            throw new RuntimeException(e);
        }
    }

    public int getAccuracy()
    {
        return accuracy;
    }

    public String getAdministrativeAreaName()
    {
        return administrativeAreaName;
    }

    public String getDependentLocalityName()
    {
        return dependentLocalityName;
    }

    public String getPostalCodeNumber()
    {
        return postalCodeNumber;
    }

    public String getThoroughfareName()
    {
        return thoroughfareName;
    }

    public String getLocalityName()
    {
        return localityName;
    }

    public String getSubAdministrativeAreaName()
    {
        return subAdministrativeAreaName;
    }

    public String getCountryNameCode()
    {
        return countryNameCode;
    }
}
