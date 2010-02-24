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

package org.chenillekit.mail;

import java.io.IOException;
import java.util.Properties;

import org.apache.tapestry5.ioc.MappedConfiguration;
import org.apache.tapestry5.ioc.annotations.SubModule;

/**
 * @version $Id$
 */
@SubModule(value = { ChenilleKitMailModule.class })
public class ChenilleKitMailTestModule
{
	public static void contributeApplicationDefaults(MappedConfiguration<String, String> contributions)
	{
		Properties prop = new Properties();
		
		try {
			prop.load(ChenilleKitMailTestModule.class.getResource("/smtp.properties").openStream());
		} catch (IOException ioe)
		{
			throw new RuntimeException("Unable to load smtp.properties", ioe);
		}
		
		for (Object key : prop.keySet())
		{
			String value = prop.getProperty(key.toString());
			
			contributions.add(key.toString(), value);
		}
	}
}
