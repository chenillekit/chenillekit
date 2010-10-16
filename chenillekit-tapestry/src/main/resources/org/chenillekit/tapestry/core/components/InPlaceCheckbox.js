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

Ck.InPlaceCheckbox = Class.create();
Ck.InPlaceCheckbox.prototype = {
	initialize: function(elementId, requestUrl, onCompleteCallback)
	{
		this.element = $(elementId);
		this.requestUrl = requestUrl;
		this.onCompleteCallback = onCompleteCallback;

		Event.observe(this.element, 'click', this._click.bind(this));
	},
	_click: function()
	{
		var successHandler = function(t)
		{
			if (this.onCompleteCallback != undefined)
				this.onCompleteCallback.call(this, t.responseText);
		}.bind(this);

		Tapestry.ajaxRequest(this.requestUrl, {
			onSuccess : successHandler,
			parameters : {checked : this.element.checked}
		});
	}
};

Tapestry.Initializer.ckinplacecheckbox = function(spec)
{
	new Ck.InPlaceCheckbox(spec.elementId, spec.requestUrl, spec.onCompleteCallback);
};
