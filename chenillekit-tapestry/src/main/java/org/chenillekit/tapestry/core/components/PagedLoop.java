/*
 * Apache License
 * Version 2.0, January 2004
 * http://www.apache.org/licenses/
 *
 * Copyright 2008-2010 by chenillekit.org
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 */

package org.chenillekit.tapestry.core.components;

import org.apache.tapestry5.Block;
import org.apache.tapestry5.ClientElement;
import org.apache.tapestry5.ComponentResources;
import org.apache.tapestry5.ValueEncoder;
import org.apache.tapestry5.annotations.BeginRender;
import org.apache.tapestry5.annotations.Component;
import org.apache.tapestry5.annotations.Environmental;
import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.corelib.components.Delegate;
import org.apache.tapestry5.corelib.components.Loop;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.services.ComponentDefaultProvider;
import org.apache.tapestry5.services.javascript.JavascriptSupport;
import org.apache.tapestry5.util.StringToEnumCoercion;
import org.chenillekit.tapestry.core.internal.PagedSource;
import org.chenillekit.tapestry.core.internal.PagerPosition;

/**
 * Provides a paged list similar to the grid component. However, this uses a ul
 * instead of a table by default. The list and its items are configurable via
 * parameters.
 *
 * @version $Id$
 */
public class PagedLoop implements ClientElement
{
    @Environmental
    private JavascriptSupport javascriptSupport;

    @Parameter(value = "prop:componentResources.id", defaultPrefix = "literal")
    private String clientId;

    /**
     * The element to render. If not null, then the loop will render the indicated element around its body (on each pass through the loop).
     * The default is derived from the component template.
     */
    @Parameter(value = "prop:componentResources.elementName", defaultPrefix = "literal")
    private String element;

    /**
     * Defines the collection of values for the loop to iterate over.
     */
    @SuppressWarnings("unused")
    @Parameter(required = true)
    private Iterable<?> source;

    private PagedSource<?> pagedSource;

    /**
     * Defines where the pager (used to navigate within the "pages" of results)
     * should be displayed: "top", "bottom", "both" or "none".
     */
    @Parameter(value = "bottom", defaultPrefix = "literal")
    private String pagerPosition;

    private PagerPosition internalPagerPosition;

    /**
     * The number of rows of data displayed on each page. If there are more rows than will fit, the Grid will divide
     * up the rows into "pages" and (normally) provide a pager to allow the user to navigate within the overall result set.
     */
    @Parameter("25")
    private int rowsPerPage;

    @Persist
    private int currentPage;

    /**
     * The current value, set before the component renders its body.
     */
    @SuppressWarnings("unused")
    @Parameter
    private Object value;

    /**
     * If true and the Loop is enclosed by a Form, then the normal state saving logic is turned off.
     * Defaults to false, enabling state saving logic within Forms.
     */
    @SuppressWarnings("unused")
    @Parameter(name = "volatile")
    private boolean isVolatile;

    /**
     * The index into the source items.
     */
    @SuppressWarnings("unused")
    @Parameter
    private int index;

	/**
	 * Value encoder for the value, usually determined automatically from the type of the property bound to the value
	 * parameter.
	 */
	@Parameter(required = true)
	private ValueEncoder encoder;

    @SuppressWarnings("unused")
    @Component(parameters = {"source=pagedSource",
            "element=prop:element", "value=inherit:value",
            "volatile=inherit:volatile", "encoder=inherit:encoder",
            "index=inherit:index"})
    private Loop loop;

    @Component(parameters = {"source=pagedSource", "rowsPerPage=rowsPerPage",
            "currentPage=currentPage"})
    private Pager internalPager;

    @SuppressWarnings("unused")
    @Component(parameters = "to=pagerTop")
    private Delegate pagerTop;

    @SuppressWarnings("unused")
    @Component(parameters = "to=pagerBottom")
    private Delegate pagerBottom;

    /**
     * A Block to render instead of the table (and pager, etc.) when the source
     * is empty. The default is simply the text "There is no data to display".
     * This parameter is used to customize that message, possibly including
     * components to allow the user to create new objects.
     */
    @Parameter(value = "block:empty")
    private Block empty;

	@Inject
	private ComponentResources resources;

	@Inject
	private ComponentDefaultProvider defaultProvider;

    private String assignedClientId;

    public String getElement()
    {
        return element;
    }

    public Object getPagerTop()
    {
        return internalPagerPosition.isMatchTop() ? internalPager : null;
    }

    public Object getPagerBottom()
    {
        return internalPagerPosition.isMatchBottom() ? internalPager : null;
    }

    public PagedSource<?> getPagedSource()
    {
        return pagedSource;
    }

    public int getRowsPerPage()
    {
        return rowsPerPage;
    }

    public void setRowsPerPage(int rowsPerPage)
    {
        this.rowsPerPage = rowsPerPage;
    }

    public int getCurrentPage()
    {
        return currentPage;
    }

    public void setCurrentPage(int currentPage)
    {
        this.currentPage = currentPage;
    }

	ValueEncoder defaultEncoder()
	{
		return defaultProvider.defaultValueEncoder("value", resources);
	}

	@SuppressWarnings("unchecked")
    Object setupRender()
    {
		if (currentPage == 0)
			currentPage = 1;

		assignedClientId = javascriptSupport.allocateClientId(clientId);
        internalPagerPosition = new StringToEnumCoercion<PagerPosition>(
                PagerPosition.class).coerce(pagerPosition);

        pagedSource = new PagedSource(source);

        int availableRows = pagedSource.getTotalRowCount();

        // If there's no rows, display the empty block placeholder.
        if (availableRows == 0)
        {
            return empty;
        }

        int startIndex = (currentPage - 1) * rowsPerPage;
        int endIndex = Math.min(startIndex + rowsPerPage - 1,
                                availableRows - 1);

        pagedSource.prepare(startIndex, endIndex);

        return null;
    }

    @BeginRender
    Object begin()
    {
        // Skip rendering of component (template, body, etc.) when there's
        // nothing to display.
        // The empty placeholder will already have rendered.
        return (pagedSource.getTotalRowCount() != 0);
    }

    void onAction(int newPage)
    {
        // TODO: Validate newPage in range
        currentPage = newPage;
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
}
