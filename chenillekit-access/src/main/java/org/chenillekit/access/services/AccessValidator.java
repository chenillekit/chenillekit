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

import org.apache.tapestry5.services.ComponentEventRequestParameters;
import org.apache.tapestry5.services.PageRenderRequestParameters;

/**
 *
 * @version $Id$
 *
 */
public interface AccessValidator
{	
	/**
	 * Check the rights of the user to render the requested page instance.
	 * 
	 * @param renderParameters the {@link PageRenderRequestParameters} requested
	 * @return <code>true</code> where the user has enough rights or the page
	 * isn't restricted, <code>false</code> otherwise
	 */
	public boolean hasAccessToPageRender(PageRenderRequestParameters renderParameters);
	
	/**
	 * Check the rights of the user to execute/fire the corresponding event on components.
	 * 
	 * @param eventParameters the {@link ComponentEventRequestParameters} requested
	 * @return <code>true</code> where the user has enough rights or the event isn't
	 * restricted, <code>false</code> otherwise.
	 */
	public boolean hasAccessToComponentEvent(ComponentEventRequestParameters eventParameters);

}
