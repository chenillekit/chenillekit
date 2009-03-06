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

import org.apache.tapestry5.ioc.MappedConfiguration;
import org.apache.tapestry5.ioc.ServiceBinder;

import org.chenillekit.mail.services.impl.SimpleSmtpServiceImpl;

/**
 * @version $Id$
 */
public class ChenilleKitMailModule
{
    public static void bind(ServiceBinder binder)
    {
        binder.bind(SimpleSmtpServiceImpl.class).withId("SimpleSmtpService");
    }

    /**
     * Contributes factory defaults that may be overridden.
     */
    public static void contributeFactoryDefaults(MappedConfiguration<String, String> contribution)
    {
        contribution.add(ChenilleKitMailConstants.SMTP_HOST, "localhost");
        contribution.add(ChenilleKitMailConstants.SMTP_PORT, "25");
        contribution.add(ChenilleKitMailConstants.SMTP_USER, "");
        contribution.add(ChenilleKitMailConstants.SMTP_PASSWORD, "");
        contribution.add(ChenilleKitMailConstants.SMTP_DEBUG, "false");
        contribution.add(ChenilleKitMailConstants.SMTP_SSL, "false");
        contribution.add(ChenilleKitMailConstants.SMTP_TLS, "false");
        contribution.add(ChenilleKitMailConstants.SMTP_SSLPORT, "465");
    }
}
