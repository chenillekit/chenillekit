/**
 * 
 */
package org.chenillekit.access.utils;

import org.apache.tapestry5.annotations.OnEvent;
import org.apache.tapestry5.services.TransformMethodSignature;
import org.chenillekit.access.ChenilleKitAccessConstants;

/**
 * @author massimo
 *
 */
public class ChenillekitAccessInternalUtils
{
	private ChenillekitAccessInternalUtils() {}
	
	
	/**
     * check if user has required role to access page/component/event.
     *
     * @param userRoles     role weigh the user have
     * @param requiredRoles role weigh required for page/component/event access
     *
     * @return true if user fulfill the required role
     */
    public static final boolean hasUserRequiredRole(int userRoleWeigh, int requiredRoleWeigh)
    {
        return userRoleWeigh >= requiredRoleWeigh ? true : false;
    }

    /**
     * check if user has required group to access page/component/event.
     *
     * @param userGroups     groups the user have
     * @param requiredGroups groups required for page/component/event access
     *
     * @return true if user has the required group
     */
    public static final boolean hasUserRequiredGroup(String[] userGroups, String[] requiredGroups)
    {
        boolean hasGroup = false;

        /**
         * if no group required
         */
        if (requiredGroups.length == 0)
            return true;

        for (String requiredGroup : requiredGroups)
        {
            for (String userGroup : userGroups)
            {
                if (userGroup.equalsIgnoreCase(requiredGroup))
                {
                    hasGroup = true;
                    break;
                }
            }

            if (hasGroup)
                break;
        }

        return hasGroup;
    }
	
	/**
	 * 
	 * @param componentId
	 * @param eventType
	 * @param suffix
	 * @return
	 */
	public static final String buildMetaForHandlerMethod(String componentId, String eventType, String suffix)
    {
    	return ChenilleKitAccessConstants.RESTRICTED_EVENT_HANDLER_PREFIX
    				+ "-" + componentId + "-" + eventType + "-" + suffix;
    	
    }
	
	/**
     * build an CSV string from group array.
     *
     * @return CSV string
     */
    public static final String getStringArrayAsString(String[] groups)
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
    
    /**
     * 
     * @param groups
     * @return
     */
    public static final String[] getStringAsStringArray(String groups)
    {
    	return groups.split(",");
    }
    
    /**
     * This code is taken deliberatly from
     * http://svn.apache.org/viewvc/tapestry/tapestry5/trunk/tapestry-core/src/main/java/org/apache/tapestry5/internal/transform/OnEventWorker.java?view=log
     */
    public static final String extractComponentId(TransformMethodSignature method, OnEvent annotation)
    {
        if (annotation != null) return annotation.component();

        // Method name started with "on". Extract the component id, if present.

        String name = method.getMethodName();

        int fromx = name.indexOf("From");

        if (fromx < 0) return "";

        return name.substring(fromx + 4);
    }
    
    /**
     * This code is taken deliberatly from:
     * http://svn.apache.org/viewvc/tapestry/tapestry5/trunk/tapestry-core/src/main/java/org/apache/tapestry5/internal/transform/OnEventWorker.java?view=log
     */
    public static final String extractEventType(TransformMethodSignature method, OnEvent annotation)
    {
        if (annotation != null) return annotation.value();

        // Method name started with "on". Extract the event type.

        String name = method.getMethodName();

        int fromx = name.indexOf("From");

        return fromx == -1 ? name.substring(2) : name.substring(2, fromx);
    }

}
