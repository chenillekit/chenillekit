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

package org.chenillekit.image.tests;

import org.chenillekit.image.ChenilleKitImageTestModule;
import org.chenillekit.image.services.ImageService;
import org.chenillekit.test.AbstractTestSuite;
import org.testng.annotations.BeforeTest;

/**
 * @author <a href="mailto:shomburg@hsofttec.com">S.Homburg</a>
 * @version $Id$
 */
public class TestImageService extends AbstractTestSuite
{
	private ImageService imageService;

	@BeforeTest
	public void initializeTest()
	{
		setup_registry(ChenilleKitImageTestModule.class);
		imageService = registry.getService(ImageService.class);
	}
}
