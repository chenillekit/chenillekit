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

package org.chenillekit.tapestry.core.mixins;

import org.apache.tapestry5.BindingConstants;
import org.apache.tapestry5.ClientElement;
import org.apache.tapestry5.MarkupWriter;
import org.apache.tapestry5.annotations.Environmental;
import org.apache.tapestry5.annotations.Import;
import org.apache.tapestry5.annotations.InjectContainer;
import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.services.javascript.JavascriptSupport;

/**
 * This mixin allows a user to more easily enter fixed width input where you would like them to enter the data in a
 * certain format (dates,phone numbers, etc).
 * <p/>
 * It has been tested on Internet Explorer 6/7, Firefox 1.5/2/3, Safari, Opera, and Chrome.
 * A mask is defined by a format made up of mask literals and mask definitions.
 * Any character not in the definitions list below is considered a mask literal.
 * Mask literals will be automatically entered for the user as they type and will
 * not be able to be removed by the user. The following mask definitions are predefined:
 * <p/>
 * <ul>
 * <li>a - Represents an alpha character (A-Z,a-z)</li>
 * <li>9 - Represents a numeric character (0-9)</li>
 * <li>* - Represents an alphanumeric character (A-Z,a-z,0-9)</li>
 * </ul>
 *
 * @version $Id$
 */
@Import(library = {"prototype-maskedinput.js"})
public class MaskedInput
{
	@Parameter(required = true, defaultPrefix = BindingConstants.LITERAL, allowNull = false)
	private String mask;

	@InjectContainer
	private ClientElement clientElement;

	@Environmental
	private JavascriptSupport javascriptSupport;

	/**
	 * Tapestry render phase method. End a tag here.
	 *
	 * @param writer the markup writer
	 */
	void afterRender(MarkupWriter writer)
	{
		javascriptSupport.addScript("new MaskedInput('%s', '%s');", clientElement.getClientId(), mask);
	}
}
