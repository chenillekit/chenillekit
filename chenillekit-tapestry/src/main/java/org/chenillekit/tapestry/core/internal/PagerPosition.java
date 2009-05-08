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

package org.chenillekit.tapestry.core.internal;

/**
 * @version $Id$
 */
public enum PagerPosition
{
    /**
     * Position the pager above the paged content.
     */
    TOP(true, false),

    /**
     * Position the pager below the paged content (this is the default).
     */
    BOTTOM(false, true),

    /**
     * Show the pager above and below the paged content.
     */
    BOTH(true, true),

    /**
     * Don't show a pager (the application will need to supply its own
     * navigation mechanism).
     */
    NONE(false, false);

    private final boolean _matchTop;

    private final boolean _matchBottom;

    private PagerPosition(boolean matchTop, boolean matchBottom)
    {
        _matchTop = matchTop;
        _matchBottom = matchBottom;
    }

    public boolean isMatchBottom()
    {
        return _matchBottom;
    }

    public boolean isMatchTop()
    {
        return _matchTop;
    }
}
