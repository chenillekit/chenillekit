package org.chenillekit.tapestry.core.mixins;

import org.apache.tapestry5.BindingConstants;
import org.apache.tapestry5.ClientElement;
import org.apache.tapestry5.MarkupWriter;
import org.apache.tapestry5.annotations.Environmental;
import org.apache.tapestry5.annotations.IncludeJavaScriptLibrary;
import org.apache.tapestry5.annotations.IncludeStylesheet;
import org.apache.tapestry5.annotations.InjectContainer;
import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.dom.Element;
import org.apache.tapestry5.services.javascript.JavascriptSupport;

/**
 * @version $Id$
 */
@IncludeJavaScriptLibrary(value = {"niftycube.js"})
@IncludeStylesheet(value = {"niftyCorners.css"})
public class NiftyCube
{
	/**
	 * let you allow to restrict the direction of resizing.
	 * 'vertical', 'horizontal' or empty for both
	 */
	@Parameter(required = false, defaultPrefix = BindingConstants.LITERAL, value = "normal")
	private String options;

	/**
	 * RenderSupport to get unique client side id.
	 */
	@Environmental
	private JavascriptSupport javascriptSupport;

	/**
	 * The field component to which this mixin is attached.
	 */
	@InjectContainer
	private ClientElement clientElement;

	/**
	 * Tapestry render phase method. End a tag here.
	 *
	 * @param writer the markup writer
	 */
	void afterRender(MarkupWriter writer)
	{
		Element element = writer.getDocument().getElementById(clientElement.getClientId());
		javascriptSupport.addScript("Nifty('%s#%s','%s');", element.getName(), clientElement.getClientId(), options);
	}
}
