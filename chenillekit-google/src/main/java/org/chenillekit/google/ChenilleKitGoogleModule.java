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

import java.util.Map;

import org.apache.tapestry5.ioc.Resource;
import org.apache.tapestry5.ioc.internal.util.Defense;

import org.chenillekit.google.services.GoogleMapService;
import org.chenillekit.google.services.impl.GoogleMapServiceImpl;
import org.chenillekit.google.utils.ProxyConfig;
import org.slf4j.Logger;

/**
 * @author <a href="mailto:homburgs@gmail.com">S.Homburg</a>
 * @version $Id$
 */
public class ChenilleKitGoogleModule
{
    /**
     * initialize the google map service.
     *
     * @param syslog        the logging service
     * @param configuration google map configuration
     *
     * @return
     */
    public static GoogleMapService buildGoogleMapService(Logger syslog, Map<String, Object> configuration)
    {
        Resource keyResource = (Resource) configuration.get("google.map.key");

        Defense.notNull(keyResource, "google.map.key");

        Integer timeout = (Integer) configuration.get("google.service.timeout");
        ProxyConfig proxyConfig = (ProxyConfig) configuration.get("google.service.proxy");
        return new GoogleMapServiceImpl(syslog, keyResource, proxyConfig, timeout);
    }

}
