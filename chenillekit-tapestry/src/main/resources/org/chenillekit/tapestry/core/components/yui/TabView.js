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

Ck.YuiTabView = Class.create();
Ck.YuiTabView.prototype = {
	initialize: function(clientId, tabs, options)
	{
		this.widget = new YAHOO.widget.TabView(clientId, options);
		$A(tabs).each(function(tabObject)
		{
			var tab = this.widget.getTab(tabObject.id);
			tab.addListener('click', function(e)
			{
				YAHOO.util.Connect.asyncRequest('POST', tabObject.dataSrc, {success:this.successHandler,
					failure:this.failureHandler, argument: e.id});
			}, this);
		}, this);
	},
	failureHandler: function(o)
	{
		alert(o.status + " " + o.statusText);
	},
	successHandler: function(o)
	{
		alert(o);
	}
}
