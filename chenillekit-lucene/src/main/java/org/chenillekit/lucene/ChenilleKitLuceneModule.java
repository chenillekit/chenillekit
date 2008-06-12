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

import java.util.Map;

import org.apache.tapestry5.ioc.Resource;
import org.apache.tapestry5.ioc.services.RegistryShutdownHub;

import org.chenillekit.lucene.services.IndexerService;
import org.chenillekit.lucene.services.impl.IndexerServiceImpl;
import org.slf4j.Logger;

/**
 * @author <a href="mailto:homburgs@gmail.com">shomburg</a>
 * @version $Id$
 */
public class ChenilleKitLuceneModule
{
    /**
     * bind the <a href="http://lucene.apache.org/java/docs/index.html">lucene</a> based indexer service.
     *
     * @param logger        system logger
     * @param configuration IOC configuration map
     * @param shutdownHub   the shutdown hub
     *
     * @return indexer engine
     */
    public static IndexerService buildIndexerService(Logger logger, Map<String, Resource> configuration,
                                                     RegistryShutdownHub shutdownHub)
    {
        IndexerServiceImpl service = new IndexerServiceImpl(logger, configuration.get(IndexerService.CONFIG_KEY_PROPERTIES));
        shutdownHub.addRegistryShutdownListener(service);
        return service;
    }
}
