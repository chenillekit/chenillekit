/**
 * 
 */
package org.chenillekit.access.services.impl;

import org.chenillekit.access.WebSessionUser;

/**
 * @author massimo
 *
 */
public class TestWebSessionUser implements WebSessionUser
{
	private final String name;
	private final int roleWeight;
	private final String[] groups;
	
	
	public TestWebSessionUser(String name, int roleWeigth, String... groups)
	{
		this.name = name;
		this.roleWeight = roleWeigth;
		this.groups = groups;
	}

	/* (non-Javadoc)
	 * @see org.chenillekit.access.WebSessionUser#getGroups()
	 */
	public String[] getGroups()
	{
		return this.groups;
	}

	/* (non-Javadoc)
	 * @see org.chenillekit.access.WebSessionUser#getName()
	 */
	public String getName()
	{
		return this.name;
	}

	/* (non-Javadoc)
	 * @see org.chenillekit.access.WebSessionUser#getRoleWeight()
	 */
	public int getRoleWeight()
	{
		return this.roleWeight;
	}

	/**
	 * 
	 */
	public int getUserId()
	{
		return 0;
	}
	

}
