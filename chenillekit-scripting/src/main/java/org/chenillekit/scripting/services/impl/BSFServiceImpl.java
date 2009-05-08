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

package org.chenillekit.scripting.services.impl;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringReader;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import org.apache.bsf.BSFException;
import org.apache.bsf.BSFManager;
import org.apache.bsf.util.IOUtils;
import org.apache.tapestry5.ioc.Resource;

import org.chenillekit.scripting.services.ScriptingService;
import org.slf4j.Logger;

/**
 * service implementation for scripting framework <a href="http://jakarta.apache.org/bsf">BSF</a>.
 *
 * @version $Id: BSFServiceImpl.java 682 2008-05-20 22:00:02Z homburgs $
 */
public class BSFServiceImpl implements ScriptingService
{
    private BSFManager _bsfManager;
    private Logger _syslog;
    private static Random _random = new Random();

    public BSFServiceImpl(Logger syslog)
    {
        _bsfManager = new BSFManager();
        _syslog = syslog;
    }

    /**
     * Eval a statement.
     *
     * @param language         script language
     * @param expressionReader script language expression
     * @param predefinedVars   a map of predefined variables
     *
     * @return the result returns from statement
     */
    private Object internalEval(String language, Reader expressionReader, Map<String, Object> predefinedVars)
    {
        setPredefinedVarsToInterpreter(predefinedVars);

        if (!BSFManager.isLanguageRegistered(language))
            throw new RuntimeException(String.format("language '%s' unknown to BSFManager", language));

        try
        {
            String fileName = String.format("script_%d", _random.nextLong());

            if (_syslog.isInfoEnabled())
                _syslog.info("interpreter for language '{}' evaluates script '{}/{}'",
                             new Object[]{language, _bsfManager.getTempDir(), fileName});

            return _bsfManager.eval(language, fileName, 0, 0, IOUtils.getStringFromReader(expressionReader));
        }
        catch (BSFException e)
        {
            throw new RuntimeException(e);
        }
        catch (IOException e)
        {
            throw new RuntimeException(e);
        }
    }

    /**
     * Eval a statement.
     *
     * @param language  script language
     * @param statement script statement
     *
     * @return the result returns from statement
     */
    public Object eval(String language, String statement)
    {
        return eval(language, statement, null);
    }

    /**
     * Eval a statement.
     *
     * @param language       script language
     * @param statement      script statement
     * @param predefinedVars a map of predefined variables
     *
     * @return the result returns from statement
     */
    public Object eval(String language, String statement, Map<String, Object> predefinedVars)
    {
        return internalEval(language, new StringReader(statement), predefinedVars);
    }

    /**
     * Eval a script.
     *
     * @param scriptResource script resource
     *
     * @return the result returns from script
     */
    public Object eval(Resource scriptResource)
    {
        return eval(scriptResource, null);
    }

    /**
     * Eval a script.
     *
     * @param scriptResource script resource
     * @param predefinedVars a map of predefined variables
     *
     * @return the result returns from script
     */
    public Object eval(Resource scriptResource, Map<String, Object> predefinedVars)
    {
        checkIfUrlIsNull(scriptResource);

        try
        {
            String language = BSFManager.getLangFromFilename(scriptResource.getFile());
            return internalEval(language, new InputStreamReader(scriptResource.toURL().openStream()), predefinedVars);
        }
        catch (IOException e)
        {
            throw new RuntimeException(e);
        }
        catch (BSFException e)
        {
            throw new RuntimeException(e);
        }
    }

    /**
     * import all predefined vars to the interpreter.
     *
     * @param predefinedVars a map of predefined variables
     */
    private void setPredefinedVarsToInterpreter(Map<String, Object> predefinedVars)
    {
        if (predefinedVars == null)
            predefinedVars = new HashMap<String, Object>();

        for (String key : predefinedVars.keySet())
            _bsfManager.registerBean(key, predefinedVars.get(key));
    }

    /**
     * throws an exception if URL of resource is null.
     *
     * @param resource
     */
    private void checkIfUrlIsNull(Resource resource)
    {
        if (resource.toURL() == null)
            throw new RuntimeException(String.format("URL of resource '%s('%s')' is null",
                                                     resource.getClass().getName(), resource.getPath()));
    }
}