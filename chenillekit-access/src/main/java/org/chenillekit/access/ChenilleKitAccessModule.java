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

import org.apache.tapestry5.internal.services.RequestPageCache;
import org.apache.tapestry5.ioc.MappedConfiguration;
import org.apache.tapestry5.ioc.OrderedConfiguration;
import org.apache.tapestry5.ioc.ServiceBinder;
import org.apache.tapestry5.ioc.annotations.Marker;
import org.apache.tapestry5.ioc.internal.util.Defense;
import org.apache.tapestry5.services.ApplicationStateManager;
import org.apache.tapestry5.services.ComponentClassTransformWorker;
import org.apache.tapestry5.services.ComponentEventRequestFilter;
import org.apache.tapestry5.services.ComponentSource;
import org.apache.tapestry5.services.MetaDataLocator;
import org.apache.tapestry5.services.PageRenderRequestFilter;
import org.chenillekit.access.annotations.ChenilleKitAccess;
import org.chenillekit.access.services.AccessValidator;
import org.chenillekit.access.services.AuthService;
import org.chenillekit.access.services.PasswordEncoder;
import org.chenillekit.access.services.impl.AccessValidatorImpl;
import org.chenillekit.access.services.impl.AuthServiceImpl;
import org.chenillekit.access.services.impl.ComponentEventAccessFilter;
import org.chenillekit.access.services.impl.PageRenderAccessFilter;
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
        binder.bind(ComponentEventRequestFilter.class, ComponentEventAccessFilter.class).withMarker(ChenilleKitAccess.class);
        binder.bind(PageRenderRequestFilter.class, PageRenderAccessFilter.class).withMarker(ChenilleKitAccess.class);
    }

    /**
     * @param configuration
     */
    /**
     * instantiate the contributed password encoder.
     *
     * @param contribution
     *
     * @return password encoder
     */
    public static PasswordEncoder buildPasswordEncoder(Map<String, Class> contribution)
    {
        try
        {
            Class encoderClass = contribution.get(ChenilleKitAccessConstants.PASSWORD_ENCODER);
            Defense.notNull(encoderClass, ChenilleKitAccessConstants.PASSWORD_ENCODER);

            return (PasswordEncoder) contribution.get(ChenilleKitAccessConstants.PASSWORD_ENCODER).newInstance();
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
     * @param configuration
     */
    public static void contributeComponentClassTransformWorker(
    			OrderedConfiguration<ComponentClassTransformWorker> configuration)
    {
        configuration.add("Restricted", new RestrictedWorker(), "after:Secure");
    }

    /**
     * build the authentificate service.
     *
     * @param logger          system logger
     * @param passwordEncoder the password encoder
     *
     * @return
     */
    public static AuthService buildAuthService(Logger logger, PasswordEncoder passwordEncoder)
    {
        return new AuthServiceImpl(logger, passwordEncoder);
    }

    /**
     * 
     * @param stateManager
     * @param componentSource
     * @param locator
     * @param logger
     * @param contribution
     * @return
     */
    @Marker(ChenilleKitAccess.class)
    public static AccessValidator buildAccessValidator(ApplicationStateManager stateManager,
                                                       ComponentSource componentSource,
                                                       MetaDataLocator locator,
                                                       Logger logger,
                                                       Map<String, Class> contribution)
    {
        Class webSessionUserClass = contribution.get(ChenilleKitAccessConstants.WEB_USER_IMPLEMENTATION);
        return new AccessValidatorImpl(stateManager, componentSource, locator, logger, webSessionUserClass);
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


}
