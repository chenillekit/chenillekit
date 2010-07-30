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

package org.chenillekit.demo.components;

import org.apache.tapestry5.MarkupWriter;
import org.apache.tapestry5.annotations.AfterRender;
import org.apache.tapestry5.annotations.BeginRender;
import org.apache.tapestry5.annotations.Import;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.services.javascript.JavaScriptSupport;
import org.chenillekit.tapestry.core.base.AbstractYahooComponent;

/**
 * @author <a href="mailto:homburgs@googlemail.com">sven</a>
 * @version $Id$
 */
@Import(stack = {"yahoo"}, stylesheet = {"${yahoo.yui}/assets/skins/sam/tabview.css"},
		library = {"${yahoo.yui}/tabview/tabview${yahoo.yui.mode}.js"})
public class YuiTabSet extends AbstractYahooComponent
{
	/**
	 * RenderSupport to get unique client side id.
	 */
	@Inject
	private JavaScriptSupport javascriptSupport;

	/**
	 * Tapestry render phase method.
	 * Start a tag here, end it in afterRender
	 *
	 * @param writer the markup writer
	 */
	@BeginRender
	void beginRender(MarkupWriter writer)
	{
		writer.element("div", "id", getClientId(), "class", "yui-navset");
	}


	/**
	 * Tapestry render phase method. End a tag here.
	 *
	 * @param writer the markup writer
	 */
	@AfterRender
	void afterRender(MarkupWriter writer)
	{
		writer.end();
		javascriptSupport.addScript("new YAHOO.widget.TabView('%s');", getClientId());
	}
}
