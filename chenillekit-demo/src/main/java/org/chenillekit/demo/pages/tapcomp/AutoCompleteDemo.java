/*
 * Apache License
 * Version 2.0, January 2004
 * http://www.apache.org/licenses/
 *
 * Copyright 2008 by chenillekit.org
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 */

package org.chenillekit.demo.pages.tapcomp;

import java.util.List;

import org.apache.tapestry5.FieldTranslator;
import org.apache.tapestry5.MarkupWriter;
import org.apache.tapestry5.ValidationException;
import org.apache.tapestry5.annotations.Component;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.ioc.Messages;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.ioc.internal.util.CollectionFactory;

import org.chenillekit.demo.components.LeftSideMenu;
import org.chenillekit.demo.data.Track;
import org.chenillekit.demo.services.MusicLibrary;
import org.chenillekit.tapestry.core.components.prototype_ui.AutoComplete;

/**
 * @version $Id$
 */
public class AutoCompleteDemo
{
	@Property
	@Inject
	private MusicLibrary musicLibrary;

	@Inject
	private Messages messages;

	@Property
	private Track track;

	@Persist
	@Property
	private List<Track> selectedTracks;

	@Property
	private Track selectedRow;

	@Component(parameters = {"menuName=demo"})
	private LeftSideMenu menu;

	@Component(parameters = {"selected=selectedTracks", "translate=prop:translator", "label=title"})
	private AutoComplete autoComplete;

	/**
	 * Tapestry render phase method.
	 * Initialize temporary instance variables here.
	 */
	void setupRender()
	{
		if (selectedTracks == null)
			selectedTracks = CollectionFactory.newList();
	}

	public List<Track> onProvidecompletionsFromAutoComplete(String partialValue)
	{
		List<Track> resultSet = musicLibrary.findByMatchingTitle(partialValue);
		return resultSet;
	}

	public FieldTranslator getTranslator()
	{
		return new FieldTranslator<Track>()
		{

			/**
			 * Converts a server-side value to a client-side string. This allows for formatting of the value in a way
			 * appropriate to the end user. The output client value should be parsable by {@link #parse(String)}.
			 *
			 * @param value the server side value (which will not be null)
			 *
			 * @return client-side value to present to the user
			 */
			public String toClient(Track value)
			{
				String clientValue = "0";
				if (value != null)
					clientValue = String.valueOf(value.getId());

				return clientValue;
			}

			/**
			 * Invokes {@link org.apache.tapestry5.Translator#render(org.apache.tapestry5.Field , String, org.apache.tapestry5.MarkupWriter ,org.apache.tapestry5.services.FormSupport)}. This is
			 * called at a point "inside" the tag, so that additional attributes may be added.  In many cases, the underlying
			 * {@link org.apache.tapestry5.Validator} may write client-side JavaScript to enforce the constraint as well.
			 *
			 * @param writer markup writer to direct output to.
			 *
			 * @see org.apache.tapestry5.MarkupWriter#attributes(Object[])
			 */
			public void render(MarkupWriter writer)
			{
			}

			/**
			 * Returns the type of  the server-side value.
			 *
			 * @return a type
			 */
			public Class<Track> getType()
			{
				return Track.class;
			}

			/**
			 * Invoked after the client-submitted value has been {@link org.apache.tapestry5.Translator translated} to check that the value conforms
			 * to expectations (often, in terms of minimum or maximum value). If and only if the value is approved by all
			 * Validators is the value applied by the field.
			 *
			 * @throws org.apache.tapestry5.ValidationException
			 *          if the value violates the constraint
			 */
			public Track parse(String clientValue) throws ValidationException
			{
				Track serverValue = null;

				if (clientValue != null && clientValue.length() > 0 && !clientValue.equals("0"))
					serverValue = musicLibrary.getById(new Long(clientValue));

				return serverValue;
			}
		};
	}
}