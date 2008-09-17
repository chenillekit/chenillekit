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
import org.chenillekit.access.annotations.Restricted;
import org.chenillekit.access.utils.ChenillekitAccessInternalUtils;

/**
 * @author <a href="mailto:mlusetti@gmail.com">M.Lusetti</a>
 * @version $Id: AccessControlWorker.java 88 2008-06-16 16:43:40Z homburgs $
 */
public class RestrictedWorker implements ComponentClassTransformWorker
{
//	private final Logger logger;
	
	public RestrictedWorker()
	{
//		this.logger = logger;
	}
	
    /* (non-Javadoc)
      * @see org.apache.tapestry5.services.ComponentClassTransformWorker#transform(org.apache.tapestry5.services.ClassTransformation, org.apache.tapestry5.model.MutableComponentModel)
      */
    public void transform(ClassTransformation transformation, MutableComponentModel model)
    {
        processPageRestriction(transformation, model);
        
        processEventHandlerRestrictions(transformation, model);
        
        processComponentsRestrictions(transformation, model);
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
            String roleWeigh = Integer.toString(pageRestricted.roles());
            String groupString = ChenillekitAccessInternalUtils.getStringArrayAsString(pageRestricted.groups());

            model.setMeta(ChenilleKitAccessConstants.RESTRICTED_PAGE_ROLE, roleWeigh);

            if (groupString.length() > 0)
                model.setMeta(ChenilleKitAccessConstants.RESTRICTED_PAGE_GROUP, ChenillekitAccessInternalUtils.getStringArrayAsString(pageRestricted.groups()));
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
    			String componentId = ChenillekitAccessInternalUtils.extractComponentId(method, event);
    			String eventType = ChenillekitAccessInternalUtils.extractEventType(method, event);
    			
    			model.setMeta(ChenillekitAccessInternalUtils.buildMetaForHandlerMethod(componentId,
    								eventType,
    								ChenilleKitAccessConstants.RESTRICTED_EVENT_HANDLER_GROUPS_SUFFIX),
    								ChenillekitAccessInternalUtils.getStringArrayAsString(restricted.groups()));
    			model.setMeta(ChenillekitAccessInternalUtils.buildMetaForHandlerMethod(componentId,
    								eventType,
    								ChenilleKitAccessConstants.RESTRICTED_EVENT_HANDLER_ROLE_SUFFIX),
    								Integer.toString(restricted.roles()));
    		}
    		else
    		{
//    			this.logger.warn(ChenilleKitAccessConstants.CHENILLEKIT_ACCESS,
//    					"Restrict annotation on a non event handler method: " + methodName + ", IGNORED");
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
    private void processComponentsRestrictions(ClassTransformation transformation, MutableComponentModel model)
    {
    	
    }
}
