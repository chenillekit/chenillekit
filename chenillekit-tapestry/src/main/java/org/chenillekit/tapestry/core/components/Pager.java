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

import org.apache.tapestry5.BindingConstants;
import org.apache.tapestry5.ComponentResources;
import org.apache.tapestry5.EventConstants;
import org.apache.tapestry5.Link;
import org.apache.tapestry5.MarkupWriter;
import org.apache.tapestry5.annotations.BeginRender;
import org.apache.tapestry5.annotations.IncludeStylesheet;
import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.annotations.SetupRender;
import org.apache.tapestry5.ioc.Messages;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.runtime.Component;

import org.chenillekit.tapestry.core.internal.PagedSource;

/**
 * Blatant copy and paste of {@link org.apache.tapestry5.corelib.components.GridPager} with modifications.
 *
 * @author Tod Orr
 * @version $Id$
 */
@IncludeStylesheet(value = {"Pager.css"})
public class Pager
{
    /**
     * The source of the data displayed by the PagedList (this is used to
     * determine
     * {@link PagedSource#getTotalRowCount() how many rows are available},
     * which in turn determines the page count).
     */
    @Parameter
    private PagedSource<?> _source;

    /**
     * The number of rows displayed per page.
     */
    @Parameter
    private int _rowsPerPage;

    /**
     * The current page number (indexed from 1).
     */
    @Parameter
    private int _currentPage;

    /**
     * If this pager is in a custom position it must provide the id of the
     * PagedLoop it is associated with.
     */
    @Parameter(defaultPrefix = BindingConstants.LITERAL)
    private String _for;

    /**
     * Number of pages before and after the current page in the range. The pager
     * always displays links for 2 * range + 1 pages, unless that's more than
     * the total number of available pages.
     */
    @Parameter("5")
    private int _range;

    private int _lastIndex;

    private int _maxPages;

    @Inject
    private ComponentResources _resources;

    @Inject
    private Messages _messages;

    private Component _pagedLoopComponent;

    private PagedLoop _pagedLoop;

    @SetupRender
    void setup()
    {
        if (_for != null)
        {
            _source = getPagedLoop().getPagedSource();
            _rowsPerPage = getPagedLoop().getRowsPerPage();
            _currentPage = getPagedLoop().getCurrentPage();
        }
    }

    @BeginRender
    void render(MarkupWriter writer)
    {

        int availableRows = _source.getTotalRowCount();

        _maxPages = ((availableRows - 1) / _rowsPerPage) + 1;

        if (_maxPages < 2)
        {
            return;
        }

        writer.element("div", "class", "ck_paged_loop_pager");

        _lastIndex = 0;

        for (int i = 1; i <= 2; i++)
        {
            writePageLink(writer, i);
        }

        int low = _currentPage - _range;
        int high = _currentPage + _range;

        if (low < 1)
        {
            low = 1;
            high = 2 * _range + 1;
        }
        else
        {
            if (high > _maxPages)
            {
                high = _maxPages;
                low = high - 2 * _range;
            }
        }

        for (int i = low; i <= high; i++)
        {
            writePageLink(writer, i);
        }

        for (int i = _maxPages - 1; i <= _maxPages; i++)
        {
            writePageLink(writer, i);
        }

        writer.end();
    }

    void onAction(int newPage)
    {
        // TODO: Validate newPage in range
        _currentPage = newPage;
        if (_for != null)
        {
            getPagedLoopComponent().getComponentResources().triggerEvent(
                    EventConstants.ACTION, new Integer[]{newPage},
                    null);
        }
    }

    private Component getPagedLoopComponent()
    {
        if (_for != null && _pagedLoopComponent == null)
        {
            _pagedLoopComponent = _resources.getPage().getComponentResources().getEmbeddedComponent(_for);
        }
        return _pagedLoopComponent;
    }

    private PagedLoop getPagedLoop()
    {
        if (_for != null && _pagedLoop == null)
        {
            _pagedLoop = (PagedLoop) getPagedLoopComponent();
        }
        return _pagedLoop;
    }

    private void writePageLink(MarkupWriter writer, int pageIndex)
    {

        if (pageIndex < 1 || pageIndex > _maxPages)
            return;

        if (pageIndex <= _lastIndex)
            return;

        if (pageIndex != _lastIndex + 1)
            writer.write(" ... "); // &#8230; is ellipsis

        _lastIndex = pageIndex;

        if (pageIndex == _currentPage)
        {
            writer.element("span", "class", "ck_paged_loop_current");
            writer.write(Integer.toString(pageIndex));
            writer.end();
            return;
        }

        Link link = _resources.createActionLink(EventConstants.ACTION, false, pageIndex);

        writer.element("a", "href", link, "title", _messages.format("goto-page", pageIndex));

        writer.write(Integer.toString(pageIndex));
        writer.end();

    }

}
