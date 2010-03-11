/*
 * Apache License
 * Version 2.0, January 2004
 * http://www.apache.org/licenses/
 *
 * Copyright 2008-2010 by chenillekit.org
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 */

package org.chenillekit.tapestry.core.components;

import org.apache.tapestry5.ComponentResources;
import org.apache.tapestry5.MarkupWriter;
import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.annotations.SupportsInformalParameters;
import org.apache.tapestry5.ioc.annotations.Inject;

/**
 * Replace from <em>maxLength</em> all text with parameter value <em>suffix</em>.
 *
 * @version $Id$
 */
@SupportsInformalParameters
public class TrimmedString
{
    public static final String TRIM_LEFT = "left";
    public static final String TRIM_RIGHT = "right";

    /**
     * the string value
     */
    @Parameter(required = true, defaultPrefix = "prop")
    private String value;

    /**
     * max. length of string value
     */
    @Parameter(required = false, defaultPrefix = "literal", value = "20")
    private int maxLength;

    /**
     * the suffix for strings that longer then <em>maxLength</em>
     */
    @Parameter(required = false, defaultPrefix = "literal", value = "...")
    private String suffix;

    /**
     * trim the string at the <code>left</code> or <code>right</code> site.
     * Default is <code>right</code>.
     */
    @Parameter(required = false, defaultPrefix = "literal", value = TRIM_RIGHT)
    private String trimPos;

    /**
     * ComponentResources. For blocks, messages, crete actionlink, trigger event
     */
    @Inject
    private ComponentResources _resources;


    boolean beginRender(MarkupWriter writer)
    {
        String tmpValue = value;

        if (tmpValue == null)
            return true;

        if (tmpValue.length() > maxLength)
        {
            if (trimPos.equalsIgnoreCase(TRIM_LEFT))
                tmpValue = suffix + value.substring(value.length() - maxLength);
            else
                tmpValue = value.substring(0, maxLength) + suffix;
        }

        writer.element("span", "title", value);
        _resources.renderInformalParameters(writer);
        writer.write(tmpValue);
        writer.end();

        return false;
    }
}
