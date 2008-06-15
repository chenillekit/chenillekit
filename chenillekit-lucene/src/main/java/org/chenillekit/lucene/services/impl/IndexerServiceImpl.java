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
import org.apache.lucene.document.Document;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.store.RAMDirectory;
import org.apache.tapestry5.ioc.Resource;
import org.apache.tapestry5.ioc.internal.util.Defense;
import org.apache.tapestry5.ioc.services.RegistryShutdownListener;

import org.chenillekit.lucene.services.IndexerService;
import org.slf4j.Logger;

/**
 * implements indexer based on <a href="http://lucene.apache.org/java/docs/index.html">lucene</a>.
 *
 * @author <a href="mailto:homburgs@gmail.com">S.Homburg</a>
 * @version $Id$
 */
public class IndexerServiceImpl implements IndexerService<Document>, RegistryShutdownListener
{
    private Logger logger;
    private IndexWriter _diskIndexWriter;
    private Directory _directory;
    private IndexWriter _ramIndexWriter;

    public IndexerServiceImpl(final Logger logger, final Resource configResource)
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
            boolean createFolder = configuration.getBoolean(IndexerService.PROPERTIES_KEY_OIF, false);
            String analyzerClassName = configuration.getString(IndexerService.PROPERTIES_KEY_ACN, StandardAnalyzer.class.getName());
            int maxFieldLength = configuration.getInt(IndexerService.PROPERTIES_KEY_MFL, 250000);

            // To store an index on disk, use this instead (note that the
            // parameter true will overwrite the index in that directory
            // if one exists):
            if (!indexFolderFile.exists())
                createFolder = true;

            _directory = FSDirectory.getDirectory(indexFolderFile);
            Class analyzerClass = getClass().getClassLoader().loadClass(analyzerClassName);
            _diskIndexWriter = new IndexWriter(_directory, (Analyzer) analyzerClass.newInstance(), createFolder);
            _diskIndexWriter.setMaxFieldLength(maxFieldLength);
        }
        catch (IOException e)
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
        catch (ClassNotFoundException e)
        {
            throw new RuntimeException(e);
        }
        catch (ConfigurationException e)
        {
            throw new RuntimeException(e);
        }
    }

    /**
     * optimize the index.
     */
    public boolean optimizeIndex()
    {
        boolean optimized = true;

        try
        {
            _diskIndexWriter.optimize();
            return optimized;
        }
        catch (IOException e)
        {
            throw new RuntimeException(e);
        }
    }

    /**
     * add a document to the standard index.
     *
     * @param document
     */
    public void addDocument(Document document)
    {
        addDocument(_diskIndexWriter, document);
    }

    /**
     * add a document to the given index.
     *
     * @param indexWriter
     * @param document
     */
    public void addDocument(IndexWriter indexWriter, Document document)
    {
        try
        {
            logger.debug("adding document '%s', to writer", document);

            indexWriter.addDocument((org.apache.lucene.document.Document) document);
        }
        catch (IOException e)
        {
            throw new RuntimeException(e);
        }
    }

    /**
     * close all handles inside the service.
     * wenn ein RAM-IndexWriter offen ist, dann wird dieser gemerged.
     *
     * @link LuceneServiceImpl.mergeRamIndexWriterToDisk
     */
    public void close()
    {
        try
        {
            if (_ramIndexWriter != null)
                mergeRamIndexWriterToDisk();

            if (_diskIndexWriter != null)
                _diskIndexWriter.close();

            if (_directory != null)
                _directory.close();
        }
        catch (IOException e)
        {
            throw new RuntimeException(e);
        }
    }

    /**
     * creates a ram located index writer
     */
    public IndexWriter createRamIndexWriter()
    {
        if (_diskIndexWriter == null)
            throw new RuntimeException("index writer not initialized");

        if (_ramIndexWriter != null)
            throw new RuntimeException("ram disk writer always initialized");

        try
        {
            _ramIndexWriter = new IndexWriter(new RAMDirectory(), _diskIndexWriter.getAnalyzer(), true);
            return _ramIndexWriter;
        }
        catch (IOException e)
        {
            throw new RuntimeException(e);
        }
    }

    /**
     * merged a ram located index writer to disk.
     * ram located index would be closed after merging
     */
    public void mergeRamIndexWriterToDisk()
    {
        if (_diskIndexWriter == null)
            throw new RuntimeException("index writer not initialized");

        if (_ramIndexWriter == null)
            throw new RuntimeException("ram disk writer always initialized");

        try
        {
            logger.debug("merging ram to disk");

            _diskIndexWriter.addIndexes(new Directory[]{_ramIndexWriter.getDirectory()});

            logger.debug("optimizing disk indexer");

            optimizeIndex();

            logger.debug("flush disk indexer");

            _diskIndexWriter.flush();
        }
        catch (IOException e)
        {
            throw new RuntimeException(e);
        }
        finally
        {
            try
            {
                logger.debug("close disk indexer");

                _ramIndexWriter.close();
                _ramIndexWriter = null;
            }
            catch (IOException e)
            {
                throw new RuntimeException(e);
            }
        }
    }

    /**
     * get the amount of documents stored in the disk index.
     *
     * @return amount of documents
     */
    public int getDocCount()
    {
        return _diskIndexWriter.docCount();
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
