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
import org.apache.lucene.document.Fieldable;

/**
 * Implements indexer based on <a href="http://lucene.apache.org/java/docs/index.html">lucene</a>.
 *
 * @version $Id$
 */
public interface IndexerService
{
    /**
     * Add a document to the standard index.
     *
     * @param document
     * @deprecated use {@link addDocument(Fieldable... fields)}
     */
    void addDocument(Document document);
    
    /**
	 * Add a document build from the actual fields and boost value
	 * 
	 * @param boost float boost value, if <code>null</code> no boost is used
	 * @param fields the {@link Fieldable}s to actually add to the {@link Document}
	 */
	void addDocument(Float boost, Fieldable... fields);

    /**
     * Delete documents by the given field name and the query.
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
