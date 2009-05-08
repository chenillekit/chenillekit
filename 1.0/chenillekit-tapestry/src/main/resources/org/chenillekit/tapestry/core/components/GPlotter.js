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

Ck.GPlotter = Class.create();
Ck.GPlotter.prototype = {
	initialize: function(elementId, key, errorCallbackFunction, options)
	{
		this.options = Object.extend({
			zoomLevel:		 13,
			smallControl:	  true,
			largeControl:	  false,
			typeControl:	   true,
			label:			 "location"
		}, options || {});

		this.elementId = elementId;
		this.key = key;
		this.map = null;
		this.errorCallbackFunction = errorCallbackFunction;

		if (GBrowserIsCompatible())
		{
			this.map = new GMap2($(elementId));

			if (this.options.smallControl)
				this.map.addControl(new GSmallMapControl());

			if (this.options.typeControl)
				this.map.addControl(new GMapTypeControl());

			if (this.options.largeControl)
				this.map.addControl(new GLargeMapControl());
		}

	},
	setMarker: function(latitude, longitude, street, country, state, zipcode, city)
	{
		var point = new GLatLng(latitude, longitude);
		this.map.setCenter(point, this.options.zoomLevel);

		var marker = new GMarker(point);
		var text = this.__getString(this.options.label, "<b>", "</b>");

		text += this.__getString(street, "<br/>", "");
		text += this.__getString(country, "<br/>", "");
		text += this.__getString(state, " (", ")");
		text += this.__getString(zipcode, "-", "");
		text += this.__getString(city, " ", "");

		GEvent.addListener(marker, "click", function()
		{
			marker.openInfoWindowHtml(text);
		});


		this.map.addOverlay(marker);
	},
	callException: function()
	{
		if (this.errorCallbackFunction.length > 0)
			eval(this.errorCallbackFunction + "('" + this.elementId + "')");
	},
	/**
	 * helper method.
	 */
	__getString:function(stringValue, preString, postString)
	{
		var returnString = "";

		if (stringValue == null)
			stringValue = "";

		if (stringValue != "")
			returnString += preString + stringValue + postString;

		return returnString;
	}
}