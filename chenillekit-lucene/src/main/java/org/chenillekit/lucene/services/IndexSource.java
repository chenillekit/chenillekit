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

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.search.IndexSearcher;

/**
 * Service providing direct Lucene facility for operating with the index
 *
 * @version $Id$
 */
public interface IndexSource
{
	/**
	 * @return the choosen {@link Analyzer} used through the service
	 */
	public Analyzer getAnalyzer();
	
	/**
	 * @return a new freshly created {@link IndexSearcher} instance
	 */
	public IndexSearcher createIndexSearcher();
	
	/**
	 * @return an {@link IndexWriter} shared instance.
	 */
	public IndexWriter getIndexWriter();
	

}
