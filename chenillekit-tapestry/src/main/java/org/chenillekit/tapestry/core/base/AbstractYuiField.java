package org.chenillekit.tapestry.core.base;

import org.apache.tapestry5.MarkupWriter;
import org.apache.tapestry5.annotations.IncludeJavaScriptLibrary;
import org.apache.tapestry5.corelib.base.AbstractField;
import org.apache.tapestry5.dom.Element;

/**
 * @version $Id$
 */
@IncludeJavaScriptLibrary(value = {"${yahoo.yui}/yahoo-dom-event/yahoo-dom-event.js",
		"${yahoo.yui}/element/element${yahoo.yui.mode}.js"})
abstract public class AbstractYuiField extends AbstractField
{
	private static final String YUI_CSS_CLASS = "yui-skin-sam";

	/**
	 * Tapestry render phase method.
	 * Called after component template is rendered.
	 *
	 * @param writer the markup writer
	 */
	void afterRenderTemplate(MarkupWriter writer)
	{
		Element bodyElement = writer.getDocument().find("html/body");
		if (bodyElement == null)
			throw new RuntimeException("there ist no 'html/body' element in this page");

		String cssClassValue = bodyElement.getAttribute("class");
		if (cssClassValue == null)
			bodyElement.attribute("class", YUI_CSS_CLASS);
		else
		{
			if (!cssClassValue.contains(YUI_CSS_CLASS))
				bodyElement.addClassName(YUI_CSS_CLASS);
		}
	}
}