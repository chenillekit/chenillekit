/*
 *  Apache License
 *  Version 2.0, January 2004
 *  http://www.apache.org/licenses/
 *
 *  Copyright 2008 by chenillekit.org
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 */

package org.chenillekit.tapestry.core.models;

import java.util.Map;

import org.apache.tapestry5.beaneditor.PropertyModel;
import org.apache.tapestry5.beaneditor.RelativePosition;
import org.apache.tapestry5.ioc.internal.util.CollectionFactory;
import org.apache.tapestry5.ioc.internal.util.Defense;
import org.apache.tapestry5.PropertyConduit;
import org.apache.tapestry5.internal.beaneditor.PropertyModelImpl;
import org.apache.tapestry5.internal.beaneditor.BeanEditorMessages;

/**
 * @author <a href="mailto:homburgs@googlemail.com">shomburg</a>
 * @version $Id$
 */
public class TabSetModel
{
    private final Map<String, PropertyModel> properties = CollectionFactory.newCaseInsensitiveMap();

    private void validateNewPropertyName(String propertyName)
    {
        Defense.notBlank(propertyName, "propertyName");

        if (properties.containsKey(propertyName))
            throw new RuntimeException(BeanEditorMessages.duplicatePropertyName(
                    beanType,
                    propertyName));
    }

    public PropertyModel add(RelativePosition position, String existingPanelId,
                             String panelId, PropertyConduit conduit)
    {
        Defense.notNull(position, "position");

        validateNewPropertyName(propertyName);

        // Locate the existing one.

        PropertyModel existing = get(existingPropertyName);

        // Use the case normalized property name.

        int pos = propertyNames.indexOf(existing.getPropertyName());

        PropertyModel newModel = new PropertyModelImpl(this, propertyName, conduit, messages);

        properties.put(propertyName, newModel);

        int offset = position == RelativePosition.AFTER ? 1 : 0;

        propertyNames.add(pos + offset, propertyName);

        return newModel;
    }

}
