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

import java.util.ArrayList;
import java.util.List;

import org.apache.tapestry5.OptionGroupModel;
import org.apache.tapestry5.OptionModel;
import org.apache.tapestry5.internal.OptionModelImpl;
import org.apache.tapestry5.ioc.services.PropertyAccess;
import org.apache.tapestry5.util.AbstractSelectModel;

/**
 * @version $Id: GenericSelectionModel.java 682 2008-05-20 22:00:02Z homburgs $
 */
public class GenericSelectionModel<T> extends AbstractSelectModel
{
    private String _labelField;
    private List<T> _list;
    private final PropertyAccess _adapter;

    public GenericSelectionModel(List<T> list, String labelField, PropertyAccess adapter)
    {
        _labelField = labelField;
        _list = list;
        _adapter = adapter;
    }

    public List<OptionGroupModel> getOptionGroups()
    {
        return null;
    }

    public List<OptionModel> getOptions()
    {
        List<OptionModel> optionModelList = new ArrayList<OptionModel>();
        for (T obj : _list)
        {
            if (_labelField == null)
                optionModelList.add(new OptionModelImpl(obj + "", obj));
            else
                optionModelList.add(new OptionModelImpl(_adapter.get(obj, _labelField) + "", obj));
        }
        return optionModelList;
    }
}
