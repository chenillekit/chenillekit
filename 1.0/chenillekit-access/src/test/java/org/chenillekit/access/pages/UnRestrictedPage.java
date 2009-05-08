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

package org.chenillekit.access.pages;

import org.apache.tapestry5.annotations.OnEvent;
import org.chenillekit.access.annotations.Restricted;

/**
 *
 * @version $Id$
 */
public class UnRestrictedPage
{
	@Restricted(groups = { "ADMINS" })
	void onActionFromTestRights()
	{
		throw new RuntimeException("This should never be reached!");
	}

	@Restricted(groups = { "ADMINS" })
	@OnEvent(component = "testRightsOnEvent")
	void thisThrowRuntimeException()
	{
		throw new RuntimeException("This one too!");
	}
}