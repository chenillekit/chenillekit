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

package org.chenillekit.google;

import org.apache.tapestry5.ioc.MappedConfiguration;
import org.apache.tapestry5.ioc.annotations.SubModule;

/**
 * @author <a href="mailto:homburgs@gmail.com">S.Homburg</a>
 * @version $Id$
 */
@SubModule(value = {ChenilleKitGoogleModule.class})
public class ChenilleKitGoogleTestModule
{
	public static void contributeApplicationDefaults(MappedConfiguration<String, String> contribution)
	{
		contribution.add(ChenilleKitGoogleConstants.GOOGLE_KEY, "ABQIAAAAi_YFwIW0ZYz1tm9NA5dpjxQgrEgu6vWt7HL-5aFrx0YLtTRf-hQe7xlPB5qe4SL3L7K1LcW221_now");
//		contribution.add(ChenilleKitGoogleConstants.GOOGLE_PROXY, "http://proxy.depot120.dpd.de:3128");
	}
}
