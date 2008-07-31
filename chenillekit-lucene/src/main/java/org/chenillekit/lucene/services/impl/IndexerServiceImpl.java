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
import org.apache.lucene.index.Term;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.store.RAMDirectory;
import org.apache.tapestry5.ioc.Resource;
import org.apache.tapestry5.ioc.internal.util.Defense;
import org.apache.tapestry5.ioc.services.ThreadCleanupListener;

import org.chenillekit.lucene.services.IndexerService;
import org.slf4j.Logger;

/**
 * implements indexer based on <a href="http://lucene.apache.org/java/docs/index.html">lucene</a>.
 *
 * @author <a href="mailto:homburgs@gmail.com">S.Homburg</a>
 * @version $Id$
 */
public class IndexerServiceImpl implements IndexerService<Document>, ThreadCleanupListener
{
    private Logger logger;
    private Directory directory;
    private IndexWriter diskIndexWriter;
    private IndexWriter ramIndexWriter;
    private IndexWriter actualIndexWriter;
    private boolean optimizeAfterRamwriterClosed;
    private boolean enableLuceneOutput;

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
            boolean optimizeAfterRamwriterClosed = configuration.getBoolean(PROPERTIES_KEY_OPT, false);
            boolean enableLuceneOutput = configuration.getBoolean(PROPERTIES_KEY_ELO, false);
            String analyzerClassName = configuration.getString(IndexerService.PROPERTIES_KEY_ACN, StandardAnalyzer.class.getName());
            int maxFieldLength = configuration.getInt(IndexerService.PROPERTIES_KEY_MFL, 250000);

            // To store an index on disk, use this instead (note that the
            // parameter true will overwrite the index in that directory
            // if one exists):
            if (!indexFolderFile.exists())
                createFolder = true;

            directory = FSDirectory.getDirectory(indexFolderFile);
            Class analyzerClass = getClass().getClassLoader().loadClass(analyzerClassName);

            if (enableLuceneOutput)
                IndexWriter.setDefaultInfoStream(System.out);
            
            diskIndexWriter = new IndexWriter(directory, (Analyzer) analyzerClass.newInstance(), createFolder);
            diskIndexWriter.setMaxFieldLength(maxFieldLength);
            createRamIndexWriter();

            useRamWriter(false);
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
     * change between ram and disk writer.
     * <p/>
     * if <em>use</em> set to false and the ram writer was active,
     * the ram writer content merged to disk writer first.
     *
     * @param use
     */
    public void useRamWriter(boolean use)
    {
        if (use)
            actualIndexWriter = ramIndexWriter;
        else
        {
            if (isRamWriterActive())
                mergeRamIndexWriterToDisk(false);

            actualIndexWriter = diskIndexWriter;
        }
    }

    /**
     * test if ram indexwriter is active.
     */
    public boolean isRamWriterActive()
    {
        return actualIndexWriter == ramIndexWriter;
    }

    /**
     * optimize the index.
     * <p/>
     * if the ram writer is active, the content of ram write merged to disk writer first.
     */
    public void optimizeIndex()
    {
        mergeRamIndexWriterToDisk(false);
    }

    /**
     * add a document to the standard index.
     *
     * @param document
     */
    public void addDocument(Document document)
    {
        addDocument(actualIndexWriter, document);
    }

    /**
     * delete documents by the given field name and the query.
     *
     * @param field       name of the field
     * @param queryString
     */
    public void delDocuments(String field, String queryString)
    {
        try
        {
            diskIndexWriter.deleteDocuments(new Term(field, queryString));
            if (ramIndexWriter.docCount() > 0)
                ramIndexWriter.deleteDocuments(new Term(field, queryString));
        }
        catch (IOException e)
        {
            throw new RuntimeException();
        }
    }

    /**
     * add a document to the given index.
     *
     * @param indexWriter
     * @param document
     */
    private void addDocument(IndexWriter indexWriter, Document document)
    {
        try
        {
            if (logger.isDebugEnabled())
                logger.debug("adding document '%s', to writer", document);

            indexWriter.addDocument(document);
        }
        catch (IOException e)
        {
            throw new RuntimeException(e);
        }
    }

    /**
     * close all handles inside the service.
     *
     * @link LuceneServiceImpl.mergeRamIndexWriterToDisk
     */
    private void close()
    {
        try
        {
            if (ramIndexWriter != null)
                mergeRamIndexWriterToDisk(true);

            if (diskIndexWriter != null)
                diskIndexWriter.close();

            if (ramIndexWriter != null)
                ramIndexWriter.close();

            if (directory != null)
                directory.close();
        }
        catch (IOException e)
        {
            throw new RuntimeException(e);
        }
    }

    /**
     * creates a ram located index writer
     */
    private IndexWriter createRamIndexWriter()
    {
        if (diskIndexWriter == null)
            throw new RuntimeException("index writer not initialized");

        try
        {
            if (ramIndexWriter == null)
                ramIndexWriter = new IndexWriter(new RAMDirectory(), diskIndexWriter.getAnalyzer(), true);

            return ramIndexWriter;
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
    private void mergeRamIndexWriterToDisk(boolean closing)
    {
        if (diskIndexWriter == null)
            throw new RuntimeException("index writer not initialized");

        if (ramIndexWriter == null)
            throw new RuntimeException("ram index writer not initialized");

        try
        {
            logger.debug("merging ram to disk");

            diskIndexWriter.addIndexes(new Directory[]{ramIndexWriter.getDirectory()});

            logger.debug("optimizing disk indexer");

            if (optimizeAfterRamwriterClosed || closing)
                diskIndexWriter.optimize();

            logger.debug("flush disk indexer");

            diskIndexWriter.flush();
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

                ramIndexWriter.close();
                ramIndexWriter = null;
                createRamIndexWriter();
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
        return diskIndexWriter.docCount();
    }

    /**
     * Invoked by {@link org.apache.tapestry5.ioc.services.PerthreadManager} service when a thread performs and
     * end-of-request cleanup.
     */
    public void threadDidCleanup()
    {
        close();
    }
}
