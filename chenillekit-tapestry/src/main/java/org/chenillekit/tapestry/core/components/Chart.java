/*
 * Apache License
 * Version 2.0, January 2004
 * http://www.apache.org/licenses/
 *
 * Copyright 2008-2010 by chenillekit.org
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 */

package org.chenillekit.tapestry.core.components;

import org.apache.tapestry5.BindingConstants;
import org.apache.tapestry5.ClientElement;
import org.apache.tapestry5.ComponentResources;
import org.apache.tapestry5.MarkupWriter;
import org.apache.tapestry5.annotations.AfterRender;
import org.apache.tapestry5.annotations.BeginRender;
import org.apache.tapestry5.annotations.Environmental;
import org.apache.tapestry5.annotations.Import;
import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.annotations.SetupRender;
import org.apache.tapestry5.annotations.SupportsInformalParameters;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.json.JSONArray;
import org.apache.tapestry5.json.JSONObject;
import org.apache.tapestry5.services.javascript.JavaScriptSupport;
import org.chenillekit.tapestry.core.utils.XYDataItem;

import java.util.List;

/**
 * chart component based on <a href="http://solutoire.com/flotr/">Flotr javascript library</a>.
 *
 * @version $Id$
 */
@SupportsInformalParameters
@Import(library = {"../excanvas.js", "chart/flotr.debug-0.2.0-test.js", "Chart.js"})
public class Chart implements ClientElement
{
	/**
	 * The id used to generate a page-unique client-side identifier for the component. If a component renders multiple
	 * times, a suffix will be appended to the to id to ensure uniqueness.
	 */
	@Parameter(value = "prop:componentResources.id", defaultPrefix = BindingConstants.LITERAL)
	private String clientId;

	/**
	 * the list of data item arrays.
	 */
	@Parameter(name = "dataItems", required = false, defaultPrefix = BindingConstants.PROP)
	private List<List<XYDataItem>> dataItemsList;

	/**
	 * PageRenderSupport to get unique client side id.
	 */
	@Environmental
	private JavaScriptSupport javascriptSupport;

	/**
	 * For blocks, messages, create actionlink, trigger event.
	 */
	@Inject
	private ComponentResources resources;

	private String assignedClientId;

	/**
	 * Tapestry render phase method.
	 * Initialize temporary instance variables here.
	 */
	@SetupRender
	void setupRender()
	{
		assignedClientId = javascriptSupport.allocateClientId(clientId);
	}

	/**
	 * Tapestry render phase method.
	 * Start a tag here, end it in afterRender
	 */
	@BeginRender
	void beginRender(MarkupWriter writer)
	{
		writer.element("div", "id", getClientId());
		resources.renderInformalParameters(writer);
		writer.end();
	}

	/**
	 * Tapestry render phase method. End a tag here.
	 */
	@AfterRender
	void afterRender()
	{
		JSONObject spec = new JSONObject();
		JSONObject config = new JSONObject();
		JSONArray dataArray = null;

		//
		// Let subclasses do more.
		//
		configure(config);

		//
		// do it only if user give us some values
		//
		if (dataItemsList != null && dataItemsList.size() > 0)
		{
			dataArray = new JSONArray();

			for (List<XYDataItem> dataItems : dataItemsList)
			{
				JSONArray data = buildDataValues(dataItems);
				dataArray.put(data);
			}
		}


		//
		// if the user doesn't give us some chart values we add an empty value array.
		//
		if (dataArray != null)
		    spec.put("data", dataArray);
		else if (config.has("data"))
		    spec.put("data", config.get("data"));
		else
		    spec.put("data", new JSONArray(new JSONArray()));

		if (config.has("options"))
		    spec.put("options", config.get("options"));

		spec.put("element", getClientId());

		javascriptSupport.addInitializerCall("ckflotrchart",spec);
	}

	/**
	 * let us build the data value string for Flotr.
	 *
	 * @param dataItems a list of data items
	 *
	 * @return a JSON array containing the data items
	 */
	private static JSONArray buildDataValues(List<XYDataItem> dataItems)
	{
		JSONArray data = new JSONArray();

		for (XYDataItem dataItem : dataItems)
			data.put(new JSONArray(dataItem.getXValue(), dataItem.getYValue()));

		return data;
	}

	/**
	 * Invoked to allow subclasses to further configure the parameters passed to this component's javascript
	 * options. Subclasses may override this method to configure additional features of the Flotr.
	 * <p/>
	 * This implementation does nothing.
	 *
	 * @param config parameters object
	 */
	protected void configure(JSONObject config)
	{
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
