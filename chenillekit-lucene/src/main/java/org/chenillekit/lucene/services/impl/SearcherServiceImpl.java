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

package org.chenillekit.lucene.services.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.Searcher;
import org.apache.lucene.search.TopScoreDocCollector;
import org.apache.lucene.util.Version;
import org.apache.tapestry5.ioc.services.ThreadCleanupListener;
import org.chenillekit.lucene.ChenilleKitLuceneRuntimeException;
import org.chenillekit.lucene.services.IndexSource;
import org.chenillekit.lucene.services.SearcherService;
import org.slf4j.Logger;

/**
 * implements seacher based on <a href="http://lucene.apache.org/java/docs/index.html">lucene</a>.
 *
 * @version $Id$
 */
public class SearcherServiceImpl implements SearcherService, ThreadCleanupListener
{
	private static final int MAX_SCORE_DOC = 100;

    private Logger logger;

    private final Searcher indexSearcher;

    private final Analyzer analyzer;
    
    private final Version version;

    /**
     * 
     * @param logger
     * @param indexSource
     * @param version
     */
    public SearcherServiceImpl(Logger logger, IndexSource indexSource, Version version)
    {
    	this.logger = logger;

    	this.analyzer = indexSource.getAnalyzer();
    	
    	this.version = version;

    	this.indexSearcher = indexSource.createIndexSearcher();
    }
    
    /*
     * (non-Javadoc)
     * @see org.chenillekit.lucene.services.SearcherService#search(java.lang.String, java.lang.String, java.lang.Integer)
     */
    public List<Document> search(String fieldName, String queryString, Integer howMany)
    {
    	QueryParser parser = new QueryParser(this.version, fieldName, this.analyzer);

    	Query query;
		try
		{
			query = parser.parse(queryString);

		}
		catch (ParseException pe)
		{
			this.logger.error(String.format("Unable to parse the query string: '%s'", pe.getMessage()), pe);
			throw new ChenilleKitLuceneRuntimeException(pe);
		}
		
		return search(fieldName, query, howMany);
    	
	}
    
    /*
     * (non-Javadoc)
     * @see org.chenillekit.lucene.services.SearcherService#search(java.lang.String, org.apache.lucene.search.Query, java.lang.Integer)
     */
    public List<Document> search(String fieldName, Query query, Integer howMany)
    {
    	ScoreDoc[] scores;

    	int total = howMany != null ? howMany.intValue() : MAX_SCORE_DOC;
    	
    	TopScoreDocCollector collector = TopScoreDocCollector.create(total, true);
		try
		{
			this.indexSearcher.search(query, collector);
			scores = collector.topDocs().scoreDocs;
		}
		catch (IOException ioe)
		{
			this.logger.error(String.format("Unable to access the index for searching: '%s'", ioe.getMessage()), ioe);
			throw new ChenilleKitLuceneRuntimeException(ioe);
		}

    	List<Document> docs = new ArrayList<Document>();

    	for (int i = 0; i < scores.length; i++)
    	{
			int docId = scores[i].doc;

			try
			{
				docs.add(this.indexSearcher.doc(docId));
			}
			catch (CorruptIndexException cie)
			{
				this.logger.error(String.format("The index result corrupted: '%s'", cie.getMessage()), cie);
				throw new ChenilleKitLuceneRuntimeException(cie);
			}
			catch (IOException ioe)
			{
				this.logger.error(String.format("Unable to access the index for searching: '%s'", ioe.getMessage()), ioe);
				throw new ChenilleKitLuceneRuntimeException(ioe);
			}
		}

		return docs;
	}


	/*
     * (non-Javadoc)
     * @see org.apache.tapestry5.ioc.services.ThreadCleanupListener#threadDidCleanup()
     */
	public void threadDidCleanup()
	{
		try
		{
			this.indexSearcher.close();
		}
		catch (IOException ioe)
		{
			this.logger.error("Unable to close the IndexSearcher during thread cleanup: " + ioe.getMessage(), ioe);
		}
	}



}