/*
 * Apache License
 * Version 2.0, January 2004
 * http://www.apache.org/licenses/
 *
 * Copyright 2008-2010 by chenillekit.org
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 */

package org.chenillekit.tapestry.core.services.impl;

import org.apache.tapestry5.Asset;
import org.apache.tapestry5.func.F;
import org.apache.tapestry5.func.Mapper;
import org.apache.tapestry5.services.AssetSource;
import org.apache.tapestry5.services.javascript.JavaScriptStack;
import org.apache.tapestry5.services.javascript.StylesheetLink;

import java.util.Collections;
import java.util.List;

/**
 * @author <a href="mailto:shomburg@depot120.dpd.de">S.Homburg</a>
 * @version $Id$
 */
public class YahooStack implements JavaScriptStack
{
	private final List<Asset> javascriptStack;

	public YahooStack(final AssetSource assetSource)
	{
		Mapper<String, Asset> pathToAsset = new Mapper<String, Asset>()
		{
			public Asset map(String path)
			{
				return assetSource.getExpandedAsset(path);
			}
		};

		javascriptStack = F.flow("${yahoo.yui}/yahoo-dom-event/yahoo-dom-event.js",
				"${yahoo.yui}/element/element${yahoo.yui.mode}.js").map(pathToAsset).toList();
	}

	public List<String> getStacks()
	{
		return Collections.emptyList();
	}

	/**
	 * Returns a list of <em>localized</em> assets for JavaScript libraries that form the stack.
	 */
	public List<Asset> getJavaScriptLibraries()
	{
		return javascriptStack;
	}

	/**
	 * Returns a list of <em>localized<m/e> links for stylesheets that form the stack.
	 */
	public List<StylesheetLink> getStylesheets()
	{
		return Collections.emptyList();
	}

	/**
	 * Returns static JavaScript initialization for the stack. This block of JavaScript code will be added to the
	 * page that imports the stack. The code executes outside of any other function (i.e., the code is not deferred
	 * until the DOM is loaded). As with the other methods, if localization is a factor, the result of this method
	 * should be localized.
	 */
	public String getInitialization()
	{
		return "";
	}
}
