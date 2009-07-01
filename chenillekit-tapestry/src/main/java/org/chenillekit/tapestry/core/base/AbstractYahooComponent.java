package org.chenillekit.tapestry.core.base;

import org.apache.tapestry5.BindingConstants;
import org.apache.tapestry5.ClientElement;
import org.apache.tapestry5.MarkupWriter;
import org.apache.tapestry5.RenderSupport;
import org.apache.tapestry5.annotations.IncludeJavaScriptLibrary;
import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.dom.Element;
import org.apache.tapestry5.ioc.annotations.Inject;

/**
 * @version $Id$
 */
@IncludeJavaScriptLibrary(value = {"${yahoo.yui}/yahoo-dom-event/yahoo-dom-event.js",
		"${yahoo.yui}/element/element${yahoo.yui.mode}.js"})
abstract public class AbstractYahooComponent implements ClientElement
{
	private static final String YUI_CSS_CLASS = "yui-skin-sam";

	/**
	 * The id used to generate a page-unique client-side identifier for the component. If a
	 * component renders multiple times, a suffix will be appended to the to id to ensure
	 * uniqueness. The uniqued value may be accessed via the
	 * {@link #getClientId() clientId property}.
	 */
	@Parameter(value = "prop:componentResources.id", defaultPrefix = BindingConstants.LITERAL)
	private String clientId;

	/**
	 * dis-/enable the button.
	 */
	@Parameter(value = "false")
	private boolean disabled;

	/**
	 * RenderSupport to get unique client side id.
	 */
	@Inject
	private RenderSupport renderSupport;


	private String assignedClientId;

	/**
	 * Tapestry render phase method.
	 * Initialize temporary instance variables here.
	 */
	void setupRender()
	{
		assignedClientId = renderSupport.allocateClientId(clientId);
	}

	/**
	 * Tapestry render phase method.
	 * Called after component template is rendered.
	 *
	 * @param writer the markup writer
	 */
	void afterRenderTemplate(MarkupWriter writer)
	{
		Element bodyElement = writer.getDocument().find("html/body");
		String cssClassValue = bodyElement.getAttribute("class");
		if (cssClassValue == null)
			bodyElement.attribute("class", YUI_CSS_CLASS);
		else
		{
			if (!cssClassValue.contains(YUI_CSS_CLASS))
				bodyElement.addClassName(YUI_CSS_CLASS);
		}
	}

	public final String getClientId()
	{
		return assignedClientId;
	}

	public boolean isDisabled()
	{
		return disabled;
	}
}
