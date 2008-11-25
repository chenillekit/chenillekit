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
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

import org.apache.tapestry5.ioc.Resource;
import org.apache.tapestry5.ioc.internal.util.AbstractResource;

/**
 * A resource stored with in any location (local, remote or jar archive).
 *
 * @version $Id: URIResource.java 367 2008-02-06 09:59:36Z homburgs $
 */
public class URIResource extends AbstractResource
{
    URI _uri;

    public URIResource(String path)
    {
        super(path);
        try
        {
            _uri = new URI(path);
        }
        catch (URISyntaxException e)
        {
            throw new RuntimeException(e);
        }
    }

    public URIResource(URI uri) throws MalformedURLException
    {
        this(uri.toURL().toString());
    }

    public URIResource(File file) throws MalformedURLException
    {
        this(file.toURI());
    }

    public URIResource(URL url) throws URISyntaxException, MalformedURLException
    {
        this(url.toURI());
    }

    /**
     * Factory method provided by subclasses.
     */
    @Override
    protected Resource newResource(String path)
    {
        return new URIResource(path);
    }

    /**
     * Returns the URL for the resource, or null if it does not exist.
     */
    public URL toURL()
    {
        try
        {
            if (_uri.toURL().openConnection().getContentLength() > 0)
                return _uri.toURL();
        }
        catch (Exception e)
        {
            // do nothing
        }

        return null;
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
        return String.format("uri:%s", getPath());
    }
}
