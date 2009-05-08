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

package org.chenillekit.tapestry.core.services.impl;

import java.io.IOException;
import javax.servlet.http.HttpServletResponse;

import org.apache.tapestry5.internal.services.ResourceStreamer;
import org.apache.tapestry5.ioc.Resource;
import org.apache.tapestry5.services.Dispatcher;
import org.apache.tapestry5.services.Request;
import org.apache.tapestry5.services.Response;

import org.chenillekit.core.resources.URIResource;
import org.chenillekit.tapestry.core.ChenilleKitCoreConstants;
import org.chenillekit.tapestry.core.services.URIAssetAliasManager;

/**
 * @version $Id: URIDispatcher.java 682 2008-05-20 22:00:02Z homburgs $
 */
public class URIDispatcher implements Dispatcher
{
    private final ResourceStreamer streamer;

    private final URIAssetAliasManager aliasManager;

    public URIDispatcher(ResourceStreamer streamer, URIAssetAliasManager aliasManager)
    {
        this.streamer = streamer;
        this.aliasManager = aliasManager;
    }

    /**
     * Analyzes the incoming request and performs an appropriate operation for each.
     *
     * @return true if a response was delivered, false if the servlet container should be allowed to handle the request
     */
    public boolean dispatch(Request request, Response response) throws IOException
    {
        String path = request.getPath();

        // Remember that the request path does not include the context path, so we can simply start
        // looking for the asset path prefix right off the bat.
        if (!path.startsWith(ChenilleKitCoreConstants.URI_PATH_PREFIX)) return false;

        String resourcePath = aliasManager.toResourcePath(path);

        Resource resource = new URIResource(resourcePath);

        if (!resource.exists())
        {
            response.sendError(HttpServletResponse.SC_NOT_FOUND, String.format("asset '%s' doesn exists!", resource));
            return true;
        }

        streamer.streamResource(resource);

        return true;
    }
}
