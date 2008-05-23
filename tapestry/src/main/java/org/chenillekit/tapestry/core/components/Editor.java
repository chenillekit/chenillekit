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
 *
 */

package org.chenillekit.tapestry.core.components;

import org.apache.tapestry5.Asset;
import org.apache.tapestry5.MarkupWriter;
import org.apache.tapestry5.RenderSupport;
import org.apache.tapestry5.annotations.AfterRender;
import org.apache.tapestry5.annotations.Environmental;
import org.apache.tapestry5.annotations.IncludeJavaScriptLibrary;
import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.corelib.base.AbstractTextField;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.ioc.services.SymbolSource;
import org.apache.tapestry5.services.ClasspathAssetAliasManager;
import org.apache.tapestry5.services.Request;

/**
 * <p>The editor component provides a rich text editor as a form control.
 * Based on the <a href="http://www.fckeditor.net/">FCKeditor</a>, the editor
 * is highly configurable (and can therefore be complicated). This component
 * aims to keep usage simple, outsourcing most of the configuration to an
 * optional external javascript file.</p>
 * <p/>
 * <p>The most important configurations are that of an external configuration
 * file and the toolbars present in the editor. To support this, the editor component
 * exposes the <code>customConfiguration</code> and <code>toolbarSet</code>
 * parameters.</p>
 * <p/>
 * <p>In the interest of usability, the editor component will function as
 * classic textarea element.</p>
 * <p/>
 * <p>NOTE: This component is built on the 2.x version of FCKeditor.</p>
 *
 * @author Tod Orr
 * @version $Id: Editor.java 682 2008-05-20 22:00:02Z homburgs $
 * @see <a href="http://docs.fckeditor.net/FCKeditor_2.x/Developers_Guide">FCKeditor developer's guide</a>
 * @see <a href="http://docs.fckeditor.net/FCKeditor_2.x/Users_Guide">FCKeditor user's guide</a>
 */
@IncludeJavaScriptLibrary("fckeditor/fckeditor.js")
public class Editor extends AbstractTextField
{
    /**
     * The height of the editor.
     */
    @Parameter(defaultPrefix = "literal", value = "300px")
    private String _height;

    /**
     * The width of the editor.
     */
    @Parameter(defaultPrefix = "literal", value = "300px")
    private String _width;

    /**
     * A custom configuration for this editor.
     * See the FCKeditor manual for details on custom configurations.
     */
    @Parameter
    private Asset _customConfiguration;

    /**
     * The toolbar set to be used with this editor. Default possible values
     * are <code>Default</code> and <code>Basic</code>.
     * Toolbar sets can be configured in a {@link #_customConfiguration custom configuration}.
     */
    @Parameter(defaultPrefix = "literal", value = "Default")
    private String _toolbarSet;

    @Inject
    private ClasspathAssetAliasManager _cpam;

    @Inject
    private SymbolSource _symbolSource;

    @Environmental
    private RenderSupport _pageRenderSupport;

    @Inject
    private Request request;

    private String _value;

    @Override
    protected final void writeFieldTag(MarkupWriter writer, String value)
    {
        // At it's most basic level, editor should function as a textarea.
        writer.element("textarea",
                       "name", getControlName(),
                       "id", getClientId(),
                       "cols", getWidth());

        // Save until needed in afterRender().
        _value = value;
    }

    @AfterRender
    final void afterRender(MarkupWriter writer)
    {
        if (_value != null) writer.write(_value);
        writer.end();
        writeScript();
    }

    final void writeScript()
    {
        String editorVar = "editor_" + getClientId().replace(':', '_');

        String fckEditorBasePath = _cpam.toClientURL(_symbolSource.expandSymbols("${ck.components}")) + "/fckeditor/";

        _pageRenderSupport.addScript("var %s = new FCKeditor('%s');", editorVar, getClientId());
        _pageRenderSupport.addScript("%s.BasePath = '%s';", editorVar, fckEditorBasePath);

        if (_customConfiguration != null)
        {
            /**
             * FCK loads itself via an iframe, in which its own html file is loaded that
             * takes care of bootstrapping the editor (which includes loading any custom
             * config files). This html file is stored on the classpath and when it loads
             * custom config files, it receives a relative name. The path of the html
             * file is:
             *
             * org/chenillekit/tapestry/core/components/fckeditor/editor/fckeditor.html
             *
             * Now when that page is loaded in the iframe, it will load the configuration
             * file by writing out a new script tag using the path it receives, which is
             * relative. Because the path is relative tapestry interprets the config file
             * to be a relative asset on the classpath (relative to the html file). So
             * it looks for this on the classpath:
             *
             * org/chenillekit/tapestry/core/components/fckeditor/editor/myeditor.js
             *
             * Instead of something like:
             *
             * /MyApp/myeditor.js
             *
             * The following hack mangles the URL by appending the interpreted asset
             * path to the (absolute) context path. This solves the problem for context
             * and classpath assets.
             */
            String contextPath = request.getContextPath();
            String hackedPath = _customConfiguration.toClientURL();
            if (!hackedPath.startsWith(contextPath))
                hackedPath = contextPath + "/" + hackedPath;
            _pageRenderSupport.addScript("%s.Config['CustomConfigurationsPath'] = '%s';", editorVar, hackedPath);
        }

        if (_toolbarSet != null)
            _pageRenderSupport.addScript("%s.ToolbarSet = '%s';", editorVar, _toolbarSet);

        _pageRenderSupport.addScript("%s.Height = '%s';", editorVar, _height);
        _pageRenderSupport.addScript("%s.Width = '%s';", editorVar, _width);
        _pageRenderSupport.addScript("%s.ReplaceTextarea();", editorVar);
    }
}