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
	initialize: function(elementId, key, errorCallbackFunction, options, dragendCallbackFunction)
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
		this.dragendCallbackFunction = dragendCallbackFunction;

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
	setCenter: function(latitude, longitude)
	{
		var point = new GLatLng(latitude, longitude);
		this.map.setCenter(point, this.options.zoomLevel);
	},

	setMarker: function(latitude, longitude, infoText, hiddenLatId, hiddenLngId, draggable)
	{
		var point = new GLatLng(latitude, longitude);

		var marker = new GMarker(point, {draggable: draggable});

		GEvent.addListener(marker, "click", function()
		{
			marker.openInfoWindowHtml(infoText);
		});

		if (draggable) {

			GEvent.addListener(marker, "dragstart", function() {
				this.closeInfoWindow();
			});

			GEvent.addListener(marker, "dragend", function() {
				document.getElementById(hiddenLatId).value = marker.getPoint().lat();
				document.getElementById(hiddenLngId).value = marker.getPoint().lng();
			});

		}

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
	},

	closeInfoWindow: function() {
		this.map.closeInfoWindow();
	}

}
