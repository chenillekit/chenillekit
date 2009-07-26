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
 *
 */

package org.chenillekit.tapestry.core.mixins.yui;

import org.apache.tapestry5.BindingConstants;
import org.apache.tapestry5.ComponentEventCallback;
import org.apache.tapestry5.ComponentResources;
import org.apache.tapestry5.Field;
import org.apache.tapestry5.Link;
import org.apache.tapestry5.MarkupWriter;
import org.apache.tapestry5.RenderSupport;
import org.apache.tapestry5.StreamResponse;
import org.apache.tapestry5.annotations.IncludeJavaScriptLibrary;
import org.apache.tapestry5.annotations.IncludeStylesheet;
import org.apache.tapestry5.annotations.InjectContainer;
import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.corelib.components.TextArea;
import org.apache.tapestry5.internal.util.Holder;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.json.JSONObject;
import org.apache.tapestry5.upload.services.MultipartDecoder;
import org.apache.tapestry5.upload.services.UploadedFile;
import org.apache.tapestry5.util.TextStreamResponse;

import org.chenillekit.tapestry.core.base.AbstractYuiElement;

/**
 * @version $Id$
 */
@IncludeStylesheet(value = {"${yahoo.yui}/assets/skins/sam/skin.css"})
@IncludeJavaScriptLibrary(value = {"../../Chenillekit.js",
		"${yahoo.yui}/container/container_core${yahoo.yui.mode}.js",
		"${yahoo.yui}/menu/menu${yahoo.yui.mode}.js",
		"${yahoo.yui}/button/button${yahoo.yui.mode}.js",
		"${yahoo.yui}/connection/connection${yahoo.yui.mode}.js",
		"${yahoo.yui}/editor/editor${yahoo.yui.mode}.js",
		"yui-image-uploader26.js",
		"Editor.js"})
public class Editor extends AbstractYuiElement
{
	private static String INTERNAL_EVENT = "internalUploaded";
	private static String UPLOAD_EVENT = "uploaded";

	/**
	 * Allow image uploads.
	 */
	@Parameter(required = false, defaultPrefix = BindingConstants.PROP, value = "false")
	private boolean allowUploads;

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
	private MultipartDecoder decoder;

	@InjectContainer
	private Field clientElement;

	/**
	 * Tapestry render phase method.
	 * Start a tag here, end it in afterRender
	 *
	 * @param writer the markup writer
	 */
	void beginRender(MarkupWriter writer)
	{
		if (!(clientElement instanceof TextArea))
			throw new RuntimeException("Mixin 'yui/Editor' must have a TextArea as parent component");
	}


	/**
	 * Tapestry render phase method. End a tag here.
	 *
	 * @param writer the markup writer
	 */
	void afterRender(MarkupWriter writer)
	{
		JSONObject options = new JSONObject();

		configure(options);

		options.put("disabled", clientElement.isDisabled());
		options.put("handleSubmit", true);

		Link uploadEventLink = null;
		if (allowUploads)
			uploadEventLink = resources.createEventLink(INTERNAL_EVENT);

		renderSupport.addScript("new Ck.YuiEditor('%s', '%s', %s);", clientElement.getClientId(), uploadEventLink, options);
	}

	StreamResponse onInternalUploaded()
	{
		UploadedFile uploaded = decoder.getFileUpload("fileElement");

		final Holder<JSONObject> valueHolder = Holder.create();

		ComponentEventCallback callback = new ComponentEventCallback<JSONObject>()
		{
			public boolean handleResult(JSONObject result)
			{
				valueHolder.put(result);
				return true;
			}
		};

		resources.triggerEvent(UPLOAD_EVENT, new Object[]{uploaded}, callback);

		return new TextStreamResponse("text/html", valueHolder.get().toString());
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