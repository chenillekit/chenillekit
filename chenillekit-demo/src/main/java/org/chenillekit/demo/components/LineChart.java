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

package org.chenillekit.demo.components;

import org.apache.tapestry5.json.JSONObject;

import org.chenillekit.tapestry.core.components.Chart;

/**
 * @author <a href="mailto:homburgs@googlemail.com">shomburg</a>
 * @version $Id$
 */
public class LineChart extends Chart
{
    /**
     * Invoked to allow subclasses to further configure the parameters passed to this component's javascript
     * options. Subclasses may override this method to configure additional features of the Flotr library.
     *
     * @param config parameters object
     */
    protected void configure(JSONObject config)
    {
        JSONObject options = new JSONObject();

        options.put("mouse", new JSONObject("{track: true, color: purple, sensibility: 1, trackDecimals: 1}"));
        options.put("points", new JSONObject("{show: true}"));
        options.put("lines", new JSONObject("{show: true}"));

        config.put("options", options);
    }
}
