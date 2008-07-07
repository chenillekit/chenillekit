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
import java.util.Locale;

import org.apache.tapestry5.Binding;
import org.apache.tapestry5.BindingConstants;
import org.apache.tapestry5.ComponentResources;
import org.apache.tapestry5.FieldValidator;
import org.apache.tapestry5.MarkupWriter;
import org.apache.tapestry5.OptionModel;
import org.apache.tapestry5.SelectModel;
import org.apache.tapestry5.SelectModelVisitor;
import org.apache.tapestry5.ValidationException;
import org.apache.tapestry5.ValidationTracker;
import org.apache.tapestry5.ValueEncoder;
import org.apache.tapestry5.annotations.BeforeRenderTemplate;
import org.apache.tapestry5.annotations.Environmental;
import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.corelib.base.AbstractField;
import org.apache.tapestry5.dom.Element;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.ioc.internal.util.CollectionFactory;
import org.apache.tapestry5.ioc.internal.util.TapestryException;
import org.apache.tapestry5.services.FieldValidatorDefaultSource;
import org.apache.tapestry5.services.Request;
import org.apache.tapestry5.services.ValueEncoderSource;
import org.apache.tapestry5.util.EnumSelectModel;

import org.chenillekit.tapestry.core.encoders.MultipleValueEncoder;
import org.chenillekit.tapestry.core.renderes.MultipleSelectModelRenderer;


/**
 * Select a list of items from a list of values, using an [X]HTML multiple select element on the client side.
 * <p/>
 * the only diffrence to the original tapestry select component is that the "value" parameter expected a java.util.List object.
 *
 * @author <a href="mailto:homburgs@googlemail.com">S.Homburg</a>
 * @version $Id: MultipleSelect.java 703 2008-07-06 13:54:03Z homburgs $
 * @link http://tapestry.apache.org/t5components/tapestry-core/component-parameters.html#orgapachetapestrycorelibcomponentsselect
 */
public class MultipleSelect extends AbstractField
{
    private class Renderer extends MultipleSelectModelRenderer
    {
        public Renderer(MarkupWriter writer)
        {
            super(writer, _encoder);
        }

        @Override
        protected boolean isOptionSelected(OptionModel optionModel)
        {
            boolean hit = false;
            Object testValue = optionModel.getValue();

            if (_value != null)
            {
                for (Object value : _value)
                {
                    hit = testValue == value || (testValue != null && testValue.equals(value));
                    if (hit)
                        break;
                }
            }

            return hit;
        }
    }

    /**
     * Allows a specific implementation of org.apache.tapestry5.ValueEncoder to be supplied.
     * This is used to create client-side string values for the different options.
     */
    @Parameter
    private MultipleValueEncoder _encoder;

    /**
     * The model used to identify the option groups and options to be presented to the user.
     * This can be generated automatically for Enum types.
     */
    @Parameter(required = true)
    private SelectModel _model;

    /**
     * Performs input validation on the value supplied by the user in the form submission.
     */
    @Parameter(defaultPrefix = BindingConstants.VALIDATE)
    @SuppressWarnings("unchecked")
    private FieldValidator<Object> _validate = NOOP_VALIDATOR;

    /**
     * should the component multi select able.
     */
    @Parameter(defaultPrefix = BindingConstants.PROP, value = "true")
    @SuppressWarnings("unchecked")
    private boolean multiple;

    /**
     * The list of value to read or update.
     */
    @Parameter(required = true, principal = true)
    private List _value;

    @Inject
    private FieldValidatorDefaultSource _fieldValidatorDefaultSource;

    @Inject
    private Locale _locale;

    @Inject
    private Request _request;

    @Inject
    private ComponentResources _resources;

    @Environmental
    private ValidationTracker _tracker;

    @Inject
    private ValueEncoderSource _valueEncoderSource;

    @Override
    protected void processSubmission(String elementName)
    {
        String[] primaryKeys = _request.getParameters(elementName);
        List selectedValues = primaryKeys != null ? _encoder.toValue(primaryKeys) : CollectionFactory.newList();

        try
        {
            for (Object selectedValue : selectedValues)
                _validate.validate(selectedValue);

            if (_validate.isRequired() && selectedValues.size() == 0)
                throw new ValidationException("at least one selection is required");

            _value = selectedValues;
        }
        catch (ValidationException ex)
        {
            _tracker.recordError(this, ex.getMessage());
        }
    }

    void afterRender(MarkupWriter writer)
    {
        writer.end();
    }

    void beginRender(MarkupWriter writer)
    {
        Element element = writer.element("select", "name", getControlName(), "id", getClientId());

        if (multiple)
            element.attribute("multiple", "multiple");

        _resources.renderInformalParameters(writer);
    }

    @SuppressWarnings("unchecked")
    ValueEncoder defaultEncoder()
    {
        // TODO must check for new one
        return _valueEncoderSource.getValueEncoder(String.class);
    }

    @SuppressWarnings("unchecked")
    SelectModel defaultModel()
    {
        Class valueType = _resources.getBoundType("value");

        if (valueType == null) return null;

        if (Enum.class.isAssignableFrom(valueType))
            return new EnumSelectModel(valueType, _resources.getContainerMessages());

        return null;
    }

    FieldValidator defaultValidate()
    {
        Class type = _resources.getBoundType("value");

        if (type == null)
            return null;

        return _fieldValidatorDefaultSource.createDefaultValidator(this, getClientId(), _resources.getContainerMessages(),
                                                                   _locale, type, _resources.getAnnotationProvider("value"));
    }

    Binding defaultValue()
    {
        return createDefaultParameterBinding("value");
    }

    @BeforeRenderTemplate
    void options(MarkupWriter writer)
    {
        SelectModelVisitor renderer = new Renderer(writer);
        if (_model == null)
            throw new TapestryException("select model cannot be null", this, null);

        _model.visit(renderer);
    }

    /**
     * only for testing.
     *
     * @param model
     */
    public void setModel(SelectModel model)
    {
        _model = model;
    }

    /**
     * only for testing.
     *
     * @param value
     */
    public void setValue(List value)
    {
        _value = value;
    }

    /**
     * only for testing.
     *
     * @param encoder
     */
    public void setValueEncoder(MultipleValueEncoder encoder)
    {
        _encoder = encoder;
    }
}
