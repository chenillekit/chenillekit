/*
 *  Apache License
 *  Version 2.0, January 2004
 *  http://www.apache.org/licenses/
 *
 *  Copyright 2008 by chenillekit.org
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 */

/**
 *
 */

package org.chenillekit.access.services.impl;

import org.apache.tapestry5.annotations.OnEvent;
import org.apache.tapestry5.model.MutableComponentModel;
import org.apache.tapestry5.services.ClassTransformation;
import org.apache.tapestry5.services.ComponentClassTransformWorker;
import org.apache.tapestry5.services.TransformMethodSignature;
import org.chenillekit.access.ChenilleKitAccessConstants;
import org.chenillekit.access.ChenilleKitAccessException;
import org.chenillekit.access.annotations.Restricted;
import org.chenillekit.access.internal.ChenillekitAccessInternalUtils;
import org.slf4j.Logger;

/**
 *
 * @version $Id: AccessControlWorker.java 88 2008-06-16 16:43:40Z homburgs $
 */
public class RestrictedWorker implements ComponentClassTransformWorker
{
	private final Logger logger;
	
	public RestrictedWorker(Logger logger)
	{
		this.logger = logger;
	}


	/* (non-Javadoc)
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

	/**
	 * Read and process restriction on page classes annotated with {@link Restricted} annotation
	 *
	 * @param transformation Contains class-specific information used when transforming a raw component class
	 *                       into an executable component class.
	 * @param model          Mutable version of {@link org.apache.tapestry5.model.ComponentModel} used during
	 *                       the transformation phase.
	 */
	private void processPageRestriction(ClassTransformation transformation, MutableComponentModel model)
	{
		Restricted pageRestricted = transformation.getAnnotation(Restricted.class);
		if (pageRestricted != null)
		{
			String roleWeigh = Integer.toString(pageRestricted.role());
			String[] groups = pageRestricted.groups();

			model.setMeta(ChenilleKitAccessConstants.RESTRICTED_PAGE_ROLE, roleWeigh);

			if (groups.length > 0)
				model.setMeta(ChenilleKitAccessConstants.RESTRICTED_PAGE_GROUP,
						ChenillekitAccessInternalUtils.getStringArrayAsString(groups));
			if (logger.isDebugEnabled())
			{
				logger.debug(transformation.getClassName() + " has restrictions:");
				logger.debug("    " + model.getMeta(ChenilleKitAccessConstants.RESTRICTED_PAGE_ROLE));
				logger.debug("    " + model.getMeta(ChenilleKitAccessConstants.RESTRICTED_PAGE_GROUP));
			}
		}
	}

	/**
	 * inject meta datas about annotated methods
	 *
	 * @param transformation Contains class-specific information used when transforming a raw component class
	 *                       into an executable component class.
	 * @param model          Mutable version of {@link org.apache.tapestry5.model.ComponentModel} used during
	 *                       the transformation phase.
	 */
	private void processEventHandlerRestrictions(ClassTransformation transformation, MutableComponentModel model)
	{
		
		for (TransformMethodSignature method : transformation.findMethodsWithAnnotation(Restricted.class))
		{
			String methodName = method.getMethodName();
			Restricted restricted = transformation.getMethodAnnotation(method, Restricted.class);
			OnEvent event = transformation.getMethodAnnotation(method, OnEvent.class);
			
			if (methodName.startsWith("on") || event != null)
			{
				String componentId = extractComponentId(method, event);
				String eventType = extractEventType(method, event);

				String groupMeta = ChenillekitAccessInternalUtils.buildMetaForHandlerMethod(componentId,
						eventType,
						ChenilleKitAccessConstants.RESTRICTED_EVENT_HANDLER_GROUPS_SUFFIX);

				String roleMeta = ChenillekitAccessInternalUtils.buildMetaForHandlerMethod(componentId,
						eventType,
						ChenilleKitAccessConstants.RESTRICTED_EVENT_HANDLER_ROLE_SUFFIX);
				
				String groupsString = ChenillekitAccessInternalUtils.getStringArrayAsString(restricted.groups());
				
				if (groupsString.trim().length() > 0)
					model.setMeta(groupMeta, groupsString);
				
				model.setMeta(roleMeta, Integer.toString(restricted.role()));
				
				if (logger.isDebugEnabled())
				{
					logger.debug( methodName + " has restrictions:");
					logger.debug("    " + roleMeta + " => " + Integer.toString(restricted.role()));
					logger.debug("    " + groupMeta + " => " + groupsString);
				}
			}
			else
			{
				throw new ChenilleKitAccessException("Cannot put Restricted annotation on a non event handler method");
			}
		}
	}

	/**
	 * Inject meta datas about annotated methods
	 *
	 * @param transformation Contains class-specific information used when transforming a raw component class
	 *                       into an executable component class.
	 * @param model          Mutable version of {@link org.apache.tapestry5.model.ComponentModel} used during
	 *                       the transformation phase.
	 */
	private void processComponentsRestrictions(ClassTransformation transformation, MutableComponentModel model)
	{
		logger.warn("Component restriction is not yet implemented");
	}
	
	/**
	 * Extract the component id from the method signature or the annotation, following
	 * the principles of the Tapestry5 conventions. If the {@link OnEvent} annotation
	 * is present it takes precedence over the method signature.
	 * This method is taken from Tapestry5 OnEventWorker.
	 * 
	 * @param method the {@link TransformMethodSignature} signature to look for
	 * @param annotation the, eventually present, {@link OnEvent} annotation. 
	 * @return the componentId of the associated component, it could also be
	 * the empty string if the event handler method is not associated to a
	 * particular component.
	 */
	private String extractComponentId(TransformMethodSignature method, OnEvent annotation)
    {
        if (annotation != null) return annotation.component();

        // Method name started with "on". Extract the component id, if present.

        String name = method.getMethodName();

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
	 * @param method the {@link TransformMethodSignature} signature to look for
	 * @param annotation the, eventually present, {@link OnEvent} annotation.
	 * @return the event type for which the method act as an 'handler method', it
	 * could not be an empty string and defaults to <code>Action</code>.
	 */
	private String extractEventType(TransformMethodSignature method, OnEvent annotation)
    {
        if (annotation != null) return annotation.value();

        // Method name started with "on". Extract the event type.

        String name = method.getMethodName();

        int fromx = name.indexOf("From");

        return fromx == -1 ? name.substring(2) : name.substring(2, fromx);
    }
}
