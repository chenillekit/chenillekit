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

package org.chenillekit.tapestry.core.components;

import org.apache.tapestry5.BindingConstants;
import org.apache.tapestry5.ClientElement;
import org.apache.tapestry5.ComponentResources;
import org.apache.tapestry5.EventConstants;
import org.apache.tapestry5.MarkupWriter;
import org.apache.tapestry5.annotations.Environmental;
import org.apache.tapestry5.annotations.Import;
import org.apache.tapestry5.annotations.OnEvent;
import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.services.Request;
import org.apache.tapestry5.services.javascript.JavaScriptSupport;

import java.util.Locale;


/**
 * a slider component that dont must emmbedded in a form..
 *
 * @version $Id$
 */
@Import(library = {"${tapestry.scriptaculous}/controls.js", "${tapestry.scriptaculous}/slider.js"},
		stylesheet = {"Slider.css"})
public class Slider implements ClientElement
{
	private final static String handleCSS = "ck_slider-handle";
	private final static String trackCSS = "ck_slider-track";
	private final static String valueCSS = "ck_slider-value";

	/**
	 * The id used to generate a page-unique client-side identifier for the component. If a component renders multiple
	 * times, a suffix will be appended to the to id to ensure uniqueness.
	 */
	@Parameter(value = "prop:componentResources.id", defaultPrefix = BindingConstants.LITERAL)
	private String clientId;

	/**
	 * The value to read or update.
	 */
	@Parameter(required = true)
	private Number value;

	/**
	 * min. slide-able value.
	 */
	@Parameter(value = "0", required = false)
	private Number min;

	/**
	 * max. slide-able value.
	 */
	@Parameter(value = "100", required = false)
	private Number max;

	/**
	 * increments x on every step.
	 */
	@Parameter(value = "1", required = false)
	private Number inc;

	/**
	 * If true, then the field will render out with a disabled attribute (to turn off client-side behavior).
	 * Further, a disabled field ignores any value in the request when the form is submitted.
	 */
	@Parameter(value = "false", required = false)
	private boolean disabled;

	@Inject
	private ComponentResources componentResources;

	@Inject
	private Request request;

	private String handleId;
	private String tackId;
	private String ouputId;

	@Environmental
	private JavaScriptSupport javascriptSupport;

	private String assignedClientId;

	void setupRender()
	{
		assignedClientId = javascriptSupport.allocateClientId(clientId);
	}

	void beginRender(MarkupWriter writer)
	{
		handleId = "handle_" + getClientId();
		tackId = "track_" + getClientId();
		ouputId = "ouput_" + getClientId();

		writer.element("div", "id", tackId,
					   "class", trackCSS);
		writer.element("div", "id", handleId,
					   "class", handleCSS);
	}

	void afterRender(MarkupWriter writer)
	{
		writer.end();
		writer.end();

		writer.element("div", "id", ouputId, "class", valueCSS);

		if (value == null)
			value = 0;

		writer.write(value.toString());

		writer.end();


		String jsCommand = "new Control.Slider('%s','%s',{sliderValue:" + getNumberPattern(value) + ",range:" +
				"$R(" + getNumberPattern(min) + "," + getNumberPattern(max) + "),increment:" + getNumberPattern(inc) +
				",onSlide:function(v){$('%s').innerHTML = v}";
		jsCommand = String.format(Locale.US, jsCommand, handleId, tackId, value, min, max, inc, ouputId);

		if (disabled)
			jsCommand += ",disabled:true";

		jsCommand += ", onChange:function(value){$('%s').innerHTML = value; new Ajax.Request('%s/' + value,{method:'get', onFailure: function(){ alert('%s')}})}});";
		jsCommand = String.format(Locale.US, jsCommand, ouputId, getActionLink(), "Something went wrong...");

		javascriptSupport.addScript(jsCommand);
	}

	@OnEvent(value = "action")
	private void onAction(Number value)
	{
		this.value = value;
	}

	public Number getValue()
	{
		return value;
	}

	public void setValue(Number value)
	{
		this.value = value;
	}

	public String getActionLink()
	{
		return componentResources.createEventLink(EventConstants.ACTION).toURI();
	}

	private String getNumberPattern(Number value)
	{
		String numberPattern = "%d";

		if (value instanceof Float || value instanceof Double)
			numberPattern = "%f";

		return numberPattern;
	}

	/**
	 * Returns a unique id for the element. This value will be unique for any given rendering of a
	 * page. This value is intended for use as the id attribute of the client-side element, and will
	 * be used with any DHTML/Ajax related JavaScript.
	 */
	public String getClientId()
	{
		return assignedClientId;
	}
}