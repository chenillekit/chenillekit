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

package org.chenillekit.access.integration.app1.services.impl;

import org.chenillekit.access.WebSessionUser;
import org.chenillekit.access.services.AppServerLoginService;

/**
 * Place holder implementation, it simply does nothing.
 *
 * @version $Id$
 */
public class NoOpAppServerLoginService implements AppServerLoginService
{
	/* (non-Javadoc)
	 * @see org.chenillekit.access.services.AppServerLoginService#appServerLogin(org.chenillekit.access.WebSessionUser)
	 */
	public void appServerLogin(WebSessionUser<?> user)
	{
		// Do nothing...
	}

}
