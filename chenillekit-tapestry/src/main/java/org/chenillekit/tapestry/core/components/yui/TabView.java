package org.chenillekit.tapestry.core.components.yui;

import java.util.List;

import org.apache.tapestry5.BindingConstants;
import org.apache.tapestry5.ComponentEventCallback;
import org.apache.tapestry5.ComponentResources;
import org.apache.tapestry5.MarkupWriter;
import org.apache.tapestry5.RenderSupport;
import org.apache.tapestry5.annotations.IncludeJavaScriptLibrary;
import org.apache.tapestry5.annotations.IncludeStylesheet;
import org.apache.tapestry5.annotations.OnEvent;
import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.annotations.SupportsInformalParameters;
import org.apache.tapestry5.internal.util.Holder;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.json.JSONArray;
import org.apache.tapestry5.json.JSONObject;
import org.apache.tapestry5.services.Request;

import org.chenillekit.tapestry.core.base.AbstractYahooComponent;

/**
 * The TabView component is designed to enable developers to create
 * navigable tabbed views of content.
 *
 * @version $Id$
 */
@IncludeJavaScriptLibrary(value = {"${yahoo.yui}/tabview/tabview${yahoo.yui.mode}.js",
		"${yahoo.yui}/connection/connection${yahoo.yui.mode}.js",
		"TabView.js"})
@IncludeStylesheet(value = {"${yahoo.yui}/tabview/assets/skins/sam/tabview.css"})
@SupportsInformalParameters
public class TabView extends AbstractYahooComponent
{
	private static final String INTERNAL_EVENT_NAME = "internalEvent";
	public static final String EVENT_NAME = "tabClicked";
	public static final String PARAM_NAME = "tabId";

	/**
	 * Integer indicating the index of the Tab that should actived in the TabView.
	 * The index starts with 0, the second is 1 and so on ...
	 */
	@Parameter(required = false, defaultPrefix = BindingConstants.PROP, value = "0")
	private int activeIndex;

	/**
	 * the optional tab list.
	 */
	@Parameter(allowNull = true, required = false, defaultPrefix = BindingConstants.PROP)
	private List<Tab> tabs;

	/**
	 * How the Tabs should be oriented relative to the TabView.
	 * possible is "left", "right", "top" or "bottom"
	 */
	@Parameter(required = false, defaultPrefix = BindingConstants.LITERAL, value = "top")
	private String orientation;

	/**
	 * RenderSupport to get unique client side id.
	 */
	@Inject
	private RenderSupport renderSupport;

	/**
	 * For blocks, messages, crete actionlink, trigger event.
	 */
	@Inject
	private ComponentResources resources;

	@Inject
	private Request request;

	private JSONArray tabsArray;

	/**
	 * Tapestry render phase method.
	 * Start a tag here, end it in afterRender
	 *
	 * @param writer the markup writer
	 */
	void beginRender(MarkupWriter writer)
	{
		tabsArray = new JSONArray();

		if (tabs != null)
		{
			if (tabs.size() == 0)
				throw new RuntimeException("tab list should not be null");

			writer.element("div", "id", getClientId(), "class", "yui-navset");

			writer.element("ul", "class", "yui-nav");
			for (int counter = 0; counter < tabs.size(); counter++)
			{
				Tab tab = tabs.get(counter);

				String tabUrl = "#tab" + counter;
				if (tab.getContent() == null)
					tabsArray.put(new JSONObject().put("id", counter).
							put("dataSrc", resources.createEventLink(INTERNAL_EVENT_NAME, counter).toAbsoluteURI()));

				writer.element("li", "class", (counter == activeIndex ? "selected" : ""));

				writer.element("a", "href", tabUrl);

				writer.element("em");
				writer.write(tab.getLabel());
				writer.end();
				writer.end();
				writer.end();
			}
			writer.end();

			writer.element("div", "class", "yui-content");
			for (Tab tab : tabs)
			{
				writer.element("div");
				writer.element("p");
				writer.writeRaw(tab.getContent());
				writer.end();
				writer.end();
			}

			writer.end();

			writer.end();
		}
		else
		{
			writer.element("div", "id", getClientId(), "class", "yui-navset");
			resources.renderInformalParameters(writer);
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

		options.put("activeIndex", activeIndex);
		options.put("orientation", orientation);

		configure(options);

		if (tabs == null)
		{
			writer.end();
		}

		renderSupport.addScript("new Ck.YuiTabView('%s', %s, %s);", getClientId(), tabsArray, options);
	}

	@OnEvent(value = TabView.INTERNAL_EVENT_NAME)
	Object onTabClicked(String tabId)
	{
		final Holder<Object> valueHolder = Holder.create();

		ComponentEventCallback callback = new ComponentEventCallback<Object>()
		{
			public boolean handleResult(Object result)
			{
				valueHolder.put(result);
				return true;
			}
		};

		resources.triggerEvent(EVENT_NAME, new Object[]{tabId}, callback);

		return valueHolder.get();
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