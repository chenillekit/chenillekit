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

package org.chenillekit.google;

import org.apache.tapestry5.ioc.MappedConfiguration;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.ioc.annotations.Symbol;

import org.chenillekit.google.services.GoogleGeoCoder;
import org.chenillekit.google.services.impl.GoogleGeoCoderImpl;
import org.slf4j.Logger;

/**
 * @version $Id$
 */
public class ChenilleKitGoogleModule
{
	/**
	 * Contributes factory defaults that may be overridden.
	 */
	public static void contributeFactoryDefaults(MappedConfiguration<String, String> contribution)
	{
		contribution.add(ChenilleKitGoogleConstants.GOOGLE_KEY, "");
		contribution.add(ChenilleKitGoogleConstants.GOOGLE_PROXY, "");
		contribution.add(ChenilleKitGoogleConstants.GOOGLE_REFERER, "");
		contribution.add(ChenilleKitGoogleConstants.GOOGLE_TIMEOUT, "30000");
	}

	public static GoogleGeoCoder buildGoogleGeoCoder(Logger logger,

													 @Inject @Symbol(ChenilleKitGoogleConstants.GOOGLE_KEY)
													 final String googleKey,

													 @Inject @Symbol(ChenilleKitGoogleConstants.GOOGLE_TIMEOUT)
													 final int timeout,

													 @Inject @Symbol(ChenilleKitGoogleConstants.GOOGLE_PROXY)
													 final String proxy,

													 @Inject @Symbol(ChenilleKitGoogleConstants.GOOGLE_REFERER)
													 final String referer)
	{
		return new GoogleGeoCoderImpl(logger, googleKey, timeout, referer, proxy);
	}
}
