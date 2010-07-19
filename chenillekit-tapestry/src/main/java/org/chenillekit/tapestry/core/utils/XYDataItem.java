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

package org.chenillekit.tapestry.core.utils;

import java.io.Serializable;

/**
 * @version $Id$
 */
public class XYDataItem implements Serializable, Comparable
{
    private static final long serialVersionUID = 2751513470325494890L;

    /**
     * The x-value.
     */
    private Number _xValue;

    /**
     * The y-value.
     */
    private Number _yValue;

    /**
     * Constructs a new data item.
     *
     * @param xValue the x-value.
     * @param yValue the y-value.
     */
    public XYDataItem(Number xValue, Number yValue)
    {
        assert xValue != null;
        assert yValue != null;

        _xValue = xValue;
        _yValue = yValue;
    }

    /**
     * Returns the x-value.
     *
     * @return The x-value.
     */
    public Number getXValue()
    {
        return _xValue;
    }

    /**
     * Returns the y-value.
     *
     * @return The y-value.
     */
    public Number getYValue()
    {
        return _yValue;
    }

    /**
     * Sets the y-value for this data item.
     *
     * @param yValue the new y-value.
     */
    public void setYValue(Number yValue)
    {
        assert yValue != null;
        _yValue = yValue;
    }

    /**
     * Sets the x-value for this data item.
     *
     * @param xValue the new x-value.
     */
    public void setXValue(Number xValue)
    {
        assert xValue != null;
        _xValue = xValue;
    }

    /**
     * Tests if this object is equal to another.
     *
     * @param obj the object to test against for equality (<code>null</code>
     *            permitted).
     *
     * @return A boolean.
     */
    public boolean equals(Object obj)
    {
        if (this == obj) return true;
        if (!(obj instanceof XYDataItem)) return false;

        XYDataItem that = (XYDataItem) obj;

        if (_xValue != null ? !_xValue.equals(that._xValue) : that._xValue != null) return false;
        if (_yValue != null ? !_yValue.equals(that._yValue) : that._yValue != null) return false;

        return true;
    }

    /**
     * Returns a hash code.
     *
     * @return A hash code.
     */
    public int hashCode()
    {
        int result;
        result = (_xValue != null ? _xValue.hashCode() : 0);
        result = 31 * result + (_yValue != null ? _yValue.hashCode() : 0);
        return result;
    }

    /**
     * Returns an integer indicating the order of this object relative to
     * another object.
     * <p/>
     * For the order we consider only the x-value:
     * negative == "less-than", zero == "equal", positive == "greater-than".
     *
     * @param o1 the object being compared to.
     *
     * @return An integer indicating the order of this data pair object
     *         relative to another object.
     */
    public int compareTo(Object o1)
    {

        int result;

        // CASE 1 : Comparing to another TimeSeriesDataPair object
        // -------------------------------------------------------
        if (o1 instanceof XYDataItem)
        {
            XYDataItem dataItem = (XYDataItem) o1;
            double compare = _xValue.doubleValue() - dataItem.getXValue().doubleValue();
            if (compare > 0.0)
                result = 1;
            else
            {
                if (compare < 0.0)
                    result = -1;
                else
                    result = 0;
            }
        }
        // CASE 2 : Comparing to a general object
        // ---------------------------------------------
        // consider time periods to be ordered after general objects
        else
            result = 1;

        return result;
    }


    public String toString()
    {
        return String.format("[%s,%s]", _xValue.toString(), _yValue.toString());
    }
}
