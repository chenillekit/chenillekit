/*
 * Apache License
 * Version 2.0, January 2004
 * http://www.apache.org/licenses/
 *
 * Copyright 1996-2008 by Sven Homburg
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 */

package org.chenillekit.tapestry.core.bindings;

import java.util.ArrayList;
import java.util.List;

import org.apache.tapestry5.Binding;
import org.apache.tapestry5.internal.bindings.AbstractBinding;
import org.apache.tapestry5.ioc.Location;

/**
 * Binding type for collection values.
 * This binding called by expression "list:".
 *
 * @version $Id$
 */
public class ListBinding extends AbstractBinding
{
    private final List<Binding> _bindings;
    private final boolean _invariant;

    public ListBinding(Location location, List<Binding> bindings, boolean invariant)
    {
        super(location);

        _bindings = bindings;
        _invariant = invariant;
    }

    public Object get()
    {
        List<Object> values = new ArrayList<Object>(_bindings.size());
        for (Binding binding : _bindings)
            values.add(binding.get());

        return values.toArray();
    }

    @Override
    public boolean isInvariant()
    {
        return this._invariant;
    }

    @Override
    public Class<Object[]> getBindingType()
    {
        return Object[].class;
    }
}