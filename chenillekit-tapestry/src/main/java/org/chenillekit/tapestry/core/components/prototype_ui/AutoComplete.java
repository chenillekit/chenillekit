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

package org.chenillekit.tapestry.core.components.prototype_ui;

import java.util.Collections;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.tapestry5.BindingConstants;
import org.apache.tapestry5.ComponentEventCallback;
import org.apache.tapestry5.ComponentResources;
import org.apache.tapestry5.FieldTranslator;
import org.apache.tapestry5.MarkupWriter;
import org.apache.tapestry5.ValidationException;
import org.apache.tapestry5.ValidationTracker;
import org.apache.tapestry5.annotations.Environmental;
import org.apache.tapestry5.annotations.IncludeJavaScriptLibrary;
import org.apache.tapestry5.annotations.IncludeStylesheet;
import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.corelib.base.AbstractField;
import org.apache.tapestry5.internal.util.Holder;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.ioc.services.PropertyAccess;
import org.apache.tapestry5.ioc.services.TypeCoercer;
import org.apache.tapestry5.json.JSONArray;
import org.apache.tapestry5.json.JSONObject;
import org.apache.tapestry5.services.Request;
import org.apache.tapestry5.services.ResponseRenderer;
import org.apache.tapestry5.services.javascript.JavascriptSupport;

import static org.apache.tapestry5.ioc.internal.util.CollectionFactory.newList;

/**
 * This AutoComplete component based on <a href="http://www.prototype-ui.com/">Prototype-UI's</a>
 * <a href="http://blog.xilinus.com/2008/2/22/new-component-auto_complete-in-prototype-ui">autocomplete</a> widget.
 *
 * @version $Id$
 */
@IncludeJavaScriptLibrary(value = {"../../Chenillekit.js", "prototype-ui.js", "AutoComplete.js"})
@IncludeStylesheet(value = {"themes/auto_complete/default.css", "themes/shadow/drop_shadow.css",
		"themes/shadow/auto_complete.css"})
public class AutoComplete extends AbstractField
{
	static final String EVENT_NAME = "autocomplete";

	private static final String PARAM_NAME = "search";

	/**
	 * The value to read or update.
	 */
	@Parameter(required = true, allowNull = false)
	private List<Object> selected;

	/**
	 * The object which will perform translation between server-side and client-side representations. If not specified,
	 * a value will usually be generated based on the type of the value parameter.
	 */
	@Parameter(required = true, allowNull = false, defaultPrefix = BindingConstants.TRANSLATE)
	private FieldTranslator<Object> translate;

	/**
	 * this parameter contains the name of the object property, that should display to user in the item list and the
	 * box of selected items.
	 */
	@Parameter(required = true, allowNull = false, defaultPrefix = BindingConstants.LITERAL, name = "label")
	private String labelPropertyName;

	@Inject
	private Request request;

	@Inject
	private ResponseRenderer responseRenderer;

	@Inject
	private TypeCoercer coercer;

	@Inject
	private ComponentResources resources;

	@Environmental
	private JavascriptSupport javascriptSupport;

	@Inject
	private PropertyAccess propertyAccess;

	@Environmental
	private ValidationTracker tracker;

	void beginRender(MarkupWriter writer)
	{
		writer.element("input",
					   "type", "hidden",
					   "name", getControlName(),
					   "id", getClientId() + "-internal");
		writer.end();

		writer.element("input",
					   "type", "text",
					   "id", getClientId());

	}

	void afterRender(MarkupWriter writer)
	{
		writer.end();

		JSONObject config = new JSONObject();
		config.put("url", resources.createEventLink(EVENT_NAME).toAbsoluteURI());
		config.put("preSelected", generateResponseMarkup(selected));

		configure(config);

		javascriptSupport.addScript("new Ck.AutoComplete('%s', %s);", getClientId(), config);
	}

	/**
	 * Invoked to allow subclasses to further configure the parameters passed to this component's javascript
	 * options. Subclasses may override this method to configure additional features of this component.
	 * <p/>
	 * This implementation does nothing. For more information about window options look at
	 * this <a href="http://prototype-window.xilinus.com/documentation.html#initialize">page</a>.
	 *
	 * @param options windows option object
	 */
	protected void configure(JSONObject options)
	{

	}

	JSONArray onAutocomplete()
	{
		String input = request.getParameter(PARAM_NAME);

		final Holder<List> matchesHolder = Holder.create();

		// Default it to an empty list.

		matchesHolder.put(Collections.emptyList());

		ComponentEventCallback callback = new ComponentEventCallback()
		{
			public boolean handleResult(Object result)
			{
				List matches = coercer.coerce(result, List.class);

				matchesHolder.put(matches);

				return true;
			}
		};

		resources.triggerEvent("providecompletions", new Object[]{input}, callback);

		return generateResponseMarkup(matchesHolder.get());
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
		String parameterValue = request.getParameter(elementName);
		String[] values = parameterValue.split(",");

		// Use a couple of local variables to cut down on access via bindings
		List<Object> selected = this.selected;

		if (selected == null) selected = newList();
		else selected.clear();

		int count = values.length;
		try
		{
			for (int i = 0; i < count; i++)
			{
				String value = StringUtils.trim(values[i]);

				Object objectValue = translate.parse(value);

				if (objectValue != null)
					selected.add(objectValue);
			}
		}
		catch (ValidationException ex)
		{
			tracker.recordError(this, ex.getMessage());
		}

		this.selected = selected;
	}

	/**
	 * Generates the markup response that will be returned to the client; this should be an &lt;ul&gt; element with
	 * nested &lt;li&gt; elements. Subclasses may override this to produce more involved markup (including images and
	 * CSS class attributes).
	 *
	 * @param matches list of matching objects, each should be converted to a string
	 */
	protected JSONArray generateResponseMarkup(List matches)
	{
		JSONArray jsonObject = new JSONArray();
		for (Object o : matches)
		{
			Object value = translate.toClient(o);
			Object label = propertyAccess.get(o, labelPropertyName);
			JSONObject item = new JSONObject();
			item.put("text", label);
			item.put("value", value);
			jsonObject.put(item);
		}

		return jsonObject;
	}
}
