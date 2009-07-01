package org.chenillekit.tapestry.core.mixins.yui;

import org.apache.tapestry5.BindingConstants;
import org.apache.tapestry5.ClientElement;
import org.apache.tapestry5.MarkupWriter;
import org.apache.tapestry5.RenderSupport;
import org.apache.tapestry5.annotations.IncludeJavaScriptLibrary;
import org.apache.tapestry5.annotations.IncludeStylesheet;
import org.apache.tapestry5.annotations.InjectContainer;
import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.corelib.base.AbstractField;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.json.JSONObject;

import org.chenillekit.tapestry.core.base.AbstractYahooComponent;

/**
 * @author <a href="mailto:homburgs@googlemail.com">sven</a>
 * @version $Id$
 */
@IncludeStylesheet(value = {"${yahoo.yui}/button/assets/skins/sam/button.css"})
@IncludeJavaScriptLibrary(value = {"${yahoo.yui}/button/button${yahoo.yui.mode}.js"})
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
	private RenderSupport renderSupport;

	@InjectContainer
	private ClientElement clientElement;

	/**
	 * Tapestry render phase method.
	 * Start a tag here, end it in afterRender
	 *
	 * @param writer the markup writer
	 */
	void beginRender(MarkupWriter writer)
	{
		writer.element("span", "id", getClientId(), "class", "yui-button");
		writer.element("span", "class", "first-child");

		if (type != null && type.length() > 0)
		{
			if (type.equalsIgnoreCase("menu"))
			{
				writer.element("input", "type", "button", "id", clientElement.getClientId() + "Button");
				writer.end();
			}
		}
	}


	/**
	 * Tapestry render phase method. End a tag here.
	 *
	 * @param writer the markup writer
	 */
	void afterRender(MarkupWriter writer)
	{
		JSONObject options = new JSONObject();

		writer.end();
		writer.end();

		options.put("label", label);
		options.put("id", clientElement.getClientId());

		if (clientElement instanceof AbstractField)
			options.put("disabled", ((AbstractField) clientElement).isDisabled());
		else
			options.put("disabled", isDisabled());

		if (type != null && type.length() > 0)
		{
			if (type.equalsIgnoreCase("menu"))
				options.put("menu", clientElement.getClientId() + "Button");
		}

		configure(options);

		renderSupport.addScript("new YAHOO.widget.Button('%s', %s);", getClientId(), options);
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
