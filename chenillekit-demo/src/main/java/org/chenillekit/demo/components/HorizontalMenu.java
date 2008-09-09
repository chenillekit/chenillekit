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

package org.chenillekit.demo.components;

import org.apache.tapestry5.annotations.Component;
import org.apache.tapestry5.annotations.Mixins;
import org.apache.tapestry5.corelib.components.PageLink;

/**
 * @author <a href="mailto:shomburg@depot120.dpd.de">S.Homburg</a>
 * @version $Id$
 */
public class HorizontalMenu extends LeftSideMenu
{
    @Component(parameters = {"page=prop:menuItem.pageName", "style=menuItem.styles",
            "context=menuItem.contextParameters", "color=orange"})
    @Mixins({"OvalButton"})
    private PageLink pageLink;
}
