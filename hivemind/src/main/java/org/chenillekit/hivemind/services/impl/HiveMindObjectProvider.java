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

package org.chenillekit.hivemind.services.impl;

import org.apache.hivemind.ApplicationRuntimeException;
import org.apache.hivemind.Registry;
import org.apache.hivemind.impl.RegistryBuilder;
import org.apache.tapestry.ioc.AnnotationProvider;
import org.apache.tapestry.ioc.ObjectLocator;
import org.apache.tapestry.ioc.ObjectProvider;

import org.slf4j.Logger;

/**
 * HiveMind based {@link ObjectProvider} which will serve HiveMind
 * services through Tapestry5 IoC context.
 *
 * @author <a href="mailto:mlusetti@gmail.com">Massimo Lusetti</a>
 * @version $Id$
 */
public class HiveMindObjectProvider implements ObjectProvider
{
    private final Logger _logger;

    private Registry _registry;


    public HiveMindObjectProvider(Logger logger)
    {
        _logger = logger;
    }

    void initialise()
    {
        if (_registry == null)
        {
            _logger.debug("Initializing HiveMind registry with default builder");

            _registry = RegistryBuilder.constructDefaultRegistry();
        }
    }


    /**
     * At fist call initialize the HiveMimind {@link org.apache.hivemind.Registry} the try to get
     * the service by type (tClass)
     *
     * @param tClass             the service interface type to retreive from HiveMind
     * @param annotationProvider the {@link org.apache.tapestry.ioc.AnnotationProvider} (unused)
     * @param objectLocator      the {@link org.apache.tapestry.ioc.ObjectLocator}
     *
     * @return the proxy instance for the requested service or {@code null} in case.
     */
    public <T> T provide(Class<T> tClass, AnnotationProvider annotationProvider, ObjectLocator objectLocator)
    {
        initialise();

        if (_registry == null)
            throw new RuntimeException("HiveMind Registry not configured");

        try
        {
            if (_registry.containsService(tClass))
            {
                Object service = _registry.getService(tClass);

                // NOTE: ClassCastException should be trapped by HiveMind registry method
                return tClass.cast(service);
            }
            else
            {
                // NOTE: This could be taken out if still too verbose
                _logger.debug("No service found for: {}", tClass.toString());

                return null;
            }
        }
        catch (ApplicationRuntimeException are)
        {
            if (_logger.isWarnEnabled())
            {
                _logger.warn("Impossibile to locate the service for type {} from HiveMind registry", tClass.getCanonicalName(), are);
            }
            return null;
        }
    }
}
