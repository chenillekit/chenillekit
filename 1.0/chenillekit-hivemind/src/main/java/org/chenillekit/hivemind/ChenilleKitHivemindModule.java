/*
 * Apache License
 * Version 2.0, January 2004
 * http://www.apache.org/licenses/
 *
 * Copyright 2009 by chenillekit.org
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 */

package org.chenillekit.hivemind;

import org.apache.tapestry5.ioc.ObjectProvider;

import org.chenillekit.hivemind.services.impl.HiveMindObjectProvider;
import org.slf4j.Logger;

/**
 * @version $Id$
 */
public class ChenilleKitHivemindModule
{
	/**
	 * Build the actual provider for HiveMind services
	 *
	 * @param logger logging facility
	 *
	 * @return the {@link org.apache.tapestry5.ioc.ObjectProvider} delegated to lookup objects
	 */
	public static ObjectProvider buildHiveMind(Logger logger)
	{
		return new HiveMindObjectProvider(logger);
	}
}
