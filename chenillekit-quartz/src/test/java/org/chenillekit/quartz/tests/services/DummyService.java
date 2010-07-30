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

package org.chenillekit.quartz.tests.services;

/**
 *
 *
 * @version $Id$
 */
public interface DummyService
{
	/**
	 * Dummy interface for a dummy service just to test JobDataMap passing service through.
	 * @param value
	 */
	public void runMePlease(String value);

}
