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

package org.chenillekit.tapestry.core.services;

/**
 * @version $Id: URIAssetAliasManager.java 669 2008-05-09 00:05:47Z homburgs $
 */
public interface URIAssetAliasManager
{
    /**
     * Takes a resource path to a URI resource and adds the uri path prefix to the path. May also convert part
     * of the path to an alias (based on the manager's configuration).
     *
     * @param resourcePath resource path (with no leading slash)
     * @return URL ready to send to the client
     */
    String toClientURL(String resourcePath);

    /**
     * Reverses {@link #toClientURL(String)}, stripping off the uri prefix, and re-expanding any aliased folders back
     * to complete folders.
     */
    String toResourcePath(String clientURL);
}
