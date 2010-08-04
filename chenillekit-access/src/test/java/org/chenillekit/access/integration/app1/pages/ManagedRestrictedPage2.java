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

package org.chenillekit.access.integration.app1.pages;

import org.apache.tapestry5.annotations.Component;
import org.apache.tapestry5.annotations.OnEvent;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.corelib.components.EventLink;
import org.chenillekit.access.ChenilleKitAccessConstants;

/**
 * @version $Id$
 */
@org.chenillekit.access.annotations.ManagedRestricted
public class ManagedRestrictedPage2
{
	@Component(parameters = {"event=event1"}) @SuppressWarnings("unused")
	private EventLink firstEventLink;

	@Component(parameters = {"event=event2"}) @SuppressWarnings("unused")
	private EventLink secondEventLink;

	@Persist("flash") @Property @SuppressWarnings("unused")
	private String secureString;

	@Persist("flash") @Property @SuppressWarnings("unused")
	private String errorMessage;

	@OnEvent(value = "event1")
	@org.chenillekit.access.annotations.ManagedRestricted
	void onEvent1()
	{
		secureString = "event1 triggered";
	}

	@OnEvent(value = "event2")
	@org.chenillekit.access.annotations.ManagedRestricted
	void onEvent2()
	{
		secureString = "event2 triggered";
	}

	@OnEvent(value = ChenilleKitAccessConstants.EVENT_NOT_ENOUGH_ACCESS_RIGHTS)
	void userHasNotEnoughAccessRights()
	{
		errorMessage = "you have no access";
	}
}