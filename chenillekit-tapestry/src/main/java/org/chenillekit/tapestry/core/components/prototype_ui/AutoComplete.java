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

import org.apache.tapestry5.Binding;
import org.apache.tapestry5.BindingConstants;
import org.apache.tapestry5.ComponentEventCallback;
import org.apache.tapestry5.ComponentResources;
import org.apache.tapestry5.FieldTranslator;
import org.apache.tapestry5.FieldValidationSupport;
import org.apache.tapestry5.FieldValidator;
import org.apache.tapestry5.MarkupWriter;
import org.apache.tapestry5.NullFieldStrategy;
import org.apache.tapestry5.RenderSupport;
import org.apache.tapestry5.ValidationException;
import org.apache.tapestry5.ValidationTracker;
import org.apache.tapestry5.annotations.Environmental;
import org.apache.tapestry5.annotations.IncludeJavaScriptLibrary;
import org.apache.tapestry5.annotations.IncludeStylesheet;
import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.corelib.base.AbstractField;
import org.apache.tapestry5.internal.services.LinkFactory;
import org.apache.tapestry5.internal.util.Holder;
import org.apache.tapestry5.ioc.annotations.Inject;
import static org.apache.tapestry5.ioc.internal.util.CollectionFactory.newList;
import org.apache.tapestry5.ioc.services.PropertyAccess;
import org.apache.tapestry5.ioc.services.TypeCoercer;
import org.apache.tapestry5.json.JSONArray;
import org.apache.tapestry5.json.JSONObject;
import org.apache.tapestry5.services.ComponentDefaultProvider;
import org.apache.tapestry5.services.MarkupWriterFactory;
import org.apache.tapestry5.services.Request;
import org.apache.tapestry5.services.ResponseRenderer;

/**
 * @version $Id$
 */
@IncludeJavaScriptLibrary(value = {"prototype-ui.js", "AutoComplete.js"})
@IncludeStylesheet(value = {"themes/auto_complete/default.css", "themes/shadow/drop_shadow.css", "themes/shadow/auto_complete.css"})
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
	 * Defines how nulls on the server side, or sent from the client side, are treated. The selected strategy may
	 * replace the nulls with some other value. The default strategy leaves nulls alone.  Another built-in strategy,
	 * zero, replaces nulls with the value 0.
	 */
	@Parameter(defaultPrefix = BindingConstants.NULLFIELDSTRATEGY, value = "default")
	private NullFieldStrategy nulls;

	/**
	 * Request object for information on current request.
	 */
	@Inject
	private Request request;

	@Inject
	private ResponseRenderer responseRenderer;

	@Inject
	private MarkupWriterFactory factory;

	@Inject
	private ComponentDefaultProvider defaultProvider;

	@Inject
	private TypeCoercer coercer;

	/**
	 * For blocks, messages, crete actionlink, trigger event.
	 */
	@Inject
	private ComponentResources resources;

	@Inject
	private LinkFactory linkFactory;

	/**
	 * RenderSupport to get unique client side id.
	 */
	@Inject
	private RenderSupport renderSupport;

	@Inject
	private PropertyAccess propertyAccess;

	@Environmental
	private ValidationTracker tracker;

	/**
	 * Performs input validation on the value supplied by the user in the form submission.
	 */
	@Parameter(defaultPrefix = BindingConstants.VALIDATE)
	@SuppressWarnings("unchecked")
	private FieldValidator<Object> validate;

	@Inject
	private FieldValidationSupport fieldValidationSupport;

	/**
	 * Computes a default value for the "validate" parameter using {@link org.apache.tapestry5.services.FieldValidatorDefaultSource}.
	 */
	Binding defaultValidate()
	{
		return defaultProvider.defaultValidatorBinding("selected", resources);
	}

	/**
	 * Computes a default value for the "translate" parameter using {@link org.apache.tapestry5.services.ComponentDefaultProvider#defaultTranslator(String,
	 * org.apache.tapestry5.ComponentResources)}.
	 */
	final Binding defaultTranslate()
	{
		return defaultProvider.defaultTranslatorBinding("selected", resources);
	}

	void beginRender(MarkupWriter writer)
	{
		JSONArray selectedValues = new JSONArray();
		for (Object object : selected)
		{
			Object id = translate.toClient(object);
			selectedValues.put(id);
		}

		writer.element("input",
					   "type", "hidden",
					   "value", selectedValues,
					   "name", getControlName(),
					   "id", getClientId() + "-internal");
		writer.end();

		writer.element("input",
					   "type", "text",
					   "id", getClientId());

		validate.render(writer);
	}

	void afterRender(MarkupWriter writer)
	{
		JSONArray selectedValues = new JSONArray();
		for (Object object : selected)
		{
			Object value = propertyAccess.get(object, "title");
			selectedValues.put(value);
		}

		writer.end();

		JSONObject config = new JSONObject();
		config.put("url", resources.createEventLink(EVENT_NAME).toAbsoluteURI());
		if (selectedValues.length() > 0)
		{
//			selectedValues.
//			config.put("tokens", "[KEY_COMA, " + selectedValues + "]");
		}
		config.put("shadow", "drop_shadow");
		renderSupport.addScript("new Ck.AutoComplete('%s', %s);", getClientId(), config);
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

		String submittedValue = request.getParameter(elementName);
		tracker.recordInput(this, submittedValue);

		int count = values.length;
		for (int i = 0; i < count; i++)
		{
			String value = values[i];

			Object objectValue = null;
			try
			{
				objectValue = translate.parse(value);
			}
			catch (ValidationException e)
			{
				throw new RuntimeException(e);
			}

			selected.add(objectValue);
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
			Object id = propertyAccess.get(o, "id");
			Object value = propertyAccess.get(o, "title");
			JSONObject item = new JSONObject();
			item.put("text", value);
			item.put("value", id);
			jsonObject.put(item);
		}

		return jsonObject;
	}
}