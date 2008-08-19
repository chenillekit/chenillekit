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

import org.apache.tapestry5.ComponentResources;
import org.apache.tapestry5.Link;
import org.apache.tapestry5.MarkupWriter;
import org.apache.tapestry5.RenderSupport;
import org.apache.tapestry5.annotations.Environmental;
import org.apache.tapestry5.annotations.IncludeJavaScriptLibrary;
import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.corelib.components.Checkbox;
import org.apache.tapestry5.ioc.annotations.Inject;

/**
 * A "ajaxed" <a href="http://tapestry.apache.org/t5components/tapestry-core/component-parameters.html#orgapachetapestrycorelibcomponentscheckbox">Checkbox</a> component.
 *
 * @author <a href="mailto:homburgs@gmail.com">S.Homburg</a>
 * @version $Id$
 */
@IncludeJavaScriptLibrary(value = {"AjaxCheckbox.js"})
public class AjaxCheckbox extends Checkbox
{
    /**
     * The value of this constant is {@value}.
     */
    public static final String EVENT_NAME = "checkboxclicked";

    /**
     * the javascript callback function (optional).
     * function has the response text as parameter
     */
    @Parameter(required = false, defaultPrefix = "literal")
    private String _onCompleteCallback;

    @Inject
    private ComponentResources _resources;

    @Environmental
    private RenderSupport _pageRenderSupport;

    /**
     * Mixin afterRender phrase occurs after the component itself. This is where we write the &lt;div&gt;
     * element and the JavaScript.
     *
     * @param writer
     */
    void afterRender(MarkupWriter writer)
    {
        Link link = _resources.createEventLink(EVENT_NAME);
        String ajaxString = "new Ck.AjaxCheckbox('%s', '%s'";

        if (_onCompleteCallback != null)
            ajaxString += ",'" + _onCompleteCallback + "'";

        ajaxString += ");";

        _pageRenderSupport.addScript(ajaxString, getClientId(), link.toAbsoluteURI());
    }

}
