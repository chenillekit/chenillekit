/*
 * Apache License
 * Version 2.0, January 2004
 * http://www.apache.org/licenses/
 *
 * Copyright 2009 by chenillekit.org
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 */

package org.chenillekit.tapestry.core.utils;

import java.io.IOException;
import java.io.InputStream;

import org.apache.tapestry5.StreamResponse;
import org.apache.tapestry5.services.Response;

/**
 * stolen from Tapestry wiki page.
 *
 * @version $Id$
 */
public class InlineStreamResponse implements StreamResponse
{
	private InputStream is = null;
	protected String contentType = "text/plain";// this is the default
	protected String extension = "txt";
	protected String filename = "default";

	public InlineStreamResponse(InputStream is, String... args)
	{
		this.is = is;
		if (args != null)
		{
			this.filename = args[0];
		}
	}

	public String getContentType()
	{
		return contentType;
	}

	public InputStream getStream() throws IOException
	{
		return is;
	}

	public void prepareResponse(Response arg0)
	{
		arg0.setHeader("Content-Disposition", "inline; filename=" + filename
				+ ((extension == null) ? "" : ("." + extension)));
	}
}
