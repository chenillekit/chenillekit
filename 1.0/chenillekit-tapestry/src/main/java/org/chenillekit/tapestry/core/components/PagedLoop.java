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

package org.chenillekit.tapestry.core.components;

import org.apache.tapestry5.Block;
import org.apache.tapestry5.ClientElement;
import org.apache.tapestry5.PrimaryKeyEncoder;
import org.apache.tapestry5.RenderSupport;
import org.apache.tapestry5.annotations.BeginRender;
import org.apache.tapestry5.annotations.Component;
import org.apache.tapestry5.annotations.Environmental;
import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.corelib.components.Delegate;
import org.apache.tapestry5.corelib.components.Loop;
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
    private RenderSupport _pageRenderSupport;

    @Parameter(value = "prop:componentResources.id", defaultPrefix = "literal")
    private String _clientId;

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
    private Iterable<?> _source;

    private PagedSource<?> _pagedSource;

    /**
     * Defines where the pager (used to navigate within the "pages" of results)
     * should be displayed: "top", "bottom", "both" or "none".
     */
    @Parameter(value = "bottom", defaultPrefix = "literal")
    private String _pagerPosition;

    private PagerPosition _internalPagerPosition;

    /**
     * The number of rows of data displayed on each page. If there are more rows than will fit, the Grid will divide
     * up the rows into "pages" and (normally) provide a pager to allow the user to navigate within the overall result set.
     */
    @Parameter("25")
    private int _rowsPerPage;

    @Persist
    private int _currentPage;

    /**
     * The current value, set before the component renders its body.
     */
    @SuppressWarnings("unused")
    @Parameter
    private Object _value;

    /**
     * If true and the Loop is enclosed by a Form, then the normal state saving logic is turned off.
     * Defaults to false, enabling state saving logic within Forms.
     */
    @SuppressWarnings("unused")
    @Parameter
    private boolean _volatile;

    /**
     * The index into the source items.
     */
    @SuppressWarnings("unused")
    @Parameter
    private int _index;

    /**
     * Optional primary key converter; if provided and inside a form and not volatile, then each
     * iterated value is converted and stored into the form.
     */
    @SuppressWarnings("unused")
    @Parameter
    private PrimaryKeyEncoder<?, ?> _encoder;

    @SuppressWarnings("unused")
    @Component(parameters = {"source=pagedSource",
            "element=prop:element", "value=inherit:value",
            "volatile=inherit:volatile", "encoder=inherit:encoder",
            "index=inherit:index"})
    private Loop _loop;

    @Component(parameters = {"source=pagedSource", "rowsPerPage=rowsPerPage",
            "currentPage=currentPage"})
    private Pager _internalPager;

    @SuppressWarnings("unused")
    @Component(parameters = "to=pagerTop")
    private Delegate _pagerTop;

    @SuppressWarnings("unused")
    @Component(parameters = "to=pagerBottom")
    private Delegate _pagerBottom;

    /**
     * A Block to render instead of the table (and pager, etc.) when the source
     * is empty. The default is simply the text "There is no data to display".
     * This parameter is used to customize that message, possibly including
     * components to allow the user to create new objects.
     */
    @Parameter(value = "block:empty")
    private Block _empty;

    private String _assignedClientId;

    public String getElement()
    {
        return element;
    }

    public Object getPagerTop()
    {
        return _internalPagerPosition.isMatchTop() ? _internalPager : null;
    }

    public Object getPagerBottom()
    {
        return _internalPagerPosition.isMatchBottom() ? _internalPager : null;
    }

    public PagedSource<?> getPagedSource()
    {
        return _pagedSource;
    }

    public int getRowsPerPage()
    {
        return _rowsPerPage;
    }

    public void setRowsPerPage(int rowsPerPage)
    {
        _rowsPerPage = rowsPerPage;
    }

    public int getCurrentPage()
    {
        return _currentPage;
    }

    public void setCurrentPage(int currentPage)
    {
        _currentPage = currentPage;
    }



	@SuppressWarnings("unchecked")
    Object setupRender()
    {
		if (_currentPage == 0)
			_currentPage = 1;

		_assignedClientId = _pageRenderSupport.allocateClientId(_clientId);
        _internalPagerPosition = new StringToEnumCoercion<PagerPosition>(
                PagerPosition.class).coerce(_pagerPosition);

        _pagedSource = new PagedSource(_source);

        int availableRows = _pagedSource.getTotalRowCount();

        // If there's no rows, display the empty block placeholder.
        if (availableRows == 0)
        {
            return _empty;
        }

        int startIndex = (_currentPage - 1) * _rowsPerPage;
        int endIndex = Math.min(startIndex + _rowsPerPage - 1,
                                availableRows - 1);

        _pagedSource.prepare(startIndex, endIndex);

        return null;
    }

    @BeginRender
    Object begin()
    {
        // Skip rendering of component (template, body, etc.) when there's
        // nothing to display.
        // The empty placeholder will already have rendered.
        return (_pagedSource.getTotalRowCount() != 0);
    }

    void onAction(int newPage)
    {
        // TODO: Validate newPage in range
        _currentPage = newPage;
    }

    /**
     * Returns a unique id for the element. This value will be unique for any given rendering of a
     * page. This value is intended for use as the id attribute of the client-side element, and will
     * be used with any DHTML/Ajax related JavaScript.
     */
    public String getClientId()
    {
        return _assignedClientId;
    }
}
