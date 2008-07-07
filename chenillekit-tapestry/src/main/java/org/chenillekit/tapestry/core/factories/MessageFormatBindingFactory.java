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

package org.chenillekit.tapestry.core.factories;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.tapestry5.Binding;
import org.apache.tapestry5.BindingConstants;
import org.apache.tapestry5.ComponentResources;
import org.apache.tapestry5.ioc.Location;
import org.apache.tapestry5.services.BindingFactory;
import org.apache.tapestry5.services.BindingSource;

import org.chenillekit.tapestry.core.bindings.MessageFormatBinding;

/**
 * Binding factory where the expression is a collection type.
 *
 * @author <a href="mailto:homburgs@googlemail.com">S.Homburg</a>
 * @version $Id: MessageFormatBindingFactory.java 682 2008-05-20 22:00:02Z homburgs $
 */
public class MessageFormatBindingFactory implements BindingFactory
{
    private final BindingSource _bindingSource;

    public MessageFormatBindingFactory(BindingSource bindingSource)
    {
        _bindingSource = bindingSource;
    }

    public Binding newBinding(String description, ComponentResources container, ComponentResources component,
                              String expression, Location location)
    {
        String messageKey = expression.substring(0, expression.indexOf('='));
        List<String> parts = Arrays.asList(expression.substring(expression.indexOf('=') + 1).split(","));

        ArrayList<Binding> bindings = new ArrayList<Binding>(parts.size());
        for (String part : parts)
        {
            String prefix = BindingConstants.PROP;
            part = part.trim();

            if ('\'' == part.charAt(0) && '\'' == part.charAt(part.length() - 1))
            {
                part = part.substring(1, part.length() - 1);
                prefix = BindingConstants.LITERAL;
            }

            bindings.add(_bindingSource.newBinding(description, container, component, prefix, part, location));
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

        return new MessageFormatBinding(location, messageKey, component.getMessages(), bindings, invariant);
    }
}
