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
        Restricted pageRestricted = transformation.getAnnotation(Restricted.class);
        if (pageRestricted != null)
        {
            model.setMeta(ChenilleKitAccessConstants.PRIVATE_PAGE_GROUP, pageRestricted.groups()[0]);
            model.setMeta(ChenilleKitAccessConstants.PRIVATE_PAGE_ROLE, Integer.toString(pageRestricted.roles()[0]));
        }

        for (TransformMethodSignature method : transformation.findMethodsWithAnnotation(Restricted.class))
        {
        	
        }
    }
}
