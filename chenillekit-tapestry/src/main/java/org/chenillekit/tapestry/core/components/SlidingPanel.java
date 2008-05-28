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
import org.apache.tapestry5.ComponentResources;
import org.apache.tapestry5.MarkupWriter;
import org.apache.tapestry5.BindingConstants;
import org.apache.tapestry5.RenderSupport;
import org.apache.tapestry5.annotations.AfterRender;
import org.apache.tapestry5.annotations.BeginRender;
import org.apache.tapestry5.annotations.Environmental;
import org.apache.tapestry5.annotations.IncludeJavaScriptLibrary;
import org.apache.tapestry5.annotations.IncludeStylesheet;
import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.annotations.SupportsInformalParameters;
import org.apache.tapestry5.ioc.annotations.Inject;

/**
 * diplays a panel that can open/close its content.
 *
 * @author <a href="mailto:homburgs@googlemail.com">S.Homburg</a>
 * @version $Id: SlidingPanel.java 682 2008-05-20 22:00:02Z homburgs $
 */
@SupportsInformalParameters
@IncludeJavaScriptLibrary(value = {"../Chenillekit.js", "SlidingPanel.js"})
@IncludeStylesheet(value = {"SlidingPanel.css"})
public class SlidingPanel implements ClientElement
{
    /**
     * The id used to generate a page-unique client-side identifier for the component. If a component renders multiple
     * times, a suffix will be appended to the to id to ensure uniqueness.
     */
    @Parameter(value = "prop:componentResources.id", defaultPrefix = BindingConstants.LITERAL)
    private String _clientId;

    /**
     * the panel subject.
     */
    @Parameter(value = "", required = false)
    private String _subject;

    /**
     * show the panel initialy closed or open.
     */
    @Parameter(value = "false", required = false)
    private boolean _closed;

    @Inject
    private ComponentResources _resources;

    @Environmental
    private RenderSupport _pageRenderSupport;

    private String _assignedClientId;

    void setupRender()
    {
        _assignedClientId = _pageRenderSupport.allocateClientId(_clientId);
    }

    @BeginRender
    void doBeginRender(MarkupWriter writer)
    {
        writer.element("div", "id", getClientId(), "class", "ck_slidingpanel");
        _resources.renderInformalParameters(writer);

        writer.element("div", "id", getClientId() + "_subject", "class", "ck_slidingPanelSubject");
        writer.element("div", "id", getClientId() + "_toggler", "class", "ck_slidingPanelSubject-toggler");
        writer.end(); // Tag 'toggler'
        writer.write(_subject);
        writer.end(); // Tag 'div.subject'

        writer.element("div", "id", getClientId() + "_content", "class", "ck_slidingPanelContent", "style", "display: none;");
        writer.element("span");
    }

    @AfterRender
    void doAfterRender(MarkupWriter writer)
    {
        writer.end(); // Tag 'div.outer_panel'
        writer.end(); // Tag 'div.outer_panel'
        writer.end(); // main div
        _pageRenderSupport.addScript("new Ck.SlidingPanel('%s', %s);", getClientId(), _closed);
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
