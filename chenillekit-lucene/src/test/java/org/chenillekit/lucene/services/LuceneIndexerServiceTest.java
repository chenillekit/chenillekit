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
import org.apache.lucene.document.Field;
import org.apache.lucene.index.IndexWriter;

import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Test;

/**
 * @author <a href="mailto:homburgs@googlemail.com">shomburg</a>
 * @version $Id$
 */
public class LuceneIndexerServiceTest extends AbstractTestSuite
{
    @BeforeSuite
    public void initialize_lucene_dictionary()
    {
        IndexerService service = registry.getService(IndexerService.class);
        IndexWriter indexWriter = service.createRamIndexWriter();

        Document document1 = new Document();
        document1.add(new Field("name", "Sven Homburg", Field.Store.YES, Field.Index.TOKENIZED));
        service.addDocument(document1);

        Document document2 = new Document();
        document2.add(new Field("name", "Chris Lewis", Field.Store.YES, Field.Index.TOKENIZED));
        service.addDocument(document2);

        Document document3 = new Document();
        document3.add(new Field("name", "Massimo Lusetti", Field.Store.YES, Field.Index.TOKENIZED));
        service.addDocument(document3);

        service.mergeRamIndexWriterToDisk();
    }

    @Test
    public void indexed_records()
    {
        IndexerService service = registry.getService(IndexerService.class);
        assertEquals(service.getDocCount(), 3);
    }
}
