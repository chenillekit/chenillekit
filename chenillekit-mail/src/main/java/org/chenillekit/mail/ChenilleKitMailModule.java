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

package org.chenillekit.mail;

import org.apache.tapestry5.ioc.ServiceBinder;
import org.chenillekit.mail.services.MailService;
import org.chenillekit.mail.services.impl.MailServiceImpl;

/**
 * @version $Id$
 */
public class ChenilleKitMailModule
{
	public static void bind(ServiceBinder binder)
	{
		binder.bind(MailService.class, MailServiceImpl.class).withId("SimpleSmtpService");
	}
}
