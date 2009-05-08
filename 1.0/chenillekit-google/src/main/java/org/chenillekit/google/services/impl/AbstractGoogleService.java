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

import java.io.UnsupportedEncodingException;
import java.net.ProxySelector;
import java.net.URLEncoder;

import org.apache.tapestry5.ioc.internal.util.Defense;

import org.slf4j.Logger;
import sun.net.spi.DefaultProxySelector;

/**
 * @version $Id$
 */
abstract public class AbstractGoogleService
{
	private final Logger logger;
	private final String referer;
	private final String proxy;
	private final int timeout;
	private final String googleKey;

	private ProxySelector proxySelector;

	/**
	 * standard constructor.
	 *
	 * @param logger system logger
	 */
	protected AbstractGoogleService(Logger logger, String googleKey, int timeout, String referer, String proxy)
	{
		Defense.notNull(googleKey, "googleKey");

		this.googleKey = googleKey;
		this.timeout = timeout;
		this.referer = referer;
		this.proxy = proxy;
		this.logger = logger;

		initService();
	}

	/**
	 * read and check all service parameters.
	 */
	private void initService()
	{
		proxySelector = new DefaultProxySelector();
	}

	/**
	 * encode string if not null.
	 */
	protected String getEncodedString(String source) throws UnsupportedEncodingException
	{
		if (source == null)
			return "";

		return URLEncoder.encode(source.replace(' ', '+'), "UTF-8");
	}

	/**
	 * the the default proxy selector.
	 */
	public ProxySelector getProxySelector()
	{
		return proxySelector;
	}

	/**
	 * get the proxy.
	 */
	public String getProxy()
	{
		return proxy;
	}

	/**
	 * the configured referer.
	 */
	public String getReferer()
	{
		return referer;
	}

	/**
	 * timeout for service request.
	 */
	public int getTimeout()
	{
		return timeout;
	}

	/**
	 * the google access key.
	 */
	public String getKey()
	{
		return googleKey;
	}
}
