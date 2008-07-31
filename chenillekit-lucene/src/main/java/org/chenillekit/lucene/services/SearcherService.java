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

package org.chenillekit.lucene.services;

/**
 * implements seacher based on <a href="http://lucene.apache.org/java/docs/index.html">lucene</a>.
 *
 * @author <a href="mailto:homburgs@gmail.com">S.Homburg</a>
 * @version $Id$
 */
public interface SearcherService<T>
{
    public final String CONFIG_KEY_PROPERTIES = "lucene.properties";
    public final String PROPERTIES_KEY_IF = "search.index.folder";
    public final String PROPERTIES_KEY_ACN = "search.analyzer.class.name";

    <T> T search(String fieldName, String queryString);
}