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

package org.chenillekit.tapestry.core.utils;

/**
 * holder class for YouTube video informations.
 *
 * @version $Id$
 */
public class ProtoTubeIdHolder
{
	/**
	 * the YouTube video id
	 */
	private String id;

	/**
	 * title that should displayed for this video thumb 
	 */
	private String title;

	public ProtoTubeIdHolder(String id, String title)
	{
		this.id = id;
		this.title = title;
	}

	public String getId()
	{
		return id;
	}

	public void setId(String id)
	{
		this.id = id;
	}

	public String getTitle()
	{
		return title;
	}

	public void setTitle(String title)
	{
		this.title = title;
	}
}
