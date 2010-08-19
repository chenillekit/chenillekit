/*
 * Apache License
 * Version 2.0, January 2004
 * http://www.apache.org/licenses/
 *
 * Copyright 2008-2010 by chenillekit.org
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 */

package org.chenillekit.access.services;

/**
 * @author <a href="mailto:shomburg@hsofttec.com">S.Homburg</a>
 * @version $Id$
 */
public interface SecurityService
{
	/**
	 * check for user is authenticated.
	 */
	boolean isAuthenticate();

	/**
	 * do logout user from application and the session will invalidated.
	 */
	void logout();

	/**
	 * do logout user from application.
	 *
	 * @param invalidateSession if true, the session will invalidated
	 */
	void logout(boolean invalidateSession);

	/**
	 * returns true, if user profile contains all af the given groups.
	 */
	boolean hasGroups(String ... groups);

	/**
	 * returns true, if user profile contains the given group.
	 */
	boolean hasGroup(String group);
}
