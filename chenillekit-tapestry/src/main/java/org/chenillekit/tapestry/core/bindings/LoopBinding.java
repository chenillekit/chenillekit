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
 * @version $Id: LoopBinding.java 682 2008-05-20 22:00:02Z homburgs $
 */
public class LoopBinding extends ListBinding
{
    public LoopBinding(Location location, List<Binding> bindings, boolean invariant)
    {
        super(location, bindings, invariant);
    }
}