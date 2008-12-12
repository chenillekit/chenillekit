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

import org.apache.tapestry5.ioc.Configuration;
import org.apache.tapestry5.ioc.MappedConfiguration;
import org.apache.tapestry5.ioc.OrderedConfiguration;
import org.apache.tapestry5.ioc.ServiceBinder;
import org.apache.tapestry5.ioc.annotations.InjectService;
import org.apache.tapestry5.ioc.annotations.Marker;
import org.apache.tapestry5.ioc.internal.util.Defense;
import org.apache.tapestry5.services.ClassTransformation;
import org.apache.tapestry5.services.ComponentClassResolver;
import org.apache.tapestry5.services.ComponentClassTransformWorker;
import org.apache.tapestry5.services.ComponentEventRequestFilter;
import org.apache.tapestry5.services.ComponentSource;
import org.apache.tapestry5.services.LibraryMapping;
import org.apache.tapestry5.services.MetaDataLocator;
import org.apache.tapestry5.services.PageRenderRequestFilter;
import org.apache.tapestry5.services.Request;
import org.apache.tapestry5.services.RequestFilter;
import org.apache.tapestry5.services.RequestHandler;
import org.apache.tapestry5.services.Response;
import org.apache.tapestry5.services.Session;
import org.chenillekit.access.annotations.ChenilleKitAccess;
import org.chenillekit.access.services.AccessValidator;
import org.chenillekit.access.services.AppServerLoginService;
import org.chenillekit.access.services.AuthRedirectService;
import org.chenillekit.access.services.AuthService;
import org.chenillekit.access.services.WebSessionUserService;
import org.chenillekit.access.services.impl.AccessValidatorImpl;
import org.chenillekit.access.services.impl.AuthRedirectServiceImpl;
import org.chenillekit.access.services.impl.ComponentEventAccessFilter;
import org.chenillekit.access.services.impl.PageRenderAccessFilter;
import org.chenillekit.access.services.impl.RestrictedWorker;
import org.chenillekit.access.services.impl.WebSessionUserServiceImpl;
import org.slf4j.Logger;

/**
 *
 * @version $Id$
 */
public class ChenilleKitAccessModule
{
	public static void bind(ServiceBinder binder)
	{
		binder.bind(ComponentEventRequestFilter.class, ComponentEventAccessFilter.class).withMarker(ChenilleKitAccess.class);
		binder.bind(PageRenderRequestFilter.class, PageRenderAccessFilter.class).withMarker(ChenilleKitAccess.class);
		binder.bind(WebSessionUserService.class, WebSessionUserServiceImpl.class).withMarker(ChenilleKitAccess.class);
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
	 * build the authentificate service.
	 *
	 * @param logger system logger
	 * @param contribution contribution map
	 * @param userService user storage service
	 * @param appServerLoginService assure login to the appserver can be handled correctly
	 * @return auth service for login handling
	 */
	public static AuthRedirectService buildAuthRedirectService(final Logger logger,
															final Map<String, Class> contribution,
															final WebSessionUserService userService,
															final AppServerLoginService appServerLoginService)
	{
		try
		{
			Class authServiceClass = contribution.get(ChenilleKitAccessConstants.WEB_USER_AUTH_SERVICE);
			Defense.notNull(authServiceClass, ChenilleKitAccessConstants.WEB_USER_AUTH_SERVICE);
			return new AuthRedirectServiceImpl(logger, (AuthService)authServiceClass.newInstance(), userService, appServerLoginService);
		}
		catch (InstantiationException e)
		{
			throw new RuntimeException(e);
		}
		catch (IllegalAccessException e)
		{
			throw new RuntimeException(e);
		}
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
													Logger logger,
													WebSessionUserService userService)
	{
		return new AccessValidatorImpl(componentSource, locator, logger, userService);
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

	/**
	 * User management filter, the login method on the user should be called when the user logs in. If the user does not
	 * yet exist (which is possible when logging in using some SSO solution, then the user should be created).
	 *
	 * @param webSessionUserService login info service
	 * @param appServerLoginService assure login to the appserver can be handled correctly
	 * @return request filter
	 */
	public RequestFilter buildWebSessionUserFilter( final WebSessionUserService webSessionUserService,
													final AppServerLoginService appServerLoginService )
	{
		return new RequestFilter()
		{
			public boolean service( Request request, Response response, RequestHandler handler )
				throws IOException
			{
				Session session = request.getSession( false );
				WebSessionUser wsu = null;
				if ( null != session ) wsu = (WebSessionUser) session.getAttribute( ChenilleKitAccessConstants.WEB_SESSION_USER_KEY );
				appServerLoginService.appServerLogin( wsu );
				webSessionUserService.setUser( wsu );

				// The reponsibility of a filter is to invoke the corresponding method
				// in the handler. When you chain multiple filters together, each filter
				// received a handler that is a bridge to the next filter.
				boolean res = handler.service( request, response );

				wsu = webSessionUserService.getUser();
				session = request.getSession( wsu != null );
				if ( null != session ) session.setAttribute( ChenilleKitAccessConstants.WEB_SESSION_USER_KEY, wsu );
				return res;
			}
		};
	}

	/**
	 * This is a contribution to the RequestHandler service configuration. This is how we extend Tapestry using the
	 * timing filter. A common use for this kind of filter is transaction management or security.
	 *
	 * @param configuration configuration to add to
	 * @param webSessionUserFilter filter info
	 */
	public void contributeRequestHandler( OrderedConfiguration<RequestFilter> configuration,
										@InjectService( "WebSessionUserFilter" ) RequestFilter webSessionUserFilter )
	{
		// Each contribution to an ordered configuration has a name, When necessary, you may
		// set constraints to precisely control the invocation order of the contributed filter
		// within the pipeline.
		configuration.add( "WebSessionUser", webSessionUserFilter );
	}
}
