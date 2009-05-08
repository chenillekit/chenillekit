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

import org.apache.tapestry5.BindingConstants;
import org.apache.tapestry5.ClientElement;
import org.apache.tapestry5.ComponentResources;
import org.apache.tapestry5.MarkupWriter;
import org.apache.tapestry5.RenderSupport;
import org.apache.tapestry5.annotations.Component;
import org.apache.tapestry5.annotations.Environmental;
import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.SupportsInformalParameters;
import org.apache.tapestry5.corelib.components.Select;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.ioc.services.PropertyAccess;

import org.chenillekit.tapestry.core.internal.GenericSelectionModel;
import org.chenillekit.tapestry.core.internal.GenericValueEncoder;

/**
 * let you make a list of beans selectable.
 *
 * @version $Id: BeanSelect.java 682 2008-05-20 22:00:02Z homburgs $
 */
// TODO make beans multi-selectable
@SupportsInformalParameters
public class BeanSelect implements ClientElement
{
	/**
	 * The id used to generate a page-unique client-side identifier for the component. If a component renders multiple
	 * times, a suffix will be appended to the to id to ensure uniqueness.
	 */
	@Parameter(value = "prop:componentResources.id", defaultPrefix = BindingConstants.LITERAL)
	private String clientId;

	/**
	 * The value(bean) to read or update.
	 */
	@Parameter(required = true, principal = true)
	private Object value;

	/**
	 * The list of bean should diplayed.
	 */
	@Parameter(required = true)
	private List<Object> list;

	/**
	 * Name of the Field that should reflect the label in the select option tag.
	 */
	@Parameter(required = true, defaultPrefix = BindingConstants.LITERAL)
	private String labelField;

	/**
	 * Name of the Field that should reflect the value in the select option tag.
	 */
	@Parameter(required = true, defaultPrefix = BindingConstants.LITERAL)
	private String valueField;

	@Component(inheritInformalParameters = true, parameters = {"blankLabel=inherit:blankLabel",
			"blankOption=inherit:blankOption", "clientId=prop:clientId",
			"validate=inherit:validate", "value=value", "model=beanModel", "encoder=beanEncoder"})
	private Select select;

	@Inject
	private ComponentResources resources;

	@Inject
	private PropertyAccess propertyAccess;

	@Persist
	private GenericSelectionModel<Object> model;

	@Persist
	private GenericValueEncoder<Object> encoder;

	@Environmental
	private RenderSupport renderSupport;

	private String assignedClientId;

	void setupRender()
	{
		assignedClientId = renderSupport.allocateClientId(clientId);
	}

	void beginRender(MarkupWriter writer)
	{
		encoder = new GenericValueEncoder<Object>(list, valueField, propertyAccess);
		model = new GenericSelectionModel<Object>(list, labelField, propertyAccess);
	}

	public GenericSelectionModel<Object> getBeanModel()
	{
		return model;
	}

	public GenericValueEncoder<Object> getBeanEncoder()
	{
		return encoder;
	}

	public Object getValue()
	{
		return value;
	}

	public void setValue(Object value)
	{
		this.value = value;
	}

	/**
	 * only for testing
	 */
	void setup(Object value, List<Object> list, String labelField, String valueField)
	{
		this.value = value;
		this.list = list;
		this.labelField = labelField;
		this.valueField = valueField;
	}

	/**
	 * only for testing
	 */
	void inject(ComponentResources resources, PropertyAccess propertyAccess)
	{
		this.resources = resources;
		this.propertyAccess = propertyAccess;
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
