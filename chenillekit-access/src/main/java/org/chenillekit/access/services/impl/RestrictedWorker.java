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

/**
 *
 */

package org.chenillekit.access.services.impl;

import org.apache.tapestry5.annotations.OnEvent;
import org.apache.tapestry5.func.Predicate;
import org.apache.tapestry5.model.MutableComponentModel;
import org.apache.tapestry5.services.ClassTransformation;
import org.apache.tapestry5.services.ComponentClassTransformWorker;
import org.apache.tapestry5.services.TransformMethod;
import org.chenillekit.access.ChenilleKitAccessConstants;
import org.chenillekit.access.annotations.Restricted;
import org.chenillekit.access.internal.ChenillekitAccessInternalUtils;
import org.slf4j.Logger;

import java.lang.annotation.Annotation;
import java.util.List;

/**
 * @version $Id$
 */
public class RestrictedWorker implements ComponentClassTransformWorker
{
	private final Logger logger;

	public RestrictedWorker(Logger logger)
	{
		this.logger = logger;
	}

	/**
	 * (non-Javadoc)
	 *
	 * @see org.apache.tapestry5.services.ComponentClassTransformWorker#transform(org.apache.tapestry5.services.ClassTransformation, org.apache.tapestry5.model.MutableComponentModel)
	 */
	public void transform(ClassTransformation transformation, MutableComponentModel model)
	{
		if (logger.isDebugEnabled())
			logger.debug("Processing page render restrictions:");
		processPageRestriction(transformation, model);

		if (logger.isDebugEnabled())
			logger.debug("Processing event handlers restrictions:");
		processEventHandlerRestrictions(transformation, model);

		// processComponentsRestrictions(transformation, model);
	}

	protected List<TransformMethod> getMatchedMethods(ClassTransformation transformation, final Class<? extends Annotation> annotation)
	{
		return transformation.matchMethods(new Predicate<TransformMethod>()
		{
			@Override
			public boolean accept(TransformMethod method)
			{
				return (hasCorrectPrefix(method) || hasAnnotation(method)) && !method.isOverride();
			}

			private boolean hasCorrectPrefix(TransformMethod method)
			{
				String methodName = method.getName();

				boolean res = methodName.startsWith("on") &&
						method.getAnnotation(annotation) != null;
				return res;
			}

			private boolean hasAnnotation(TransformMethod method)
			{
				boolean res = method.getAnnotation(OnEvent.class) != null &&
						method.getAnnotation(annotation) != null;
				return res;
			}
		});
	}

	/**
	 * Read and process restriction on page classes annotated with {@link Restricted} annotation
	 *
	 * @param transformation Contains class-specific information used when transforming a raw component class
	 *                       into an executable component class.
	 * @param model		  Mutable version of {@link org.apache.tapestry5.model.ComponentModel} used during
	 *                       the transformation phase.
	 */
	protected void processPageRestriction(ClassTransformation transformation, MutableComponentModel model)
	{
		Restricted restricted = transformation.getAnnotation(Restricted.class);
		if (restricted != null)
			setGroupRoleMeta(true, model, null, null, restricted.groups(), restricted.role());
	}

	/**
	 * inject meta datas about annotated methods
	 *
	 * @param transformation Contains class-specific information used when transforming a raw component class
	 *                       into an executable component class.
	 * @param model		  Mutable version of {@link org.apache.tapestry5.model.ComponentModel} used during
	 *                       the transformation phase.
	 */
	protected void processEventHandlerRestrictions(ClassTransformation transformation, MutableComponentModel model)
	{
		List<TransformMethod> matchedMethods = getMatchedMethods(transformation, Restricted.class);

		for (TransformMethod method : matchedMethods)
		{
			Restricted restricted = method.getAnnotation(Restricted.class);

			String componentId = extractComponentId(method, method.getAnnotation(OnEvent.class));
			String eventType = extractEventType(method, method.getAnnotation(OnEvent.class));

			setGroupRoleMeta(false, model, componentId, eventType, restricted.groups(), restricted.role());
		}
	}

	/**
	 * Extract the component id from the method signature or the annotation, following
	 * the principles of the Tapestry5 conventions. If the {@link OnEvent} annotation
	 * is present it takes precedence over the method signature.
	 * This method is taken from Tapestry5 OnEventWorker.
	 *
	 * @param method	 the {@link TransformMethod} signature to look for
	 * @param annotation the, eventually present, {@link OnEvent} annotation.
	 *
	 * @return the componentId of the associated component, it could also be
	 *         the empty string if the event handler method is not associated to a
	 *         particular component.
	 */
	protected String extractComponentId(TransformMethod method, OnEvent annotation)
	{
		if (annotation != null) return annotation.component();

		// Method name started with "on". Extract the component id, if present.

		String name = method.getName();

		int fromx = name.indexOf("From");

		if (fromx < 0) return "";

		return name.substring(fromx + 4);
	}

	/**
	 * Extract the event type from the method signatur or the annotation, following
	 * the principles of the Tapestry5 conventions. If the {@link OnEvent} annotation
	 * is present it takes precendence over the method signature.
	 * This method is taken from Tapestry5 OnEventWorker.
	 *
	 * @param method	 the {@link TransformMethod} signature to look for
	 * @param annotation the, eventually present, {@link OnEvent} annotation.
	 *
	 * @return the event type for which the method act as an 'handler method', it
	 *         could not be an empty string and defaults to <code>Action</code>.
	 */
	protected String extractEventType(TransformMethod method, OnEvent annotation)
	{
		if (annotation != null) return annotation.value();

		// Method name started with "on". Extract the event type.

		String name = method.getName();

		int fromx = name.indexOf("From");

		return fromx == -1 ? name.substring(2) : name.substring(2, fromx);
	}

	/**
	 * set meta values for role and groups.
	 *
	 * @param metaForPage if true, we set meta for a page, otherwise for an event
	 * @param model
	 * @param componentId the component id
	 * @param eventType   the event type
	 * @param groups	  groups for access
	 * @param roleWeight  role weight for access
	 */
	protected void setGroupRoleMeta(boolean metaForPage, MutableComponentModel model, String componentId, String eventType, String[] groups, int roleWeight)
	{
		// mark the page as restricted, even if the no group or role weight set.
		if (metaForPage)
			model.setMeta(ChenilleKitAccessConstants.RESTRICTED_PAGE, Boolean.TRUE.toString());
		
		String groupsString = ChenillekitAccessInternalUtils.getStringArrayAsString(groups);

		if (groupsString.trim().length() > 0)
			model.setMeta(ChenillekitAccessInternalUtils.getGroupsMetaId(metaForPage, componentId, eventType), groupsString);

		model.setMeta(ChenillekitAccessInternalUtils.getRoleMetaId(metaForPage, componentId, eventType), Integer.toString(roleWeight));
	}
}
