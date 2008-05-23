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

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;

import org.apache.tapestry5.MarkupWriter;
import org.apache.tapestry5.BindingConstants;
import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.annotations.Service;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.services.Request;

import org.slf4j.Logger;

/**
 * Formats a Number object with the given <em>mask<em>.
 *
 * @author <a href="mailto:homburgs@gmail.com">S.Homburg</a>
 * @version $Id: NumberFormat.java 682 2008-05-20 22:00:02Z homburgs $
 */
public class NumberFormat
{
    @Inject
    private Logger _logger;

    /**
     * the numeric value.
     */
    @Parameter(required = true, defaultPrefix = BindingConstants.PROP)
    private Number _value;

    /**
     * the format mask.
     */
    @Parameter(required = false, defaultPrefix = BindingConstants.LITERAL, value = "#,##0.0#;(#,##0.0#)")
    private String _mask;

    @Inject
    @Service("Request")
    private Request _request;

    private String getFormatedValue()
    {
        try
        {
            java.text.NumberFormat numberFormat = new DecimalFormat(_mask, new DecimalFormatSymbols(_request.getLocale()));
            return numberFormat.format(_value);
        }
        catch (Exception e)
        {
            _logger.error(e.getLocalizedMessage());
            return _value.toString();
        }
    }

    void beginRender(MarkupWriter writer)
    {
        if (_value != null)
            writer.write(getFormatedValue());
    }
}
