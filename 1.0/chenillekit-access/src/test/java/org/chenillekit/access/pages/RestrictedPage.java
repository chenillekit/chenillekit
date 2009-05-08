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

import org.apache.tapestry5.ComponentResources;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.chenillekit.access.ChenilleKitAccessConstants;
import org.chenillekit.access.annotations.Restricted;

/**
 *
 * @version $Id$
 */
@Restricted(role = 2)
public class RestrictedPage
{
	@Inject
	private ComponentResources resources;

	public String getRoleMetaValue()
	{
		return resources.getComponentModel().getMeta(ChenilleKitAccessConstants.RESTRICTED_PAGE_ROLE);
	}
}