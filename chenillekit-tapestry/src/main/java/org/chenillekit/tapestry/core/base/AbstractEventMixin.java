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

import java.util.List;

import org.apache.tapestry5.ClientElement;
import org.apache.tapestry5.ComponentResources;
import org.apache.tapestry5.Link;
import org.apache.tapestry5.MarkupWriter;
import org.apache.tapestry5.RenderSupport;
import org.apache.tapestry5.annotations.Environmental;
import org.apache.tapestry5.annotations.IncludeJavaScriptLibrary;
import org.apache.tapestry5.annotations.InjectContainer;
import org.apache.tapestry5.annotations.MixinAfter;
import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.ioc.annotations.Inject;

/**
 * @author <a href="mailto:homburgs@googlemail.com">S.Homburg</a>
 * @version $Id: AbstractEventMixin.java 682 2008-05-20 22:00:02Z homburgs $
 */
@IncludeJavaScriptLibrary(value = {"../Chenillekit.js", "../components/CkOnEvents.js"})
@MixinAfter
abstract public class AbstractEventMixin implements EventMixin
{
    @Inject
    private ComponentResources resources;

    @InjectContainer
    private ClientElement clientElement;

    /**
     * the javascript callback function (optional).
     * function has one parameter: the response text
     */
    @Parameter(required = false, defaultPrefix = "literal")
    private String onCompleteCallback;

    /**
     * <a href="http://www.prototypejs.org/api/event/stop">Event.stop</a>
     */
    @Parameter(required = false, defaultPrefix = "literal")
    private boolean stop;

    /**
     * The context for the link (optional parameter). This list of values will be converted into strings and included in
     * the URI. The strings will be coerced back to whatever their values are and made available to event handler
     * methods.
     */
    @Parameter
    private List<?> context;

    @Environmental
    private RenderSupport pageRenderSupport;

    private Object[] contextArray;

    /**
     * get the conext parameter(s)
     *
     * @return conext parameter(s)
     */
    public List<?> getContext()
    {
        return context;
    }

    void setupRender()
    {
        contextArray = context == null ? new Object[0] : context.toArray();
    }

    void afterRender(MarkupWriter writer)
    {
        Link link = resources.createActionLink(getEventName().toLowerCase(), true, contextArray);
        String id = clientElement.getClientId();

        String ajaxString = "new Ck.OnEvent('%s', '%s', %b, '%s'";

        if (resources.isBound("onCompleteCallback"))
            ajaxString += ",'" + onCompleteCallback + "'";

        ajaxString += ");";

        boolean doStop = resources.isBound("stop") && stop;

        pageRenderSupport.addScript(ajaxString, getEventName(), id, doStop, link.toAbsoluteURI());
    }
}
