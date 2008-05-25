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

import java.util.Locale;

import org.apache.tapestry5.ComponentResources;
import org.apache.tapestry5.MarkupWriter;
import org.apache.tapestry5.Translator;
import org.apache.tapestry5.ValidationException;
import org.apache.tapestry5.ValidationTracker;
import org.apache.tapestry5.annotations.AfterRender;
import org.apache.tapestry5.annotations.Environmental;
import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.corelib.base.AbstractField;
import org.apache.tapestry5.ioc.Messages;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.services.ComponentDefaultProvider;
import org.apache.tapestry5.services.Request;
import org.apache.tapestry5.services.ValidationMessagesSource;

/**
 * place a hidden field into a form.
 *
 * @author <a href="mailto:homburgs@googlemail.com">S.Homburg</a>
 * @version $Id$
 */
public class Hidden extends AbstractField
{
    @Inject
    private ComponentResources _resources;

    @Inject
    private Request _request;

    @Inject
    private ValidationMessagesSource _messagesSource;

    @Inject
    private Locale _locale;

    @Environmental
    private ValidationTracker _tracker;

    /**
     * The object which will perform translation between server-side and client-side
     * representations. If not specified, a value will usually be generated based on the type of the
     * value parameter.
     */
    @Parameter
    private Translator _translate;

    /**
     * The value to read or update.
     */
    @Parameter(required = true)
    private Object _value;

    @Inject
    private ComponentDefaultProvider _defaultProvider;

    /**
     * Computes a default value for the "translate" parameter using {@link org.apache.tapestry5.services.ComponentDefaultProvider#defaultTranslator(String,
     * org.apache.tapestry5.ComponentResources)}.
     */
    final Translator defaultTranslate()
    {
        return _defaultProvider.defaultTranslator("value", _resources);
    }

    void beginRender(MarkupWriter writer)
    {
        if (_translate == null)
            _translate = defaultTranslate();

        if (_value == null)
            _value = "";

        writer.element("input", "type", "hidden",
                       "id", getClientId(),
                       "name", getControlName(),
                       "value", _value);

        _resources.renderInformalParameters(writer);
    }

    @AfterRender
    void doAfterRender(MarkupWriter writer)
    {
        writer.end();
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
     * Method implemented by subclasses to actually do the work of processing the submission of the
     * form. The element's elementName property will already have been set. This method is only
     * invoked if the field is <strong>not {@link #isDisabled() disabled}</strong>.
     *
     * @param elementName the name of the element (used to find the correct parameter in the request)
     */
    @Override
    protected void processSubmission(String elementName)
    {
        String rawValue = _request.getParameter(elementName);

        Messages messages = _messagesSource.getValidationMessages(_locale);

        try
        {
            _value = _translate.parseClient(rawValue, messages);
        }
        catch (ValidationException ex)
        {
            _tracker.recordError(this, ex.getMessage());
        }
    }
}
