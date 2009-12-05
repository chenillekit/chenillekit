/*
 * Apache License
 * Version 2.0, January 2004
 * http://www.apache.org/licenses/
 *
 * Copyright 1996-2008 by Sven Homburg
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 */

package org.chenillekit.core.resources;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Locale;

import org.apache.tapestry5.ioc.Resource;
import org.apache.tapestry5.ioc.internal.util.LocalizedNameGenerator;

/**
 * A resource stored with in any location (local, remote or jar archive).
 *
 * @version $Id$
 */
public class URIResource implements Resource
{
	private URI uri;

	public URIResource(String uri)
	{
		try
		{
			this.uri = new URI(uri);
		}
		catch (URISyntaxException e)
		{
			throw new RuntimeException(e);
		}
	}

	public URIResource(URI uri) throws MalformedURLException
	{
		this.uri = uri;
	}

	public URIResource(File file) throws MalformedURLException
	{
		this(file.toURI());
	}

	public URIResource(URL url) throws URISyntaxException, MalformedURLException
	{
		this(url.toURI());
	}

	protected Resource newResource(String uri)
	{
		return new URIResource(uri);
	}

	/**
	 * Returns true if the resource exists; if a stream to the content of the file may be openned.
	 *
	 * @return true if the resource exists, false if it does not
	 */
	public boolean exists()
	{
		InputStream is = null;

		try
		{
			is = uri.toURL().openStream();
			int i = is.read();
			return true;
		}
		catch (Exception e)
		{
			return false;
		}
		finally
		{
			try
			{
				if (is != null)
					is.close();
			}
			catch (Exception e)
			{
				// do nothing
			}
		}
	}

	/**
	 * Opens a stream to the content of the resource, or returns null if the resource does not exist.
	 *
	 * @return an open, buffered stream to the content, if available
	 */
	public InputStream openStream() throws IOException
	{
		return uri.toURL().openStream();
	}

	/**
	 * Returns the URL for the resource, or null if it does not exist.
	 */
	public URL toURL()
	{
		try
		{
			if (exists())
				return uri.toURL();
		}
		catch (Exception e)
		{
			// do nothing
		}

		return null;
	}

	/**
	 * Returns a localized version of the resource. May return null if no such resource exists.
	 */
	public Resource forLocale(Locale locale)
	{
		for (String path : new LocalizedNameGenerator(this.uri.toString(), locale))
		{
			Resource potential = createResource(path);

			if (potential.exists()) return potential;
		}

		return null;
	}

	/**
	 * Creates a new resource, unless the path matches the current Resource's path (in which case, this resource is
	 * returned).
	 */
	private Resource createResource(String path)
	{
		if (this.uri.toString().equals(path)) return this;

		return newResource(path);
	}

	/**
	 * Returns a Resource based on a relative path, relative to the folder containing the resource. Understands the "."
	 * (current folder) and ".." (parent folder) conventions, and treats multiple sequential slashes as a single slash.
	 */
	public Resource forFile(String relativePath)
	{
		return createResource(relativePath);
	}

	/**
	 * Returns a new Resource with the extension changed (or, if the resource does not have an extension, the extension
	 * is added). The new Resource may not exist (that is, {@link #toURL()} may return null.
	 *
	 * @param extension to apply to the resource, such as "html" or "properties"
	 *
	 * @return the new resource
	 */
	public Resource withExtension(String extension)
	{
		throw new RuntimeException("not implemented yet!");
	}

	/**
	 * Returns the portion of the path up to the last forward slash; this is the directory or folder portion of the
	 * Resource.
	 */
	public String getFolder()
	{
		String folderName = "";
		String completePath = toURL().getPath();

		if (completePath != null)
		{
			int lastSlash = completePath.lastIndexOf('/');
			if (lastSlash > 0)
				folderName = completePath.substring(0, lastSlash);
		}

		return folderName;
	}

	/**
	 * Returns the file portion of the Resource path, everything that follows the final forward slash.
	 */
	public String getFile()
	{
		String fileName = "";
		String completePath = toURL().getPath();

		if (completePath != null)
		{
			if (completePath.lastIndexOf('/') > 0)
				fileName = completePath.substring(completePath.lastIndexOf('/') + 1);
			else
				fileName = completePath;
		}

		return fileName;
	}

	/**
	 * Return the path (the combination of folder and file).
	 */
	public String getPath()
	{
		return toURL().toExternalForm();
	}

	@Override
	public boolean equals(Object obj)
	{
		if (obj == null) return false;

		if (obj == this) return true;

		if (obj.getClass() != getClass()) return false;

		URIResource other = (URIResource) obj;

		return other.getPath().equals(getPath());
	}

	@Override
	public int hashCode()
	{
		return 227 ^ getPath().hashCode();
	}

	@Override
	public String toString()
	{
		return String.format("uri:%s", uri.toString());
	}
}
