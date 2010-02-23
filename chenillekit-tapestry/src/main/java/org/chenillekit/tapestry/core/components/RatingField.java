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

package org.chenillekit.tapestry.core.components;

import java.util.List;

import org.apache.tapestry5.Asset;
import org.apache.tapestry5.ComponentResources;
import org.apache.tapestry5.MarkupWriter;
import org.apache.tapestry5.ValueEncoder;
import org.apache.tapestry5.annotations.Component;
import org.apache.tapestry5.annotations.Environmental;
import org.apache.tapestry5.annotations.IncludeJavaScriptLibrary;
import org.apache.tapestry5.annotations.IncludeStylesheet;
import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.annotations.Path;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.corelib.base.AbstractField;
import org.apache.tapestry5.corelib.components.Label;
import org.apache.tapestry5.corelib.components.Loop;
import org.apache.tapestry5.corelib.components.Radio;
import org.apache.tapestry5.corelib.components.RadioGroup;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.ioc.services.PropertyAccess;
import org.apache.tapestry5.json.JSONObject;
import org.apache.tapestry5.services.Environment;
import org.apache.tapestry5.services.javascript.JavascriptSupport;

import org.chenillekit.tapestry.core.internal.GenericValueEncoder;


/**
 * This component provides the ability to associate a RadioGroup and its
 * subordinate Radio fields with a set of values displayed as a rating scale.
 * This is typified by the "star field" where grayed stars represent the choices
 * and highlighted stars represent the chosen value and all values up to the
 * chosen value from left to right.
 * <p/>
 * This is in fact that default visual appearance. However, the images can be
 * overridden via parameters and the entire component can, of course, be styled
 * via CSS.
 * <p/>
 * As an added benefit, since the underlying representation is simply a
 * RadioGroup with Radio fields it should degrade well when JS and/or CSS is
 * disabled. This should keep the component rather accessible.
 * <p/>
 * By default the first value display image will be hidden as this typically
 * will indicate no value.
 *
 * @version $Id$
 */
@IncludeJavaScriptLibrary(value = {"../Chenillekit.js", "Rating.js"})
@IncludeStylesheet(value = {"Rating.css"})
public class RatingField<T> extends AbstractField
{
	/**
	 * The value to read or update.
	 */
	@Parameter(required = true)
	@Property
	private T value;

	/**
	 * the rateable value list.
	 */
	@Parameter(required = true)
	@Property
	private List<T> source;

	/**
	 * Encoder used to translate between server-side objects
	 * and client-side strings.
	 */
	@Parameter
	private ValueEncoder encoder;

	/**
	 * the optional Selected-Image
	 */
	@Parameter(required = false)
	private Asset selectedImage;

	/**
	 * the optional UnSelected-Image
	 */
	@Parameter(required = false)
	private Asset unselectedImage;

	@Inject
	@Path("rating_default_selected.gif")
	private Asset defaultSelectedImage;

	@Inject
	@Path("rating_default_unselected.gif")
	private Asset defaultUnselectedImage;

	@Inject
	private Environment environment;

	@Environmental
	private JavascriptSupport javascriptSupport;

	@Inject
	private ComponentResources componentResources;

	@Inject
	private PropertyAccess propertyAccess;

	@Component(parameters = {"value=prop:value", "encoder=encoder"})
	private RadioGroup radioGroup;

	@Component(parameters = {"source=prop:source", "value=loopValue"})
	private Loop loop;

	@Property
	private T loopValue;

	@Component(parameters = {"value=loopValue", "label=prop:radioLabel"})
	private Radio radio;

	@Component(parameters = {"for=radio"})
	private Label label;

	/**
	 * Returns the image representing an unselected value.
	 *
	 * @return
	 */
	public Asset getUnselectedImage()
	{
		return (unselectedImage == null) ? defaultUnselectedImage : unselectedImage;
	}

	/**
	 * Returns the image representing a selected value.
	 *
	 * @return
	 */
	public Asset getSelectedImage()
	{
		return selectedImage == null ? defaultSelectedImage : selectedImage;
	}

	/**
	 * Returns an appropriate ValueEncoder implementation based on the value
	 * type.
	 *
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public ValueEncoder getEncoder()
	{
		if (encoder == null)
			encoder = new GenericValueEncoder(source);

		return encoder;
	}

	/**
	 * Returns a reasonable label for the radio value. If the value is primitive
	 * it will be returned as is. Otherwise the toString() method will be called
	 * on the value object.
	 *
	 * @return
	 */
	public String getRadioLabel()
	{
		return loopValue.toString();
	}

	/**
	 * Method implemented by subclasses to actually do the work of processing the submission of the form. The element's
	 * elementName property will already have been set. This method is only invoked if the field is <strong>not {@link
	 * #isDisabled() disabled}</strong>.
	 *
	 * @param elementName the name of the element (used to find the correct parameter in the request)
	 */
	protected void processSubmission(String elementName)
	{
	}

	public void afterRender(MarkupWriter writer)
	{
		JSONObject options = new JSONObject();

		options.put("disabled", isDisabled());

		//
		// Let subclasses do more.
		//
		configure(options);

		javascriptSupport.addScript("new Ck.RatingField('%s', '%s', '%s', %s);",
								getClientId(),
								getSelectedImage().toClientURL(),
								getUnselectedImage().toClientURL(),
								options);
	}

	/**
	 * Invoked to allow subclasses to further configure the parameters passed to this component's javascript
	 * options. Subclasses may override this method to configure additional features of the Window.
	 * <p/>
	 * This implementation does nothing.
	 *
	 * @param options windows option object
	 */
	protected void configure(JSONObject options)
	{
	}
}
