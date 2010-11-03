/*
 * Apache License
 * Version 2.0, January 2004
 * http://www.apache.org/licenses/
 *
 * Copyright 2008-2010 by chenillekit.org
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 */

package org.chenillekit.lucene;

import java.net.URL;
import java.util.List;

import org.apache.lucene.util.Version;
import org.apache.tapestry5.ioc.MappedConfiguration;
import org.apache.tapestry5.ioc.ScopeConstants;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.ioc.annotations.Scope;
import org.apache.tapestry5.ioc.annotations.Symbol;
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
	public static IndexSource buildIndexSource(Logger logger, List<URL> configuration,
						RegistryShutdownHub shutdownHub,
						@Inject
						@Symbol(ChenilleKitLuceneConstants.LUCENE_COMPATIBILITY_VERSION)
						String version)
	{
		IndexSourceImpl service = new IndexSourceImpl(logger, configuration, Version.valueOf(version));

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
    @Scope(ScopeConstants.PERTHREAD)
    public static SearcherService buildSearcherService(Logger logger, IndexSource source,
                                                       PerthreadManager threadManager,
                                                       @Inject
                                                       @Symbol(ChenilleKitLuceneConstants.LUCENE_COMPATIBILITY_VERSION)
                                                       String version)
    {
        SearcherServiceImpl service = new SearcherServiceImpl(logger, source, Version.valueOf(version));

        threadManager.addThreadCleanupListener(service);

        return service;
    }
    
    public static void contributeFactoryDefaults(MappedConfiguration<String, String> configuration)
    {
        configuration.add(ChenilleKitLuceneConstants.LUCENE_COMPATIBILITY_VERSION, Version.LUCENE_30.toString());
    }
}
