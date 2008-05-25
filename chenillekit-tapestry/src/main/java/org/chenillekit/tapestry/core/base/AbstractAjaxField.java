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

package org.chenillekit.tapestry.core.base;

import java.io.Serializable;

import org.apache.tapestry5.Binding;
import org.apache.tapestry5.ComponentAction;
import org.apache.tapestry5.ComponentResources;
import org.apache.tapestry5.Field;
import org.apache.tapestry5.FieldValidator;
import org.apache.tapestry5.MarkupWriter;
import org.apache.tapestry5.RenderSupport;
import org.apache.tapestry5.ValidationDecorator;
import org.apache.tapestry5.ValidationException;
import org.apache.tapestry5.annotations.Environmental;
import org.apache.tapestry5.annotations.Mixin;
import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.annotations.SetupRender;
import org.apache.tapestry5.corelib.mixins.DiscardBody;
import org.apache.tapestry5.corelib.mixins.RenderDisabled;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.services.ComponentDefaultProvider;
import org.apache.tapestry5.services.FormSupport;

/**
 * @author <a href="mailto:shomburg@hsofttec.com">S.Homburg</a>
 * @version $Id$
 */
abstract public class AbstractAjaxField implements Field
{
    /**
     * The user presentable label for the field. If not provided, a reasonable label is generated
     * from the component's id, first by looking for a message key named "id-label" (substituting
     * the component's actual id), then by converting the actual id to a presentable string (for
     * example, "userId" to "User Id").
     */
    @Parameter(defaultPrefix = "literal")
    private String _label;

    /**
     * If true, then the field will render out with a disabled attribute (to turn off client-side
     * behavior). Further, a disabled field ignores any value in the request when the form is
     * submitted.
     */
    @Parameter("false")
    private boolean _disabled;

    @SuppressWarnings("unused")
    @Mixin
    private RenderDisabled _renderDisabled;

    @SuppressWarnings("unused")
    @Mixin
    private DiscardBody _discardBody;

    @Environmental
    private ValidationDecorator _decorator;

    protected static final FieldValidator NOOP_VALIDATOR = new FieldValidator()
    {
        public void validate(Object value) throws ValidationException
        {
            // Do nothing
        }

        public void render(MarkupWriter writer)
        {
        }

        public boolean isRequired()
        {
            return false;
        }
    };

    static class SetupAction implements ComponentAction<AbstractAjaxField>, Serializable
    {
        private static final long serialVersionUID = 2690270808212097020L;

        private final String _elementName;

        public SetupAction(final String elementName)
        {
            _elementName = elementName;
        }

        public void execute(AbstractAjaxField component)
        {
            component.setupControlName(_elementName);
        }
    }

    /**
     * The id used to generate a page-unique client-side identifier for the component. If a
     * component renders multiple times, a suffix will be appended to the to id to ensure
     * uniqueness. The uniqued value may be accessed via the
     * {@link #getClientId() clientId property}.
     */
    @Parameter(value = "prop:componentResources.id", defaultPrefix = "literal")
    private String _clientId;

    private String _assignedClientId;

    private String _controlName;

    @Environmental
    private FormSupport _formSupport;

    @Environmental
    private RenderSupport _pageRenderSupport;

    @Inject
    private ComponentResources _resources;

    @Inject
    private ComponentDefaultProvider _defaultProvider;

    final String defaultLabel()
    {
        return _defaultProvider.defaultLabel(_resources);
    }

    public final String getLabel()
    {
        return _label;
    }

    @SetupRender
    final void setup()
    {
        // By default, use the component id as the (base) client id. If the clientid
        // parameter is bound, then that is the value to use.

        String id = _clientId;

        // Often, these elementName and _clientId will end up as the same value. There are many
        // exceptions, including a form that renders inside a loop, or a form inside a component
        // that is used multiple times.

        _assignedClientId = _pageRenderSupport.allocateClientId(id);
    }

    public final String getClientId()
    {
        return _assignedClientId;
    }

    /**
     * Returns the value used as the name attribute of the rendered element. This value will be
     * unique within an enclosing form, even if the same component renders multiple times.
     *
     * @see org.apache.tapestry5.services.FormSupport#allocateControlName(String)
     */
    public String getControlName()
    {
        return _controlName;
    }

    public final boolean isDisabled()
    {
        return _disabled;
    }

    /**
     * Invoked from within a ComponentCommand callback, to restore the component's elementName.
     */
    private void setupControlName(String controlName)
    {
        _controlName = controlName;
    }

    /**
     * Used by subclasses to create a default binding to a property of the container matching the
     * component id.
     *
     * @return a binding to the property, or null if the container does not have a corresponding
     *         property
     */
    protected final Binding createDefaultParameterBinding(String parameterName)
    {
        return _defaultProvider.defaultBinding(parameterName, _resources);
    }

    /**
     * Returns false; most components do not support declarative validation.
     */
    public boolean isRequired()
    {
        return false;
    }
}
