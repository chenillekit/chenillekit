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

package org.chenillekit.demo.utils;

/**
 * @version $Id$
 */
public class GMapAddress
{
	private String _name;
	private String _state;
	private String _country;
	private String _zipCode;
	private String _city;
	private String _street;

	public GMapAddress(String name, String state, String country, String zipCode, String city)
	{
		this(name, state, country, zipCode, city, null);
	}

	public GMapAddress(String name, String state, String country, String zipCode, String city, String street)
	{
		_name = name;
		_state = state;
		_country = country;
		_city = city;
		_zipCode = zipCode;
		_street = street;
	}

	public String getName()
	{
		return _name;
	}

	public String getState()
	{
		return _state;
	}

	public String getCountry()
	{
		return _country;
	}

	public String getCity()
	{
		return _city;
	}

	public String getZipCode()
	{
		return _zipCode;
	}

	public String getStreet()
	{
		return _street;
	}
}
