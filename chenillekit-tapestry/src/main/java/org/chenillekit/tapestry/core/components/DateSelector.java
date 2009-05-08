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

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import org.apache.tapestry5.Binding;
import org.apache.tapestry5.BindingConstants;
import org.apache.tapestry5.ComponentResources;
import org.apache.tapestry5.FieldValidationSupport;
import org.apache.tapestry5.FieldValidator;
import org.apache.tapestry5.MarkupWriter;
import org.apache.tapestry5.ValidationException;
import org.apache.tapestry5.ValidationTracker;
import org.apache.tapestry5.annotations.Environmental;
import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.corelib.base.AbstractField;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.ioc.internal.util.CollectionFactory;
import org.apache.tapestry5.services.ComponentDefaultProvider;
import org.apache.tapestry5.services.Request;

/**
 * @author <a href="mailto:shomburg@depot120.dpd.de">S.Homburg</a>
 * @version $Id$
 */
public class DateSelector extends AbstractField
{
	@Parameter(required = true, defaultPrefix = BindingConstants.PROP)
	private Date value;

	@Parameter(defaultPrefix = BindingConstants.PROP, value = "1970")
	private int firstYear;

	@Parameter(defaultPrefix = BindingConstants.PROP, value = "2030")
	private int lastYear;

	@Parameter(defaultPrefix = BindingConstants.PROP, value = "false")
	private boolean longMonth;

	/**
	 * The object that will perform input validation (which occurs after translation). The validate binding prefix is
	 * generally used to provide this object in a declarative fashion.
	 */
	@Parameter(defaultPrefix = BindingConstants.VALIDATE)
	@SuppressWarnings("unchecked")
	private FieldValidator<Object> validate;

	@Property
	@Persist
	private int dayValue;

	@Property
	@Persist
	private int monthValue;

	@Property
	@Persist
	private int yearValue;

	@Inject
	private Locale locale;

	@Inject
	private ComponentDefaultProvider defaultProvider;

	@Inject
	private Request request;

	@Inject
	private ComponentResources resources;

	@Environmental
	private ValidationTracker tracker;

	@Inject
	private FieldValidationSupport fieldValidationSupport;

	private List<Integer> days = CollectionFactory.newList();
	private List<Integer> months = CollectionFactory.newList();
	private List<Integer> years = CollectionFactory.newList();

	private Calendar calendar = Calendar.getInstance(locale);

	/**
	 * Computes a default value for the "validate" parameter using {@link org.apache.tapestry5.services.FieldValidatorDefaultSource}.
	 */
	final Binding defaultValidate()
	{
		return defaultProvider.defaultValidatorBinding("value", resources);
	}

	/**
	 * Tapestry render phase method.
	 * Initialize temporary instance variables here.
	 */
	void setupRender()
	{
		if (value == null)
			value = new Date();

		calendar.setTime(value);
		dayValue = calendar.get(Calendar.DAY_OF_MONTH);
		monthValue = calendar.get(Calendar.MONTH);
		yearValue = calendar.get(Calendar.YEAR);
	}

	/**
	 * Tapestry render phase method.
	 * Start a tag here, end it in afterRender
	 */
	void beginRender(MarkupWriter writer)
	{
		writer.element("span",
					   "class", "ck-dateselector",
					   "id", getClientId());

		writer.element("select",
					   "class", "day",
					   "id", getClientId() + "_day",
					   "name", getControlName() + "_day");

		for (int day = 1; day < 32; day++)
		{
			org.apache.tapestry5.dom.Element element = writer.element("option",
																	  "value", day);

			if (dayValue == day)
				element.attribute("selected", "selected");

			writer.write(String.format("%02d", day));
			writer.end();
		}
		writer.end();

		writer.element("select",
					   "class", "month",
					   "id", getClientId() + "_month",
					   "name", getControlName() + "_month");

		for (int month = 0; month < 12; month++)
		{
			org.apache.tapestry5.dom.Element element = writer.element("option",
																	  "value", month);

			if (monthValue == month)
				element.attribute("selected", "selected");

			writer.write(String.format("%02d", month + 1));

			writer.end();
		}
		writer.end();

		writer.element("select",
					   "class", "year",
					   "id", getClientId() + "_year",
					   "name", getControlName() + "_year");

		for (int year = firstYear; year <= lastYear; year++)
		{
			org.apache.tapestry5.dom.Element element = writer.element("option",
																	  "value", year);

			if (yearValue == year)
				element.attribute("selected", "selected");

			writer.write(String.format("%d", year));
			writer.end();
		}
		writer.end();
	}

	/**
	 * Tapestry render phase method. End a tag here.
	 */
	void afterRender(MarkupWriter writer)
	{
		writer.end();
	}


	public List<Integer> getDaySelectModel()
	{
		return days;
	}

	public List<Integer> getMonthSelectModel()
	{
		return months;
	}

	public List<Integer> getYearSelectModel()
	{
		return years;
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
		String rawDayValue = request.getParameter(elementName + "_day");
		String rawMonthValue = request.getParameter(elementName + "_month");
		String rawYearValue = request.getParameter(elementName + "_year");

		try
		{
			calendar.set(Integer.parseInt(rawYearValue), Integer.parseInt(rawMonthValue), Integer.parseInt(rawDayValue));
			calendar.setLenient(false);
			Date translated = calendar.getTime();
			fieldValidationSupport.validate(translated, resources, validate);
			value = translated;
		}
		catch (ValidationException ex)
		{
			tracker.recordError(this, ex.getMessage());
		}
		catch (IllegalArgumentException ex)
		{
			tracker.recordError(this, ex.getMessage());
		}
	}
}
