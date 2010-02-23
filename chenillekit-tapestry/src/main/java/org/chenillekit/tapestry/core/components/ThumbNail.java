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

import org.apache.tapestry5.Asset;
import org.apache.tapestry5.BindingConstants;
import org.apache.tapestry5.ClientElement;
import org.apache.tapestry5.ComponentResources;
import org.apache.tapestry5.MarkupWriter;
import org.apache.tapestry5.annotations.Environmental;
import org.apache.tapestry5.annotations.IncludeJavaScriptLibrary;
import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.annotations.SupportsInformalParameters;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.services.AssetSource;
import org.apache.tapestry5.services.javascript.JavascriptSupport;

import org.chenillekit.tapestry.core.services.ThumbNailService;

/**
 * converts a image asset into a <a href="http://en.wikipedia.org/wiki/Thumbnail">thumbnail</a>.
 *
 * @version $Id$
 */
@SupportsInformalParameters
@IncludeJavaScriptLibrary(value = {"../Chenillekit.js", "ThumbNail.js"})
public class ThumbNail implements ClientElement
{
    /**
     * The id used to generate a page-unique client-side identifier for the component. If a component renders multiple
     * times, a suffix will be appended to the to id to ensure uniqueness. The uniqued value may be accessed via the
     * {@link #getClientId() clientId property}.
     */
    @Parameter(value = "prop:componentResources.id", defaultPrefix = BindingConstants.LITERAL)
    private String _clientId;

    /**
     * the asset path or asset itself, that converted to thumbnail.
     */
    @Parameter(name = "asset", required = true, defaultPrefix = BindingConstants.LITERAL)
    private Object _assetObject;

    /**
     * the height (pixel) of the thumbnail.
     */
    @Parameter(required = true, defaultPrefix = BindingConstants.PROP)
    private int _thumbHeight;

    /**
     * output quality of the thumbnail.
     */
    @Parameter(value = "80", required = false, defaultPrefix = BindingConstants.PROP)
    private float _quality;

    /**
     * if true, clicking mouse over the thumbnail show the original image.
     */
    @Parameter(value = "false", required = false, defaultPrefix = BindingConstants.PROP)
    private boolean _onClickAction;

    private String _assignedClientId;

    @Environmental
    private JavascriptSupport javascriptSupport;

    @Inject
    private ComponentResources _resources;

    @Inject
    private AssetSource _assetSource;

    @Inject
    private ThumbNailService _thumbNailService;

    private Asset _asset;

    /**
     * Tapestry render phase method.
     * Initialize temporary instance variables here.
     */
    void setupRender()
    {
        // By default, use the component id as the (base) client id. If the clientid
        // parameter is bound, then that is the value to use.
        // Often, these controlName and _clientId will end up as the same value. There are many
        // exceptions, including a form that renders inside a loop, or a form inside a component
        // that is used multiple times.
        _assignedClientId = javascriptSupport.allocateClientId(_clientId);

        if (_assetObject instanceof String)
            _asset = _assetSource.getAsset(_resources.getBaseResource(), (String) _assetObject, null);
        else if (_assetObject instanceof Asset)
            _asset = (Asset) _assetObject;
        else
            throw new RuntimeException("parameter 'asset' neither a string nor an asset object");
    }

    /**
     * Tapestry render phase method.
     * Start a tag here, end it in afterRender
     *
     * @param writer the markup writer
     */
    void beginRender(MarkupWriter writer)
    {
        writer.element("img", "id", getClientId(), "src", generateThumbNail().toClientURL());
        _resources.renderInformalParameters(writer);
    }

    /**
     * Tapestry render phase method. End a tag here.
     *
     * @param writer the markup writer
     */
    void afterRender(MarkupWriter writer)
    {
        writer.end();

        if (_onClickAction)
            javascriptSupport.addScript("new Ck.ThumbNail('%s', '%s');", getClientId(), _asset.toClientURL());
    }

    private Asset generateThumbNail()
    {
        return _thumbNailService.convertToThumbnail(_asset, _thumbHeight, _quality);
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
