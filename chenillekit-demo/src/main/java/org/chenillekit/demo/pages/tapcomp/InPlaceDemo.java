/*
 *  Apache License
 *  Version 2.0, January 2004
 *  http://www.apache.org/licenses/
 *
 *  Copyright 2008 by chenillekit.org
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 */

package org.chenillekit.demo.pages.tapcomp;

import java.util.List;

import org.apache.tapestry5.annotations.Component;
import org.apache.tapestry5.annotations.OnEvent;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.annotations.Retain;
import org.apache.tapestry5.beaneditor.BeanModel;
import org.apache.tapestry5.corelib.components.Grid;
import org.apache.tapestry5.ioc.Messages;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.services.BeanModelSource;

import org.chenillekit.demo.components.LeftSideMenu;
import org.chenillekit.demo.data.Track;
import org.chenillekit.demo.services.MusicLibrary;
import org.chenillekit.tapestry.core.components.InPlaceCheckbox;
import org.chenillekit.tapestry.core.components.InPlaceEditor;

/**
 * @version $Id$
 */
public class InPlaceDemo
{
	@Component(parameters = {"menuName=demo"})
	private LeftSideMenu menu;

	@Property
	private boolean inPlaceCheckboxValue;

	@Component(parameters = {"value=inPlaceCheckboxValue", "onCompleteCallback=onCheckboxClicked", "context=literal:testValue"})
	private InPlaceCheckbox inPlaceCheckbox;

	@Inject
	private MusicLibrary musicLibrary;

	@Inject
	private Messages messages;

	@Property
	private Track track;

	@Component(parameters = {"value=track.title", "context=track.id", "size=30"})
	private InPlaceEditor inPlaceEditor;

	@Component(parameters = {"source=trackList", "row=track", "model=beanModel", "rowsPerPage=10"})
	private Grid grid;

	@Inject
	private BeanModelSource beanModelSource;

	@Retain
	@Property(write = false)
	private BeanModel beanModel;

	@Retain
	@Property(read = false)
	private List<Track> trackList;

	/**
	 * Tapestry page lifecycle method.
	 * Called when the page is instantiated and added to the page pool.
	 * Initialize components, and resources that are not request specific.
	 */
	void pageLoaded()
	{
		beanModel = beanModelSource.create(Track.class, false, messages);
		beanModel.exclude("genre", "playCount", "rating", "album");
		trackList = musicLibrary.getTracks();
	}

	public List<Track> getTrackList()
	{
		return trackList;
	}

	@OnEvent(component = "inPlaceCheckbox", value = "clicked")
	void onClickedFromPlaceCheckbox(boolean value)
	{
		inPlaceCheckboxValue = value;
		System.err.println("inPlaceCheckbox value is: " + value);
	}

	@OnEvent(component = "inPlaceEditor", value = InPlaceEditor.SAVE_EVENT)
	void actionFromEditor(Long id, String value)
	{
		Track track = musicLibrary.getById(id);
		track.setTitle(value);
		System.err.println("Track #" + id + " changed to '" + value + "'");
	}
}