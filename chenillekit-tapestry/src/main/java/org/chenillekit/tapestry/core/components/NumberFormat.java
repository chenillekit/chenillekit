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
import org.apache.tapestry5.MarkupWriter;
import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.annotations.Service;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.services.Request;
import org.slf4j.Logger;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;

/**
 * Formats a Number object with the given <em>mask<em>.
 *
 * @version $Id$
 */
public class NumberFormat
{
    @Inject
    private Logger logger;

    /**
     * the numeric value.
     */
    @Parameter(required = true, defaultPrefix = BindingConstants.PROP)
    private Number value;

    /**
     * the format mask.
     */
    @Parameter(required = false, defaultPrefix = BindingConstants.LITERAL, value = "#,##0.0#;(#,##0.0#)")
    private String mask;

    @Inject
    @Service("Request")
    private Request request;

    private String getFormatedValue()
    {
        try
        {
            java.text.NumberFormat numberFormat = new DecimalFormat(mask, new DecimalFormatSymbols(request.getLocale()));
            return numberFormat.format(value);
        }
        catch (Exception e)
        {
            logger.error(e.getLocalizedMessage());
            return value.toString();
        }
    }

    void beginRender(MarkupWriter writer)
    {
        if (value != null)
            writer.write(getFormatedValue());
    }
}
