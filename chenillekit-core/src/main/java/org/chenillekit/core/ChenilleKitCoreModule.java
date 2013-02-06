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

package org.chenillekit.core;

import org.apache.tapestry5.ioc.ServiceBinder;

import org.chenillekit.core.services.ConfigurationService;
import org.chenillekit.core.services.ImageService;
import org.chenillekit.core.services.impl.ConfigurationServiceImpl;
import org.chenillekit.core.services.impl.ImageServiceImpl;

/**
 * @version $Id$
 */
public class ChenilleKitCoreModule
{
	public static void bind(ServiceBinder binder)
	{
		binder.bind(ConfigurationService.class, ConfigurationServiceImpl.class);
		binder.bind(ImageService.class, ImageServiceImpl.class);
	}
}
