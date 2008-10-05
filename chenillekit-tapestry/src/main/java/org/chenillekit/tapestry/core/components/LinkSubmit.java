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

import org.apache.tapestry5.ClientElement;
import org.apache.tapestry5.ComponentResources;
import org.apache.tapestry5.MarkupWriter;
import org.apache.tapestry5.RenderSupport;
import org.apache.tapestry5.annotations.Environmental;
import org.apache.tapestry5.annotations.IncludeJavaScriptLibrary;
import org.apache.tapestry5.annotations.Mixin;
import org.apache.tapestry5.annotations.SupportsInformalParameters;
import org.apache.tapestry5.corelib.mixins.RenderInformals;
import org.apache.tapestry5.ioc.annotations.Inject;

/**
 * LinkSubmit allows arbitrary page (DOM) elements to submit a form.
 *
 * @author Chris Lewis Jan 8, 2008 <chris@thegodcode.net>
 * @version $Id$
 * @since 0.2.0
 */
@SupportsInformalParameters
@IncludeJavaScriptLibrary({"../Chenillekit.js", "ClickSubmit.js"})
public class LinkSubmit implements ClientElement
{
    @Mixin
    private RenderInformals renderInformals;

    @Inject
    private ComponentResources resources;

    @Environmental
    private RenderSupport renderSupport;

    /**
     * The client-side id.
     */
    private String clientId;

    /**
     * Tapestry render phase method.
     * Initialize temporary instance variables here.
     */
    void setupRender()
    {
        clientId = renderSupport.allocateClientId(resources.getId());
    }

    /**
     * Tapestry render phase method.
     * Start a tag here, end it in afterRender
     *
     * @param writer the markup writer
     */
    void beginRender(final MarkupWriter writer)
    {
        renderSupport.addScript("new Ck.ClickSubmit('%s');", getClientId());
        writer.element("a", "id", getClientId(), "href", "#");
    }

    /**
     * Tapestry render phase method. End a tag here.
     *
     * @param writer the markup writer
     */
    void afterRender(final MarkupWriter writer)
    {
        writer.end();
    }

    public String getClientId()
    {
        return clientId;
    }
}