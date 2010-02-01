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

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import org.apache.tapestry5.internal.services.RequestPathOptimizer;
import org.apache.tapestry5.ioc.internal.util.CollectionFactory;
import org.apache.tapestry5.services.Request;

import org.chenillekit.tapestry.core.ChenilleKitCoreConstants;
import org.chenillekit.tapestry.core.services.URIAssetAliasManager;

/**
 * @version $Id$
 */
public class URIAssetAliasManagerImpl implements URIAssetAliasManager
{
    private final Request request;

    private final RequestPathOptimizer optimizer;

    /**
     * Map from alias to path.
     */
    private final Map<String, String> aliasToPathPrefix = CollectionFactory.newMap();

    /**
     * Map from path to alias.
     */
    private final Map<String, String> pathPrefixToAlias = CollectionFactory.newMap();

    private final List<String> sortedAliases;

    private final List<String> sortedPathPrefixes;

    /**
     * Configuration is a map of aliases (short names) to complete names. Keys and values should end with a slash, but
     * one will be provided as necessary, so don't both.
     */
    @SuppressWarnings({"JavaDoc"})
    public URIAssetAliasManagerImpl(Request request, RequestPathOptimizer optimizer, Map<String, String> configuration)
    {
        this.request = request;
        this.optimizer = optimizer;

        for (Map.Entry<String, String> e : configuration.entrySet())
        {
            String alias = withSlash(e.getKey());
            String path = withSlash(e.getValue());

            aliasToPathPrefix.put(alias, path);
            pathPrefixToAlias.put(path, alias);
        }

        Comparator<String> sortDescendingByLength = new Comparator<String>()
        {
            public int compare(String o1, String o2)
            {
                return o2.length() - o1.length();
            }
        };

        sortedAliases = CollectionFactory.newList(aliasToPathPrefix.keySet());
        Collections.sort(sortedAliases, sortDescendingByLength);

        sortedPathPrefixes = CollectionFactory.newList(aliasToPathPrefix.values());
        Collections.sort(sortedPathPrefixes, sortDescendingByLength);
    }

    private String withSlash(String input)
    {
        if (input.endsWith("/")) return input;

        return input + "/";
    }

    public String toClientURL(String resourcePath)
    {
        String path = toCompleteClientURI(resourcePath);

        return optimizer.optimizePath(path);
    }

    private String toCompleteClientURI(String resourcePath)
    {
        StringBuilder builder = new StringBuilder(request.getContextPath());
        builder.append(ChenilleKitCoreConstants.URI_PATH_PREFIX);

        for (String pathPrefix : sortedPathPrefixes)
        {
            if (resourcePath.startsWith(pathPrefix))
            {
                String alias = pathPrefixToAlias.get(pathPrefix);
                builder.append(alias);
                builder.append(resourcePath.substring(pathPrefix.length()));

                return builder.toString();
            }
        }

        // No alias available as a prefix (kind of unlikely, but whatever).

        builder.append(resourcePath);

        return builder.toString();
    }

    public String toResourcePath(String clientURL)
    {
        String basePath = clientURL.substring(ChenilleKitCoreConstants.URI_PATH_PREFIX.length());

        for (String alias : sortedAliases)
        {
            if (basePath.startsWith(alias))
                return aliasToPathPrefix.get(alias) + basePath.substring(alias.length());
        }

        return basePath;
    }
}
