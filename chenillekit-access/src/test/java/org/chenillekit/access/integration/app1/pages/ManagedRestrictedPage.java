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

import org.apache.tapestry5.EventConstants;
import org.apache.tapestry5.annotations.Component;
import org.apache.tapestry5.annotations.OnEvent;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.corelib.components.ActionLink;
import org.apache.tapestry5.corelib.components.EventLink;

import org.chenillekit.access.ChenilleKitAccessConstants;

/**
 * @version $Id$
 */
@org.chenillekit.access.annotations.ManagedRestricted
public class ManagedRestrictedPage
{
	@Component(parameters = {"event=event1"}) @SuppressWarnings("unused")
	private EventLink firstEventLink;

	@Component(parameters = {"event=event2"}) @SuppressWarnings("unused")
	private EventLink secondEventLink;

	@Component(parameters = {"event=event3", "context=literal:Hey Dude From Event 3"}) @SuppressWarnings("unused")
	private EventLink thirdEventLink;

	@Component @SuppressWarnings("unused")
	private ActionLink firstActionLink;

	@Component @SuppressWarnings("unused")
	private ActionLink secondActionLink;

	@Component(parameters = {"context=literal:Hey Dude From Action 3"}) @SuppressWarnings("unused")
	private ActionLink thirdActionLink;

	@Persist @Property @SuppressWarnings("unused")
	private String contextString;

	@Persist @Property @SuppressWarnings("unused")
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

	@OnEvent(value = "event3")
	@org.chenillekit.access.annotations.ManagedRestricted
	void onEvent3(String context)
	{
		secureString = "event3 triggered";
		this.contextString = context;
	}

	@OnEvent(component = "firstActionLink", value = EventConstants.ACTION)
	@org.chenillekit.access.annotations.ManagedRestricted
	void onAction1()
	{
		secureString = "action1 triggered";
	}

	@OnEvent(component = "secondActionLink", value = EventConstants.ACTION)
	@org.chenillekit.access.annotations.ManagedRestricted
	void onAction2()
	{
		secureString = "action2 triggered";
	}

	@OnEvent(component = "thirdActionLink", value = EventConstants.ACTION)
	@org.chenillekit.access.annotations.ManagedRestricted
	void onAction3(String context)
	{
		secureString = "action3 triggered";
		this.contextString = context;
	}

	@OnEvent(value = ChenilleKitAccessConstants.EVENT_NOT_ENOUGH_ACCESS_RIGHTS)
	void userHasNotEnoughAccessRights()
	{
		errorMessage = "you have no access";
	}
}