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

package org.chenillekit.demo.pages.tapcomp;

import java.util.ArrayList;
import java.util.List;

import org.apache.tapestry5.ComponentResources;
import org.apache.tapestry5.annotations.Component;
import org.apache.tapestry5.annotations.InjectComponent;
import org.apache.tapestry5.annotations.Mixins;
import org.apache.tapestry5.annotations.OnEvent;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.annotations.Retain;
import org.apache.tapestry5.corelib.components.ActionLink;
import org.apache.tapestry5.corelib.components.EventLink;
import org.apache.tapestry5.corelib.components.Zone;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.services.Request;

import org.chenillekit.demo.components.LeftSideMenu;
import org.chenillekit.tapestry.core.components.yui.Slider;
import org.chenillekit.tapestry.core.components.yui.StateButton;
import org.chenillekit.tapestry.core.components.yui.Tab;
import org.chenillekit.tapestry.core.components.yui.TabView;

/**
 * @version $Id$
 */
public class YuiComponentsDemo
{
	@Retain
	@Property
	private boolean stateButtonValue;

	@Retain
	@Property
	private int sliderValue;

	@Retain
	@Property
	private List<Tab> tabs;

	@Component(parameters = {"menuName=demo"})
	private LeftSideMenu menu;

	@Component(parameters = {"event=doNothing"})
	@Mixins("ck/yui/Button")
	private EventLink eventLink;

	@Component(parameters = {"value=stateButtonValue"})
	private StateButton stateButton;

	@Component(parameters = {"value=sliderValue", "changeCallback=sliderChangeCallback"})
	private Slider yuiSlider;

	@Component(parameters = {"orientation=top", "activeIndex=1"})
	private TabView yuiTabView1;

	@Component(parameters = {"orientation=top", "activeIndex=1", "tabs=tabs"})
	private TabView yuiTabView2;

	@Component(parameters = {"zone=counterZone"})
	@Mixins("ck/yui/Button")
	private ActionLink clicker;

	@Inject
	private ComponentResources resources;

	@Property
	@Persist
	private int clickCount;

	@InjectComponent
	private Zone counterZone;

	Object onActionFromClicker()
	{
		clickCount++;

		return counterZone.getBody();
	}

	/**
	 * Request object for information on current request.
	 */
	@Inject
	private Request request;

	/**
	 * Tapestry page lifecycle method.
	 * Called when the page is instantiated and added to the page pool.
	 * Initialize components, and resources that are not request specific.
	 */
	void pageLoaded()
	{
		tabs = new ArrayList<Tab>();
		tabs.add(new Tab("Tab1"));
		tabs.add(new Tab("Tab2", "Blabla2"));
		tabs.add(new Tab("Tab3", "Blabla3"));
	}

	@OnEvent(value = "doNothing")
	public void doNothing()
	{

	}

	@OnEvent(component = "yuiTabView2", value = "tabClicked")
	public Object doTabClicked(String tabId)
	{
		return resources.getBlock("tab" + tabId);
	}
}