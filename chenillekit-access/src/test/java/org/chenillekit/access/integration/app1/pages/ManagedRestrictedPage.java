/*
 * Apache License
 * Version 2.0, January 2004
 * http://www.apache.org/licenses/
 *
 * Copyright 2010 by chenillekit.org
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 */

package org.chenillekit.access.integration.app1.pages;

import org.apache.tapestry5.annotations.Component;
import org.apache.tapestry5.corelib.components.EventLink;

/**
 * @version $Id$
 */
@org.chenillekit.access.annotations.ManagedRestricted
public class ManagedRestrictedPage
{
    @Component(parameters = {"event=event1"})
    private EventLink firstEventLink;

    @Component(parameters = {"event=event2"})
    private EventLink secondEventLink;
}