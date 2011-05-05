/**
 * 
 */
package org.chenillekit.lucene.services;

import org.apache.lucene.document.Field;
import org.chenillekit.test.AbstractTestSuite;
import org.testng.annotations.Test;

/**
 * Test features available as Near-Real-Time by Lucene
 */
@Test
public class LuceneNearRealTimeTest extends AbstractTestSuite
{
	public void nearRealTimeSearch()
	{
		final String rand = Double.toString(Math.random());
		
		IndexerService indexerService = registry.getService(IndexerService.class);
		indexerService.addDocument(null, new Field("rand", rand, Field.Store.NO, Field.Index.NOT_ANALYZED));
		
		SearcherService searcherService = registry.getService(SearcherService.class);
		int res = searcherService.search("rand", rand, null).size();
		
		assertEquals(res, 1);
		
		indexerService.delDocuments("rand", rand);
		
		res = searcherService.search("rand", rand, null).size();
		
		assertEquals(res, 0);
	}
	
	public void nearRealTimeSearchThreaded()
	{
		final double rand = Math.random();
		
		Thread ti = new Thread(
				new Runnable()
				{	
					public void run()
					{
						IndexerService indexerService = registry.getService(IndexerService.class);
						indexerService.addDocument(null, new Field("rand", Double.toString(rand), Field.Store.NO, Field.Index.NOT_ANALYZED));
					}
				}
			);
		
		ti.start();
		
		try{ Thread.sleep(2000l); }
		catch (InterruptedException e) { }

		SearcherService searcherService = registry.getService(SearcherService.class);
		int res = searcherService.search("rand", Double.toString(rand), null).size();

		assertEquals(res, 1);
		
		
		
	}

}
