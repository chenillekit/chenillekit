/*
 * Apache License
 * Version 2.0, January 2004
 * http://www.apache.org/licenses/
 *
 * Copyright 1996-2008 by Sven Homburg
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 */

package org.chenillekit.template.services.impl;

import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.io.InputStream;
import java.util.Collection;
import java.util.Map;

import org.apache.tapestry5.ioc.Resource;

import freemarker.template.Configuration;
import freemarker.template.DefaultObjectWrapper;
import freemarker.template.Template;
import org.chenillekit.template.services.TemplateService;
import org.slf4j.Logger;

/**
 * template service based on <a href="http://freemarker.sourceforge.net">FreeMarker</a> framework.
 *
 * @author <a href="mailto:homburgs@googlemail.com">S.Homburg</a>
 * @version $Id$
 */
public class FreeMarkerServiceImpl implements TemplateService
{
    public static final String CONFIG_RESOURCE_KEY = "freemarker.configuration";

    private Configuration _configuration;
    private Logger _serviceLog;

    /**
     * Standard-Konstruktor.
     *
     * @param serviceLog
     * @param configuration
     */
    public FreeMarkerServiceImpl(Logger serviceLog, Configuration configuration)
    {
        _serviceLog = serviceLog;
        _configuration = configuration;
    }

    /**
     * merge data from parameter map with template resource into given output stream.
     *
     * @param template     name of the template file
     * @param outputStream where to write in
     * @param parameterMap parameters for template
     */
    public void mergeDataWithResource(Resource template, OutputStream outputStream, Map parameterMap)
    {
        mergeDataWithResource(template, outputStream, parameterMap, (Collection) null);
    }

    /**
     * merge data from parameter map and collection of elements with template resource into given output stream.
     *
     * @param template     name of the template file
     * @param outputStream where to write in
     * @param parameterMap parameters for template
     * @param elements     collection of elements
     */
    public void mergeDataWithResource(Resource template, OutputStream outputStream, Map parameterMap, Collection elements)
    {
        mergeDataWithResource(template, outputStream, parameterMap, elements != null ? elements.toArray() : null);
    }

    /**
     * merge data from parameter map and array of elements with template resource into given output stream.
     *
     * @param template     name of the template file
     * @param outputStream where to write in
     * @param parameterMap parameters for template
     * @param elements     array of elements
     */
    public void mergeDataWithResource(Resource template, OutputStream outputStream, Map parameterMap, Object[] elements)
    {
        try
        {
            if (_configuration == null)
            {
                _configuration = new Configuration();

                // Specify how templates will see the data model. This is an advanced topic...
                // but just use this:
                _configuration.setObjectWrapper(new DefaultObjectWrapper());
                _configuration.setClassForTemplateLoading(Resource.class, "/");
            }

            //noinspection unchecked
            parameterMap.put("elementList", elements);

            if (_serviceLog.isInfoEnabled())
                _serviceLog.info("processing template resource '" + template.getPath() + "'");

            String path = template.getPath();
            Template freeMarkerTemplate = _configuration.getTemplate(path);
            Writer out = new OutputStreamWriter(outputStream);
            freeMarkerTemplate.process(parameterMap, out);
            out.flush();

            if (_serviceLog.isInfoEnabled())
                _serviceLog.info("processing template file '" + template.getPath() + "' finished");

        }
        catch (Exception e)
        {
            throw new RuntimeException(e.getLocalizedMessage());
        }
    }

    /**
     * merge data from parameter map with template resource into given output stream.
     *
     * @param templateStream template stream
     * @param outputStream   where to write in
     * @param parameterMap   parameters for template
     */
    public void mergeDataWithStream(InputStream templateStream, OutputStream outputStream, Map parameterMap)
    {
        mergeDataWithStream(templateStream, outputStream, parameterMap, (Collection) null);
    }

    /**
     * merge data from parameter map and array of elements with template stream into given output stream.
     *
     * @param templateStream template stream
     * @param outputStream   where to write in
     * @param parameterMap   parameters for template
     * @param elements       collection of elements
     */
    public void mergeDataWithStream(InputStream templateStream, OutputStream outputStream, Map parameterMap, Collection elements)
    {
        mergeDataWithStream(templateStream, outputStream, parameterMap, elements != null ? elements.toArray() : null);
    }

    /**
     * merge data from parameter map and array of elements with template stream into given output stream.
     *
     * @param templateStream template stream
     * @param outputStream   where to write in
     * @param parameterMap   parameters for template
     * @param elements       array of elements
     */
    public void mergeDataWithStream(InputStream templateStream, OutputStream outputStream, Map parameterMap, Object[] elements)
    {
        throw new RuntimeException("not implemented yet!");
    }
}