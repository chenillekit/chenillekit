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
import java.util.Iterator;
import java.util.List;

/**
 * Provides an implementation that will only iterate
 * over the objects within the range provided when prepare() is called.
 *
 * @author Tod Orr
 * @version $Id$
 */
public class PagedSource<T> implements Iterable<T>
{
    private List<T> _source = new ArrayList<T>();

    private List<T> _pageSource = new ArrayList<T>();

    private Integer _iterableSize;

    public PagedSource(Iterable<T> source)
    {
        for (T aSource : source)
            _source.add(aSource);
    }

    /**
     * @return
     *
     * @see java.lang.Iterable#iterator()
     */
    public Iterator<T> iterator()
    {
        return _pageSource.iterator();
    }

    public int getTotalRowCount()
    {
        if (_iterableSize != null)
            return _iterableSize;

        _iterableSize = 0;

        Iterator<?> it = _source.iterator();
        while (it.hasNext())
        {
            it.next();
            _iterableSize++;
        }

        return _iterableSize;
    }

    public void prepare(int startIndex, int endIndex)
    {
        for (int i = startIndex; i <= endIndex; i++)
        {
            _pageSource.add(_source.get(i));
        }
    }

}
