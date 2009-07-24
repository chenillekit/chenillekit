package org.chenillekit.tapestry.core.utils;

/**
 * @author <a href="mailto:homburgs@googlemail.com">sven</a>
 * @version $Id$
 */
public class ProtoTubeIdHolder
{
	private String id;
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
