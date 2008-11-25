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

package org.chenillekit.scripting.services;

import java.util.Map;

import org.apache.tapestry5.ioc.Resource;

/**
 * service interface for script interpreters like beanshell, groovy, etc.
 *
 * @version $Id: ScriptingService.java 682 2008-05-20 22:00:02Z homburgs $
 */
public interface ScriptingService
{
    /**
     * Eval a statement.
     *
     * @param language  script language
     * @param statement script statement
     *
     * @return the result returns from statement
     */
    Object eval(String language, String statement);

    /**
     * Eval a statement.
     *
     * @param language       script language
     * @param statement      script resource
     * @param predefinedVars a map of predefined variables
     */
    Object eval(String language, String statement, Map<String, Object> predefinedVars);

    /**
     * Eval a statement.
     *
     * @param scriptResource script resource
     *
     * @return the result returns from statement
     */
    Object eval(Resource scriptResource);

    /**
     * Eval a statement.
     *
     * @param scriptResource script resource
     * @param predefinedVars a map of predefined variables
     */
    Object eval(Resource scriptResource, Map<String, Object> predefinedVars);
}
