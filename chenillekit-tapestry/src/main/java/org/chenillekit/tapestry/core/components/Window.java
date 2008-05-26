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

import org.apache.tapestry5.MarkupWriter;
import org.apache.tapestry5.json.JSONObject;

import org.chenillekit.tapestry.core.base.AbstractWindow;

/**
 * creates a window based on jvascript <a href="http://prototype-window.xilinus.com/">window</a> library.
 *
 * @author <a href="mailto:homburgs@gmail.com">S.Homburg</a>
 * @version $Id: Window.java 682 2008-05-20 22:00:02Z homburgs $
 */
public class Window extends AbstractWindow
{
    /**
     * Tapestry render phase method. End a tag here.
     *
     * @param writer the markup writer
     */
    void afterRender(MarkupWriter writer)
    {
        JSONObject options = new JSONObject();

        options.put("className", getClassName());
        options.put("width", getWidth());
        options.put("height", getHeight());

        //
        // Let subclasses do more.
        //
        configure(options);

        getPageRenderSupport().addScript("%s = new Window(%s);", getClientId(), options);
        if (isShow())
            getPageRenderSupport().addScript("%s.show%s(%s);", getClientId(), isCenter() ? "Center" : "", isModal());
    }
}