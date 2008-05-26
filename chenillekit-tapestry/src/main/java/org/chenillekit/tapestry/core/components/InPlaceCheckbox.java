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

package org.chenillekit.tapestry.core.components;

import java.util.List;

import org.apache.tapestry5.Binding;
import org.apache.tapestry5.BindingConstants;
import org.apache.tapestry5.ComponentResources;
import org.apache.tapestry5.EventConstants;
import org.apache.tapestry5.Link;
import org.apache.tapestry5.MarkupWriter;
import org.apache.tapestry5.RenderSupport;
import org.apache.tapestry5.annotations.IncludeJavaScriptLibrary;
import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.json.JSONObject;
import org.apache.tapestry5.services.Request;

import org.chenillekit.tapestry.core.base.AbstractAjaxField;

/**
 * a "just in place" checkbox component that dont must emmbedded in a form.
 * sends an event after click named "checkboxclicked".
 *
 * @author <a href="mailto:homburgs@gmail.com">S.Homburg</a>
 * @version $Id$
 */
@IncludeJavaScriptLibrary(value = {"../Chenillekit.js", "InPlaceCheckbox.js"})
public class InPlaceCheckbox extends AbstractAjaxField
{
    public static final String EVENT_NAME = "checkboxclicked";

    @Inject
    private Request request;

    /**
     * The value to read or update.
     */
    @Parameter(required = true)
    private boolean value;

    /**
     * the javascript callback function (optional).
     * function has one parameter: the response text
     */
    @Parameter(required = false, defaultPrefix = BindingConstants.LITERAL)
    private String onCompleteCallback;

    /**
     * The context for the link (optional parameter). This list of values will be converted into strings and included in
     * the URI.
     */
    @Parameter(required = false)
    private List context;

    /**
     * For blocks, messages, crete actionlink, trigger event.
     */
    @Inject
    private ComponentResources resources;


    /**
     * RenderSupport to get unique client side id.
     */
    @Inject
    private RenderSupport renderSupport;


    private Object[] contextArray;

    Binding defaultValue()
    {
        return createDefaultParameterBinding("value");
    }

    void setupRender()
    {
        contextArray = context == null ? new Object[0] : context.toArray();
    }

    /**
     * Tapestry render phase method.
     * Start a tag here, end it in afterRender
     *
     * @param writer the markup writer
     */
    void beginRender(MarkupWriter writer)
    {
        writer.element("input",
                       "type", "checkbox",
                       "name", getControlName(),
                       "id", getClientId(),
                       "checked", value ? "checked" : null);

        resources.renderInformalParameters(writer);
    }

    /**
     * Tapestry render phase method. End a tag here.
     *
     * @param writer the markup writer
     */
    void afterRender(MarkupWriter writer)
    {
        writer.end(); // input

        String ajaxString = "new Ck.InPlaceCheckbox('%s', '%s'";

        if (onCompleteCallback != null)
            ajaxString += ",'" + onCompleteCallback + "'";

        ajaxString += ");";

        renderSupport.addScript(ajaxString, getClientId(), getActionLink());
    }

    public String getActionLink()
    {
        Link link = resources.createActionLink(EventConstants.ACTION, false, contextArray);
        return link.toAbsoluteURI();
    }

    JSONObject onAction(Object[] values)
    {
        resources.triggerEvent(EVENT_NAME, values, null);
        return new JSONObject().put("value", values[values.length - 1]);
    }
}