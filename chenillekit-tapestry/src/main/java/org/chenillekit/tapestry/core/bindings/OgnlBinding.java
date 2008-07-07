/*
 * Apache License
 * Version 2.0, January 2004
 * http://www.apache.org/licenses/
 *
 * Copyright 1996-2008 by Sven Homburg
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 */

package org.chenillekit.tapestry.core.bindings;

import java.util.Map;

import org.apache.tapestry5.internal.bindings.AbstractBinding;
import org.apache.tapestry5.ioc.Location;
import org.apache.tapestry5.ioc.internal.util.CollectionFactory;
import org.apache.tapestry5.ioc.internal.util.TapestryException;

import ognl.Ognl;

/**
 * Binding type for OGNL expressions.
 * This binding called by expression "ognl:".
 *
 * @author <a href="mailto:homburgs@gmail.com">S.Homburg</a>
 * @version $Id: OgnlBinding.java 682 2008-05-20 22:00:02Z homburgs $
 */
public class OgnlBinding extends AbstractBinding
{
    private static final Map<String, Object> _cache = CollectionFactory.newMap();

    private String _expression;
    private Object _root;

    public OgnlBinding(final Location location, final Object root, final String expression)
    {
        super(location);
        _expression = expression;
        _root = root;
    }

    /**
     * Gets a parsed OGNL expression from the input string.
     *
     * @throws TapestryException if the expression can not be parsed.
     */
    public synchronized Object getParsedExpression(String expression)
    {
        Object result = _cache.get(expression);

        if (result == null)
        {
            try
            {
                result = Ognl.parseExpression(expression);
            }
            catch (Exception ex)
            {
                throw new TapestryException(ex.getLocalizedMessage(), getLocation(), ex);
            }

            _cache.put(expression, result);
        }

        return result;
    }

    /**
     * Reads the current value of the property (or other resource). When reading properties of
     * objects that are primitive types, this will return an instance of the wrapper type. In some
     * cases, a binding is read only and this method will throw a runtime exception.
     */
    public Object get()
    {
        Object ognlExpression = getParsedExpression(_expression);

        try
        {
            Map context = Ognl.createDefaultContext(_root);
            return Ognl.getValue(ognlExpression, context, _root);
        }
        catch (Exception ex)
        {
            throw new TapestryException(ex.getLocalizedMessage(), getLocation(), ex);
        }
    }

    /**
     * Returns false; these properties are always dynamic.
     */
    @Override
    public boolean isInvariant()
    {
        return false;
    }

    @Override
    public String toString()
    {
        return String.format("OgnlBinding[%s]", _expression);
    }
}