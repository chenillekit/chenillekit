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

var Button = Class.create();
Button.prototype = {
	initialize:function(elementId, options)
	{
		this.elementId = elementId;
		this.options = options;
		YAHOO.util.Event.onContentReady(elementId, this.createButton.bindAsEventListener(this));
	},
	createButton:function()
	{
		new YAHOO.widget.Button(this.elementId, this.options);
	}
};