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

package org.chenillekit.tapestry.core.pages;

import java.util.ArrayList;
import java.util.List;

import org.apache.tapestry5.annotations.Component;
import org.apache.tapestry5.annotations.OnEvent;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Property;

import org.chenillekit.tapestry.core.components.TabSet;

/**
 * demonstrates some simple components.
 *
 * @version $Id$
 */
public class TabSetDemo
{
    @Persist
    @Property
    private String activePanel;

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

    @OnEvent(component = "tabset", value = "action")
    public void onChange(String choosenPanelId)
    {
        activePanel = choosenPanelId;
    }
}