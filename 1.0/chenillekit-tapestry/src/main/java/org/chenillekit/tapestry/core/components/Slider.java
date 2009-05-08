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

import java.util.Locale;

import org.apache.tapestry5.BindingConstants;
import org.apache.tapestry5.ClientElement;
import org.apache.tapestry5.ComponentResources;
import org.apache.tapestry5.EventConstants;
import org.apache.tapestry5.MarkupWriter;
import org.apache.tapestry5.RenderSupport;
import org.apache.tapestry5.annotations.Environmental;
import org.apache.tapestry5.annotations.IncludeJavaScriptLibrary;
import org.apache.tapestry5.annotations.IncludeStylesheet;
import org.apache.tapestry5.annotations.OnEvent;
import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.services.Request;


/**
 * a slider component that dont must emmbedded in a form..
 *
 * @version $Id: Slider.java 682 2008-05-20 22:00:02Z homburgs $
 */
@IncludeJavaScriptLibrary(value = {"${tapestry.scriptaculous}/controls.js", "${tapestry.scriptaculous}/slider.js"})
@IncludeStylesheet(value = {"Slider.css"})
public class Slider implements ClientElement
{
    private final static String _handleCSS = "ck_slider-handle";
    private final static String _trackCSS = "ck_slider-track";
    private final static String _valueCSS = "ck_slider-value";

    /**
     * The id used to generate a page-unique client-side identifier for the component. If a component renders multiple
     * times, a suffix will be appended to the to id to ensure uniqueness.
     */
    @Parameter(value = "prop:componentResources.id", defaultPrefix = BindingConstants.LITERAL)
    private String _clientId;

    /**
     * The value to read or update.
     */
    @Parameter(required = true)
    private Number _value;

    /**
     * min. slide-able value.
     */
    @Parameter(value = "0", required = false)
    private Number _min;

    /**
     * max. slide-able value.
     */
    @Parameter(value = "100", required = false)
    private Number _max;

    /**
     * increments x on every step.
     */
    @Parameter(value = "1", required = false)
    private Number _inc;

    /**
     * If true, then the field will render out with a disabled attribute (to turn off client-side behavior).
     * Further, a disabled field ignores any value in the request when the form is submitted.
     */
    @Parameter(value = "false", required = false)
    private boolean _disabled;

    @Inject
    private ComponentResources _resources;

    @Inject
    private Request _request;

    private String _handleId;
    private String _tackId;
    private String _ouputId;

    @Environmental
    private RenderSupport _pageRenderSupport;

    private String _assignedClientId;

    void setupRender()
    {
        _assignedClientId = _pageRenderSupport.allocateClientId(_clientId);
    }

    void beginRender(MarkupWriter writer)
    {
        _handleId = "handle_" + getClientId();
        _tackId = "track_" + getClientId();
        _ouputId = "ouput_" + getClientId();

        writer.element("div", "id", _tackId,
                       "class", _trackCSS);
        writer.element("div", "id", _handleId,
                       "class", _handleCSS);
    }

    void afterRender(MarkupWriter writer)
    {
        writer.end();
        writer.end();

        writer.element("div", "id", _ouputId, "class", _valueCSS);

        if (_value == null)
            _value = 0;

        writer.write(_value.toString());

        writer.end();


        String jsCommand = "new Control.Slider('%s','%s',{sliderValue:" + getNumberPattern(_value) + ",range:" +
                "$R(" + getNumberPattern(_min) + "," + getNumberPattern(_max) + "),increment:" + getNumberPattern(_inc) +
                ",onSlide:function(v){$('%s').innerHTML = v}";
        jsCommand = String.format(Locale.US, jsCommand, _handleId, _tackId, _value, _min, _max, _inc, _ouputId);

        if (_disabled)
            jsCommand += ",disabled:true";

        jsCommand += ", onChange:function(value){$('%s').innerHTML = value; new Ajax.Request('%s/' + value,{method:'get', onFailure: function(){ alert('%s')}})}});";
        jsCommand = String.format(Locale.US, jsCommand, _ouputId, getActionLink(), "Something went wrong...");

        _pageRenderSupport.addScript(jsCommand);
    }

    @OnEvent(value = "action")
    private void onAction(Number value)
    {
        _value = value;
    }

    public Number getValue()
    {
        return _value;
    }

    public void setValue(Number value)
    {
        _value = value;
    }

    public String getActionLink()
    {
        return _resources.createActionLink(EventConstants.ACTION, false).toURI();
    }

    private String getNumberPattern(Number value)
    {
        String numberPattern = "%d";

        if (value instanceof Float || value instanceof Double)
            numberPattern = "%f";

        return numberPattern;
    }

    /**
     * Returns a unique id for the element. This value will be unique for any given rendering of a
     * page. This value is intended for use as the id attribute of the client-side element, and will
     * be used with any DHTML/Ajax related JavaScript.
     */
    public String getClientId()
    {
        return _assignedClientId;
    }
}