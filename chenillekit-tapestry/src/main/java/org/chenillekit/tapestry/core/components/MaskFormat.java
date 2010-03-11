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

import org.apache.tapestry5.BindingConstants;
import org.apache.tapestry5.ComponentResources;
import org.apache.tapestry5.MarkupWriter;
import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.annotations.SupportsInformalParameters;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.slf4j.Logger;

import javax.swing.text.MaskFormatter;

/**
 * <code>MaskFormat</code> is used to format strings. The behavior
 * of a <code>MaskFormat</code> is controlled by way of a String mask
 * that specifies the valid characters.
 * <p/>
 * for more informations <a target="blank" href="http://java.sun.com/j2se/1.5.0/docs/api/javax/swing/text/MaskFormatter.html">click here</a>
 *
 * @version $Id$
 */
@SupportsInformalParameters
public class MaskFormat
{
    @Inject
    private Logger logger;
    /**
     * the string value
     */
    @Parameter(required = true)
    private String value;

    /**
     * the format mask
     */
    @Parameter(required = true, defaultPrefix = BindingConstants.LITERAL)
    private String mask;

    /**
     * the placeholder character
     */
    @Parameter(required = false, defaultPrefix = BindingConstants.LITERAL)
    private String placeholder;

    /**
     * the valid characters
     */
    @Parameter(required = false, defaultPrefix = BindingConstants.LITERAL)
    private String validCharacters;

    /**
     * ComponentResources. For blocks, messages, crete actionlink, trigger event
     */
    @Inject
    private ComponentResources resources;

    private String getFormatedValue()
    {
        try
        {
            MaskFormatter mf = new javax.swing.text.MaskFormatter(mask);
            mf.setValueContainsLiteralCharacters(false);

            if (placeholder != null)
                mf.setPlaceholderCharacter(placeholder.toCharArray()[0]);

            if (validCharacters != null)
                mf.setValidCharacters(validCharacters);

            return mf.valueToString(value);
        }
        catch (Exception e)
        {
            logger.error(e.getLocalizedMessage());
            return value;
        }
    }

    void beginRender(MarkupWriter writer)
    {
        String formattedValue = getFormatedValue();

        if (logger.isDebugEnabled())
            logger.debug("parameter value: %s / transformed value: %s", value, formattedValue);

        writer.element("span");
        resources.renderInformalParameters(writer);
        writer.write(formattedValue);
        writer.end();
    }
}
