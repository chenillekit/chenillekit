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

import javax.swing.text.MaskFormatter;

import org.apache.tapestry5.BindingConstants;
import org.apache.tapestry5.ComponentResources;
import org.apache.tapestry5.MarkupWriter;
import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.annotations.SupportsInformalParameters;
import org.apache.tapestry5.ioc.annotations.Inject;

import org.slf4j.Logger;

/**
 * <code>MaskFormat</code> is used to format strings. The behavior
 * of a <code>MaskFormat</code> is controlled by way of a String mask
 * that specifies the valid characters.
 * <p/>
 * for more informations <a target="blank" href="http://java.sun.com/j2se/1.5.0/docs/api/javax/swing/text/MaskFormatter.html">click here</a>
 *
 * @author <a href="mailto:homburgs@gmail.com">S.Homburg</a>
 * @version $Id: MaskFormat.java 682 2008-05-20 22:00:02Z homburgs $
 */
@SupportsInformalParameters
public class MaskFormat
{
    @Inject
    private Logger _logger;
    /**
     * the string value
     */
    @Parameter(required = true)
    private String _value;

    /**
     * the format mask
     */
    @Parameter(required = true, defaultPrefix = BindingConstants.LITERAL)
    private String _mask;

    /**
     * the placeholder character
     */
    @Parameter(required = false, defaultPrefix = BindingConstants.LITERAL)
    private String _placeholder;

    /**
     * the valid characters
     */
    @Parameter(required = false, defaultPrefix = BindingConstants.LITERAL)
    private String _validCharacters;

    /**
     * ComponentResources. For blocks, messages, crete actionlink, trigger event
     */
    @Inject
    private ComponentResources _resources;

    private String getFormatedValue()
    {
        try
        {
            MaskFormatter mf = new javax.swing.text.MaskFormatter(_mask);
            mf.setValueContainsLiteralCharacters(false);

            if (_placeholder != null)
                mf.setPlaceholderCharacter(_placeholder.toCharArray()[0]);

            if (_validCharacters != null)
                mf.setValidCharacters(_validCharacters);

            return mf.valueToString(_value);
        }
        catch (Exception e)
        {
            _logger.error(e.getLocalizedMessage());
            return _value;
        }
    }

    void beginRender(MarkupWriter writer)
    {
        String formattedValue = getFormatedValue();

        if (_logger.isDebugEnabled())
            _logger.debug("parameter value: %s / transformed value: %s", _value, formattedValue);

        writer.element("span");
        _resources.renderInformalParameters(writer);
        writer.write(formattedValue);
        writer.end();
    }
}
