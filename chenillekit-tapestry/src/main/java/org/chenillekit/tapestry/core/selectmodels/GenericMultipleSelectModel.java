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

package org.chenillekit.tapestry.core.selectmodels;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.tapestry5.OptionGroupModel;
import org.apache.tapestry5.OptionModel;
import org.apache.tapestry5.internal.OptionModelImpl;
import org.apache.tapestry5.ioc.internal.util.Defense;
import org.apache.tapestry5.ioc.services.PropertyAccess;
import org.apache.tapestry5.ioc.services.PropertyAdapter;
import org.apache.tapestry5.util.AbstractSelectModel;

import org.chenillekit.tapestry.core.encoders.MultipleValueEncoder;

/**
 * @author <a href="mailto:homburgs@googlemail.com">S.Homburg</a>
 * @version $Id: GenericMultipleSelectModel.java 700 2008-06-30 13:56:56Z homburgs $
 */
public class GenericMultipleSelectModel<T> extends AbstractSelectModel implements MultipleValueEncoder<T>
{
    private List<PropertyAdapter> _labelFieldAdapters = new ArrayList<PropertyAdapter>();
    private PropertyAdapter _idFieldAdapter;
    private List<T> _selectables;

    public GenericMultipleSelectModel(List<T> selectables, Class clasz, String labelPropertyName, String valuePropertyName, PropertyAccess access)
    {
        Defense.notBlank(labelPropertyName, "labelField");
        _selectables = selectables;

        if (valuePropertyName != null)
            _idFieldAdapter = access.getAdapter(clasz).getPropertyAdapter(valuePropertyName);

        if (labelPropertyName != null)
        {
            String[] labels = labelPropertyName.split(",");
            for (String label : labels)
                _labelFieldAdapters.add(access.getAdapter(clasz).getPropertyAdapter(label));
        }
    }

    public List<OptionGroupModel> getOptionGroups()
    {
        return null;
    }

    public List<OptionModel> getOptions()
    {
        List<OptionModel> optionModelList = new ArrayList<OptionModel>();

        if (_labelFieldAdapters.size() == 0)
        {
            for (T obj : _selectables)
                optionModelList.add(new OptionModelImpl(nvl(obj), obj));
        }
        else
        {
            for (T obj : _selectables)
            {
                String label = "";
                for (int i = 0; i < _labelFieldAdapters.size(); i++)
                {
                    PropertyAdapter propertyAdapter = _labelFieldAdapters.get(i);
                    if (i > 0)
                        label += "|" + nvl(propertyAdapter.get(obj));
                    else
                        label += nvl(propertyAdapter.get(obj));
                }

                optionModelList.add(new OptionModelImpl(label, obj));
            }
        }

        return optionModelList;
    }

    // ValueEncoder functions
    public String toClient(T obj)
    {
        if (_idFieldAdapter == null)
            return obj + "";
        else
            return _idFieldAdapter.get(obj) + "";
    }

    public List<T> toValue(String[] strings)
    {
        try
        {
            List<String> selectedList = Arrays.asList(strings);
            List<T> valueList = new ArrayList<T>();
            for (String selected : selectedList)
            {
                for (T value : _selectables)
                {
                    if (selected.equals(_idFieldAdapter.get(value).toString()))
                        valueList.add(value);
                }
            }
            return valueList;
        }
        catch (NullPointerException e)
        {
            return new ArrayList<T>();
        }
    }

    private String nvl(Object o)
    {
        if (o == null)
            return "";
        else
            return o.toString();
    }
}