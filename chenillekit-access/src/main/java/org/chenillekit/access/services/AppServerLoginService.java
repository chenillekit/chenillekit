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

package org.chenillekit.access.services;

import org.chenillekit.access.WebSessionUser;

/**
 * Service which allows logging into the application server on which the application runs.
 *
 * @version $Id$
 */
public interface AppServerLoginService
{
	void appServerLogin( WebSessionUser user );
}
