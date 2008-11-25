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

import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.tapestry5.BindingConstants;
import org.apache.tapestry5.MarkupWriter;
import org.apache.tapestry5.annotations.Mixin;
import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.corelib.mixins.RenderInformals;

/**
 * Formats a Date object with the given <em>pattern<em>.
 * <p/>
 * The given html tag sourrunds the formatted date value
 * end the optional parameter <em>bodyPosition</em> shifts the
 * tag body to the left or right side, or discarded it.
 *
 * @version $Id: DateFormat.java 682 2008-05-20 22:00:02Z homburgs $
 */
public class DateFormat
{
    /**
     * discard body
     */
    public static int BODY_POS_NONE = 0;

    /**
     * shift body to left
     */
    public static int BODY_POS_LEFT = 1;

    /**
     * shift body to right
     */
    public static int BODY_POS_RIGHT = 2;

    /**
     * the date value.
     */
    @Parameter(required = true, defaultPrefix = BindingConstants.PROP)
    private Date _value;

    /**
     * the format pattern.
     */
    @Parameter(required = false, defaultPrefix = BindingConstants.LITERAL, value = "yyyy-MM-dd")
    private String _pattern;

    /**
     * the eelement body position.
     * may be : BODY_POS_NONE (default) / BODY_POS_LEFT / BODY_POS_RIGHT
     */
    @Parameter(required = false, defaultPrefix = BindingConstants.LITERAL, value = "0")
    private int _bodyPosition;

    @Mixin
    private RenderInformals _renderInformals;

//    @Mixin
//    private org.apache.tapestry.commons.mixins.Element _element;

    private SimpleDateFormat _sdf;

    void pageLoaded()
    {
        _sdf = new SimpleDateFormat();
    }

    boolean beginRender(MarkupWriter writer)
    {
        if (_value == null)
            return false;

        _sdf.applyPattern(_pattern);

        if (_bodyPosition == BODY_POS_NONE || _bodyPosition == BODY_POS_RIGHT)
            writer.write(_sdf.format(_value));

        return _bodyPosition != BODY_POS_NONE;
    }

    void afterRender(MarkupWriter writer)
    {
        if (_value == null)
            return;

        if (_bodyPosition == BODY_POS_LEFT)
            writer.write(_sdf.format(_value));
    }
}
