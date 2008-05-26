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

import org.apache.tapestry5.BindingConstants;
import org.apache.tapestry5.ComponentResources;
import org.apache.tapestry5.MarkupWriter;
import org.apache.tapestry5.RenderSupport;
import org.apache.tapestry5.annotations.AfterRender;
import org.apache.tapestry5.annotations.BeginRender;
import org.apache.tapestry5.annotations.Environmental;
import org.apache.tapestry5.annotations.IncludeJavaScriptLibrary;
import org.apache.tapestry5.annotations.IncludeStylesheet;
import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.dom.Element;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.services.Environment;

/**
 * shows an tooltip if mouse slides over the declared content.
 *
 * @author <a href="mailto:homburgs@googlemail.com">S.Homburg</a>
 * @version $Id: Tooltip.java 682 2008-05-20 22:00:02Z homburgs $
 */
@IncludeJavaScriptLibrary(value = {"Tooltip.js"})
@IncludeStylesheet(value = {"Tooltip.css"})
public class Tooltip
{
    /**
     * The id used to generate a page-unique client-side identifier for the component. If a component renders multiple
     * times, a suffix will be appended to the to id to ensure uniqueness.
     */
    @Parameter(value = "prop:componentResources.id", defaultPrefix = BindingConstants.LITERAL)
    private String _clientId;

    /**
     * the tooltip title.
     */
    @Parameter(value = "", required = false, defaultPrefix = "literal")
    private String _title;

    /**
     * the tooltip content.
     */
    @Parameter(value = "", required = false, defaultPrefix = "literal")
    private String _value;

    /**
     * the tooltip effect ("blind", "appear", "slide").
     */
    @Parameter(required = false, defaultPrefix = "literal")
    private String _effect;

    @Inject
    private ComponentResources _resources;

    @Environmental
    private RenderSupport _pageRenderSupport;

    @Inject
    private Environment _environment;

    private String _assignedClientId;

    void setupRender()
    {
        _assignedClientId = _pageRenderSupport.allocateClientId(_clientId);
    }

    @BeginRender
    void doBeginRender(MarkupWriter writer)
    {
        Element element = writer.element("div");
        element.attribute("id", _assignedClientId);
    }

    @AfterRender
    void doAfterRender(MarkupWriter writer)
    {
        writer.end();

        String jsCommand = "new Ck.Tip('%s', '%s'";
        jsCommand += ", {className: 'ck_tooltip'";

        if (_title != null)
            jsCommand += ", title: '" + _title + "'";

        if (_effect != null)
            jsCommand += ", effect: '" + _effect + "'";

        jsCommand += "});";
        _pageRenderSupport.addScript(jsCommand, _assignedClientId, _value);
    }
}
