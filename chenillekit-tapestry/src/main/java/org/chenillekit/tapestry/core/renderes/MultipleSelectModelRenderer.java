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

package org.chenillekit.tapestry.core.renderes;

import java.util.Map;

import org.apache.tapestry5.MarkupWriter;
import org.apache.tapestry5.OptionGroupModel;
import org.apache.tapestry5.OptionModel;
import org.apache.tapestry5.SelectModelVisitor;
import org.apache.tapestry5.ioc.internal.util.TapestryException;

import org.chenillekit.tapestry.core.encoders.MultipleValueEncoder;

/**
 * @author <a href="mailto:homburgs@googlemail.com">S.Homburg</a>
 * @version $Id: MultipleSelectModelRenderer.java 682 2008-05-20 22:00:02Z homburgs $
 */
public class MultipleSelectModelRenderer implements SelectModelVisitor
{
    private final MarkupWriter _writer;

    private final MultipleValueEncoder _encoder;

    public MultipleSelectModelRenderer(final MarkupWriter writer, MultipleValueEncoder encoder)
    {
        _writer = writer;
        _encoder = encoder;
    }

    public void beginOptionGroup(OptionGroupModel groupModel)
    {
        _writer.element("optgroup", "label", groupModel.getLabel());

        writeDisabled(groupModel.isDisabled());
        writeAttributes(groupModel.getAttributes());
    }

    public void endOptionGroup(OptionGroupModel groupModel)
    {
        _writer.end(); // select
    }

    @SuppressWarnings("unchecked")
    public void option(OptionModel optionModel)
    {
        Object optionValue = optionModel.getValue();

        if (_encoder == null)
            throw new TapestryException("value encoder cannot be null", this, null);

        String clientValue = _encoder.toClient(optionValue);

        _writer.element("option", "value", clientValue);

        if (isOptionSelected(optionModel))
            _writer.attributes("selected", "selected");

        writeDisabled(optionModel.isDisabled());
        writeAttributes(optionModel.getAttributes());

        _writer.write(optionModel.getLabel());

        _writer.end();
    }

    private void writeDisabled(boolean disabled)
    {
        if (disabled) _writer.attributes("disabled", "disabled");
    }

    private void writeAttributes(Map<String, String> attributes)
    {
        if (attributes == null) return;

        for (Map.Entry<String, String> e : attributes.entrySet())
            _writer.attributes(e.getKey(), e.getValue());
    }

    protected boolean isOptionSelected(OptionModel optionModel)
    {
        return false;
    }
}
