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
import org.apache.tapestry5.annotations.SetupRender;
import org.apache.tapestry5.corelib.mixins.RenderInformals;

/**
 * Formats a Date object with the given <em>pattern<em>.
 * <p/>
 * The given html tag sourrunds the formatted date value
 * end the optional parameter <em>bodyPosition</em> shifts the
 * tag body to the left or right side, or discarded it.
 *
 * @version $Id$
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
    private Date value;

    /**
     * the format pattern.
     */
    @Parameter(required = false, defaultPrefix = BindingConstants.LITERAL, value = "yyyy-MM-dd")
    private String pattern;

    /**
     * the eelement body position.
     * may be : BODY_POS_NONE (default) / BODY_POS_LEFT / BODY_POS_RIGHT
     */
    @Parameter(required = false, defaultPrefix = BindingConstants.LITERAL, value = "0")
    private int bodyPosition;

    @Mixin
    private RenderInformals renderInformals;

    private SimpleDateFormat sdf;

	/**
	 * Tapestry render phase method.
	 * Initialize temporary instance variables here.
	 */
	@SetupRender
	void setupRender()
	{
		sdf = new SimpleDateFormat();
	}

    boolean beginRender(MarkupWriter writer)
    {
        if (value == null)
            return false;

        sdf.applyPattern(pattern);

        if (bodyPosition == BODY_POS_NONE || bodyPosition == BODY_POS_RIGHT)
            writer.write(sdf.format(value));

        return bodyPosition != BODY_POS_NONE;
    }

    void afterRender(MarkupWriter writer)
    {
        if (value == null)
            return;

        if (bodyPosition == BODY_POS_LEFT)
            writer.write(sdf.format(value));
    }
}
