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

import java.io.File;
import java.io.IOException;

import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.Hits;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.Searcher;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.tapestry5.ioc.Resource;
import org.apache.tapestry5.ioc.internal.util.Defense;
import org.apache.tapestry5.ioc.services.RegistryShutdownListener;

import org.chenillekit.lucene.services.IndexerService;
import org.chenillekit.lucene.services.SearcherService;
import org.slf4j.Logger;

/**
 * implements seacher based on <a href="http://lucene.apache.org/java/docs/index.html">lucene</a>.
 *
 * @author <a href="mailto:homburgs@gmail.com">S.Homburg</a>
 * @version $Id$
 */
public class SearcherServiceImpl implements SearcherService<Hits>, RegistryShutdownListener
{
    private Logger logger;
    private Directory directory;
    private Searcher indexSearcher;
    private Analyzer standardAnalyzer;

    public SearcherServiceImpl(final Logger logger, final Resource configResource)
    {
        Defense.notNull(configResource, "configResource");
        this.logger = logger;

        if (!configResource.exists())
            throw new RuntimeException(String.format("config resource '%s' not found!", configResource.toURL().toString()));

        initLucene(configResource);
    }

    private void initLucene(Resource configResource)
    {
        try
        {
            Configuration configuration = new PropertiesConfiguration(configResource.toURL());
            File indexFolderFile = new File(configuration.getString(IndexerService.PROPERTIES_KEY_IF));
            if (!indexFolderFile.exists())
                throw new RuntimeException(String.format("index directory '%s' doesnt exists", indexFolderFile.getAbsoluteFile()));

            String analyzerClassName = configuration.getString(IndexerService.PROPERTIES_KEY_ACN, StandardAnalyzer.class.getName());
            Class analyzerClass = getClass().getClassLoader().loadClass(analyzerClassName);
            standardAnalyzer = (Analyzer) analyzerClass.newInstance();

            directory = FSDirectory.getDirectory(indexFolderFile);
            indexSearcher = new IndexSearcher(directory);
        }
        catch (IOException e)
        {
            throw new RuntimeException(e);
        }
        catch (ConfigurationException e)
        {
            throw new RuntimeException(e);
        }
        catch (ClassNotFoundException e)
        {
            throw new RuntimeException(e);
        }
        catch (IllegalAccessException e)
        {
            throw new RuntimeException(e);
        }
        catch (InstantiationException e)
        {
            throw new RuntimeException(e);
        }
    }

    @SuppressWarnings({"unchecked"})
    public <T> T search(String fieldName, String queryString)
    {
        try
        {
            QueryParser parser = new QueryParser(fieldName, standardAnalyzer);
            Query query = parser.parse(queryString);
            return (T) indexSearcher.search(query);
        }
        catch (ParseException e)
        {
            throw new RuntimeException(e);
        }
        catch (IOException e)
        {
            throw new RuntimeException(e);
        }
    }

    /**
     * close all handles inside the service.
     */
    public void close()
    {
        try
        {
            if (indexSearcher != null)
                indexSearcher.close();

            if (directory != null)
                directory.close();
        }
        catch (IOException e)
        {
            throw new RuntimeException(e);
        }
    }

    /**
     * Invoked when the registry shuts down, giving services a chance to perform any final
     * operations. Service implementations should not attempt to invoke methods on other services
     * (via proxies) as the service proxies may themselves be shutdown.
     */
    public void registryDidShutdown()
    {
        close();
    }
}