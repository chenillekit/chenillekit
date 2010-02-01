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

package org.chenillekit.demo.components;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.LineNumberReader;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.tapestry5.BindingConstants;
import org.apache.tapestry5.ComponentResources;
import org.apache.tapestry5.annotations.Component;
import org.apache.tapestry5.annotations.Mixins;
import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.corelib.components.Loop;
import org.apache.tapestry5.corelib.components.PageLink;
import org.apache.tapestry5.internal.services.ContextResource;
import org.apache.tapestry5.ioc.Resource;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.ioc.internal.util.CollectionFactory;
import org.apache.tapestry5.services.ApplicationGlobals;
import org.apache.tapestry5.services.Context;

/**
 * @version $Id$
 */
public class LeftSideMenu
{
    /**
     * base name of the menu to display.
     */
    @Parameter(required = true, defaultPrefix = BindingConstants.LITERAL)
    private String menuName;

    /**
     * some parameters, used by application
     */
    @Parameter(required = false, defaultPrefix = BindingConstants.PROP)
    private Map<String, ?> parameters;

    /**
     * ComponentResources. For blocks, messages, crete actionlink, trigger event
     */
    @Property
    @Inject
    private ComponentResources resources;

    /**
     * ComponentResources. For blocks, messages, crete actionlink, trigger event
     */
    @Inject
    private Context context;

    @Inject
    private ApplicationGlobals applicationGlobals;

    private Resource configFile;

    @Property(write = false)
    private List<MenuConfiguration> menuEntryList;

    @Property
    private MenuConfiguration menuItem;

    @Component(parameters = {"source=menuEntryList", "value=menuItem"})
    private Loop menuItemLoop;

    @Component(parameters = {"page=prop:menuItem.pageName", "style=menuItem.styles",
            "context=menuItem.contextParameters"})
    @Mixins({"ck/yui/Button"})
    private PageLink pageLink;


    /**
     * Tapestry page lifecycle method.
     * Called when the page is instantiated and added to the page pool.
     * Initialize components, and resources that are not request specific.
     */
    void pageLoaded()
    {
        String configFilePath = applicationGlobals.getServletContext().getInitParameter("leftsidemenu.config.path");
        if (configFilePath == null)
            throw new RuntimeException("please configure 'leftsidemenu.config.path' in your WEB-INF/web.xml");


        configFile = new ContextResource(context, String.format("%s/menu.%s.conf", configFilePath, menuName));
    }

    /**
     * Tapestry render phase method.
     * Initialize temporary instance variables here.
     */
    void setupRender()
    {
        menuEntryList = buildMenuItemList();
    }

    /**
     * build the menu item list.
     *
     * @return list of menu items
     */
    private List<MenuConfiguration> buildMenuItemList()
    {
        List<MenuConfiguration> menuEntryList = CollectionFactory.newList();
        LineNumberReader lineNumberReader = null;

        try
        {
            lineNumberReader = new LineNumberReader(new FileReader(new File(configFile.toURL().toURI())));

            String readedLine;
            while ((readedLine = lineNumberReader.readLine()) != null)
            {
                String[] values = StringUtils.split(readedLine, '|');
                if (!values[0].equalsIgnoreCase("separator"))
                {
                    Object[] contextParameters = null;
                    if (values.length == 4 && parameters != null)
                    {
                        String[] placeHolders = values[3].split(",");
                        contextParameters = new Object[placeHolders.length];
                        for (int i = 0; i < placeHolders.length; i++)
                        {
                            String placeHolder = placeHolders[i];
                            contextParameters[i] = parameters.get(placeHolder);
                        }
                    }

                    menuEntryList.add(new MenuConfiguration(values[0].trim(), values[1].trim(), values[2].trim(), contextParameters));
                }
                else
                    menuEntryList.add(new MenuConfiguration());
            }

            return menuEntryList;
        }
        catch (FileNotFoundException e)
        {
            throw new RuntimeException(e);
        }
        catch (IOException e)
        {
            throw new RuntimeException(e);
        }
        catch (URISyntaxException e)
        {
            throw new RuntimeException(e);
        }
        finally
        {
            try
            {
                if (lineNumberReader != null)
                    lineNumberReader.close();
            }
            catch (IOException e)
            {
            }
        }
    }

    public class MenuConfiguration
    {
        private final String menuDesc;
        private final String pageName;
        private final String styles;
        private final Object[] contextParameters;
        private boolean separator = false;

        MenuConfiguration(String menuDesc, String pageName, String styles, Object[] contextParameters)
        {
            this.menuDesc = menuDesc;
            this.pageName = pageName;
            this.styles = styles;
            this.contextParameters = contextParameters;
        }

        public MenuConfiguration()
        {
            this("", "", "", null);
            this.separator = true;
        }

        public boolean isSeparator()
        {
            return separator;
        }

        public String getMenuDesc()
        {
            return menuDesc;
        }

        public String getPageName()
        {
            return pageName;
        }

        public String getStyles()
        {
            return styles;
        }

        public Object[] getContextParameters()
        {
            return contextParameters;
        }
    }
}
