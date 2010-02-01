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
 *
 */

package org.chenillekit.tapestry.core.components;

import java.util.List;

import org.apache.tapestry5.BindingConstants;
import org.apache.tapestry5.ClientElement;
import org.apache.tapestry5.ComponentResources;
import org.apache.tapestry5.Link;
import org.apache.tapestry5.MarkupWriter;
import org.apache.tapestry5.RenderSupport;
import org.apache.tapestry5.annotations.Environmental;
import org.apache.tapestry5.annotations.IncludeJavaScriptLibrary;
import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.annotations.SupportsInformalParameters;
import org.apache.tapestry5.corelib.components.PageLink;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.services.PageRenderLinkSource;

/**
 * Render a button tag element and bind to it's "click" event an event on the server side.
 * The event name is customizable and it defaults to "CLICKED".
 * <p/>
 * If parameter <code>pageName</code> is given the component act like a {@link PageLink}
 * to the page corresponding to the logical name <code>pageName</code>.
 *
 * @version $Id$
 */
@SupportsInformalParameters
@IncludeJavaScriptLibrary(value = {"../Chenillekit.js", "CkOnEvents.js"})
public class Button implements ClientElement
{
    static final String CLICKED_EVENT = "clicked";

    static final String TYPE_BUTTON = "button";
    static final String TYPE_SUBMIT = "submit";
    static final String TYPE_RESET = "reset";

    /**
     * Type of the button, possible value are "button", "submit" and "reset".
     */
    @Parameter(defaultPrefix = BindingConstants.LITERAL, value = TYPE_BUTTON)
    private String type;

    /**
     * The name of the event fired. Defaults to <code>clicked</code>.
     */
    @Parameter(defaultPrefix = BindingConstants.LITERAL, value = CLICKED_EVENT)
    private String event;

    /**
     * If specified the components act as a {@link PageLink} doing a link
     * for rendering the logical <code>pageName</code>.
     */
    @Parameter(defaultPrefix = BindingConstants.LITERAL, required = false)
    private String pageName;

    /**
     * <code>Disabled</code> attribute of the element.
     */
    @Parameter(value = "false")
    private boolean disabled;

    /**
     * The id used to generate a page-unique client-side identifier for the component. If a component renders multiple
     * times, a suffix will be appended to the to id to ensure uniqueness.
     */
    @Parameter(value = "prop:componentResources.id", defaultPrefix = BindingConstants.LITERAL)
    private String clientId;

    /**
     * The context for the link (optional parameter). This list of values will
     * be converted into strings and included in the URI. The strings will be
     * coerced back to whatever their values are and made available to event
     * handler methods or the passivate mthod of the page to link to in case
     * a <code>pageName</code> has been specified.
     */
    @Parameter
    private List<?> context;

    @Environmental
    private RenderSupport renderSupport;

    @Inject
    private ComponentResources resources;
    
    @Inject
    private PageRenderLinkSource pageRenderResources;

    private String assignedClientId;

    private Object[] contextArray;

    void setupRender()
    {
        assignedClientId = renderSupport.allocateClientId(clientId);
        contextArray = context == null ? new Object[0] : context.toArray();
    }

    void beginRender(MarkupWriter writer)
    {
        writer.element("button", "type", type, "id", getClientId());
        resources.renderInformalParameters(writer);
    }

    void afterRender(MarkupWriter writer)
    {
        if (!disabled && type.equalsIgnoreCase(TYPE_BUTTON))
        {
            Link link;
            
            if (pageName != null)
            {
            	link = pageRenderResources.createPageRenderLinkWithContext(pageName, contextArray);
            }
            else
            {
            	link = resources.createEventLink(event, contextArray);
            }
            
            renderSupport.addScript("new Ck.ButtonEvent('%s', '%s');",
                                    getClientId(), link.toAbsoluteURI());
        }

        // Close the button tag
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
