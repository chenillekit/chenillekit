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
import java.util.List;
import java.util.Properties;
import java.util.Set;

import org.apache.tapestry5.ioc.Configuration;
import org.apache.tapestry5.ioc.MappedConfiguration;
import org.apache.tapestry5.ioc.OrderedConfiguration;
import org.apache.tapestry5.ioc.ServiceBinder;
import org.apache.tapestry5.ioc.annotations.InjectService;
import org.apache.tapestry5.ioc.annotations.Marker;
import org.apache.tapestry5.ioc.services.ChainBuilder;
import org.apache.tapestry5.services.ApplicationStateContribution;
import org.apache.tapestry5.services.ApplicationStateCreator;
import org.apache.tapestry5.services.ApplicationStateManager;
import org.apache.tapestry5.services.ComponentClassResolver;
import org.apache.tapestry5.services.ComponentClassTransformWorker;
import org.apache.tapestry5.services.ComponentRequestFilter;
import org.apache.tapestry5.services.ComponentSource;
import org.apache.tapestry5.services.Cookies;
import org.apache.tapestry5.services.LibraryMapping;
import org.apache.tapestry5.services.MetaDataLocator;
import org.chenillekit.access.annotations.ChenilleKitAccess;
import org.chenillekit.access.internal.NoOpAuthenticationService;
import org.chenillekit.access.services.AccessValidator;
import org.chenillekit.access.services.AuthenticationService;
import org.chenillekit.access.services.RedirectService;
import org.chenillekit.access.services.impl.AccessValidatorImpl;
import org.chenillekit.access.services.impl.ComponentRequestAccessFilter;
import org.chenillekit.access.services.impl.CookieRedirectAccessFilter;
import org.chenillekit.access.services.impl.RedirectServiceImpl;
import org.chenillekit.access.services.impl.RestrictedWorker;
import org.slf4j.Logger;

/**
 * Main Module class for ChenilleKitAccess, i mean T5 Module class.
 * 
 * @version $Id$
 */
public class ChenilleKitAccessModule
{
	/**
	 * Binding via fluent API by T5 {@link ServiceBinder}
	 * @param binder
	 */
	public static void bind(ServiceBinder binder)
	{	
		binder.bind(ComponentRequestFilter.class, ComponentRequestAccessFilter.class).withMarker(ChenilleKitAccess.class);
		binder.bind(RedirectService.class, RedirectServiceImpl.class);
	}
	
	/**
	 * Actually builds a chain of commands from various implementation of
	 * {@link AuthenticationService} to serve as authentication mechanism
	 * for the web application and letting every user provide their own.
	 * 
	 * @param configuration
	 * @param chainBuilder
	 * @return
	 */
    public static AuthenticationService buildAuthenticationService(
            final List<AuthenticationService> configuration,
            @InjectService("ChainBuilder")
            ChainBuilder chainBuilder)
    {
        return chainBuilder.build(AuthenticationService.class, configuration);
    }
    
    /**
     * 
     * @param configuration
     */
    public static void contributeApplicationStateManager(
    		MappedConfiguration<Class, ApplicationStateContribution> configuration)
    {
      ApplicationStateCreator<WebSessionUser> creator = new ApplicationStateCreator<WebSessionUser>()
      {
		public WebSessionUser create()
		{
			// It sounds better to throw an IllegaAccess
			// but Error and Exceptions are for other use case
			// as declared in the respective javadocs
			throw new IllegalStateException("WebSessionUser must be provided, not instantiated");
		}  
      };
      
      // FIXME Is "session" string available as a constants from Tapestry?
      configuration.add(WebSessionUser.class, new ApplicationStateContribution("session", creator));
    }
    
    /**
     * 
     * @param configuration
     */
    public static void contributeAuthenticationService(OrderedConfiguration<AuthenticationService> configuration)
    {
    	configuration.add("no-op", new NoOpAuthenticationService(), "after:*");
    }

	/**
	 * Contribute our {@link ComponentClassTransformWorker} to transformation pipeline to add our code to
	 * loaded classes
	 * @param configuration component class transformer configuration
	 */
	public static void contributeComponentClassTransformWorker(
				OrderedConfiguration<ComponentClassTransformWorker> configuration)
	{
		configuration.add("Restricted", new RestrictedWorker(), "after:Secure");
	}

	/**
	 * Contribute our virtual folder to {@link ComponentClassResolver} service
	 *
	 * @param configuration configuration for the service where we contribute to
	 */
	public static void contributeComponentClassResolver(Configuration<LibraryMapping> configuration)
	{
		configuration.add(new LibraryMapping("ckaccess", "org.chenillekit.access"));
	}
	
	/**
	 *
	 * @param componentSource component source
	 * @param locator meta data locator
	 * @param logger system logger
	 * @return build access validator
	 */
	@Marker(ChenilleKitAccess.class)
	public static AccessValidator buildAccessValidator(ComponentSource componentSource,
													MetaDataLocator locator,
													Logger logger, ApplicationStateManager manager)
	{	
		return new AccessValidatorImpl(componentSource, locator, logger, manager);
	}
	
	/**
	 * 
	 * @param configuration
	 * @param accessFilter
	 */
	public static void contributeComponentRequestHandler(OrderedConfiguration<ComponentRequestFilter> configuration,
							@ChenilleKitAccess ComponentRequestFilter accessFilter,
							Cookies cookies, RedirectService redirect)
	{
		configuration.add("AccessControl", accessFilter, "before:*");
		
		CookieRedirectAccessFilter cookieFilter = new CookieRedirectAccessFilter(cookies, redirect);
		
		configuration.add("CookieRedirect", cookieFilter, "after:AccessControl");
	}

	/**
	 * @param configuration
	 */
	public static void contributeFactoryDefaults(MappedConfiguration<String, String> configuration)
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
