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

/**
 * Conditionally renders its body.
 *
 * @version $Id$
 */
public class Equals
{
    /**
     * If true, then the body of the If component is rendered.
     * If false, the body is omitted.
     */
    @Parameter(required = true)
    private Object left;

    @Parameter(required = true)
    private Object right;

    /**
     * Optional parameter to invert the test.
     * If true, then the body is rendered when the test parameter is false (not true).
     */
    @Parameter
    private boolean negate;

    /**
     * An alternate {@link org.apache.tapestry5.Block} to render if the test parameter is false.
     * The default, null, means render nothing in that situation.
     */
    @Parameter(name = "else")
    private Block elseBlock;

    /**
     * Returns null if the test parameter is true, which allows normal
     * rendering (of the body). If
     * the test parameter is false, returns the else parameter (this
     * may also be null).
     */
    Object beginRender()
    {
        if (left == null)
        {
            if (right == null)
                return null;
            else
                return elseBlock;
        }

        return left.equals(right) != negate ? null : elseBlock;
    }

    /**
     * If the test parameter is true, then the body is rendered,
     * otherwise not. The component does
     * not have a template or do any other rendering besides its body.
     */
    boolean beforeRenderBody()
    {
        if (left == null)
            return right == null;

        return left.equals(right) != negate;
    }

    /**
     * only for testing.
     *
     * @param left
     * @param right
     * @param negate
     * @param elseBlock
     */
    void setup(Object left, Object right, boolean negate, Block elseBlock)
    {
        this.left = left;
        this.right = right;
        this.negate = negate;
        this.elseBlock = elseBlock;
    }
}
