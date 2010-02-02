/*
 * Apache License
 * Version 2.0, January 2004
 * http://www.apache.org/licenses/
 *
 * Copyright 2010 by chenillekit.org
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 */

package org.chenillekit.access.pages;

import org.apache.tapestry5.ioc.internal.util.CollectionFactory;

import java.util.Collections;
import java.util.List;

/**
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
            new Item("RestrictedPage", "Restricted", "tests Restricted page"),
            new Item("UnRestrictedPage", "UnRestricted", "tests UnRestricted page"),
            new Item("RestrictedTextField", "RestrictedTextField", "tests RestrictedTextField page"),
            new Item("NotEnoughRights", "NotEnoughRights", "test Restricted page without rights to access"),
            new Item("ManagedRestrictedPage", "ManagedRestrictedPage", "test ManagedRestricted page without rights to access"),
            new Item("Invisible", "Invisible", "Invisible page"),
            new Item("Logout", "Logout", "Logout user"),
            new Item("Login", "Login", "Login access form")
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
