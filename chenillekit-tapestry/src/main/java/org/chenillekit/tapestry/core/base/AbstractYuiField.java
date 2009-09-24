package org.chenillekit.tapestry.core.base;

import org.apache.tapestry5.MarkupWriter;
import org.apache.tapestry5.MarkupWriterListener;
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
	void afterRenderTemplate(final MarkupWriter writer)
	{
		writer.addListener(new MarkupWriterListener()
		{
			public void elementDidStart(Element element)
			{
				Element bodyElement = element.getDocument().find("html/body");
				if (bodyElement == null)
					return;

				String cssClassValue = bodyElement.getAttribute("class");
				if (cssClassValue == null)
					bodyElement.attribute("class", YUI_CSS_CLASS);
				else
				{
					if (!cssClassValue.contains(YUI_CSS_CLASS))
						bodyElement.addClassName(YUI_CSS_CLASS);
				}

				if (bodyElement.getAttribute("class") != null)
					writer.removeListener(this);
			}

			public void elementDidEnd(Element element)
			{
			}
		});
	}
}