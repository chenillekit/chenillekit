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

package org.chenillekit.google.utils;

import java.util.ArrayList;
import java.util.Collection;

/**
 * @author <a href="mailto:homburgs@googlemail.com">shomburg</a>
 * @version $Id$
 */
public class GeoCodeResultList extends ArrayList<GeoCodeResult>
{
    public int minAccuracy = 7;

    /**
     * Constructs an empty list with the specified initial capacity.
     *
     * @param initialCapacity the initial capacity of the list.
     *
     * @throws IllegalArgumentException if the specified initial capacity
     *                                  is negative
     */
    public GeoCodeResultList(int initialCapacity)
    {
        super(initialCapacity);
    }

    /**
     * Constructs an empty list with an initial capacity of ten.
     */
    public GeoCodeResultList()
    {
    }

    /**
     * Constructs a list containing the elements of the specified
     * collection, in the order they are returned by the collection's
     * iterator.  The <tt>ArrayList</tt> instance has an initial capacity of
     * 110% the size of the specified collection.
     *
     * @param c the collection whose elements are to be placed into this list.
     *
     * @throws NullPointerException if the specified collection is null.
     */
    public GeoCodeResultList(Collection<GeoCodeResult> c)
    {
        super(c);
    }

    public int goodMatches()
    {
        int goodMatches = 0;

        for (GeoCodeResult geoCodeResult : (Iterable<GeoCodeResult>) this)
        {
            if (geoCodeResult.getHttpResult() == GeoCodeResult.G_GEO_SUCCESS && minAccuracy <= geoCodeResult.getAccuracy())
                goodMatches++;
        }

        return goodMatches;
    }

    /**
     * set the min. accuracy to mark a {@link GeoCodeResult} as a good match
     *
     * @param minAccuracy default is 7 (Intersection level accuracy.)
     * @see GeoCodeResult
     */
    public void setMinAccuracyForGoodMatch(int minAccuracy)
    {
        this.minAccuracy = minAccuracy;
    }
}
