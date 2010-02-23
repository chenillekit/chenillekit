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
import org.apache.tapestry5.Block;
import org.apache.tapestry5.ClientElement;
import org.apache.tapestry5.ComponentResources;
import org.apache.tapestry5.MarkupWriter;
import org.apache.tapestry5.annotations.Component;
import org.apache.tapestry5.annotations.Environmental;
import org.apache.tapestry5.annotations.IncludeJavaScriptLibrary;
import org.apache.tapestry5.annotations.IncludeStylesheet;
import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.corelib.components.EventLink;
import org.apache.tapestry5.corelib.components.Loop;
import org.apache.tapestry5.corelib.components.Zone;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.services.ClientBehaviorSupport;
import org.apache.tapestry5.services.Request;
import org.apache.tapestry5.services.javascript.JavascriptSupport;

/**
 * Simple tab controlled panel component.
 * <p/>
 * This component looks in the container message resource for an entry like <em>label-panelid</em> for inserting as panel title.
 * If key not found the panel id inserted instead.
 *
 * @version $Id$
 */
@IncludeJavaScriptLibrary(value = {"../Chenillekit.js", "TabSet.js"})
@IncludeStylesheet(value = {"TabSet.css"})
public class TabSet implements ClientElement
{
	private static String EVENT_NAME = "clicked";

	@Inject
	private ComponentResources resources;

	/**
	 * The id used to generate a page-unique client-side identifier for the component. If a component renders multiple
	 * times, a suffix will be appended to the to id to ensure uniqueness.
	 */
	@Parameter(value = "prop:componentResources.id", defaultPrefix = BindingConstants.LITERAL)
	private String clientId;

	/**
	 * Name of a function on the client-side Tapestry.ElementEffect object that is invoked after the Zone's content has
	 * been updated. If not specified, then the basic "highlight" method is used, which performs a classic "yellow fade"
	 * to indicate to the user that and update has taken place.
	 */
	@Parameter(defaultPrefix = BindingConstants.LITERAL)
	private String update;

	/**
	 * list of div id's (for each panel).
	 */
	@Parameter(required = true, defaultPrefix = "list")
	private List<String> panelIds;

	/**
	 * set the panel with given id as activated.
	 */
	@Parameter
	private String activePanelId;

	@Environmental
	private JavascriptSupport javascriptSupport;

	@Environmental
	private ClientBehaviorSupport clientBehaviorSupport;

	@Inject
	private Request request;

	@Component(parameters = {"source=inherit:panelIds", "value=panelId"})
	private Loop tabLoop;

	@Component(parameters = {"update=inherit:update"})
	private Zone contentZone;

	@Component(parameters = {"event=clicked", "zone=contentZone", "context=panelId"})
	private EventLink eventLink;

	@Property
	private String panelId;

	private String assignedClientId;

	void setupRender()
	{
		assignedClientId = javascriptSupport.allocateClientId(clientId);

		if (activePanelId == null)
			activePanelId = panelIds.get(0);
	}

	/**
	 * Tapestry render phase method. End a tag here.
	 *
	 * @param writer the markup writer
	 */
	void afterRender(MarkupWriter writer)
	{
		javascriptSupport.addScript("new Ck.TabSet('%s', '%s');", getClientId() + "_panel", activePanelId);
	}


	public String getPanelTitle()
	{
		String panelTitle = panelId;

		if (resources.getContainerResources().getMessages().contains("label-" + panelId))
			panelTitle = resources.getContainerResources().getMessages().get("label-" + panelId);

		return panelTitle;
	}

	/**
	 * activate the named block if page refreshed.
	 *
	 * @return corresponding block or an exception if named block not found.
	 */
	public Block getInitialActiveBlock()
	{
		return resources.getContainer().getComponentResources().getBlock(activePanelId);
	}

	/**
	 * method get the clicked panel id and returns the corresponding block.
	 *
	 * @param panelId the panel id
	 *
	 * @return corresponding block or an exception if named block not found.
	 */
	Object onClicked(String panelId)
	{
		activePanelId = panelId;
		return resources.getContainer().getComponentResources().getBlock(panelId);
	}

	/**
	 * Returns a unique id for the element. This value will be unique for any given rendering of a
	 * page. This value is intended for use as the id attribute of the client-side element, and will
	 * be used with any DHTML/Ajax related JavaScript.
	 */
	public String getClientId()
	{
		return assignedClientId;
	}
}
