/*
 * Copyright (c) 2008, Your Corporation. All Rights Reserved.
 */

package org.chenillekit.tapestry.core.factories;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.tapestry5.Binding;
import org.apache.tapestry5.BindingConstants;
import org.apache.tapestry5.ComponentResources;
import org.apache.tapestry5.ioc.Location;
import org.apache.tapestry5.ioc.internal.util.TapestryException;
import org.apache.tapestry5.services.BindingFactory;
import org.apache.tapestry5.services.BindingSource;

import org.chenillekit.tapestry.core.bindings.ListBinding;

/**
 * Binding factory where the expression is a collection type.
 *
 * @version $Id$
 */
public class LoopBindingFactory implements BindingFactory
{
    private final BindingSource _bindingSource;

    public LoopBindingFactory(BindingSource bindingSource)
    {
        _bindingSource = bindingSource;
    }

    public Binding newBinding(String description, ComponentResources container, ComponentResources component,
                              String expression, Location location)
    {
        List<String> parts = Arrays.asList(expression.split(","));
        ArrayList<Binding> bindings = new ArrayList<Binding>(parts.size());

        int startBy = 0;
        int endBy = 0;
        int stepBy = 1;

        if (parts.size() < 1)
            throw new TapestryException("not enough parameters for loop binding", this, null);

        if (parts.size() >= 3)
            stepBy = Integer.parseInt(parts.get(2));

        if (parts.size() >= 2)
        {
            endBy = Integer.parseInt(parts.get(1));
            startBy = Integer.parseInt(parts.get(0));
        }
        else if (parts.size() == 1)
            endBy = Integer.parseInt(parts.get(0));

        try
        {
            for (int x = startBy; x <= endBy; x += stepBy)
                bindings.add(_bindingSource.newBinding(description, container, component, BindingConstants.LITERAL, Integer.toString(x), location));
        }
        catch (OutOfMemoryError e)
        {
            throw new TapestryException(e.getLocalizedMessage(), this, null);
        }

        boolean invariant = true;
        for (Binding binding : bindings)
        {
            if (!binding.isInvariant())
            {
                invariant = false;
                break;
            }
        }

        return new ListBinding(location, bindings, invariant);
    }
}