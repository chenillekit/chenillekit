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

package org.chenillekit.tapestry.core.mixins;

import org.apache.tapestry5.BindingConstants;
import org.apache.tapestry5.ClientElement;
import org.apache.tapestry5.ComponentResources;
import org.apache.tapestry5.MarkupWriter;
import org.apache.tapestry5.RenderSupport;
import org.apache.tapestry5.annotations.Environmental;
import org.apache.tapestry5.annotations.IncludeJavaScriptLibrary;
import org.apache.tapestry5.annotations.IncludeStylesheet;
import org.apache.tapestry5.annotations.InjectContainer;
import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.services.Cookies;

/**
 * Helper mixin that will render a variable element type.
 * Similar to the Any component in Tapestry3.
 *
 * @version $Id: Resizable.java 682 2008-05-20 22:00:02Z homburgs $
 */
@IncludeJavaScriptLibrary(value = {"../Cookie.js", "${tapestry.scriptaculous}/dragdrop.js",
        "Resizable.js"})
@IncludeStylesheet(value = {"Resizable.css"})
public class Resizable implements ClientElement
{
    /**
     * The field component to which this mixin is attached.
     */
    @InjectContainer
    private ClientElement _clientElement;

    @Inject
    private ComponentResources _resources;

    @Environmental
    private RenderSupport _pageRenderSupport;

    @Inject
    private Cookies _cookies;

    /**
     * The id used to generate a page-unique client-side identifier for the component. If a component renders multiple
     * times, a suffix will be appended to the to id to ensure uniqueness.
     */
    @Parameter(value = "prop:componentResources.id", defaultPrefix = BindingConstants.LITERAL)
    private String _clientId;

    /**
     * let you allow to restrict the direction of resizing.
     * 'vertical', 'horizontal' or empty for both
     */
    @Parameter(defaultPrefix = BindingConstants.LITERAL)
    private String _constraint;

    /**
     * if true, width and height of the resizable element persists via cookie.
     * default is 'false'
     */
    @Parameter(value = "false", defaultPrefix = BindingConstants.PROP)
    private boolean _persist;

    private String _assignedClientId;

    void setupRender()
    {
        _assignedClientId = _pageRenderSupport.allocateClientId(_clientId);
    }

    /**
     * Tapestry render phase method. End a tag here.
     *
     * @param writer the markup writer
     */
    void afterRender(MarkupWriter writer)
    {
        // start handle south
        writer.element("div", "id", "handle_" + _clientElement.getClientId(),
                       "class", "ck-resizable-handle");
        // end handle south
        writer.end();

        if (_persist)
        {
            int width = getIntValueFromCookie(_clientElement.getClientId() + ".width");
            int height = getIntValueFromCookie(_clientElement.getClientId() + ".height");

            if (width > 0)
                _pageRenderSupport.addScript("$('%s').style.width = '%dpx';", _clientElement.getClientId(), width);

            if (height > 0)
                _pageRenderSupport.addScript("$('%s').style.height = '%dpx';", _clientElement.getClientId(), height);
        }

        String jsString = "%s = new Resizable('%s', {handle:$('handle_%s')";
        if (_constraint != null)
            jsString += String.format(",constraint:'%s'", _constraint);
        jsString += ", persist:%s});";

        _pageRenderSupport.addScript(jsString, getClientId(), _clientElement.getClientId(), _clientElement.getClientId(), _persist);
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

    /**
     * get an integer value from cookie.
     *
     * @param key the key of the value
     *
     * @return the integer value
     */
    private int getIntValueFromCookie(String key)
    {
        int intValue = 0;
        String cookieValue = _cookies.readCookieValue(key);

        if (cookieValue != null)
        {
            try
            {
                intValue = Integer.parseInt(cookieValue);
            }
            catch (NumberFormatException e)
            {
                throw new RuntimeException(e);
            }
        }

        return intValue;
    }
}