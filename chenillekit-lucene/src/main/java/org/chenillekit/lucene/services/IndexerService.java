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

import org.apache.lucene.document.Document;
import org.apache.lucene.index.IndexWriter;

/**
 * implements indexer based on <a href="http://lucene.apache.org/java/docs/index.html">lucene</a>.
 *
 * @author <a href="mailto:homburgs@gmail.com">S.Homburg</a>
 * @version $Id$
 */
public interface IndexerService
{
    public final String CONFIG_KEY_PROPERTIES = "lucene.properties";
    public final String PROPERTIES_KEY_IF = "search.index.folder";
    public final String PROPERTIES_KEY_OIF = "search.overwrite.index.folder";
    public final String PROPERTIES_KEY_ACN = "search.analyzer.class.name";
    public final String PROPERTIES_KEY_MFL = "search.max.field.length";

    /**
     * optimize the index.
     *
     * @return un-/successfull optimized
     */
    boolean optimizeIndex();

    /**
     * add a document to the standard index.
     *
     * @param document
     */
    void addDocument(Document document);

    /**
     * add a document to the given index.
     *
     * @param indexWriter
     * @param document
     */
    void addDocument(IndexWriter indexWriter, Document document);

    /**
     * close all handles inside the service.
     */
    void close();

    /**
     * creates a ram located index writer
     */
    IndexWriter createRamIndexWriter();

    /**
     * merged a ram located index writer to disk.
     * ram located index would be closed after merging
     */
    void mergeRamIndexWriterToDisk();

    /**
     * get the amount of documents stored in the disk index.
     *
     * @return amount of documents
     */
    int getDocCount();
}
