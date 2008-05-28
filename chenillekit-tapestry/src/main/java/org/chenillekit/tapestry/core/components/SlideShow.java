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

import org.apache.tapestry5.ClientElement;
import org.apache.tapestry5.MarkupWriter;
import org.apache.tapestry5.BindingConstants;
import org.apache.tapestry5.RenderSupport;
import org.apache.tapestry5.annotations.Environmental;
import org.apache.tapestry5.annotations.IncludeJavaScriptLibrary;
import org.apache.tapestry5.annotations.IncludeStylesheet;
import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.json.JSONObject;

/**
 * A component for creating a slide show from a generic group of HTML
 * elements on the client side.
 *
 * @author Chris Lewis Nov 8, 2007 <chris@thegodcode.net>
 * @version $Id: SlideShow.java 682 2008-05-20 22:00:02Z homburgs $
 */
@IncludeJavaScriptLibrary("SlideShow.js")
@IncludeStylesheet("SlideShow.css")
public class SlideShow implements ClientElement
{
    /**
     * Sets the amount of time (in seconds) each slide will be displayed.
     */
    @Parameter(value = "2")
    private int interval;

    /**
     * The slide transition object.
     */
    @Parameter(defaultPrefix = BindingConstants.LITERAL, value = "Ck.SlideShow.Tx.Crossfade")
    private String transition;

    // (in development)
    @Parameter(value = "true")
    private boolean controls;

    /**
     * Determines if the slide show will start over after displaying the final slide.
     */
    @Parameter(value = "false")
    private boolean loop;

    /**
     * Determines if the slide show will pause when the mouse hovers over it.
     */
    @Parameter(value = "true")
    private boolean pauseOnHover;

    @Parameter(value = "prop:componentResources.id", defaultPrefix = BindingConstants.LITERAL)
    private String clientId;

    @Environmental
    private RenderSupport renderSupport;

    void beginRender(final MarkupWriter writer)
    {
        JSONObject jsConfig = new JSONObject();
        jsConfig.put("interval", interval);
        jsConfig.put("transition", transition);
        jsConfig.put("controls", controls);
        jsConfig.put("loop", loop);
        jsConfig.put("pauseOnHover", pauseOnHover);
        renderSupport.addScript("new Ck.SlideShow('%s', %s);", getClientId(), jsConfig);
    }

    public String getClientId()
    {
        return clientId;
    }

}