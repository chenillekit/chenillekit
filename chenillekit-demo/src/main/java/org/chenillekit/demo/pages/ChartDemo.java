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

package org.chenillekit.demo.pages;

import java.util.List;

import org.apache.tapestry5.annotations.Cached;
import org.apache.tapestry5.annotations.Component;
import org.apache.tapestry5.ioc.internal.util.CollectionFactory;

import org.chenillekit.demo.components.BarChart;
import org.chenillekit.demo.components.LeftSideMenu;
import org.chenillekit.demo.components.LineChart;
import org.chenillekit.tapestry.core.components.Chart;
import org.chenillekit.tapestry.core.utils.XYDataItem;

/**
 * @author <a href="mailto:mlusetti@gmail.com">Massimo Lusetti</a>
 * @version $Id$
 */
public class ChartDemo
{
    @Component(parameters = {"menuName=demo"})
    private LeftSideMenu menu;

    private List<List<XYDataItem>> testData;

    @Component(parameters = {"dataItems=testData"})
    private Chart chart1;

    @Component
    private BarChart chart2;

    @Component(parameters = {"dataItems=testData"})
    private LineChart chart3;

    @Cached
    public List getTestData()
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
}