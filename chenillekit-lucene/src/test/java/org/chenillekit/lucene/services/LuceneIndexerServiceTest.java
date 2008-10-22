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
import java.util.List;

import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.tapestry5.ioc.Resource;
import org.apache.tapestry5.ioc.internal.util.ClasspathResource;
import org.chenillekit.lucene.ChenilleKitLuceneTestModule;
import org.chenillekit.test.AbstractTestSuite;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Test;

/**
 * @author <a href="mailto:homburgs@googlemail.com">shomburg</a>
 * @version $Id$
 */
public class LuceneIndexerServiceTest extends AbstractTestSuite
{
    int repeating = 100;
    int docAmount = 3;

    @BeforeSuite
    public final void setup_registry()
    {
        super.setup_registry(ChenilleKitLuceneTestModule.class);
    }

    @BeforeClass
    public void initialize_lucene_dictionary()
    {
        IndexerService service = registry.getService(IndexerService.class);
        String[] fileNames = new String[]{"airbag.txt", "consp.txt", "aliens.txt"};

        Document document = new Document();
        document.add(new Field("id", "", Field.Store.YES, Field.Index.UN_TOKENIZED));
        document.add(new Field("url", "", Field.Store.YES, Field.Index.UN_TOKENIZED));
        document.add(new Field("content", "", Field.Store.YES, Field.Index.TOKENIZED));

        for (int i = 0; i < repeating; i++)
        {
            for (String fileName : fileNames)
            {
                Resource resource = new ClasspathResource(this.getClass().getClassLoader(), fileName);

                document.getField("id").setValue(fileName + "_" + String.valueOf(i));
                document.getField("url").setValue(resource.toURL().toString());
                document.getField("content").setValue(readFile(resource.toURL()));
                service.addDocument(document);
            }
        }
    }

    @Test
    public void indexed_records()
    {
        IndexerService service = registry.getService(IndexerService.class);
        assertEquals(service.getDocCount(), repeating * docAmount);
        
        service.commit();
    }

    @Test(dependsOnMethods = {"indexed_records"})
    public void delete_document()
    {
        IndexerService service = registry.getService(IndexerService.class);
        service.delDocuments("id", "consp.txt_1");
        assertEquals(service.getDocCount(), (repeating * docAmount));
        service.commit();
        
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
