package org.chenillekit.tapestry.core.components.google;

import org.apache.tapestry5.BindingConstants;
import org.apache.tapestry5.ClientElement;
import org.apache.tapestry5.ComponentResources;
import org.apache.tapestry5.annotations.Component;
import org.apache.tapestry5.annotations.Environmental;
import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.services.javascript.JavascriptSupport;

import org.chenillekit.tapestry.core.components.Hidden;

/**
 * A GMarker marks a position on a (GPlotter) map.
 * A marker object has a latitude (lat) and a longitude (lng), which is the geographical position where the marker is
 * anchored on the map.
 * The default icon G_DEFAULT_ICON is used to display the GMarker. @todo: implement GIcon
 */
public class GMarker implements ClientElement
{

	/**
	 * The id used to generate a page-unique client-side identifier for the component. If a component renders multiple
	 * times, a suffix will be appended to the to id to ensure uniqueness.
	 */
	@Parameter(value = "prop:componentResources.id", defaultPrefix = BindingConstants.LITERAL)
	private String clientId;

	/**
	 * RenderSupport to get unique client side id.
	 */
	@Environmental
	private JavascriptSupport javascriptSupport;

	@Inject
	private ComponentResources resources;

	@Parameter(defaultPrefix = BindingConstants.LITERAL, allowNull = false)
	private String gPlotterId;

	/**
	 * The latitude coordinate in degrees, as a number between -90 and +90
	 */
	@Parameter(defaultPrefix = BindingConstants.LITERAL, allowNull = false)
	private Double lat;

	/**
	 * The longitude coordinate in degrees, as a number between -180 and +180
	 */
	@Parameter(defaultPrefix = BindingConstants.LITERAL, allowNull = false)
	private Double lng;

	/**
	 * Enables the marker to be dragged and dropped around the map
	 */
	@Parameter(defaultPrefix = BindingConstants.LITERAL, value = "false")
	private Boolean draggable;

	/**
	 * The content of the info window is given as a string that contains HTML text.
	 */
	@Parameter(defaultPrefix = BindingConstants.LITERAL)
	private String infoWindowHtml;

	@Component(parameters = {"value=inherit:lat"})
	private Hidden hiddenLat;

	@Component(parameters = {"value=inherit:lng"})
	private Hidden hiddenLng;

	private String assignedClientId;

	void setupRender()
	{
		assignedClientId = javascriptSupport.allocateClientId(clientId);
	}

	void afterRender()
	{
		javascriptSupport.addScript("%s.setMarker(%s, %s, '%s','%s','%s',%s);", gPlotterId, lat, lng,
				resources.isBound("infoWindowHtml") ? infoWindowHtml : "", hiddenLat.getClientId(),
				hiddenLng.getClientId(), draggable);
	}

	/**
	 * Returns a unique id for the element. This value will be unique for any given rendering of a
	 * page. This value is intended for use as the id attribute of the client-side element, and will
	 * be used with any DHTML/Ajax related JavaScript.
	 */
	public String getClientId()
	{
		return assignedClientId;
	}

	public Boolean getDraggable()
	{
		return draggable;
	}

}
