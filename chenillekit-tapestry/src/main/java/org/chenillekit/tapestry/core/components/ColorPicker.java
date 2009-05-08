/*
 * Apache License
 * Version 2.0, January 2004
 * http://www.apache.org/licenses/
 *
 * Copyright 2009 by chenillekit.org
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 */

package org.chenillekit.tapestry.core.components;

import org.apache.tapestry5.BindingConstants;
import org.apache.tapestry5.MarkupWriter;
import org.apache.tapestry5.RenderSupport;
import org.apache.tapestry5.annotations.IncludeJavaScriptLibrary;
import org.apache.tapestry5.annotations.IncludeStylesheet;
import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.corelib.base.AbstractTextField;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.json.JSONObject;

/**
 * A simple color picker component. Supports saving custom colors to the palette via cookies. To open the palette,
 * click the color box at the right side of the input box.
 *
 * @version $Id$
 */
@IncludeStylesheet("colorpicker/colorpicker.css")
@IncludeJavaScriptLibrary({"colorpicker/colorpicker.js", "../prototype-base-extensions.js"})
public class ColorPicker extends AbstractTextField
{
	/**
	 * The value parameter of a ColorPicker must be a {@link java.lang.String}.
	 */
	@Parameter(required = true, principal = true)
	private String value;

	/**
	 * an array of 40 colors (in hex format) to display in the swatch grid
	 */
	@Parameter(required = false, defaultPrefix = BindingConstants.PROP)
	private String[] colors;

	/**
	 * the text to display in the "add custom color" button, defaults to "Add"
	 */
	@Parameter(required = false, defaultPrefix = BindingConstants.LITERAL)
	private String label;

	/**
	 * RenderSupport to get unique client side id
	 */
	@Inject
	private RenderSupport renderSupport;


	/**
	 * Invoked from {@link #begin(org.apache.tapestry5.MarkupWriter)} to write out the element and attributes (typically, &lt;input&gt;). The
	 * {@linkplain org.apache.tapestry5.corelib.base.AbstractField#getControlName() controlName} and {@linkplain org.apache.tapestry5.corelib.base.AbstractField#getClientId() clientId}
	 * properties will already have been set or updated.
	 * <p/>
	 * Generally, the subclass will invoke {@link org.apache.tapestry5.MarkupWriter#element(String, Object[])}, and will be responsible for
	 * including an {@link org.apache.tapestry5.annotations.AfterRender} phase method to invoke {@link org.apache.tapestry5.MarkupWriter#end()}.
	 *
	 * @param writer markup write to send output to
	 * @param value  the value (either obtained and translated from the value parameter, or obtained from the tracker)
	 */
	protected void writeFieldTag(MarkupWriter writer, String value)
	{
		String clientId = getClientId();

		writer.element("input",

					   "type", "text",

					   "class", "colorpicker",

					   "name", getControlName(),

					   "id", clientId,

					   "value", value);
	}

	/**
	 * Tapestry render phase method. End a tag here.
	 */
	void afterRender(MarkupWriter writer)
	{
		writer.end(); // input

		JSONObject setup = new JSONObject();

		if (colors != null)
			setup.put("colors", colors);
		if (label != null)
			setup.put("addLabel", label);

		renderSupport.addScript("new Control.ColorPicker('%s', %s);", getClientId(), setup);
	}
}