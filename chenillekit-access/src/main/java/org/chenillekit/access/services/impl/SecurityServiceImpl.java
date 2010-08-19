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

package org.chenillekit.access.services.impl;

import org.apache.tapestry5.services.ApplicationStateManager;
import org.apache.tapestry5.services.Request;
import org.apache.tapestry5.services.Session;
import org.chenillekit.access.WebSessionUser;
import org.chenillekit.access.internal.ChenillekitAccessInternalUtils;
import org.chenillekit.access.services.SecurityService;

/**
 * @author <a href="mailto:shomburg@hsofttec.com">S.Homburg</a>
 * @version $Id$
 */
public class SecurityServiceImpl implements SecurityService
{
	private final ApplicationStateManager stateManager;
	private final Request request;

	public SecurityServiceImpl(ApplicationStateManager stateManager, Request request)
	{
		this.stateManager = stateManager;
		this.request = request;
	}

	/**
	 * check for user is authenticated.
	 */
	public boolean isAuthenticate()
	{
		WebSessionUser<?> webSessionUser = stateManager.getIfExists(WebSessionUser.class);
		return webSessionUser != null;
	}

	/**
	 * do logout user from application and the session will invalidated.
	 */
	public void logout()
	{
		logout(true);
	}

	/**
	 * do logout user from application.
	 *
	 * @param invalidateSession if true, the session will invalidated
	 */
	public void logout(boolean invalidateSession)
	{
		WebSessionUser<?> webSessionUser = stateManager.getIfExists(WebSessionUser.class);
		if (webSessionUser != null)
		{
			if (!invalidateSession)
				stateManager.set(WebSessionUser.class, null);
			else
			{
				Session session = request.getSession(false);
				if (session != null && !session.isInvalidated())
					session.invalidate();
			}
		}
	}

	/**
	 * returns true, if user profile contains all af the given groups.
	 */
	public boolean hasGroups(String... groups)
	{
		boolean hasGroups = false;

		WebSessionUser<?> webSessionUser = stateManager.getIfExists(WebSessionUser.class);
		if (webSessionUser != null)
			hasGroups = ChenillekitAccessInternalUtils.hasUserRequiredGroup(webSessionUser.getGroups(), groups);

		return hasGroups;
	}

	/**
	 * returns true, if user profile contains the given group.
	 */
	public boolean hasGroup(String group)
	{
		return hasGroups(group);
	}
}
