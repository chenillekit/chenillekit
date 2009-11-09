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

package org.chenillekit.access;

import java.io.Serializable;

/**
 *
 * @version $Id$
 */
public interface WebSessionUser extends Serializable
{	
	/**
	 * Get the human readble <em>name</em> of the user currently logged in.
	 * Usually this is used in a (sort of) layout component to show that a
	 * user is logged into the application.
	 * 
	 * @return the human readable name of the person logged in
	 */
	public String getName();
	
	/**
	 * Get the weight, in terms of responsibility, of the user currently logged in.
	 * Usually more to weight correspond more permissions. 
	 *  
	 * @return the weight (level of responsibility) of the user
	 */
	public int getRoleWeight();

	/**
	 * Get the groups which the user belongs to. Usually each group is associated
	 * with a kind of organization membership inside the application scope.
	 *  
	 * @return the array of the groups which the user belongs to
	 */
	public String[] getGroups();
	
	/**
	 * Get the id (like a PKEY) of the user. Normally this represent the primary
	 * key of the user in the database.
	 * 
	 * @return the id (identifier) of the user currently logged in
	 */
	public int getUserId();
}
