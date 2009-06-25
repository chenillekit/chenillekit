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

package org.chenillekit.tapestry.core.components;

import java.util.List;

import org.apache.tapestry5.BindingConstants;
import org.apache.tapestry5.ClientElement;
import org.apache.tapestry5.ComponentResources;
import org.apache.tapestry5.MarkupWriter;
import org.apache.tapestry5.RenderSupport;
import org.apache.tapestry5.annotations.IncludeJavaScriptLibrary;
import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.annotations.SupportsInformalParameters;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.json.JSONObject;

import org.chenillekit.tapestry.core.utils.XYDataItem;

/**
 * chart component based on <a href="http://solutoire.com/flotr/">Flotr javascript library</a>.
 *
 * @version $Id$
 */
@SupportsInformalParameters
@IncludeJavaScriptLibrary(value = {"../excanvas.js",
        "chart/flotr.debug-0.2.0-test.js"})
public class Chart implements ClientElement
{
    /**
     * The id used to generate a page-unique client-side identifier for the component. If a component renders multiple
     * times, a suffix will be appended to the to id to ensure uniqueness.
     */
    @Parameter(value = "prop:componentResources.id", defaultPrefix = BindingConstants.LITERAL)
    private String _clientId;

    /**
     * the list of data item arrays.
     */
    @Parameter(name = "dataItems", required = false, defaultPrefix = BindingConstants.PROP)
    private List<List<XYDataItem>> _dataItemsList;

    /**
     * PageRenderSupport to get unique client side id.
     */
    @Inject
    private RenderSupport _renderSupport;

    /**
     * For blocks, messages, crete actionlink, trigger event.
     */
    @Inject
    private ComponentResources _resources;

    private String _assignedClientId;

    /**
     * Tapestry render phase method.
     * Initialize temporary instance variables here.
     */
    void setupRender()
    {
        _assignedClientId = _renderSupport.allocateClientId(_clientId);
    }

    /**
     * Tapestry render phase method.
     * Start a tag here, end it in afterRender
     */
    void beginRender(MarkupWriter writer)
    {
        writer.element("div", "id", getClientId());
        _resources.renderInformalParameters(writer);
        writer.end();
    }

    /**
     * Tapestry render phase method. End a tag here.
     */
    void afterRender(MarkupWriter writer)
    {
        JSONObject config = new JSONObject();
        String dataArrayString = null;

        //
        // Let subclasses do more.
        //
        configure(config);

        //
        // do it only if user give us some values
        //
        if (_dataItemsList != null && _dataItemsList.size() > 0)
        {
            dataArrayString = "[";

            for (int i = 0; i < _dataItemsList.size(); i++)
            {
                List<XYDataItem> dataItems = _dataItemsList.get(i);

                String dataVarName = String.format("d%d", i);
                _renderSupport.addScript("var %s = %s;", dataVarName, buildDataValuesString(dataItems));
                dataArrayString += dataVarName;
                if (i < _dataItemsList.size() - 1)
                    dataArrayString += ",";
            }
            dataArrayString += "]";
        }

        String javaScriptCall = "var chart_%s = new Flotr.draw($('%s'), ";

        //
        // if the user dont give us some chart values we add an empty value array.
        //
        if (dataArrayString != null)
            javaScriptCall += "%s ";
        else if (config.has("data"))
            javaScriptCall += config.get("data") + " ";
        else
            javaScriptCall += "[[]] ";

        if (config.has("options"))
            javaScriptCall += ", " + config.get("options");

        javaScriptCall += ");";

        _renderSupport.addScript(javaScriptCall, getClientId(), getClientId(), dataArrayString);
    }

    /**
     * let us build the data value string for Flotr.
     *
     * @param dataItems a list of data items
     *
     * @return data value string
     */
    private String buildDataValuesString(List<XYDataItem> dataItems)
    {
        String dataValuesString = "[";

        for (int i = 0; i < dataItems.size(); i++)
        {
            XYDataItem dataItem = dataItems.get(i);
            dataValuesString += String.format("%s", dataItem.toString());
            if (i < dataItems.size() - 1)
                dataValuesString += ",";
        }

        return dataValuesString + "]";
    }

    /**
     * Invoked to allow subclasses to further configure the parameters passed to this component's javascript
     * options. Subclasses may override this method to configure additional features of the Flotr.
     * <p/>
     * This implementation does nothing.
     *
     * @param config parameters object
     */
    protected void configure(JSONObject config)
    {
    }

    /**
     * Returns a unique id for the element. This value will be unique for any given rendering of a
     * page. This value is intended for use as the id attribute of the client-side element, and will
     * be used with any DHTML/Ajax related JavaScript.
     */
    public String getClientId()
    {
        return _assignedClientId;
    }
}
