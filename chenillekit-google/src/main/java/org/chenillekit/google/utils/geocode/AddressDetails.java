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
	private Integer accuracy;
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
			accuracy = (Integer) getValue(json, "Accuracy");
			administrativeAreaName = (String) getValue(json, "Country", "AdministrativeArea", "AdministrativeAreaName");
			countryNameCode = (String) getValue(json, "Country", "CountryNameCode");

			dependentLocalityName = (String) getValue(json, "Country", "AdministrativeArea", "SubAdministrativeArea", "Locality",
													  "DependentLocality", "DependentLocalityName");
			postalCodeNumber = (String) getValue(json, "Country", "AdministrativeArea", "SubAdministrativeArea", "Locality",
												 "DependentLocality", "PostalCode", "PostalCodeNumber");
			thoroughfareName = (String) getValue(json, "Country", "AdministrativeArea", "SubAdministrativeArea", "Locality",
												 "DependentLocality", "Thoroughfare", "ThoroughfareName");

			localityName = (String) getValue(json, "Country", "AdministrativeArea", "SubAdministrativeArea", "Locality",
											 "LocalityName");
			subAdministrativeAreaName = (String) getValue(json, "Country", "AdministrativeArea", "SubAdministrativeArea",
														  "SubAdministrativeAreaName");
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

	public Object getValue(JSONObject jsonObject, String... keys)
	{
		JSONObject tempObject = jsonObject;
		Object value = null;

		for (int i = 0; i < keys.length; i++)
		{
			String key = keys[i];
			if (!tempObject.has(key))
				break;

			if ((i + 1) == keys.length)
				value = tempObject.get(key);
			else
				tempObject = tempObject.getJSONObject(key);

		}

		return value;
	}
}
