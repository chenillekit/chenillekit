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

import java.io.IOException;
import java.io.InputStream;

import org.apache.tapestry5.StreamResponse;
import org.apache.tapestry5.services.Response;

/**
 * @version $Id$
 */
public class ScriptStreamResponse implements StreamResponse
{
    private final String contentType;
    private final InputStream scriptStream;

    public ScriptStreamResponse(InputStream scriptStream)
    {
        assert scriptStream != null;

        this.contentType = "text/plain";
        this.scriptStream = scriptStream;
    }

    public String getContentType()
    {
        return contentType;
    }

    public InputStream getStream() throws IOException
    {
        return scriptStream;
    }

    public void prepareResponse(Response response)
    {
        // No-op by default.
    }


}
