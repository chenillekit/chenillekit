/*
 * Apache License
 * Version 2.0, January 2004
 * http://www.apache.org/licenses/
 *
 * Copyright 2010 by chenillekit.org
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 */

/**
 *
 */

package org.chenillekit.access.services.impl;

import java.util.List;

import org.apache.tapestry5.annotations.OnEvent;
import org.apache.tapestry5.model.MutableComponentModel;
import org.apache.tapestry5.services.ClassTransformation;
import org.apache.tapestry5.services.TransformMethod;

import org.chenillekit.access.ChenilleKitAccessConstants;
import org.chenillekit.access.annotations.ManagedRestricted;
import org.chenillekit.access.dao.IProtectionRule;
import org.chenillekit.access.dao.IProtectionRuleDAO;
import org.chenillekit.access.internal.ChenillekitAccessInternalUtils;
import org.slf4j.Logger;

/**
 * @version $Id$
 */
public class ManagedRestrictedWorker extends RestrictedWorker
{
	private final Logger logger;
	private final IProtectionRuleDAO protectionRuleDAO;

	public ManagedRestrictedWorker(Logger logger, IProtectionRuleDAO protectionRuleDAO)
	{
		super(logger);
		this.logger = logger;
		this.protectionRuleDAO = protectionRuleDAO;
	}

	/**
	 * Read and process restriction on page classes annotated with {@link org.chenillekit.access.annotations.ManagedRestricted} annotation
	 *
	 * @param transformation Contains class-specific information used when transforming a raw component class
	 *                       into an executable component class.
	 * @param model		  Mutable version of {@link org.apache.tapestry5.model.ComponentModel} used during
	 *                       the transformation phase.
	 */
	@Override
	protected void processPageRestriction(ClassTransformation transformation, MutableComponentModel model)
	{
		ManagedRestricted restricted = transformation.getAnnotation(ManagedRestricted.class);
		if (restricted == null)
			return;

		String className = model.getComponentClassName();

		if (logger.isDebugEnabled())
			logger.debug("searching permission for component {}", className);

		IProtectionRule protectionRule = protectionRuleDAO.retrieveProtectionRule(className);
		/**
		 * there is no protection rule for that component
		 */
		if (protectionRule == null)
			return;

		if (logger.isDebugEnabled())
			logger.debug("found permission groups {} for component {}", protectionRule.getGroups(), className);

		String roleWeight = String.valueOf(protectionRule.getRoleWeight());
		model.setMeta(ChenilleKitAccessConstants.RESTRICTED_PAGE_ROLE, roleWeight);

		if (protectionRule.getGroups().length > 0)
			model.setMeta(ChenilleKitAccessConstants.RESTRICTED_PAGE_GROUP,
						  ChenillekitAccessInternalUtils.getStringArrayAsString(protectionRule.getGroups()));

	}

	/**
	 * inject meta datas about annotated methods
	 *
	 * @param transformation Contains class-specific information used when transforming a raw component class
	 *                       into an executable component class.
	 * @param model		  Mutable version of {@link org.apache.tapestry5.model.ComponentModel} used during
	 *                       the transformation phase.
	 */
	@Override
	protected void processEventHandlerRestrictions(ClassTransformation transformation, MutableComponentModel model)
	{
		List<TransformMethod> matchedMethods = getMatchedMethods(transformation, ManagedRestricted.class);

		for (TransformMethod method : matchedMethods)
		{
			String methodName = method.getName();

			ManagedRestricted restricted = method.getAnnotation(ManagedRestricted.class);

			String componentId = extractComponentId(method, method.getAnnotation(OnEvent.class));
			String eventType = extractEventType(method, method.getAnnotation(OnEvent.class));

			String groupMeta = ChenillekitAccessInternalUtils.buildMetaForHandlerMethod(componentId,
																						eventType,
																						ChenilleKitAccessConstants.RESTRICTED_EVENT_HANDLER_GROUPS_SUFFIX);

			String roleMeta = ChenillekitAccessInternalUtils.buildMetaForHandlerMethod(componentId,
																					   eventType,
																					   ChenilleKitAccessConstants.RESTRICTED_EVENT_HANDLER_ROLE_SUFFIX);

			IProtectionRule protectionRule = protectionRuleDAO.retrieveProtectionRule(String.format("%s.%s", componentId, eventType));

			/**
			 * there is no protection rule for that component
			 */
			if (protectionRule == null)
				continue;

			if (logger.isDebugEnabled())
				logger.debug("found permission groups {} for event {}", protectionRule.getGroups(), String.format("%s.%s", componentId, eventType));

			String groupsString = ChenillekitAccessInternalUtils.getStringArrayAsString(protectionRule.getGroups());

			if (groupsString.trim().length() > 0)
				model.setMeta(groupMeta, groupsString);

			model.setMeta(roleMeta, Integer.toString(protectionRule.getRoleWeight()));
		}
	}
}