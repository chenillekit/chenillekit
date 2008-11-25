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

package org.chenillekit.image;

import org.apache.tapestry5.ioc.ServiceBinder;

import org.chenillekit.image.services.impl.ImageServiceImpl;

/**
 * @version $Id$
 */
public class ChenilleKitImageModule
{
    public static void bind(ServiceBinder binder)
    {
        binder.bind(ImageServiceImpl.class);
    }

}
