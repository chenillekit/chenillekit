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

package org.chenillekit.secure.services.impl;

import org.apache.tapestry5.model.MutableComponentModel;
import org.apache.tapestry5.services.ClassTransformation;
import org.apache.tapestry5.services.ComponentClassTransformWorker;

import org.chenillekit.secure.ChenilleKitSecureConstants;
import org.chenillekit.secure.annotations.Private;

/**
 * @author <a href="mailto:mlusetti@gmail.com">M.Lusetti</a>
 * @version $Id$
 */
public class AccessControlWorker implements ComponentClassTransformWorker
{
    /* (non-Javadoc)
      * @see org.apache.tapestry5.services.ComponentClassTransformWorker#transform(org.apache.tapestry5.services.ClassTransformation, org.apache.tapestry5.model.MutableComponentModel)
      */
    public void transform(ClassTransformation transformation, MutableComponentModel model)
    {
        Private pagePrivate = transformation.getAnnotation(Private.class);

        if (pagePrivate != null)
        {
            model.setMeta(ChenilleKitSecureConstants.PRIVATE_PAGE, "true");
            model.setMeta(ChenilleKitSecureConstants.PRIVATE_PAGE_GROUP, pagePrivate.group());
            model.setMeta(ChenilleKitSecureConstants.PRIVATE_PAGE_ROLE, Integer.toString(pagePrivate.role()));
        }
//		else
//		{
//			model.setMeta(AmadoriEcommConstants.PRIVATE_PAGE, "false");
//		}

        // TODO Add checks for method annotations
    }

}
