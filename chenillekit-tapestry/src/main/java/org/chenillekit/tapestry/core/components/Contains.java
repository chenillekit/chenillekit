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

import org.apache.tapestry5.Block;
import org.apache.tapestry5.annotations.Parameter;

import java.util.List;

/**
 * Conditionally renders its body.
 * <p/>
 * If the value object is in the list, then the body of the Contains
 * component is rendered. If not, the body is omitted and the else block
 * is rendered.
 *
 * @version $Id$
 */
public class Contains
{
    /**
     * Value which might be contained in the list.
     */
    @Parameter(required = true)
    private Object value;

    /**
     * List, which might contain the object.
     */
    @Parameter(required = true)
    private List<Object> list;

    /**
     * Optional parameter to invert the test. If true, then the body
     * is rendered when the test
     * parameter is false (not true).
     */
    @Parameter
    private boolean negate;

    /**
     * An alternate {@link org.apache.tapestry5.Block} to render if the test parameter is
     * false. The default, null, means
     * render nothing in that situation.
     */
    @Parameter(name = "else")
    private Block elseBlock;

    /**
     * Returns null if the list contains the object, which allows
     * normal rendering (of the body). If
     * the test parameter is false, returns the else parameter (this
     * may also be null).
     */
    Object beginRender()
    {
        return (list != null && list.contains(value)) != negate ? null : elseBlock;
    }

    /**
     * If the the list contains the object, then the body is rendered,
     * otherwise not. The component does
     * not have a template or do any other rendering besides its body.
     */
    boolean beforeRenderBody()
    {
        return (list != null && list.contains(value)) != negate;
    }

    void setup(Object value, List<Object> list, boolean negate, Block elseBlock)
    {
        this.value = value;
        this.list = list;
        this.negate = negate;
        this.elseBlock = elseBlock;
    }
}
