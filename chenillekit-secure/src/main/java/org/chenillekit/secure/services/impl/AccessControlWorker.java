/**
 * 
 */
package org.chenillekit.secure.services.impl;

import org.apache.tapestry5.model.MutableComponentModel;
import org.apache.tapestry5.services.ClassTransformation;
import org.apache.tapestry5.services.ComponentClassTransformWorker;
import org.chenillekit.secure.ChenilleKitConstants;
import org.chenillekit.secure.annotations.Private;

/**
 * @author massimo
 *
 */
public class AccessControlWorker implements ComponentClassTransformWorker
{

	/* (non-Javadoc)
	 * @see org.apache.tapestry5.services.ComponentClassTransformWorker#transform(org.apache.tapestry5.services.ClassTransformation, org.apache.tapestry5.model.MutableComponentModel)
	 */
	public void transform(ClassTransformation transformation,
			MutableComponentModel model)
	{
		Private pagePrivate = transformation.getAnnotation(Private.class);
		
		if (pagePrivate != null)
		{
			model.setMeta(ChenilleKitConstants.PRIVATE_PAGE, "true");
			model.setMeta(ChenilleKitConstants.PRIVATE_PAGE_GROUP, pagePrivate.group());
			model.setMeta(ChenilleKitConstants.PRIVATE_PAGE_ROLE, Integer.toString(pagePrivate.role()));
		}
//		else
//		{
//			model.setMeta(AmadoriEcommConstants.PRIVATE_PAGE, "false");
//		}
		
		
		// TODO Add checks for method annotations
		
		
	}

}
