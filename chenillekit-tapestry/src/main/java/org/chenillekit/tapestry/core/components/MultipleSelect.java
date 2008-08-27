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
import org.apache.tapestry5.services.ComponentDefaultProvider;
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
            super(writer, encoder);
        }

        @Override
        protected boolean isOptionSelected(OptionModel optionModel)
        {
            boolean hit = false;
            Object testValue = optionModel.getValue();

            if (value != null)
            {
                for (Object singleValue : value)
                {
                    hit = testValue == singleValue || (testValue != null && testValue.equals(singleValue));
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
    private MultipleValueEncoder encoder;

    /**
     * The model used to identify the option groups and options to be presented to the user.
     * This can be generated automatically for Enum types.
     */
    @Parameter(required = true)
    private SelectModel model;

    /**
     * should the component multi select able.
     */
    @Parameter(defaultPrefix = BindingConstants.PROP, value = "true")
    @SuppressWarnings("unchecked")
    private boolean multiple;

    @Parameter(defaultPrefix = BindingConstants.VALIDATE)
    @SuppressWarnings("unchecked")
    private FieldValidator<Object> validate;

    /**
     * The list of value to read or update.
     */
    @Parameter(required = true, principal = true)
    private List value;

    @Inject
    private Locale locale;

    @Inject
    private Request request;

    @Inject
    private ComponentResources resources;

    @Environmental
    private ValidationTracker tracker;

    @Inject
    private ValueEncoderSource valueEncoderSource;

    @Inject
    private ComponentDefaultProvider defaultProvider;

    @Override
    protected void processSubmission(String elementName)
    {
        String[] primaryKeys = request.getParameters(elementName);
        List selectedValues = primaryKeys != null ? encoder.toValue(primaryKeys) : CollectionFactory.newList();

        try
        {
            for (Object selectedValue : selectedValues)
                validate.validate(selectedValue);

            if (validate.isRequired() && selectedValues.size() == 0)
                throw new ValidationException("at least one selection is required");

            value = selectedValues;
        }
        catch (ValidationException ex)
        {
            tracker.recordError(this, ex.getMessage());
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

        resources.renderInformalParameters(writer);
    }

    @SuppressWarnings("unchecked")
    ValueEncoder defaultEncoder()
    {
        // TODO must check for new one
        return valueEncoderSource.getValueEncoder(String.class);
    }

    @SuppressWarnings("unchecked")
    SelectModel defaultModel()
    {
        Class valueType = resources.getBoundType("value");

        if (valueType == null) return null;

        if (Enum.class.isAssignableFrom(valueType))
            return new EnumSelectModel(valueType, resources.getContainerMessages());

        return null;
    }

    FieldValidator defaultValidate()
    {
        return defaultProvider.defaultValidator("value", resources);
    }

    Binding defaultValue()
    {
        return createDefaultParameterBinding("value");
    }

    @BeforeRenderTemplate
    void options(MarkupWriter writer)
    {
        SelectModelVisitor renderer = new Renderer(writer);
        if (model == null)
            throw new TapestryException("select model cannot be null", this, null);

        model.visit(renderer);
    }

    /**
     * only for testing.
     *
     * @param model
     */
    public void setModel(SelectModel model)
    {
        this.model = model;
    }

    /**
     * only for testing.
     *
     * @param value
     */
    public void setValue(List value)
    {
        this.value = value;
    }

    /**
     * only for testing.
     *
     * @param encoder
     */
    public void setValueEncoder(MultipleValueEncoder encoder)
    {
        this.encoder = encoder;
    }
}
