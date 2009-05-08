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
package org.chenillekit.access.services;

/**
 *
 * @version $Id$
 *
 */
public interface AccessValidator
{
	/**
	 * Check the rights of the user for the page requested
	 *
	 * @param pageName    name of the page
	 * @param componentId component id (not used yet)
	 * @param eventType   event type (not used yet)
	 *
	 * @return if true then leave the chain
	 */
	public boolean hasAccess(String pageName, String componentId, String eventType);

}
