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

package org.chenillekit.tapestry.core.mixins.yui;

import org.apache.tapestry5.BindingConstants;
import org.apache.tapestry5.ClientElement;
import org.apache.tapestry5.MarkupWriter;
import org.apache.tapestry5.annotations.AfterRender;
import org.apache.tapestry5.annotations.Import;
import org.apache.tapestry5.annotations.InjectContainer;
import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.corelib.base.AbstractField;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.json.JSONObject;
import org.apache.tapestry5.services.javascript.JavaScriptSupport;
import org.chenillekit.tapestry.core.base.AbstractYahooComponent;

/**
 * @author <a href="mailto:homburgs@googlemail.com">sven</a>
 * @version $Id$
 */
@Import(stack = {"yahoo"}, stylesheet = {"${yahoo.yui}/button/assets/skins/sam/button.css"},
		library = {"${yahoo.yui}/button/button${yahoo.yui.mode}.js", "Button.js"})
//@MixinAfter
public class Button extends AbstractYahooComponent
{
	@Parameter(required = false, defaultPrefix = BindingConstants.LITERAL)
	private String label;

	@Parameter(required = false, defaultPrefix = BindingConstants.LITERAL)
	private String type;

	/**
	 * RenderSupport to get unique client side id.
	 */
	@Inject
	private JavaScriptSupport javascriptSupport;

	@InjectContainer
	private ClientElement clientElement;

	/**
	 * Tapestry render phase method. End a tag here.
	 *
	 * @param writer the markup writer
	 */
	@AfterRender
	void afterRender(MarkupWriter writer)
	{
		JSONObject options = new JSONObject();

		options.put("label", label);

		if (clientElement instanceof AbstractField)
			options.put("disabled", ((AbstractField) clientElement).isDisabled());
		else
			options.put("disabled", isDisabled());

		configure(options);

		javascriptSupport.addScript("new Button('%s', %s);", clientElement.getClientId(), options);
	}

	/**
	 * Invoked to allow subclasses to further configure the parameters passed to this mixin's javascript
	 * options. Subclasses may override this method to configure additional features of this mixin.
	 * <p/>
	 * This implementation does nothing.
	 *
	 * @param options option object
	 */
	protected void configure(JSONObject options)
	{
	}
}
