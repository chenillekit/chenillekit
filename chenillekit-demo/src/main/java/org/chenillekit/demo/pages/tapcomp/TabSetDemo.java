/*
 *  Apache License
 *  Version 2.0, January 2004
 *  http://www.apache.org/licenses/
 *
 *  Copyright 2008 by chenillekit.org
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 */

package org.chenillekit.demo.pages.tapcomp;

import java.util.ArrayList;
import java.util.List;

import org.apache.tapestry5.StreamResponse;
import org.apache.tapestry5.annotations.Component;
import org.apache.tapestry5.annotations.OnEvent;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.util.TextStreamResponse;

import org.chenillekit.demo.components.LeftSideMenu;
import org.chenillekit.tapestry.core.components.TabSet;

/**
 * demonstrates some simple components.
 *
 * @author <a href="mailto:homburgs@gmail.com">homburgs</a>
 * @version $Id$
 */
public class TabSetDemo
{
    @Persist
    @Property
    private String activePanel;

    @Component(parameters = {"menuName=demo"})
    private LeftSideMenu menu;

    @Property
    private List<String> panelIds;

    @Component(parameters = {"panelIds=prop:panelIds", "activePanelId=activePanel"})
    private TabSet tabSet1;

    /**
     * Tapestry page lifecycle method.
     * Called when the page is instantiated and added to the page pool.
     * Initialize components, and resources that are not request specific.
     */
    void pageLoaded()
    {
        panelIds = new ArrayList<String>();
        panelIds.add("stuff1");
        panelIds.add("stuff2");
        panelIds.add("stuff3");
    }

    @OnEvent(component = "tabset1", value = "action")
    public StreamResponse onChange(String choosenPanelId)
    {
        return new TextStreamResponse("text/html", "<b>Doedel</b>");
    }
}