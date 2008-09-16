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

import org.apache.tapestry5.model.MutableComponentModel;
import org.apache.tapestry5.services.ClassTransformation;
import org.apache.tapestry5.services.ComponentClassTransformWorker;
import org.apache.tapestry5.services.TransformMethodSignature;

import org.chenillekit.access.ChenilleKitAccessConstants;
import org.chenillekit.access.annotations.Restricted;

/**
 * @author <a href="mailto:mlusetti@gmail.com">M.Lusetti</a>
 * @version $Id: AccessControlWorker.java 88 2008-06-16 16:43:40Z homburgs $
 */
public class RestrictedWorker implements ComponentClassTransformWorker
{
    /* (non-Javadoc)
      * @see org.apache.tapestry5.services.ComponentClassTransformWorker#transform(org.apache.tapestry5.services.ClassTransformation, org.apache.tapestry5.model.MutableComponentModel)
      */
    public void transform(ClassTransformation transformation, MutableComponentModel model)
    {
        // inject all values from @Restricted into page metas.
        injectMetasIntoPageClass(transformation, model);
        
        injectMethodsMetas(transformation, model);
        
        injectPageComponentsMetas(transformation, model);
    }

    /**
     * inject all values from @Restricted into page metas.
     *
     * @param transformation Contains class-specific information used when transforming a raw component class
     *                       into an executable component class.
     * @param model          Mutable version of {@link org.apache.tapestry5.model.ComponentModel} used during
     *                       the transformation phase.
     */
    private void injectMetasIntoPageClass(ClassTransformation transformation, MutableComponentModel model)
    {
        Restricted pageRestricted = transformation.getAnnotation(Restricted.class);
        if (pageRestricted != null)
        {
            String roleString = getIntArrayAsString(pageRestricted.roles());
            String groupString = getStringArrayAsString(pageRestricted.groups());

            if (roleString.length() > 0)
                model.setMeta(ChenilleKitAccessConstants.PRIVATE_PAGE_ROLE, roleString);

            if (groupString.length() > 0)
                model.setMeta(ChenilleKitAccessConstants.PRIVATE_PAGE_GROUP, getStringArrayAsString(pageRestricted.groups()));
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
    private void injectMethodsMetas(ClassTransformation transformation, MutableComponentModel model)
    {
    	for (TransformMethodSignature method : transformation.findMethodsWithAnnotation(Restricted.class))
        {

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
    private void injectPageComponentsMetas(ClassTransformation transformation, MutableComponentModel model)
    {
    	
    }

    /**
     * build an CSV string from role array.
     *
     * @return CSV string
     */
    private String getIntArrayAsString(int[] roles)
    {
        String csvString = "";

        for (int i = 0; i < roles.length; i++)
        {
            if (i == 0)
                csvString += String.valueOf(roles[i]);
            else
                csvString += "," + String.valueOf(roles[i]);
        }

        return csvString;
    }

    /**
     * build an CSV string from group array.
     *
     * @return CSV string
     */
    private String getStringArrayAsString(String[] groups)
    {
        String csvString = "";

        for (int i = 0; i < groups.length; i++)
        {
            if (i == 0)
                csvString += groups[i];
            else
                csvString += "," + groups[i];
        }

        return csvString;
    }
}
