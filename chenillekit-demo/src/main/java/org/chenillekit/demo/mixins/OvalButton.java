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

package org.chenillekit.demo.mixins;

import org.apache.tapestry5.BindingConstants;
import org.apache.tapestry5.ComponentResources;
import org.apache.tapestry5.MarkupWriter;
import org.apache.tapestry5.RenderSupport;
import org.apache.tapestry5.annotations.Environmental;
import org.apache.tapestry5.annotations.Import;
import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.ioc.annotations.Inject;

/**
 * @version $Id$
 */
@Import(stylesheet = {"../assets/styles/OvalButton.css"})
public class OvalButton
{
	// <a href="#" style="margin-left: 6px" class="squarebutton orange" t:id="linkToStart"><span>Home</span></a>

	/**
	 * For blocks, messages, crete actionlink, trigger event.
	 */
	@Inject
	private ComponentResources resources;

	/**
	 * RenderSupport to get unique client side id.
	 */
	@Environmental
	private RenderSupport renderSupport;

	/**
	 * set the link text.
	 */
	@Parameter(defaultPrefix = BindingConstants.LITERAL, required = false)
	private String minWidth;

	/**
	 * set the left margin.
	 * default is 6px
	 */
	@Parameter(defaultPrefix = BindingConstants.LITERAL, value = "6px", required = false)
	private String marginLeft;

	/**
	 * set the used color.
	 * default is "blue"
	 */
	@Parameter(defaultPrefix = BindingConstants.LITERAL, required = false)
	private String color;

	/**
	 * Tapestry render phase method.
	 * Called before component body is rendered.
	 *
	 * @param writer the markup writer
	 */
	void beforeRenderBody(MarkupWriter writer)
	{
		writer.getElement().attribute("class", "ovalbutton " + (color != null ? color : ""));
		writer.getElement().attribute("style", "text-align:center; margin-left: " + marginLeft + ";" +
				(minWidth != null ? "min-width:" + minWidth + ";" : ""));

		writer.element("span");
	}

	/**
	 * Tapestry render phase method. End a tag here.
	 *
	 * @param writer the markup writer
	 */
	void afterRender(MarkupWriter writer)
	{
		writer.end();
	}

}
