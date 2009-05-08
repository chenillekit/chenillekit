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

Ck.AutoComplete = Class.create(UI.AutoComplete, {

	initialize: function($super, element, options)
	{
		$super(element, options);
		this.bits = new Hash();
		this.count = 0;

		$(element).observe("autocomplete:element:added", function(evt)
		{
			this.bits.set(evt.memo.value, evt.memo.value);
			$(element + "-internal").value = this.bits.values().join(',');
		}.bind(this));

		$(element).observe("autocomplete:element:removed", function(evt)
		{
			this.bits.unset(evt.memo.element.readAttribute('pui-autocomplete:value'));
			$(element + "-internal").value = this.bits.values().join(',');
		}.bind(this))

		options.preSelected.each(function(object) {this.add(object.text, object.value)}.bind(this))
	}
})
