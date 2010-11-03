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

import org.apache.tapestry5.model.ComponentModel;
import org.apache.tapestry5.runtime.Component;
import org.apache.tapestry5.services.ApplicationStateManager;
import org.apache.tapestry5.services.ComponentSource;
import org.chenillekit.access.ChenilleKitAccessConstants;
import org.chenillekit.access.WebSessionUser;
import org.chenillekit.access.internal.ChenillekitAccessInternalUtils;
import org.chenillekit.access.services.AccessValidator;
import org.slf4j.Logger;

import java.util.Arrays;

/**
 * @version $Id$
 */
public class AccessValidatorImpl implements AccessValidator
{
	private final ComponentSource componentSource;
	private final Logger logger;
	private final ApplicationStateManager manager;
	private final boolean hasAccessIfNoRestrictionEventNotLoggedin;

	public AccessValidatorImpl(Logger logger,
							   ComponentSource componentSource,
							   ApplicationStateManager manager,
							   boolean hasAccessIfNoRestrictionEventNotLoggedin)
	{
		this.componentSource = componentSource;
		this.logger = logger;
		this.manager = manager;
		this.hasAccessIfNoRestrictionEventNotLoggedin = hasAccessIfNoRestrictionEventNotLoggedin;
	}


	/**
	 * We check for page/component and event type access rights.
	 * <p/>
	 * first we check the access rights for the requested page,
	 * if access granted, we step down to the next level, the components.
	 *
	 * @see org.chenillekit.access.services.AccessValidator#hasAccess(java.lang.String, java.lang.String, java.lang.String)
	 */
	public boolean hasAccess(String pageName, String componentId, String eventType)
	{
		boolean hasAccess = true;

		Component page = getPage(pageName);
		if (page == null || !isPageRestricted(page))
			return hasAccess;

		if (logger.isDebugEnabled())
			logger.debug(ChenilleKitAccessConstants.CHENILLEKIT_ACCESS, "check access for pageName/componentId/eventType: {}/{}/{}",
						 new Object[]{pageName, componentId, eventType});

		hasAccess = checkForPageAccess(page);
		if (hasAccess)
		{
			hasAccess = checkForComponentEventHandlerAccess(page, componentId, eventType);

//				if (hasAccess)
//				{
//					Field[] fields = page.getClass().getDeclaredFields();
//					for (Field field : fields)
//					{
//						if (field.isAnnotationPresent(Restricted.class) &&
//								field.isAnnotationPresent(org.apache.tapestry5.annotations.Component.class))
//						{
//							if (logger.isDebugEnabled())
//								logger.debug("found restricted component '{}' in page '{}'", field.getName(), pageName);
//
//							Component pageComponent = page.getComponentResources().getEmbeddedComponent(field.getName());
//						}
//					}
//				}
		}

		return hasAccess;
	}

	private boolean isPageRestricted(Component page)
	{
		ComponentModel componentModel = page.getComponentResources().getComponentModel();
		String isRestricted = componentModel.getMeta(ChenilleKitAccessConstants.RESTRICTED_PAGE);
		return Boolean.parseBoolean(isRestricted);
	}

	private boolean checkForComponentEventHandlerAccess(Component page,
														String componentId, String eventType)
	{
		boolean hasAccess = true;
		if (componentId != null && eventType != null)
		{
			String groups =
					page.getComponentResources().getComponentModel().getMeta(ChenillekitAccessInternalUtils.getGroupsMetaId(false, componentId, eventType));
			String role =
					page.getComponentResources().getComponentModel().getMeta(ChenillekitAccessInternalUtils.getRoleMetaId(false, componentId, eventType));

			if (groups == null)
				groups = ChenilleKitAccessConstants.NO_GROUP_RESTRICTION;

			if (role == null)
				role = "0";

			if (logger.isDebugEnabled())
				logger.debug("user needs group(s) {} and roleWeight {} for accessing {}/{}/{}",
							 new String[]{groups, role, page.getComponentResources().getPageName(), componentId, eventType});

			hasAccess = hasAccess(groups, Integer.parseInt(role), page);
		}

		return hasAccess;

	}

	/**
	 * check for page restriction, and if page restricted we check for users access rights.
	 *
	 * @param page the page(component) object
	 *
	 * @return true if user has access or the page is not restricted
	 */
	private boolean checkForPageAccess(Component page)
	{
		String groups = page.getComponentResources().getComponentModel().getMeta(ChenillekitAccessInternalUtils.getGroupsMetaId(true, null, null));
		String role = page.getComponentResources().getComponentModel().getMeta(ChenillekitAccessInternalUtils.getRoleMetaId(true, null, null));

		if (groups == null)
			groups = ChenilleKitAccessConstants.NO_GROUP_RESTRICTION;

		if (role == null)
			role = "0";

		if (logger.isDebugEnabled())
			logger.debug("user needs group(s) {} and roleWeight {} for accessing {}",
						 new String[]{groups, role, page.getComponentResources().getPageName()});

		return hasAccess(groups, Integer.parseInt(role), page);
	}

	private boolean hasAccess(String groups, Integer role, Component page)
	{
		boolean hasAccess = true;
		boolean hasGroup = true;
		boolean hasRole = true;
		WebSessionUser<?> webSessionUser = manager.getIfExists(WebSessionUser.class);

		//
		// if the the page/event/action is restricted, but has no group and role parameters ...
		//
		if (groups.equals(ChenilleKitAccessConstants.NO_GROUP_RESTRICTION) && role.equals(Integer.valueOf(0)))
		{
			//
			// if the hasAccessIfNoRestrictionEventNotLoggedin is set to false and the user is NOT logged in,
			// the user has NO access, otherwise he has (default).
			//
			if (!hasAccessIfNoRestrictionEventNotLoggedin && webSessionUser == null)
			{
				if (logger.isDebugEnabled())
					logger.debug("No user defined just yet, but must authenticated to access page '{}'!", page.getComponentResources().getPageName());

				return false;
			}
			else
				return hasAccess;
		}

		//
		// if the user NOT logged in, he has no access.
		//
		if (webSessionUser == null)
		{
			if (logger.isDebugEnabled())
				logger.debug("No user defined just yet, break group and role checking!");

			return false;
		}

		//
		// has the restriction annotation a group value ...
		//
		if (!groups.equals(ChenilleKitAccessConstants.NO_GROUP_RESTRICTION))
		{
			if (logger.isDebugEnabled())
				logger.debug("user has groups: " + Arrays.toString(webSessionUser.getGroups()) + " and needs " + groups);

			hasGroup = ChenillekitAccessInternalUtils.hasUserRequiredGroup(webSessionUser.getGroups(),
																		   ChenillekitAccessInternalUtils.getStringAsStringArray(groups));
		}

		//
		// has the restriction annotation a roleWeight value ...
		//
		if (role > 0)
		{
			if (logger.isDebugEnabled())
				logger.debug("user has roleWeight: " + webSessionUser.getRoleWeight() + " and needs roleWeight: " + role);

			hasRole = ChenillekitAccessInternalUtils.hasUserRequiredRole(webSessionUser.getRoleWeight(), role);
		}

		return hasGroup && hasRole;
	}

	/**
	 * get the page component.
	 *
	 * @param pageName the name of page
	 *
	 * @return may be null if not found
	 */
	private Component getPage(String pageName)
	{
		Component component = null;

		// This should be unnecessary...
		boolean found = false;
		while (!found)
		{
			try
			{
				component = componentSource.getPage(pageName);
				found = true;
			}
			catch (IllegalArgumentException iae)
			{
				if (pageName.lastIndexOf('/') != -1)
				{
					pageName = pageName.substring(0, pageName.lastIndexOf('/'));
					if (logger.isTraceEnabled())
						logger.trace(ChenilleKitAccessConstants.CHENILLEKIT_ACCESS, "New pagename: {}", pageName);
				}
				else
				{
					throw iae;
				}
			}
		}

		return component;
	}

}
