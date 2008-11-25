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

import org.apache.lucene.document.Document;
import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.Term;
import org.apache.tapestry5.ioc.services.ThreadCleanupListener;

import org.chenillekit.lucene.ChenilleKitLuceneRuntimeException;
import org.chenillekit.lucene.services.IndexSource;
import org.chenillekit.lucene.services.IndexerService;
import org.slf4j.Logger;

/**
 * implements indexer based on <a href="http://lucene.apache.org/java/docs/index.html">lucene</a>.
 *
 * @version $Id$
 */
public class IndexerServiceImpl implements IndexerService, ThreadCleanupListener
{
    private final Logger logger;

    private final IndexWriter indexWriter;

    public IndexerServiceImpl(final Logger logger, IndexSource indexSource)
    {
    	this.logger = logger;
    	this.indexWriter = indexSource.getIndexWriter();
    }


    /**
     * add a document to the standard index.
     *
     * @param document
     */
    public void addDocument(Document document)
    {
        addDocument(this.indexWriter, document);
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
            this.indexWriter.deleteDocuments(new Term(field, queryString));
        }
        catch (IOException ioe)
        {
        	this.logger.error(String.format("Unable to access the index for deleting docs: '%s'", ioe.getMessage()), ioe);
			throw new ChenilleKitLuceneRuntimeException(ioe);
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
        catch (IOException ioe)
        {
        	this.logger.error(String.format("Unable to access the index for updating doc '%s', reason: '%s'", document.toString(), ioe.getMessage()), ioe);
			throw new ChenilleKitLuceneRuntimeException(ioe);
        }
    }

    /**
     * get the amount of documents stored in the disk index.
     *
     * @return amount of documents
     */
    public int getDocCount()
    {
        return this.indexWriter.docCount();
    }

    /*
     * (non-Javadoc)
     * @see org.chenillekit.lucene.services.IndexerService#commit()
     */
    public void commit()
    {
    	try
    	{
			this.indexWriter.commit();
		}
    	catch (CorruptIndexException cie)
    	{
    		this.logger.error(String.format("The index result corrupted: '%s'", cie.getMessage()), cie);
			throw new ChenilleKitLuceneRuntimeException(cie);
		}
    	catch (IOException ioe)
    	{
    		this.logger.error(String.format("Unable to access the index for committing changes, reason: '%s'", ioe.getMessage()), ioe);
			throw new ChenilleKitLuceneRuntimeException(ioe);
		}
	}


	/**
     * Invoked by {@link org.apache.tapestry5.ioc.services.PerthreadManager} service when a thread performs and
     * end-of-request cleanup.
     */
    public void threadDidCleanup()
    {
    	// Commit changes
    	try
    	{
			this.indexWriter.commit();
		}
    	catch (CorruptIndexException cie)
    	{
    		this.logger.error(String.format("The index result corrupted: '%s'", cie.getMessage()), cie);
			throw new ChenilleKitLuceneRuntimeException(cie);
		}
    	catch (IOException ioe)
    	{
    		this.logger.error(String.format("Unable to access the index for commiting changes: '%s'", ioe.getMessage()), ioe);
			throw new ChenilleKitLuceneRuntimeException(ioe);
		}
    }
}
