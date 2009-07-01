package org.chenillekit.tapestry.core.components.yui;

/**
 * @version $Id$
 */
public class Tab
{
	/**
	 * The Tab's label text (or innerHTML).
	 */
	private String label;

	/**
	 * The content displayed by the activeTab.
	 * default is null.
	 */
	private String content;

	public Tab(String label)
	{
		this(label, null);
	}

	public Tab(String label, String content)
	{
		this.label = label;
		this.content = content;
	}

	public void setLabel(String label)
	{
		this.label = label;
	}

	public void setContent(String content)
	{
		this.content = content;
	}

	public String getLabel()
	{
		if (label == null)
			throw new RuntimeException("label of Tab cant be null!");

		return label;
	}

	public String getContent()
	{
		return content;
	}
}
