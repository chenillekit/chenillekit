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

Ck.YuiSlider = Class.create();
Ck.YuiSlider.prototype = {
	initialize: function(clientId, type, value, offset, range, ticks, changeCallback)
	{
		this.slider = null;
		this.ticks = ticks;
		this.value = value;
		this.changeCallback = changeCallback;

		if (type == "horiz")
			this.slider = new YAHOO.widget.Slider.getHorizSlider(clientId + 'bg', clientId + 'thumb', offset, range, ticks);
		else if (type == "vert")
			this.slider = new YAHOO.widget.Slider.getVertSlider(clientId + 'bg', clientId + 'thumb', offset, range, ticks);

		this.slider.setValue(value);
		if (this.changeCallback != null)
			eval(this.changeCallback + "(" + value + ")");

		this.slider.subscribe("slideEnd", function()
		{
			// Move the thumb to an increment of 10 pixels
			// (see using ticks for a better option)
			var val = this.slider.getValue();
			if (this.ticks > 0)
				val = val - (val % 10);

			$(clientId + 'Value').value = val;

		}.bind(this));

		if (this.changeCallback != null)
			this.slider.subscribe("change", eval(this.changeCallback));
	}
}
