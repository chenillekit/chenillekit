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
import org.apache.tapestry5.ioc.Resource;
import org.apache.tapestry5.ioc.annotations.SubModule;
import org.apache.tapestry5.ioc.internal.util.ClasspathResource;
import org.apache.tapestry5.ioc.services.ClassFactory;

import org.chenillekit.mail.services.SmtpService;

/**
 * @author <a href="mailto:homburgs@googlemail.com">shomburg</a>
 * @version $Id$
 */
@SubModule(value = {ChenilleKitMailModule.class})
public class ChenilleKitMailTestModule
{
    public static void contributeSmtpService(ClassFactory classFactory, MappedConfiguration<String, Resource> configuration)
    {
        Resource cpResource = new ClasspathResource(classFactory.getClassLoader(), "smtp.properties");
        configuration.add(SmtpService.CONFIG_KEY, cpResource);
    }
}
