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

import java.io.File;
import java.net.MalformedURLException;
import java.net.URISyntaxException;

import org.apache.tapestry5.Asset;
import org.apache.tapestry5.ComponentResources;
import org.apache.tapestry5.annotations.Component;
import org.apache.tapestry5.annotations.InjectComponent;
import org.apache.tapestry5.annotations.Mixins;
import org.apache.tapestry5.annotations.OnEvent;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.annotations.Retain;
import org.apache.tapestry5.annotations.Service;
import org.apache.tapestry5.corelib.components.ActionLink;
import org.apache.tapestry5.corelib.components.EventLink;
import org.apache.tapestry5.corelib.components.TextArea;
import org.apache.tapestry5.corelib.components.Zone;
import org.apache.tapestry5.internal.services.ContextResource;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.json.JSONObject;
import org.apache.tapestry5.services.AssetFactory;
import org.apache.tapestry5.services.Context;
import org.apache.tapestry5.services.Request;
import org.apache.tapestry5.upload.services.UploadedFile;

import org.chenillekit.demo.components.LeftSideMenu;
import org.chenillekit.tapestry.core.components.yui.Slider;
import org.chenillekit.tapestry.core.components.yui.StateButton;

/**
 * @version $Id$
 */
public class YuiComponentsDemo
{
	@Retain
	@Property
	private boolean stateButtonValue;

	@Retain
	@Property
	private int sliderValue;

	@Retain
	@Property
	private String simpleEditorValue;

	@Component(parameters = {"menuName=demo"})
	private LeftSideMenu menu;

	@Component(parameters = {"event=doNothing"})
	@Mixins("ck/yui/Button")
	private EventLink eventLink;

	@Component(parameters = {"value=stateButtonValue"})
	private StateButton stateButton;

	@Component(parameters = {"value=sliderValue", "changeCallback=sliderChangeCallback"})
	private Slider yuiSlider;

	@Component(parameters = {"value=simpleEditorValue", "Editor.allowUploads=true"})
	@Mixins("ck/yui/Editor")
	private TextArea yuiSimpleEditor;

	@Component(parameters = {"zone=counterZone"})
	@Mixins("ck/yui/Button")
	private ActionLink clicker;

	@Inject
	private ComponentResources resources;

	@Inject
	private Context context;

	@Inject
	@Service("ContextAssetFactory")
	private AssetFactory assetFactory;

	@Property
	@Persist
	private int clickCount;

	@InjectComponent
	private Zone counterZone;

	Object onActionFromClicker()
	{
		clickCount++;

		return counterZone.getBody();
	}

	/**
	 * Request object for information on current request.
	 */
	@Inject
	private Request request;

	@OnEvent(value = "doNothing")
	public void doNothing()
	{
	}

	public JSONObject onUploaded(UploadedFile uploadedFile) throws MalformedURLException, URISyntaxException
	{
		String completeFileName = "/images/" + uploadedFile.getFileName();
		File copied = context.getRealFile(completeFileName);
		uploadedFile.write(copied);

		Asset asset = assetFactory.createAsset(new ContextResource(context, completeFileName));

		return new JSONObject().put("status", "UPLOADED").put("image_url", asset.toClientURL());
	}

	@OnEvent(component = "yuiTabView2", value = "tabClicked")
	public Object doTabClicked(String tabId)
	{
		return resources.getBlock("tab" + tabId);
	}
}