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

import org.apache.tapestry5.BindingConstants;
import org.apache.tapestry5.ClientElement;
import org.apache.tapestry5.ComponentResources;
import org.apache.tapestry5.Link;
import org.apache.tapestry5.MarkupWriter;
import org.apache.tapestry5.RenderSupport;
import org.apache.tapestry5.annotations.Environmental;
import org.apache.tapestry5.annotations.IncludeJavaScriptLibrary;
import org.apache.tapestry5.annotations.IncludeStylesheet;
import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.ioc.internal.util.InternalUtils;

/**
 * Simple tab controlled panel component.
 * <p/>
 * Sends on every click at the tab an action event to his container, to inform you, wich tab is activated.
 * this component looks in the container message resource for an entry
 * like <em>label-panelid</em> for inserting as panel title.
 * if not found the panel id inserted instead.
 *
 * @author <a href="mailto:homburgs@googlemail.com">S.Homburg</a>
 * @version $Id: TabSet.java 682 2008-05-20 22:00:02Z homburgs $
 */
@IncludeJavaScriptLibrary(value = {"TabSet.js"})
@IncludeStylesheet(value = {"TabSet.css"})
public class TabSet implements ClientElement
{
    @Inject
    private ComponentResources _resources;

    /**
     * The id used to generate a page-unique client-side identifier for the component. If a component renders multiple
     * times, a suffix will be appended to the to id to ensure uniqueness.
     */
    @Parameter(value = "prop:componentResources.id", defaultPrefix = BindingConstants.LITERAL)
    private String _clientId;

    /**
     * list of div id's (for each panel).
     */
    @Parameter(required = true, defaultPrefix = "list")
    private List<String> _panelIds;

    /**
     * set the panel with given id as activated.
     */
    @Parameter
    private String _activePanelId;

    @Environmental
    private RenderSupport _pageRenderSupport;

    private String _assignedClientId;

    void setupRender()
    {
        _assignedClientId = _pageRenderSupport.allocateClientId(_clientId);

        if (_activePanelId == null)
            _activePanelId = _panelIds.get(0);
    }

    void beginRender(MarkupWriter writer)
    {
        writer.element("div", "id", getClientId(), "class", "ck_tab-set");

        for (String blockId : _panelIds)
        {
            Link link = _resources.createEventLink(blockId);

            writer.element("div", "id", "panel_" + blockId,
                           "class", "ck_tab-set-panel" + (_activePanelId.equalsIgnoreCase(blockId) ? " activated" : ""));
            writer.write(getPanelTitle(blockId));
            writer.end();
        }

        writer.element("div", "class", "ck_tab-set-content");
    }

    void afterRender(MarkupWriter writer)
    {
        Link link = _resources.createEventLink("action");
        writer.end(); // TabContent
        writer.end(); // TabGroup
        _pageRenderSupport.addScript("new TabSet('%s', '%s', '%s','%s')", getClientId(),
                                     InternalUtils.join(_panelIds, ","),
                                     _activePanelId, link.toAbsoluteURI());
    }

    private String getPanelTitle(String blockId)
    {
        String panelTitle = blockId;
        if (_resources.getContainerResources().getMessages().contains("label-" + blockId))
            panelTitle = _resources.getContainerResources().getMessages().get("label-" + blockId);

        return panelTitle;
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
