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
 *
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
 * @author <a href="mailto:homburgs@gmail.com">S.Homburg</a>
 * @version $Id: TrimmedString.java 682 2008-05-20 22:00:02Z homburgs $
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
    private String _value;

    /**
     * max. length of string value
     */
    @Parameter(required = false, defaultPrefix = "literal", value = "20")
    private int _maxLength;

    /**
     * the suffix for strings that longer then <em>maxLength</em>
     */
    @Parameter(required = false, defaultPrefix = "literal", value = "...")
    private String _suffix;

    /**
     * trim the string at the <code>left</code> or <code>right</code> site.
     * Default is <code>right</code>.
     */
    @Parameter(required = false, defaultPrefix = "literal", value = TRIM_RIGHT)
    private String _trimPos;

    /**
     * ComponentResources. For blocks, messages, crete actionlink, trigger event
     */
    @Inject
    private ComponentResources _resources;


    boolean beginRender(MarkupWriter writer)
    {
        String tmpValue = _value;

        if (tmpValue == null)
            return true;

        if (tmpValue.length() > _maxLength)
        {
            if (_trimPos.equalsIgnoreCase(TRIM_LEFT))
                tmpValue = _suffix + _value.substring(_value.length() - _maxLength);
            else
                tmpValue = _value.substring(0, _maxLength) + _suffix;
        }

        writer.element("span", "title", _value);
        _resources.renderInformalParameters(writer);
        writer.write(tmpValue);
        writer.end();

        return false;
    }
}
