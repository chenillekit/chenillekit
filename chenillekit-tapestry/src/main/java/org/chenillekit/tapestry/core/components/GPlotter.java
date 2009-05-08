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
import org.apache.tapestry5.ClientElement;
import org.apache.tapestry5.ComponentResources;
import org.apache.tapestry5.MarkupWriter;
import org.apache.tapestry5.RenderSupport;
import org.apache.tapestry5.annotations.IncludeJavaScriptLibrary;
import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.annotations.SupportsInformalParameters;
import org.apache.tapestry5.dom.Element;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.json.JSONObject;
import org.apache.tapestry5.services.Request;

import org.chenillekit.google.services.GoogleGeoCoder;
import org.chenillekit.google.utils.geocode.GeoCodeResult;


/**
 * Google Map component.
 *
 * @version $Id$
 */
@SupportsInformalParameters
@IncludeJavaScriptLibrary(value = {"../Chenillekit.js", "GPlotter.js"})
public class GPlotter implements ClientElement
{
	/**
	 * For blocks, messages, crete actionlink, trigger event.
	 */
	@Inject
	private ComponentResources resources;

	/**
	 * Request object for information on current request.
	 */
	@Inject
	private Request request;

	/**
	 * RenderSupport to get unique client side id.
	 */
	@Inject
	private RenderSupport renderSupport;

	/**
	 * inject our google map service.
	 */
	@Inject
	private GoogleGeoCoder geoCoder;

	/**
	 * The id used to generate a page-unique client-side identifier for the component. If a component renders multiple
	 * times, a suffix will be appended to the to id to ensure uniqueness.
	 */
	@Parameter(value = "prop:componentResources.id", defaultPrefix = BindingConstants.LITERAL)
	private String clientId;

	/**
	 * google map search argument: street
	 */
	@Parameter(defaultPrefix = BindingConstants.PROP, value = "")
	private String street;

	/**
	 * google map search argument: country
	 */
	@Parameter(defaultPrefix = BindingConstants.PROP, value = "")
	private String country;

	/**
	 * google map search argument: state
	 */
	@Parameter(defaultPrefix = BindingConstants.PROP, value = "")
	private String state;

	/**
	 * google map search argument: zipCode
	 */
	@Parameter(defaultPrefix = BindingConstants.PROP, value = "")
	private String zipCode;

	/**
	 * google map search argument: city
	 */
	@Parameter(defaultPrefix = BindingConstants.PROP, value = "")
	private String city;

	/**
	 * name of a javascript function that acts as error handler.
	 */
	@Parameter(defaultPrefix = BindingConstants.LITERAL, value = "")
	private String errorCallbackFunction;

	private GeoCodeResult geoCodeResult;

	private String assignedClientId;

	void setupRender()
	{
		assignedClientId = renderSupport.allocateClientId(clientId);
		geoCodeResult = geoCoder.getGeoCode(request.getLocale(), street, country, state, zipCode, city);
	}

	public String getPlotterId()
	{
		return getClientId();
	}

	/**
	 * Tapestry render phase method.
	 * Start a tag here, end it in afterRender
	 *
	 * @param writer the markup writer
	 */
	void beginRender(MarkupWriter writer)
	{
		Element root = writer.getDocument().getRootElement();
		Element head = root.find("head");

		head.element("script",
					 "src", "http://maps.google.com/maps?file=api&v=2&key=" + geoCoder.getKey() + "&hl=" +
				request.getLocale().getLanguage(),
					 "type", "text/javascript",
					 "id", "gmap");

		writer.element("div", "id", getClientId() + "_map");
		resources.renderInformalParameters(writer);
		writer.end();
	}

	/**
	 * Tapestry render phase method. End a tag here.
	 */
	void afterRender()
	{
		JSONObject configuration = new JSONObject();

		configuration.put("zoomLevel", 13);
		configuration.put("smallControl", true);
		configuration.put("largeControl", false);
		configuration.put("typeControl", true);
		configuration.put("label", "location");

		configure(configuration);

		renderSupport.addScript("var %s = new Ck.GPlotter('%s_map', '%s', '%s', %s);",
								getClientId(), getClientId(),
								geoCoder.getKey(),
								errorCallbackFunction,
								configuration.toString());

		if (geoCodeResult.getPlacemarks().size() > 0)
			renderSupport.addScript("%s.setMarker('%s', '%s', '%s', '%s', '%s', '%s', '%s');",
									getClientId(),
									geoCodeResult.getPlacemarks().get(0).getLatLng().getLatitude(),
									geoCodeResult.getPlacemarks().get(0).getLatLng().getLongitude(),
									street, country, state, zipCode, city);
		else
			renderSupport.addScript("%s.callException();", getClientId());
	}

	/**
	 * for external configuration do override.
	 *
	 * @param jsonObject config object
	 */
	protected void configure(JSONObject jsonObject)
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