/*
 * Apache License
 * Version 2.0, January 2004
 * http://www.apache.org/licenses/
 *
 * Copyright 2008-2010 by chenillekit.org
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 */
package org.chenillekit.tapestry.core.components;

import org.apache.tapestry5.BindingConstants;
import org.apache.tapestry5.ClientElement;
import org.apache.tapestry5.ComponentResources;
import org.apache.tapestry5.MarkupWriter;
import org.apache.tapestry5.annotations.Environmental;
import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.annotations.SupportsInformalParameters;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.services.javascript.JavascriptSupport;

/**
 * Helper component that will render a variable element type.
 * Similar to the Any component in Tapestry3.
 *
 * @version $Id$
 * @deprecated use Any component from tapestry 5
 */
@SupportsInformalParameters
public class Element implements ClientElement
{
    @Inject
    private ComponentResources resources;

    @Environmental
    private JavascriptSupport javascriptSupport;

    /**
     * The id used to generate a page-unique client-side identifier for the component. If a component renders multiple
     * times, a suffix will be appended to the to id to ensure uniqueness.
     */
    @Parameter(value = "prop:componentResources.id", defaultPrefix = BindingConstants.LITERAL)
    private String clientId;

    /**
     * The element to render. If not null, then the component will render the indicated element around
     * its body. The default is derived from the component template.
     */
    @Parameter(value = "prop:componentResources.elementName", defaultPrefix = "literal")
    private String elementName;

    private String assignedClientId;

    void setupRender()
    {
        assignedClientId = javascriptSupport.allocateClientId(clientId);
    }

    void beginRender(MarkupWriter writer)
    {
        writer.element(elementName, "id", getClientId());
        resources.renderInformalParameters(writer);
    }

    void afterRender(MarkupWriter writer)
    {
        writer.end();
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
