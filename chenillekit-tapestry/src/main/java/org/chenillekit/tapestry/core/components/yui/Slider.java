package org.chenillekit.tapestry.core.components.yui;

import org.apache.tapestry5.Asset;
import org.apache.tapestry5.Binding;
import org.apache.tapestry5.BindingConstants;
import org.apache.tapestry5.ComponentResources;
import org.apache.tapestry5.FieldTranslator;
import org.apache.tapestry5.FieldValidationSupport;
import org.apache.tapestry5.FieldValidator;
import org.apache.tapestry5.MarkupWriter;
import org.apache.tapestry5.NullFieldStrategy;
import org.apache.tapestry5.RenderSupport;
import org.apache.tapestry5.ValidationException;
import org.apache.tapestry5.ValidationTracker;
import org.apache.tapestry5.annotations.Environmental;
import org.apache.tapestry5.annotations.IncludeJavaScriptLibrary;
import org.apache.tapestry5.annotations.IncludeStylesheet;
import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.annotations.Path;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.services.ComponentDefaultProvider;
import org.apache.tapestry5.services.Request;

import org.chenillekit.tapestry.core.base.AbstractYahooFormComponent;

/**
 * @version $Id$
 */
@IncludeJavaScriptLibrary(value = {"${yahoo.yui}/dragdrop/dragdrop${yahoo.yui.mode}.js",
		"${yahoo.yui}/slider/slider${yahoo.yui.mode}.js",
		"../../Chenillekit.js", "Slider.js"})
@IncludeStylesheet(value = {"${yahoo.yui}/slider/assets/skins/sam/slider.css"})
public class Slider extends AbstractYahooFormComponent
{
	/**
	 * The value to be read and updated. This is not necessarily a string, a translator may be provided to convert
	 * between client side and server side representations. If not bound, a default binding is made to a property of the
	 * container matching the component's id. If no such property exists, then you will see a runtime exception due to
	 * the unbound value parameter.
	 */
	@Parameter(required = true, principal = true)
	private Number value;

	/**
	 * The user presentable label for the field. If not provided, a reasonable label is generated from the component's
	 * id, first by looking for a message key named "id-label" (substituting the component's actual id), then by
	 * converting the actual id to a presentable string (for example, "userId" to "User Id").
	 */
	@Parameter(defaultPrefix = BindingConstants.LITERAL)
	private String label;

	@Parameter(required = false, defaultPrefix = BindingConstants.PROP, value = "false")
	private boolean vertical;

	@Parameter(required = false, defaultPrefix = BindingConstants.LITERAL)
	private String changeCallback;

	@Parameter(required = false, defaultPrefix = BindingConstants.PROP, value = "200")
	private int length;

	@Parameter(required = false, defaultPrefix = BindingConstants.PROP, value = "0")
	private int ticks;

	/**
	 * The object that will perform input validation (which occurs after translation). The validate binding prefix is
	 * generally used to provide this object in a declarative fashion.
	 */
	@Parameter(defaultPrefix = BindingConstants.VALIDATE)
	@SuppressWarnings("unchecked")
	private FieldValidator<Object> validate;

	@Inject
	private Request request;

	/**
	 * The object which will perform translation between server-side and client-side representations. If not specified,
	 * a value will usually be generated based on the type of the value parameter.
	 */
	@Parameter(required = true, allowNull = false, defaultPrefix = BindingConstants.TRANSLATE)
	private FieldTranslator<Object> translate;

	/**
	 * Defines how nulls on the server side, or sent from the client side, are treated. The selected strategy may
	 * replace the nulls with some other value. The default strategy leaves nulls alone.  Another built-in strategy,
	 * zero, replaces nulls with the value 0.
	 */
	@Parameter(defaultPrefix = BindingConstants.NULLFIELDSTRATEGY, value = "default")
	private NullFieldStrategy nulls;

	@Inject
	private FieldValidationSupport fieldValidationSupport;

	@Inject
	private ComponentResources resources;

	/**
	 * RenderSupport to get unique client side id.
	 */
	@Inject
	private RenderSupport renderSupport;

	@Environmental
	private ValidationTracker tracker;

	/**
	 * RenderSupport to get unique client side id.
	 */
	@Inject
	@Path("${yahoo.yui}/slider/assets/thumb-n.gif")
	private Asset sliderThumb;

	@Inject
	private ComponentDefaultProvider defaultProvider;

	/**
	 * Computes a default value for the "translate" parameter using {@link org.apache.tapestry5.services.ComponentDefaultProvider#defaultTranslator(String,
	 * org.apache.tapestry5.ComponentResources)}.
	 */
	final Binding defaultTranslate()
	{
		return defaultProvider.defaultTranslatorBinding("value", resources);
	}

	/**
	 * Computes a default value for the "validate" parameter using {@link org.apache.tapestry5.services.FieldValidatorDefaultSource}.
	 */
	final Binding defaultValidate()
	{
		return defaultProvider.defaultValidatorBinding("value", resources);
	}

	/**
	 * The default value is a property of the container whose name matches the component's id. May return null if the
	 * container does not have a matching property.
	 *
	 * @deprecated Likely to be removed in the future, use {@link org.apache.tapestry5.annotations.Parameter#autoconnect()}
	 *             instead
	 */
	final Binding defaultValue()
	{
		return createDefaultParameterBinding("value");
	}

	/**
	 * Tapestry render phase method.
	 * Start a tag here, end it in afterRender
	 *
	 * @param writer the markup writer
	 */
	void beginRender(MarkupWriter writer)
	{
		writer.element("div", "id", getClientId() + "bg", "class", "yui-" + (vertical ? "v" : "h") + "-slider", "tabindex", "-1");
		writer.element("div", "id", getClientId() + "thumb", "class", "yui-slider-thumb");
		writer.element("img", "src", sliderThumb.toClientURL());
		writer.end();
		writer.end();
		writer.end();


		writer.element("input",
					   "type", "hidden",
					   "id", getClientId() + "Value",
					   "name", getControlName(),
					   "value", value);
		writer.end();
	}

	/**
	 * Tapestry render phase method. End a tag here.
	 *
	 * @param writer the markup writer
	 */

	void afterRender(MarkupWriter writer)
	{
		renderSupport.addScript("new Ck.YuiSlider('%s', '%s', %d, 0, %d, %d, '%s');",
								getClientId(), (vertical ? "vert" : "horiz"), value, length, ticks, changeCallback);
	}


	/**
	 * Method implemented by subclasses to actually do the work of processing the submission of the form. The element's
	 * elementName property will already have been set. This method is only invoked if the field is <strong>not {@link
	 * #isDisabled() disabled}</strong>.
	 *
	 * @param elementName the name of the element (used to find the correct parameter in the request)
	 */
	protected void processSubmission(String elementName)
	{
		String rawValue = request.getParameter(elementName);

		tracker.recordInput(this, rawValue);

		try
		{
			Object translated = fieldValidationSupport.parseClient(rawValue, resources, translate, nulls);

			fieldValidationSupport.validate(translated, resources, validate);

			value = (Number) translated;
		}
		catch (ValidationException ex)
		{
			tracker.recordError(this, ex.getMessage());
		}
	}
}
