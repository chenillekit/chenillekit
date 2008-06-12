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
import org.apache.tapestry5.ioc.Registry;
import org.apache.tapestry5.ioc.RegistryBuilder;

import org.chenillekit.lucene.ChenilleKitLuceneTestModule;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Test;
import org.testng.Assert;

/**
 * @author <a href="mailto:homburgs@googlemail.com">shomburg</a>
 * @version $Id$
 */
public class TestLuceneService extends Assert
{
    private static Registry registry;

    @BeforeSuite
    public final void setup_registry()
    {
        RegistryBuilder builder = new RegistryBuilder();

        builder.add(ChenilleKitLuceneTestModule.class);
        registry = builder.build();

        registry.performRegistryStartup();

        initialize_lucene_dictionary();
    }

    public final void shutdown()
    {
        throw new UnsupportedOperationException("No registry shutdown until @AfterSuite.");
    }

    @AfterSuite
    public final void shutdown_registry()
    {
        registry.shutdown();
        registry = null;
    }

    private void initialize_lucene_dictionary()
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
    public void find_records()
    {
        IndexerService service = registry.getService(IndexerService.class);
        assertEquals(service.getDocCount(), 3);
    }
}
