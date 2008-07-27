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

import java.util.Map;

import org.apache.tapestry5.ioc.Resource;

import org.chenillekit.mail.services.SmtpService;
import org.chenillekit.mail.services.impl.SimpleSmtpServiceImpl;
import org.slf4j.Logger;

/**
 * @author <a href="mailto:homburgs@gmail.com">shomburg</a>
 * @version $Id$
 */
public class ChenilleKitMailModule
{
    public static SmtpService buildSmtpService(Logger logger, Map<String, Resource> configuration)
    {
        return new SimpleSmtpServiceImpl(logger, configuration.get(SmtpService.CONFIG_KEY));
    }
}
