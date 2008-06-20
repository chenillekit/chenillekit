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

package org.chenillekit.access.services;

import org.apache.tapestry5.ioc.annotations.SubModule;
import org.apache.tapestry5.ioc.ServiceBinder;

import org.chenillekit.access.ChenilleKitAccessModule;

/**
 * @author <a href="mailto:shomburg@hsofttec.com">S.Homburg</a>
 * @version $Id$
 */
@SubModule(value = {ChenilleKitAccessModule.class})
public class TestAppModule
{
    public static void bind(ServiceBinder binder)
    {
        System.err.println("dsafhasjkf");
    }
}
