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
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import org.apache.tapestry5.internal.services.LinkFactory;
import org.apache.tapestry5.ioc.MappedConfiguration;
import org.apache.tapestry5.ioc.OrderedConfiguration;
import org.apache.tapestry5.ioc.ServiceBinder;
import org.apache.tapestry5.ioc.annotations.Marker;
import org.apache.tapestry5.ioc.services.SymbolSource;
import org.apache.tapestry5.services.ApplicationStateManager;
import org.apache.tapestry5.services.ComponentClassResolver;
import org.apache.tapestry5.services.ComponentClassTransformWorker;
import org.apache.tapestry5.services.ComponentEventRequestFilter;
import org.apache.tapestry5.services.ComponentSource;
import org.apache.tapestry5.services.Dispatcher;
import org.apache.tapestry5.services.MetaDataLocator;
import org.apache.tapestry5.services.PageRenderRequestFilter;
import org.chenillekit.access.annotations.ChenilleKitAccess;
import org.chenillekit.access.services.AccessValidator;
import org.chenillekit.access.services.impl.AccessController;
import org.chenillekit.access.services.impl.AccessValidatorImpl;
import org.chenillekit.access.services.impl.ComponentEventAccessController;
import org.chenillekit.access.services.impl.PageRenderAccessController;
import org.chenillekit.access.services.impl.RestrictedWorker;
import org.slf4j.Logger;

/**
 * @author <a href="mailto:homburgs@gmail.com">S.Homburg</a>
 * @version $Id$
 */
public class ChenilleKitAccessModule
{
	public static void bind(ServiceBinder binder)
	{
		binder.bind(ComponentEventRequestFilter.class, ComponentEventAccessController.class).withMarker(ChenilleKitAccess.class);
		binder.bind(PageRenderRequestFilter.class, PageRenderAccessController.class).withMarker(ChenilleKitAccess.class);
		
	}
	
	
    @Marker(ChenilleKitAccess.class)
    public static Dispatcher buildAccessControllerDispatcher(ApplicationStateManager stateManager,
                                                             ComponentClassResolver resolver,
                                                             ComponentSource componentSource,
                                                             MetaDataLocator locator,
                                                             Logger logger,
                                                             LinkFactory linkFactory,
                                                             SymbolSource symbols,
                                                             Map<String, Class> contribution)
    {
        return new AccessController(stateManager,
                                    resolver,
                                    componentSource,
                                    locator,
                                    logger,
                                    linkFactory,
                                    symbols,
                                    contribution.get(ChenilleKitAccessConstants.WEB_USER_IMPLEMENTATION));
    }
    
    @Marker(ChenilleKitAccess.class)
    public static AccessValidator buildAccessValidator(ApplicationStateManager stateManager,
                                                             ComponentSource componentSource,
                                                             MetaDataLocator locator,
                                                             Logger logger,
                                                             Map<String, Class> contribution)
    {
        return new AccessValidatorImpl(stateManager,
                                    componentSource,
                                    locator,
                                    logger,
                                    contribution.get(ChenilleKitAccessConstants.WEB_USER_IMPLEMENTATION));
    }

    /**
     * @param configuration
     * @param accessController
     */
    public void contributeMasterDispatcher(OrderedConfiguration<Dispatcher> configuration,
                                           @ChenilleKitAccess Dispatcher accessController)
    {
    	// This should be always before ComponentEvent
//        configuration.add("AccessController", accessController, "before:ComponentEvent");
    }
    
    /**
     * Contributes "AccessControl" filter which checks for access rights of requests.
     */
    public void contributePageRenderRequestHandler(OrderedConfiguration<PageRenderRequestFilter> configuration,
                                                   final @ChenilleKitAccess PageRenderRequestFilter accessFilter)
    {
        configuration.add("AccessControl", accessFilter, "before:*");
    }
    
    /**
     * Contribute "AccessControl" filter to determine if the event can be processed and the user
     * has enough rights to do so.
     */
    public void contributeComponentEventRequestHandler(OrderedConfiguration<ComponentEventRequestFilter> configuration,
    		@ChenilleKitAccess ComponentEventRequestFilter accessFilter)
    {
    	configuration.add("AccessControl", accessFilter, "before:*");
    }

    /**
     * @param configuration
     */
    public static void contributeComponentClassTransformWorker(
            OrderedConfiguration<ComponentClassTransformWorker> configuration)
    {
        configuration.add("Restricted", new RestrictedWorker(), "after:Secure");
    }


    /**
     * @param configuration
     */
    public static void contributeApplicationDefaults(MappedConfiguration<String, String> configuration)
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
