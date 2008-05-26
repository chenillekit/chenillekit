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

package org.chenillekit.tapestry.core.base;

import org.apache.tapestry5.Asset;
import org.apache.tapestry5.BindingConstants;
import org.apache.tapestry5.ClientElement;
import org.apache.tapestry5.ComponentResources;
import org.apache.tapestry5.RenderSupport;
import org.apache.tapestry5.annotations.Environmental;
import org.apache.tapestry5.annotations.IncludeJavaScriptLibrary;
import org.apache.tapestry5.annotations.IncludeStylesheet;
import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.ioc.services.SymbolSource;
import org.apache.tapestry5.json.JSONObject;
import org.apache.tapestry5.services.AssetSource;

/**
 * creates a window based on jvascript <a href="http://prototype-window.xilinus.com/">window</a> library.
 *
 * @author <a href="mailto:homburgs@gmail.com">S.Homburg</a>
 * @version $Id: AbstractWindow.java 682 2008-05-20 22:00:02Z homburgs $
 */
@IncludeJavaScriptLibrary(value = {"${tapestry.scriptaculous}/effects.js", "window/window.js",
        "window/window_effects.js"})
@IncludeStylesheet(value = {"window/themes/default.css"})
abstract public class AbstractWindow implements ClientElement
{
    /**
     * The id used to generate a page-unique client-side identifier for the component. If a component renders multiple
     * times, a suffix will be appended to the to id to ensure uniqueness. The uniqued value may be accessed via the
     * {@link #getClientId() clientId property}.
     */
    @Parameter(value = "prop:componentResources.id", defaultPrefix = BindingConstants.LITERAL)
    private String _clientId;

    /**
     * style name for the window.
     * <p/>
     * possible values are:
     * <br/>
     * <ul>
     * <li>dialog</li>
     * <li>alert (attention! has no window buttons)</li>
     * <li>alert_lite (attention! has no window buttons)</li>
     * <li>alphacube</li>
     * <li>mac_os_x</li>
     * <li>blur_os_x</li>
     * <li>mac_os_x_dialog</li>
     * <li>nuncio</li>
     * <li>spread</li>
     * <li>darkX</li>
     * <li>greenlighting</li>
     * <li>bluelighting</li>
     * <li>greylighting</li>
     * <li>darkbluelighting</li>
     * </ul>
     */
    @Parameter(value = "alphacube", defaultPrefix = BindingConstants.LITERAL, name = "style")
    private String _className;

    /**
     * initial width of the window.
     */
    @Parameter(value = "0", defaultPrefix = BindingConstants.PROP)
    private int _width;

    /**
     * initial height of the window.
     */
    @Parameter(value = "0", defaultPrefix = BindingConstants.PROP)
    private int _height;

    /**
     * Shows window at its current position.
     */
    @Parameter(value = "true", defaultPrefix = BindingConstants.PROP)
    private boolean _show;

    /**
     * Shows window centered (only if parameter "show" is true).
     */
    @Parameter(value = "true", defaultPrefix = BindingConstants.PROP)
    private boolean _center;

    /**
     * Shows window in modal mode (only if parameter "show" is true).
     */
    @Parameter(value = "true", defaultPrefix = BindingConstants.PROP)
    private boolean _modal;

    private String _assignedClientId;

    @Environmental
    private RenderSupport _pageRenderSupport;

    @Inject
    private ComponentResources _resources;

    @Inject
    private AssetSource _assetSource;

    @Inject
    private SymbolSource _symbolSource;

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
        _assignedClientId = _pageRenderSupport.allocateClientId(_clientId);
    }

    /**
     * Tapestry render phase method.
     * Start a tag here, end it in afterRender
     */
    void beginRender()
    {
        String cssStyleFile;

        String scriptPathSymbolValue = _symbolSource.expandSymbols("${commons.scripts}") + "/window/themes";

        if (_className.endsWith("lighting"))
            cssStyleFile = "lighting.css";
        else if (_className.equals("dialog"))
            cssStyleFile = "default.css";
        else if (_className.endsWith("_os_x"))
            cssStyleFile = "mac_os_x.css";
        else
            cssStyleFile = _className + ".css";

        Asset cssAsset = _assetSource.getClasspathAsset(scriptPathSymbolValue + "/" + cssStyleFile);
        _pageRenderSupport.addStylesheetLink(cssAsset, "screen");
    }

    /**
     * Invoked to allow subclasses to further configure the parameters passed to this component's javascript
     * options. Subclasses may override this method to configure additional features of the Window.
     * <p/>
     * This implementation does nothing. For more information about window options look at
     * this <a href="http://prototype-window.xilinus.com/documentation.html#initialize">page</a>.
     *
     * @param options windows option object
     */
    protected void configure(JSONObject options)
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

    public RenderSupport getPageRenderSupport()
    {
        return _pageRenderSupport;
    }

    public boolean isShow()
    {
        return _show;
    }

    public boolean isCenter()
    {
        return _center;
    }

    public boolean isModal()
    {
        return _modal;
    }

    public String getClassName()
    {
        return _className;
    }

    public int getWidth()
    {
        return _width;
    }

    public int getHeight()
    {
        return _height;
    }
}