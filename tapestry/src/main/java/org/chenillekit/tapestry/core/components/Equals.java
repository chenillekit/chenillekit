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

import org.apache.tapestry5.Block;
import org.apache.tapestry5.annotations.Parameter;

/**
 * Conditionally renders its body.
 *
 * @author <a href="mailto:florian.bw@gmail.com">F.Breitwieser</a>
 * @version $Id$
 */
public class Equals
{
    /**
     * If true, then the body of the If component is rendered.
     * If false, the body is omitted.
     */
    @Parameter(required = true)
    private Object _left;

    @Parameter(required = true)
    private Object _right;

    /**
     * Optional parameter to invert the test.
     * If true, then the body is rendered when the test parameter is false (not true).
     */
    @Parameter
    private boolean _negate;

    /**
     * An alternate {@link org.apache.tapestry5.Block} to render if the test parameter is false.
     * The default, null, means render nothing in that situation.
     */
    @Parameter
    private Block _else;

    /**
     * Returns null if the test parameter is true, which allows normal
     * rendering (of the body). If
     * the test parameter is false, returns the else parameter (this
     * may also be null).
     */
    Object beginRender()
    {
        if (_left == null)
        {
            if (_right == null)
                return null;
            else
                return _else;
        }

        return _left.equals(_right) != _negate ? null : _else;
    }

    /**
     * If the test parameter is true, then the body is rendered,
     * otherwise not. The component does
     * not have a template or do any other rendering besides its body.
     */
    boolean beforeRenderBody()
    {
        if (_left == null)
            return _right == null;

        return _left.equals(_right) != _negate;
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
        _left = left;
        _right = right;
        _negate = negate;
        _else = elseBlock;
    }
}
