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

package org.chenillekit.tapestry.core.pages;

import java.util.Collections;
import java.util.List;

import org.apache.tapestry5.ioc.internal.util.CollectionFactory;

/**
 * @author <a href="mailto:homburgs@gmail.com">S.Homburg</a>
 * @version $Id$
 */
public class Start
{
    public static class Item implements Comparable<Item>
    {
        private final String _pageName;
        private final String _label;
        private final String _description;

        public Item(String pageName, String label, String description)
        {
            _pageName = pageName;
            _label = label;
            _description = description;
        }

        public String getPageName()
        {
            return _pageName;
        }

        public String getLabel()
        {
            return _label;
        }

        public String getDescription()
        {
            return _description;
        }

        public int compareTo(Item o)
        {
            return _label.compareTo(o._label);
        }
    }

    private static final List<Item> ITEMS = CollectionFactory.newList(
            new Item("OnEventDemo", "OnEvent", "tests OnEvent mixin"),
            new Item("AccordionDemo", "Accordion", "tests Accordion component"),
            new Item("FormaterDemo", "Formater", "tests Formater components"),
            new Item("ContainsDemo", "Contains", "tests Contains component"),
            new Item("EqualsDemo", "Equals", "tests Equals component"),
            new Item("FieldSetDemo", "FieldSet", "tests FieldSet component"),
            new Item("ElementDemo", "Element", "tests Element component"),
            new Item("HiddenDemo", "Hidden", "tests Hidden component"),
            new Item("InPlaceDemo", "InPlace", "tests InPlace components"),
            new Item("TabSetDemo", "TabSet", "tests TabSet component"),
            new Item("ThumbNailDemo", "ThumbNail", "tests ThumbNail component"),
            new Item("DateTimeFieldDemo", "DateTimeField", "tests DateTimeField component")
    );

    static
    {
        Collections.sort(ITEMS);
    }

    private Item _item;

    public List<Item> getItems()
    {
        return ITEMS;
    }

    public Item getItem()
    {
        return _item;
    }

    public void setItem(Item item)
    {
        _item = item;
    }
}
