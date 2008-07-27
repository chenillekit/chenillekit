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

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.LineNumberReader;
import java.net.URISyntaxException;
import java.net.URL;

import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.IndexWriter;
import org.apache.tapestry5.ioc.Resource;
import org.apache.tapestry5.ioc.internal.util.ClasspathResource;

import org.chenillekit.core.utils.AbstractTestSuite;
import org.chenillekit.lucene.ChenilleKitLuceneTestModule;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Test;

/**
 * @author <a href="mailto:homburgs@googlemail.com">shomburg</a>
 * @version $Id$
 */
public class LuceneIndexerServiceTest extends AbstractTestSuite
{
    @BeforeSuite
    public final void setup_registry()
    {
        super.setup_registry(ChenilleKitLuceneTestModule.class);
    }

    @BeforeClass
    public void initialize_lucene_dictionary()
    {
        IndexerService service = registry.getService(IndexerService.class);
        IndexWriter indexWriter = service.createRamIndexWriter();
        String[] fileNames = new String[]{"airbag.txt", "consp.txt", "aliens.txt"};

        for (String fileName : fileNames)
        {
            System.err.println(String.format("adding file '%s' to index", fileName));

            Resource resource = new ClasspathResource(this.getClass().getClassLoader(), fileName);

            Document document = new Document();
            document.add(new Field("id", fileName, Field.Store.YES, Field.Index.UN_TOKENIZED));
            document.add(new Field("url", resource.toURL().toString(), Field.Store.YES, Field.Index.UN_TOKENIZED));
            document.add(new Field("content", readFile(resource.toURL()), Field.Store.YES, Field.Index.TOKENIZED));
            service.addDocument(document);
        }

        service.mergeRamIndexWriterToDisk();
    }

    @Test
    public void indexed_records()
    {
        IndexerService service = registry.getService(IndexerService.class);
        assertEquals(service.getDocCount(), 3);
    }

//    @Test(dependsOnMethods = {"indexed_records"})
//    public void delete_document()
//    {
//        IndexerService service = registry.getService(IndexerService.class);
//
//        System.err.println("deleted: " + service.delDocument("id", "consp.txt"));
//
//        assertEquals(service.getDocCount(), 2);
//    }

    private String readFile(URL file)
    {
        StringBuffer stringBuffer = new StringBuffer();
        LineNumberReader lnr = null;

        try
        {
            lnr = new LineNumberReader(new FileReader(new File(file.toURI())));
            String readedLine;
            while ((readedLine = lnr.readLine()) != null)
                stringBuffer.append(readedLine);

            return stringBuffer.toString();
        }
        catch (FileNotFoundException e)
        {
            throw new RuntimeException(e);
        }
        catch (URISyntaxException e)
        {
            throw new RuntimeException(e);
        }
        catch (IOException e)
        {
            throw new RuntimeException(e);
        }
        finally
        {
            try
            {
                if (lnr != null)
                    lnr.close();
            }
            catch (IOException e)
            {
            }
        }
    }
}
