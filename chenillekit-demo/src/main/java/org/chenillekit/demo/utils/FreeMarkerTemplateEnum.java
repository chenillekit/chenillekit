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

package org.chenillekit.demo.utils;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

/**
 * @author <a href="mailto:shomburg@depot120.dpd.de">S.Homburg</a>
 * @version $Id$
 */
public enum FreeMarkerTemplateEnum implements TemplateEnum
{
    FREEMARKER1("freemarker1.fmt");

    private static final Map<String, FreeMarkerTemplateEnum> lookup = new HashMap<String, FreeMarkerTemplateEnum>();

    static
    {
        for (FreeMarkerTemplateEnum s : EnumSet.allOf(FreeMarkerTemplateEnum.class))
            lookup.put(s.getName(), s);
    }

    private String _name;

    private FreeMarkerTemplateEnum(String name)
    {
        _name = name;
    }

    public String getName()
    {
        return _name;
    }

    public String getId()
    {
        return _name;
    }

    public static FreeMarkerTemplateEnum get(String name)
    {
        return lookup.get(name);
    }

}