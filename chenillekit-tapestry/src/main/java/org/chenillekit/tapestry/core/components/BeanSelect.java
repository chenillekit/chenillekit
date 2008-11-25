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
 * @deprecated
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
    private String _clientId;

    /**
     * The value(bean) to read or update.
     */
    @Parameter(required = true, principal = true)
    private Object _value;

    /**
     * The list of bean should diplayed.
     */
    @Parameter(required = true)
    private List<Object> _list;

    /**
     * Name of the Field that should reflect the label in the select option tag.
     */
    @Parameter(required = true, defaultPrefix = BindingConstants.LITERAL)
    private String _labelField;

    /**
     * Name of the Field that should reflect the value in the select option tag.
     */
    @Parameter(required = true, defaultPrefix = BindingConstants.LITERAL)
    private String _valueField;

    @Component(inheritInformalParameters = true, parameters = {"blankLabel=inherit:blankLabel",
            "blankOption=inherit:blankOption",
            "validate=inherit:validate", "value=value", "model=beanModel", "encoder=beanEncoder"})
    private Select _select;

    @Inject
    private ComponentResources _resources;

    @Inject
    private PropertyAccess _propertyAccess;

    @Persist
    private GenericSelectionModel<Object> _model;

    @Persist
    private GenericValueEncoder<Object> _encoder;

    @Environmental
    private RenderSupport _pageRenderSupport;

    private String _assignedClientId;

    void setupRender()
    {
        _assignedClientId = _pageRenderSupport.allocateClientId(_clientId);
    }

    void beginRender(MarkupWriter writer)
    {
        _encoder = new GenericValueEncoder<Object>(_list, _valueField, _propertyAccess);
        _model = new GenericSelectionModel<Object>(_list, _labelField, _propertyAccess);
    }

    public GenericSelectionModel<Object> getBeanModel()
    {
        return _model;
    }

    public GenericValueEncoder<Object> getBeanEncoder()
    {
        return _encoder;
    }

    public Object getValue()
    {
        return _value;
    }

    public void setValue(Object value)
    {
        _value = value;
    }

    /**
     * only for testing
     */
    void setup(Object value, List<Object> list, String labelField, String valueField)
    {
        _value = value;
        _list = list;
        _labelField = labelField;
        _valueField = valueField;
    }

    /**
     * only for testing
     */
    void inject(ComponentResources resources, PropertyAccess propertyAccess)
    {
        _resources = resources;
        _propertyAccess = propertyAccess;
    }

    /**
     * Returns a unique id for the element. This value will be unique for any given rendering of a
     * page. This value is intended for use as the id attribute of the client-side element, and will
     * be used with any DHTML/Ajax related JavaScript.
     */
    public String getClientId()
    {
        return _assignedClientId;
    }
}
