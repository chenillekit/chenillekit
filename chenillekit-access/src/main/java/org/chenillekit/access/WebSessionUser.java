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

/**
 *
 * @version $Id: WebSessionUser.java 447 2009-03-28 08:54:25Z mlusetti $
 */
public interface WebSessionUser
{	
	public String getName();
	
	public int getRoleWeight();

	public String[] getGroups();
}
