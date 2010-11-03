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

import java.util.List;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.Query;
import org.apache.lucene.util.Version;

/**
 * implements seacher based on <a href="http://lucene.apache.org/java/docs/index.html">lucene</a>.
 *
 * @version $Id$
 */
public interface SearcherService
{
	/**
	 * Build a {@link Query} from the provider <code>queryString</code>
	 * using the configured Lucene {@link Version} and the configured
	 * {@link Analyzer}. Sane defaults are provided.
	 * 
	 * @param fieldName the field where to apply the {@link Query}
	 * @param queryString the query {@link String}, see {@link QueryParser}
	 * @param howMany how many elements the result list could contain
	 * @return the {@link List} of the {@link Document} which satisfy the search terms
	 */
    List<Document> search(String fieldName, String queryString, Integer howMany);
    
    /**
     * Search the <em>index</em> using the externally built query object.
     * Please be sure to use the same {@link Analyzer} used to index objects otherwise
     * strange things could happen, you'll see ghosts flying around and pancake
     * exploding in the owen.
     * 
     * @param fieldName the field where to apply the {@link Query}
     * @param query the {@link Query} evaluated by the index
     * @param howMany how many elements the result list could contain
     * @return the {@link List} of the {@link Document} which satisfy the search terms
     */
    List<Document> search(String fieldName, Query query, Integer howMany);
}