/*
 * Copyright (c) 2008, Your Corporation. All Rights Reserved.
 */

package org.chenillekit.tapestry.core.bindings;

import java.util.List;

import org.apache.tapestry5.Binding;
import org.apache.tapestry5.ioc.Location;

/**
 * Binding type for collection values.
 * This binding called by expression "loop:".
 *
 * @version $Id$
 */
public class LoopBinding extends ListBinding
{
    public LoopBinding(Location location, List<Binding> bindings, boolean invariant)
    {
        super(location, bindings, invariant);
    }
}