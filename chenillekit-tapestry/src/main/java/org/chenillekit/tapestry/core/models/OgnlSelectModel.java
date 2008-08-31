/*
 * Copyright (c) 2008, Your Corporation. All Rights Reserved.
 */

package org.chenillekit.tapestry.core.models;

import java.util.List;
import java.util.Map;

import org.apache.tapestry5.OptionGroupModel;
import org.apache.tapestry5.OptionModel;
import org.apache.tapestry5.internal.OptionModelImpl;
import org.apache.tapestry5.ioc.internal.util.CollectionFactory;
import org.apache.tapestry5.ioc.internal.util.Defense;
import org.apache.tapestry5.util.AbstractSelectModel;

import ognl.Ognl;

/**
 * Defines the possible options for a &lt;select&gt; [X]HTML element.
 * This select model implementation based on <a href="http://www.ognl.org/">OGNL</a>.
 * <p/>
 * <h4>Use Case</h4>
 * your object list contains entities of type <code>User</code>. Each <code>User</code> object contains
 * an object typed <code>Address</code>, and every <code>Address</code> object contains
 * a property <code>name</code>.
 * <p/>
 * If you want to display in the list the value of the name property you should define a
 * <code>labelExpression</code> like <code>address.name</code>
 *
 * @author <a href="mailto:homburgs@googlemail.com">S.Homburg</a>
 * @version $Id$
 */
public class OgnlSelectModel<T> extends AbstractSelectModel
{
    private static final Map<String, Object> _cache = CollectionFactory.newMap();

    private List<T> _objectList;
    private String _labelExpression;

    /**
     * constructs an ognl select model.
     *
     * @param labelExpression the ognl expression converted to label.
     */
    public OgnlSelectModel(String labelExpression)
    {
        this(null, labelExpression);
    }

    /**
     * constructs an ognl select model.
     *
     * @param objectList      list of objects that should listed by select tag
     * @param labelExpression the ognl expression converted to label.
     */
    public OgnlSelectModel(List<T> objectList, String labelExpression)
    {
        Defense.notBlank(labelExpression, "labelExpression");

        _labelExpression = labelExpression;

        // more carfully i think, so we copy the object list
        if (objectList != null)
            setObjectList(objectList);
    }

    /**
     * set the object list.
     *
     * @param objectList the object list
     */
    public void setObjectList(List<T> objectList)
    {
        _objectList = CollectionFactory.newList(objectList);
    }

    /**
     * The list of groups, returns always null
     *
     * @return always null
     */
    public List<OptionGroupModel> getOptionGroups()
    {
        return null;
    }

    /**
     * The list of ungrouped options, which appear after any grouped options.
     *
     * @return the ungrouped options
     */
    public List<OptionModel> getOptions()
    {
        List<OptionModel> optionModelList = CollectionFactory.newList();

        if (_objectList != null)
        {
            for (int i = 1; i <= _objectList.size(); i++)
            {
                String label = String.valueOf(getValue(_objectList.get(i - 1), _labelExpression));
                optionModelList.add(new OptionModelImpl(label, _objectList.get(i - 1)));
            }
        }

        return optionModelList;
    }

    /**
     * Gets a parsed OGNL expression from the input string.
     *
     * @param expression the ognl expression.
     *
     * @return value of evaluated expression
     */
    private synchronized Object getParsedExpression(String expression)
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
                throw new RuntimeException(ex);
            }

            _cache.put(expression, result);
        }

        return result;
    }

    /**
     * Reads the current value of the property (or other resource). When reading properties of
     * objects that are primitive types, this will return an instance of the wrapper type. In some
     * cases, a binding is read only and this method will throw a runtime exception.
     *
     * @param rootObject
     * @param expression the ognl expression.
     *
     * @return value of evaluated expression
     */
    private Object getValue(Object rootObject, String expression)
    {
        Object ognlExpression = getParsedExpression(expression);

        try
        {
            Map context = Ognl.createDefaultContext(rootObject);
            return Ognl.getValue(ognlExpression, context, rootObject);
        }
        catch (Exception ex)
        {
            throw new RuntimeException(ex);
        }
    }
}