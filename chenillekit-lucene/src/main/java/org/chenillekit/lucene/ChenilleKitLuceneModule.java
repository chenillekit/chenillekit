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

package org.chenillekit.lucene;

import static org.apache.tapestry5.ioc.IOCConstants.PERTHREAD_SCOPE;

import java.util.Map;

import org.apache.tapestry5.ioc.Resource;
import org.apache.tapestry5.ioc.ScopeConstants;
import org.apache.tapestry5.ioc.annotations.Scope;
import org.apache.tapestry5.ioc.services.PerthreadManager;
import org.apache.tapestry5.ioc.services.RegistryShutdownHub;
import org.chenillekit.lucene.services.IndexSource;
import org.chenillekit.lucene.services.IndexerService;
import org.chenillekit.lucene.services.SearcherService;
import org.chenillekit.lucene.services.impl.IndexSourceImpl;
import org.chenillekit.lucene.services.impl.IndexerServiceImpl;
import org.chenillekit.lucene.services.impl.SearcherServiceImpl;
import org.slf4j.Logger;

/**
 * @author <a href="mailto:homburgs@gmail.com">shomburg</a>
 * @version $Id$
 */
public class ChenilleKitLuceneModule
{
	/**
	 * 
	 * @param logger
	 * @param configurationMap
	 * @param shutdownHub
	 * @return
	 */
	public static IndexSource buildIndexSource(Logger logger, Map<String, Resource> configurationMap,
						RegistryShutdownHub shutdownHub)
	{
		Resource config = configurationMap.get(ChenilleKitLuceneConstants.CONFIG_KEY_PROPERTIES);
		IndexSourceImpl service = new IndexSourceImpl(logger, config);
		
		shutdownHub.addRegistryShutdownListener(service);
		
		return service;
	}
	
    /**
     * bind the <a href="http://lucene.apache.org/java/docs/index.html">lucene</a> based indexer service.
     *
     * @param logger        system logger
     * @param soruce {@link IndexSource} service
     * @param threadManager the thread manager
     *
     * @return indexer engine
     */
    @Scope(ScopeConstants.PERTHREAD)
    public static IndexerService buildIndexerService(Logger logger, IndexSource source,
    						PerthreadManager threadManager)
    {
        IndexerServiceImpl service = new IndexerServiceImpl(logger, source);
        threadManager.addThreadCleanupListener(service);
        return service;
    }

    /**
     * bind the <a href="http://lucene.apache.org/java/docs/index.html">lucene</a> based searcher service.
     *
     * @param logger        system logger
     * @param configuration IOC configuration map
     * @param threadManager	Manager services to clean up resources
     *
     * @return searcher engine
     */
    public static SearcherService buildSearcherService(Logger logger, IndexSource source,
                                                       PerthreadManager threadManager)
    {
        SearcherServiceImpl service = new SearcherServiceImpl(logger, source);
        
        threadManager.addThreadCleanupListener(service);
        
        return service;
    }
}
