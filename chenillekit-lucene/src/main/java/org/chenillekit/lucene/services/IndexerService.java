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

/**
 * implements indexer based on <a href="http://lucene.apache.org/java/docs/index.html">lucene</a>.
 *
 * @author <a href="mailto:homburgs@gmail.com">S.Homburg</a>
 * @version $Id$
 */
public interface IndexerService<T>
{
    public final String CONFIG_KEY_PROPERTIES = "lucene.properties";
    public final String PROPERTIES_KEY_IF = "search.index.folder";
    public final String PROPERTIES_KEY_OIF = "search.overwrite.index.folder";
    public final String PROPERTIES_KEY_ACN = "search.analyzer.class.name";
    public final String PROPERTIES_KEY_MFL = "search.max.field.length";
    public final String PROPERTIES_KEY_OPT = "optimize.after.ramwriter.closed";
    public final String PROPERTIES_KEY_ELO = "enable.lucene.log.output";

    /**
     * optimize the index.
     *
     * @return un-/successfull optimized
     */
    void optimizeIndex();

    /**
     * service schould use the indexwriter or not.
     *
     * @param use
     */
    void useRamWriter(boolean use);

    /**
     * test if ram indexwriter is active.
     */
    boolean isRamWriterActive();

    /**
     * add a document to the standard index.
     *
     * @param document
     */
    void addDocument(T document);

    /**
     * delete documents by the given field name and the query.
     *
     * @param field       name of the field
     * @param queryString
     */
    void delDocuments(String field, String queryString);

    /**
     * get the amount of documents stored in the disk index.
     *
     * @return amount of documents
     */
    int getDocCount();
}
