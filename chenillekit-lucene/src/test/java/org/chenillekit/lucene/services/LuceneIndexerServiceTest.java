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

        Resource res1 = new ClasspathResource(this.getClass().getClassLoader(), "airbag.txt");
        Resource res2 = new ClasspathResource(this.getClass().getClassLoader(), "consp.txt");

        Document document1 = new Document();
        document1.add(new Field("filename", res1.toURL().toString(), Field.Store.YES, Field.Index.NO));
        document1.add(new Field("content", readFile(res1.toURL()), Field.Store.YES, Field.Index.TOKENIZED));
        service.addDocument(document1);

        Document document2 = new Document();
        document2.add(new Field("filename", res2.toURL().toString(), Field.Store.YES, Field.Index.NO));
        document2.add(new Field("content", readFile(res2.toURL()), Field.Store.YES, Field.Index.TOKENIZED));
        service.addDocument(document2);

        service.mergeRamIndexWriterToDisk();
    }

    @Test
    public void indexed_records()
    {
        IndexerService service = registry.getService(IndexerService.class);
        assertEquals(service.getDocCount(), 2);
    }

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
