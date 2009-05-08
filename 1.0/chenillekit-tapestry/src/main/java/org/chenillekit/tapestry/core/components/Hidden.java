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
import org.apache.tapestry5.corelib.base.AbstractTextField;

/**
 * place a hidden field into a form.
 *
 * @version $Id$
 */
public class Hidden extends AbstractTextField
{
    /**
     * Invoked from {@link #begin(org.apache.tapestry5.MarkupWriter)} to write out the element and attributes (typically, &lt;input&gt;). The
     * {@linkplain org.apache.tapestry5.corelib.base.AbstractField#getControlName() controlName} and {@linkplain org.apache.tapestry5.corelib.base.AbstractField#getClientId() clientId}
     * properties will already have been set or updated.
     * <p/>
     * Generally, the subclass will invoke {@link org.apache.tapestry5.MarkupWriter#element(String, Object[])}, and will be responsible for
     * including an {@link org.apache.tapestry5.annotations.AfterRender} phase method to invoke {@link org.apache.tapestry5.MarkupWriter#end()}.
     *
     * @param writer markup write to send output to
     * @param value  the value (either obtained and translated from the value parameter, or obtained from the tracker)
     */
    @Override
    protected void writeFieldTag(MarkupWriter writer, String value)
    {
        writer.element("input",

                       "type", "hidden",

                       "id", getClientId(),

                       "name", getControlName(),

                       "value", value);
    }

    final void afterRender(MarkupWriter writer)
    {
        writer.end(); // input
    }
}
