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
package org.chenillekit.template.services;

import java.io.OutputStream;
import java.util.Collection;
import java.util.Map;

import org.apache.tapestry5.ioc.Resource;


/**
 * interface for template services.
 *
 * @author <a href="mailto:homburgs@googlemail.com">S.Homburg</a>
 * @version $Id$
 */
public interface TemplateService
{
    /**
     * merge data from parameter map with template resource into given output stream.
     *
     * @param template     name of the template file
     * @param outputStream where to write in
     * @param parameterMap parameters for template
     */
    void mergeDataWithResource(Resource template, OutputStream outputStream, Map parameterMap);

    /**
     * merge data from parameter map and collection of elements with template resource into given output stream.
     *
     * @param template     name of the template file
     * @param outputStream where to write in
     * @param parameterMap parameters for template
     * @param elements     collection of elements
     */
    void mergeDataWithResource(Resource template, OutputStream outputStream, Map parameterMap, Collection elements);

    /**
     * merge data from parameter map and array of elements with template resource into given output stream.
     *
     * @param template     name of the template file
     * @param outputStream where to write in
     * @param parameterMap parameters for template
     * @param elements     array of elements
     */
    void mergeDataWithResource(Resource template, OutputStream outputStream, Map parameterMap, Object[] elements);
}
