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

package org.chenillekit.access;

import java.io.IOException;
import java.util.Properties;
import java.util.Set;

import org.apache.tapestry5.ioc.MappedConfiguration;
import org.apache.tapestry5.ioc.OrderedConfiguration;
import org.apache.tapestry5.ioc.ServiceBinder;
import org.apache.tapestry5.services.Dispatcher;

import org.chenillekit.access.annotations.ChenilleKitAccess;
import org.chenillekit.access.services.impl.AccessController;

/**
 * @author <a href="mailto:homburgs@gmail.com">S.Homburg</a>
 * @version $Id$
 */
public class ChenilleKitAccessModule
{
    /**
     * @param binder
     */
    public static void bind(ServiceBinder binder)
    {
        binder.bind(Dispatcher.class, AccessController.class).withMarker(ChenilleKitAccess.class);
    }

    /**
     * @param configuration
     * @param accessController
     */
    public void contributeMasterDispatcher(OrderedConfiguration<Dispatcher> configuration,
                                           @ChenilleKitAccess Dispatcher accessController)
    {
        configuration.add("AccessController", accessController, "before:PageRender");
    }

//    /**
//     * @param configuration
//     */
//    public static void contributeComponentClassTransformWorker(
//            OrderedConfiguration<ComponentClassTransformWorker> configuration)
//    {
//        configuration.add("Restricted", new RestrictedWorker(), "after:Secure");
//    }


    /**
     * @param configuration
     */
    public static void contributeApplicationDefaults(
            MappedConfiguration<String, String> configuration)
    {
        Properties prop = new Properties();
        try
        {
            prop.load(ChenilleKitAccessModule.class.getResourceAsStream("/chenillekit-access.properties"));
        }
        catch (IOException e)
        {
            throw new RuntimeException(e);
        }

        Set<Object> keys = prop.keySet();
        for (Object key : keys)
        {
            Object value = prop.get(key);
            configuration.add(key.toString(), value.toString());
        }
    }


}
