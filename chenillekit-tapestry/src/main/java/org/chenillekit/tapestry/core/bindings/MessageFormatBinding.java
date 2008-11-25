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
import org.apache.tapestry5.ioc.Messages;

/**
 * Binding type for collection values.
 * This binding called by expression "messageformat:".
 *
 * @version $Id: MessageFormatBinding.java 682 2008-05-20 22:00:02Z homburgs $
 */
public class MessageFormatBinding extends AbstractBinding
{
    private final String _messageKey;
    private final Messages _messages;
    private final List<Binding> _bindings;
    private final boolean _invariant;

    public MessageFormatBinding(Location location, String messageKey, Messages messages, List<Binding> bindings, boolean invariant)
    {
        super(location);

        _messageKey = messageKey;
        _messages = messages;
        _bindings = bindings;
        _invariant = invariant;
    }

    public Object get()
    {
        List<Object> values = new ArrayList<Object>(_bindings.size());
        for (Binding binding : _bindings)
            values.add(binding.get());

        return _messages.format(_messageKey, values.toArray());
    }

    @Override
    public boolean isInvariant()
    {
        return this._invariant;
    }

    @Override
    public Class getBindingType()
    {
        return String.class;
    }
}
