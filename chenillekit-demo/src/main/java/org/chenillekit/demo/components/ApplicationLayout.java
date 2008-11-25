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

package org.chenillekit.demo.components;

import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.jar.Manifest;

import org.apache.tapestry5.annotations.Component;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.ioc.internal.util.InternalUtils;

/**
 * @version $Id$
 */
public class ApplicationLayout
{
	@Component(parameters = {"menuName=base"})
	private HorizontalMenu horizontalMenu;

	@Property(write = false)
	private String demoVersion;

	/**
	 * Tapestry page lifecycle method.
	 * Called when the page is instantiated and added to the page pool.
	 * Initialize components, and resources that are not request specific.
	 */
	private void pageLoaded()
	{
		demoVersion = getVersion();
	}

	private String getVersion()
	{
		InputStream in = null;
		URL url = null;

		try
		{
			url = this.getClass().getClassLoader().getResource("META-INF/MANIFEST.MF");
			in = url.openStream();

			Manifest mf = new Manifest(in);

			in.close();

			in = null;

			return mf.getMainAttributes().getValue("Demo-Version");
		}
		catch (RuntimeException ex)
		{
			throw new RuntimeException(String.format("Exception loading version string from manifest %s: %s",
													 url.toString(),
													 InternalUtils.toMessage(ex)), ex);
		}
		catch (IOException ex)
		{
			throw new RuntimeException(String.format("Exception loading version string from manifest %s: %s",
													 url.toString(),
													 InternalUtils.toMessage(ex)), ex);
		}
		finally
		{
			close(in);
		}
	}

	/**
	 * Closes an input stream (or other Closeable), ignoring any exception.
	 *
	 * @param closeable the thing to close, or null to close nothing
	 */
	private static void close(Closeable closeable)
	{
		if (closeable != null)
		{
			try
			{
				closeable.close();
			}
			catch (IOException ex)
			{
				// Ignore.
			}
		}
	}

}

