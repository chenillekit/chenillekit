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

package org.chenillekit.access.integration.app1.pages;

import org.apache.tapestry5.ComponentResources;
import org.apache.tapestry5.annotations.Secure;
import org.apache.tapestry5.ioc.annotations.Inject;

import org.chenillekit.access.ChenilleKitAccessConstants;
import org.chenillekit.access.annotations.Restricted;
import org.slf4j.Logger;

/**
 * @version $Id: RestrictedPage.java 679 2010-08-01 20:54:46Z homburgs $
 */
@Restricted(role = 2) @Secure
public class SecureRestrictedPage
{
	@Inject
	private Logger logger;

	@Inject
	private ComponentResources resources;

	private String activationContext = "";

	void onActivate(String value1, String value2)
	{
		activationContext = value1 + " " + value2;
	}

	public String getRoleMetaValue()
	{
		return resources.getComponentModel().getMeta(ChenilleKitAccessConstants.RESTRICTED_PAGE_ROLE);
	}

	public String getActivationContext()
	{
		if (logger.isDebugEnabled())
			logger.debug("Activation Context: {}", activationContext);
		
		return activationContext;
	}
}