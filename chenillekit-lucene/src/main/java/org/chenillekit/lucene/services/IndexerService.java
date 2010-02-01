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

/**
 * implements indexer based on <a href="http://lucene.apache.org/java/docs/index.html">lucene</a>.
 *
 * @version $Id$
 */
public interface IndexerService
{
    /**
     * add a document to the standard index.
     *
     * @param document
     */
    void addDocument(Document document);

    /**
     * delete documents by the given field name and the query.
     *
     * @param field       name of the field
     * @param queryString
     */
    void delDocuments(String field, String queryString);

    /**
     * Get the number of documents stored in the disk index. Be aware the
     * presumably this methods delegates to a <code>synchronized</code>
     * methods.
     *
     * @return the number of documents indexed
     */
    int getDocCount();
    
    /**
     * Get the number of documents stored in the index counting
     * the deletions. Be aware the
     * presumably this methods delegates to a <code>synchronized</code>
     * methods.
     * This methods should never return a checked exception.
     * 
     * @return the number of documents indexed
     */
    int getDocCountWithDeletions();

    /**
     * Force a commit of changes to the index
     */
    void commit();
}
