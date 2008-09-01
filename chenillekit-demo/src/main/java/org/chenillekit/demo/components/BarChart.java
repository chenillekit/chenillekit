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

package org.chenillekit.demo.components;

import java.util.Arrays;
import java.util.List;

import org.apache.tapestry5.ioc.internal.util.CollectionFactory;
import org.apache.tapestry5.json.JSONArray;
import org.apache.tapestry5.json.JSONObject;

import org.chenillekit.tapestry.core.components.Chart;
import org.chenillekit.tapestry.core.utils.XYDataItem;

/**
 * @author <a href="mailto:homburgs@googlemail.com">shomburg</a>
 * @version $Id$
 */
public class BarChart extends Chart
{
    private List<List<XYDataItem>> getTestData()
    {
        List<List<XYDataItem>> dataList = CollectionFactory.newList();
        List<XYDataItem> list1 = CollectionFactory.newList();
        List<XYDataItem> list2 = CollectionFactory.newList();

        list1.add(new XYDataItem(0, 0.5));
        list1.add(new XYDataItem(1, 0.6));
        list1.add(new XYDataItem(2, 1.8));
        list1.add(new XYDataItem(3, 0.9));
        list1.add(new XYDataItem(4, 2));

        list2.add(new XYDataItem(0, 1.5));
        list2.add(new XYDataItem(1, 2));
        list2.add(new XYDataItem(2, 4.5));
        list2.add(new XYDataItem(3, 3.5));
        list2.add(new XYDataItem(4, 5.5));

        dataList.add(list1);
        dataList.add(list2);

        return dataList;
    }

    /**
     * Invoked to allow subclasses to further configure the parameters passed to this component's javascript
     * options. Subclasses may override this method to configure additional features of the Flotr library.
     *
     * @param config parameters object
     */
    protected void configure(JSONObject config)
    {
        String dataString = "[";
        List<List<XYDataItem>> dataItemsList = getTestData();

        for (int i = 0; i < dataItemsList.size(); i++)
        {
            List<XYDataItem> dataItems = dataItemsList.get(i);
            dataString += String.format("{data: %s, label: 'data array %d'}", Arrays.toString(dataItems.toArray()), i);
            if (i < dataItemsList.size() - 1)
                dataString += ",";
        }

        dataString += "]";

        config.put("data", new JSONArray(dataString));
        JSONObject options = new JSONObject();

        options.put("legend", new JSONObject("{position: 'nw'}"));
        options.put("bars", new JSONObject("{show: true}"));

        config.put("options", options);
    }
}