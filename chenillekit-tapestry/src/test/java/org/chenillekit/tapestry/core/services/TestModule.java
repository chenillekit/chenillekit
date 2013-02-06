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

package org.chenillekit.tapestry.core.services;

import org.apache.tapestry5.SymbolConstants;
import org.apache.tapestry5.ioc.MappedConfiguration;
import org.apache.tapestry5.ioc.annotations.InjectService;
import org.apache.tapestry5.ioc.annotations.SubModule;
import org.apache.tapestry5.services.AssetFactory;
import org.apache.tapestry5.services.Context;

import org.chenillekit.core.ChenilleKitCoreModule;
import org.chenillekit.core.services.ImageService;
import org.chenillekit.tapestry.core.services.impl.ThumbNailServiceImpl;
import org.slf4j.Logger;

/**
 * @version $Id$
 */
@SubModule(value = {ChenilleKitCoreModule.class})
public class TestModule
{
	public static void contributeApplicationDefaults(MappedConfiguration<String, String> configuration)
	{
		configuration.add(SymbolConstants.APPLICATION_VERSION, "1.0-test");
		configuration.add(SymbolConstants.PRODUCTION_MODE, "false");
	}

	public static ThumbNailService buildThumbnailService(Logger logger,
														 ImageService imageService,
														 Context context,
														 @InjectService("ContextAssetFactory")
														 AssetFactory assetFactory)
	{
		return new ThumbNailServiceImpl(logger, imageService, context, assetFactory);
	}
}
