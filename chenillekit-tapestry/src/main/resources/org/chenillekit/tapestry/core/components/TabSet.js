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

Ck.TabSet = Class.create();
Ck.TabSet.prototype = {

	initialize: function(tabPanelId, activeId)
	{
		this.tabPanelId = tabPanelId;
		this.activeId = activeId;
		this.panelLinks = $(this.tabPanelId).select('a');

		this.removeActiveStyle();

		this.panelLinks.each(function (e)
		{
			Event.observe(e, 'click', this.__clicked.bind(this))

			if (e.up().id == activeId)
				this.addActiveStyle(e.up());

		}.bind(this));
	},
	removeActiveStyle: function()
	{
		this.panelLinks.each(function (element)
		{
			element.up().removeClassName('active');
		})
	},
	addActiveStyle: function(element)
	{
		element.addClassName('active');
	},
	__clicked:function(event)
	{
		this.removeActiveStyle();

		var clickedLink = Event.element(event)
		clickedLink.up().addClassName('active');
	}
}