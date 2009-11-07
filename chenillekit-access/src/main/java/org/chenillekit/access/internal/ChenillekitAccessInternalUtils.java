/**
 *
 */
package org.chenillekit.access.internal;

import org.apache.tapestry5.EventContext;
import org.apache.tapestry5.ioc.services.TypeCoercer;
import org.chenillekit.access.ChenilleKitAccessConstants;

/**
 *	@version $Id$
 */
public class ChenillekitAccessInternalUtils
{
	private static final String[] NO_GROUPS = new String[0];
	
	private static final String CK_EVENT_CONTEXT_PREFIX = "ckEventContext";
	private static final String CK_EVENT_CONTEXT_DELIMITER = "####";

	private ChenillekitAccessInternalUtils() {}

	/**
	 * Build a string representing the context to be stored inside a cookie.
	 *
	 * @param context the context to be stored in the cookie
	 * @return a String rapresentation of the {@link EventContext}
	 */
	public static final String getContextAsString(EventContext context)
	{
		String res = "";
		
		if (context == null)
			return res;
		
		int parametersCount = context.getCount();
		
		res = CK_EVENT_CONTEXT_PREFIX;
		
		for (int i = 0; i < parametersCount; i++)
		{
			res = res + context.get(String.class, i) + CK_EVENT_CONTEXT_DELIMITER;
		}

		return res;
	}
	
	/**
	 * 
	 * @param coercer
	 * @param contextString
	 * @return
	 */
	public static final EventContext getContextFromString(TypeCoercer coercer, String contextString)
	{	
		if (contextString == null || !contextString.startsWith(CK_EVENT_CONTEXT_PREFIX))
		{
			throw new RuntimeException("Cannot parse the contextString: " + contextString);
		}
		
		String actual = contextString.substring(CK_EVENT_CONTEXT_PREFIX.length(), contextString.length());
		
		String[] elements = actual.split(CK_EVENT_CONTEXT_DELIMITER);
		
		return new ChenilleKitAccessEventContext(coercer, elements);
	}
	
	/**
	 * check if user has required role to access page/component/event.
	 *
	 * @param userRoleWeigh     role weigh the user have
	 * @param requiredRoleWeigh role weigh required for page/component/event access
	 *
	 * @return true if user fulfill the required role
	 */
	public static final boolean hasUserRequiredRole(int userRoleWeigh, int requiredRoleWeigh)
	{
		return userRoleWeigh >= requiredRoleWeigh;
	}

	/**
	 * Check if user has required group to access page/component/event.
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
		if (requiredGroups == null || requiredGroups.length == 0)
			return true;

		if (userGroups != null)
		{
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
	 * Build an CSV string from group array.
	 *
	 * @return CSV string
	 */
	public static final String getStringArrayAsString(String[] groups)
	{
		String csvString = "";

		if (groups != null)
		{
			for (int i = 0; i < groups.length; i++)
			{
				if (i == 0)
					csvString += groups[i];
				else
					csvString += "," + groups[i];
			}
		}

		return csvString;
	}

	/**
	 * Split the {@link String} array into a CSV {@link String}.
	 * 
	 * @param groups the array to split
	 * @return the CSV {@link String}
	 */
	public static final String[] getStringAsStringArray(String groups)
	{
		if ( groups == null ||
				ChenilleKitAccessConstants.NO_RESTRICTION.equals(groups)) return NO_GROUPS;
		
		return groups.split(",");
	}
	
}
