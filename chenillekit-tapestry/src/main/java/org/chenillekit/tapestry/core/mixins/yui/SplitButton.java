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

import org.chenillekit.tapestry.core.base.AbstractYuiElement;

/**
 * @author <a href="mailto:homburgs@googlemail.com">sven</a>
 * @version $Id$
 */
@IncludeStylesheet(value = {"${yahoo.yui}/button/assets/skins/sam/button.css"})
@IncludeJavaScriptLibrary(value = {"${yahoo.yui}/button/button${yahoo.yui.mode}.js"})
public class SplitButton extends AbstractYuiElement
{
	@Parameter(required = false, defaultPrefix = BindingConstants.LITERAL)
	private String label;

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

	}


	/**
	 * Tapestry render phase method. End a tag here.
	 *
	 * @param writer the markup writer
	 */
	void afterRender(MarkupWriter writer)
	{
		JSONObject options = new JSONObject();

		options.put("label", label);

		if (clientElement instanceof AbstractField)
			options.put("disabled", ((AbstractField) clientElement).isDisabled());
		else
			options.put("disabled", isDisabled());

		configure(options);

		renderSupport.addScript("new YAHOO.widget.Button('%s', %s)", clientElement.getClientId(), options);
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