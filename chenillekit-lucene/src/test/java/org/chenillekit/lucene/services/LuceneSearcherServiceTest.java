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

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.Query;
import org.apache.lucene.util.Version;
import org.apache.tapestry5.ioc.Invokable;
import org.apache.tapestry5.ioc.services.ParallelExecutor;
import org.apache.tapestry5.ioc.services.SymbolSource;
import org.chenillekit.lucene.ChenilleKitLuceneConstants;
import org.chenillekit.test.AbstractTestSuite;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

/**
 * @version $Id$
 */
public class LuceneSearcherServiceTest extends AbstractTestSuite
{
	private Version luceneVersion;
	private Analyzer analyzer;
	
	@BeforeClass
	public void initialize_search_facilities()
	{
		IndexSource source = registry.getService(IndexSource.class);
		analyzer = source.getAnalyzer();
		
		SymbolSource symbols = registry.getService(SymbolSource.class);
		luceneVersion = Version.valueOf(
				symbols.valueForSymbol(
						ChenilleKitLuceneConstants.LUCENE_COMPATIBILITY_VERSION));
	}
	
	@Test
    public void query_records() throws IOException
    {
        SearcherService service = registry.getService(SearcherService.class);
        List<Document> docs = service.search("content", "manufacturers OR \"British Government Warehouse\"", 1000);
        assertEquals(docs.size(), 199);
    }
	
	@Test
    public void query_records_second() throws IOException
    {
        SearcherService service = registry.getService(SearcherService.class);
        List<Document> docs = service.search("content", "conspiracy", 1000);
        
        assertEquals(docs.size(), 99);
    }
	
	@Test
    public void query_object() throws IOException
    {
		SearcherService service = registry.getService(SearcherService.class);
		
		QueryParser parser = new QueryParser(this.luceneVersion, "content", this.analyzer);

		try
		{
			Query query = parser.parse("\"SENSORY ORGANS\" OR \"British Government Warehouse\"");
			
			List<Document> docs = service.search("content", query, 1000);
	        
	        assertEquals(docs.size(), 199);
		}
		catch (ParseException pe)
		{
			fail("Unable to parse the query string", pe);
		}
    }
	
	@Test
    public void query_object_empty_result() throws IOException
    {
		SearcherService service = registry.getService(SearcherService.class);
		
		QueryParser parser = new QueryParser(this.luceneVersion, "content", this.analyzer);

		try
		{
			Query query = parser.parse("manufacturers AND \"British Government Warehouse\"");
			
			List<Document> docs = service.search("content", query, 1000);
	        
	        assertEquals(docs.size(), 0);
		}
		catch (ParseException pe)
		{
			fail("Unable to parse the query string", pe);
		}
    }
	
	@Test
    public void threaded_searches() throws IOException
    {
		final SearcherService service = registry.getService(SearcherService.class);
		
		ParallelExecutor executor = registry.getService(ParallelExecutor.class);
		
		List<Future<Integer>> futures = new ArrayList<Future<Integer>>();
		
		for (int i = 0; i < 100; i++)
		{
			Future<Integer> future = executor.invoke(new Invokable<Integer>()
			{
				public Integer invoke()
				{
					List<Document> docs = service.search("content", "probable", 1000);
					
					return docs.size();
				}	
			});
			
			futures.add(future);
		}
		
		
		for (Future<Integer> future : futures)
		{
			try
			{
				assertEquals(future.get().intValue(), 100);
			}
			catch (InterruptedException e)
			{
				fail("threaded_searches has been interrupted", e);
			}
			catch (ExecutionException e)
			{
				fail("threaded_searches has caused an error", e);
			}
		}
    }
}
