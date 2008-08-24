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

package org.chenillekit.google;

import org.apache.tapestry5.ioc.annotations.SubModule;
import org.apache.tapestry5.ioc.services.ClassFactory;
import org.apache.tapestry5.ioc.Resource;
import org.apache.tapestry5.ioc.MappedConfiguration;
import org.apache.tapestry5.ioc.internal.util.ClasspathResource;

import org.chenillekit.google.services.GoogleGeoCoder;

/**
 * @author <a href="mailto:homburgs@gmail.com">S.Homburg</a>
 * @version $Id$
 */
@SubModule(value = {ChenilleKitGoogleModule.class})
public class ChenilleKitGoogleTestModule
{
    public static void contributeGoogleGeoCoder(ClassFactory classFactory, MappedConfiguration<String, Resource> configuration)
    {
        Resource resource = new ClasspathResource(classFactory.getClassLoader(), "google.geocoder.properties");
        configuration.add(GoogleGeoCoder.CONFIG_KEY_PROPERTIES, resource);
    }
}
