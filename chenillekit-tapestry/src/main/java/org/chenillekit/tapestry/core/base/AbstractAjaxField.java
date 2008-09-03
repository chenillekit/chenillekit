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

import org.apache.tapestry5.BindingConstants;
import org.apache.tapestry5.ClientElement;
import org.apache.tapestry5.ComponentResources;
import org.apache.tapestry5.RenderSupport;
import org.apache.tapestry5.ValidationDecorator;
import org.apache.tapestry5.annotations.Environmental;
import org.apache.tapestry5.annotations.Mixin;
import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.annotations.SetupRender;
import org.apache.tapestry5.corelib.mixins.DiscardBody;
import org.apache.tapestry5.corelib.mixins.RenderDisabled;
import org.apache.tapestry5.ioc.annotations.Inject;

/**
 * @author <a href="mailto:shomburg@hsofttec.com">S.Homburg</a>
 * @version $Id$
 */
abstract public class AbstractAjaxField implements ClientElement
{
    /**
     * If true, then the field will render out with a disabled attribute (to turn off client-side
     * behavior). Further, a disabled field ignores any value in the request when the form is
     * submitted.
     */
    @Parameter("false")
    private boolean disabled;

    @Mixin
    private RenderDisabled renderDisabled;

    @Mixin
    private DiscardBody discardBody;

    @Environmental
    private ValidationDecorator _decorator;

    /**
     * The id used to generate a page-unique client-side identifier for the component. If a
     * component renders multiple times, a suffix will be appended to the to id to ensure
     * uniqueness. The uniqued value may be accessed via the
     * {@link #getClientId() clientId property}.
     */
    @Parameter(value = "prop:componentResources.id", defaultPrefix = BindingConstants.LITERAL)
    private String clientId;

    private String assignedClientId;

    @Environmental
    private RenderSupport renderSupport;

    @Inject
    private ComponentResources resources;

    @SetupRender
    void setupRender()
    {
        assignedClientId = renderSupport.allocateClientId(clientId);
    }

    public final String getClientId()
    {
        return assignedClientId;
    }

    public final boolean isDisabled()
    {
        return disabled;
    }
}
