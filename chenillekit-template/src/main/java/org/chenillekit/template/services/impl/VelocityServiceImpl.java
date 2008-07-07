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

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;
import java.util.Collection;
import java.util.Map;
import java.util.Properties;

import org.apache.tapestry5.ioc.Resource;
import org.apache.tapestry5.ioc.internal.util.Defense;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;

import org.chenillekit.template.services.TemplateService;
import org.slf4j.Logger;

/**
 * template service based on <a href="http://velocity.apache.org">Velocity</a> framework.
 *
 * @author <a href="mailto:homburgs@googlemail.com">S.Homburg</a>
 * @version $Id$
 */
public class VelocityServiceImpl implements TemplateService
{
    public static final String CONFIG_RESOURCE_KEY = "velocity.configuration";
    public static final String SERVICE_NAME = "VelocityService";

    private Resource _configuration;
    private Logger _serviceLog;

    /**
     * Standard-Konstruktor.
     *
     * @param serviceLog    the system looger
     * @param configuration the velocity configuration resource
     */
    public VelocityServiceImpl(Logger serviceLog, Resource configuration)
    {
        _serviceLog = serviceLog;
        _configuration = configuration;
        configure();
    }

    private void configure()
    {
        try
        {
            /**
             * Configure the engine - as an example, we are using
             * ourselves as the logger - see logging examples
             */
            Velocity.setProperty(Velocity.RUNTIME_LOG_LOGSYSTEM, this);

            /**
             * now get configuration and initialize the engine
             */
            Properties properties = new Properties();
            properties.load(_configuration.toURL().openStream());
            Velocity.init(properties);
        }
        catch (Exception e)
        {
            throw new RuntimeException(e);
        }
    }

    /**
     * baut den Context fuer Velocity zusammen.
     *
     * @param parameterMap parameters for template
     * @param elements     array of elements, can be null
     *
     * @return the velocity context
     */
    private VelocityContext buildContext(Map parameterMap, Object[] elements)
    {
        /*
        *  Make a context object and populate with the data.  This
        *  is where the Velocity engine gets the data to resolve the
        *  references (ex. $list) in the template
        */
        VelocityContext context = new VelocityContext();
        for (Object o : parameterMap.entrySet())
        {
            Map.Entry entry = (Map.Entry) o;
            context.put((String) entry.getKey(), entry.getValue());
        }
        context.put("elementList", elements);

        return context;
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
            VelocityContext context = buildContext(parameterMap, elements);

            if (_serviceLog.isInfoEnabled())
                _serviceLog.info("processing template resource '" + template.getPath() + "'");

            /*
            *  get the Template object.  This is the parsed version of your
            *  template input file.  Note that getTemplate() can throw
            *   ResourceNotFoundException : if it doesn't find the template
            *   ParseErrorException : if there is something wrong with the VTL
            *   Exception : if something else goes wrong (this is generally
            *        indicative of as serious problem...)
            */
            Template velocityTemplate = Velocity.getTemplate(template.getPath());
            if (velocityTemplate != null)
            {
                Writer out = new OutputStreamWriter(outputStream);
                velocityTemplate.merge(context, out);

                /*
                 *  flush the writer
                 */
                out.flush();
            }

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
     * merge data from parameter map and array of elements with template string into given output stream.
     *
     * @param templateStream template stream
     * @param outputStream   where to write in
     * @param parameterMap   parameters for template
     * @param elements       array of elements
     */
    public void mergeDataWithStream(InputStream templateStream, OutputStream outputStream, Map parameterMap, Object[] elements)
    {
        Defense.notNull(templateStream, "template stream");

        try
        {
            VelocityContext context = buildContext(parameterMap, elements);

            if (_serviceLog.isInfoEnabled())
                _serviceLog.info("processing template stream");

            Writer out = new OutputStreamWriter(outputStream);
            Reader reader = new InputStreamReader(templateStream);
            Velocity.evaluate(context, out, SERVICE_NAME, reader);

            /*
            *  flush the writer
            */
            out.flush();

            if (_serviceLog.isInfoEnabled())
                _serviceLog.info("processing template finished");

        }
        catch (Exception e)
        {
            throw new RuntimeException(e.getLocalizedMessage());
        }
    }
}