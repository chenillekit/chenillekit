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

import org.apache.tapestry5.ioc.AnnotationProvider;
import org.apache.tapestry5.ioc.ObjectLocator;
import org.apache.tapestry5.ioc.ObjectProvider;
import org.apache.tapestry5.ioc.internal.IOCInternalTestCase;

import org.chenillekit.hivemind.services.NullService;
import org.chenillekit.hivemind.services.SimpleService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.Test;

/**
 * Testcase for the HiveMindObjectProvider
 *
 * @version $Id$
 */
public class TestHiveMindObjectProvider extends IOCInternalTestCase
{
    private final static Logger LOG = LoggerFactory.getLogger(TestHiveMindObjectProvider.class);

    @Test
    public void successful_lookup()
    {
        ObjectProvider master = new HiveMindObjectProvider(LOG);
        AnnotationProvider annotationProvider = mockAnnotationProvider();
        ObjectLocator locator = mockObjectLocator();
        SimpleService actual = master.provide(SimpleService.class, annotationProvider, locator);
        assertNotNull(actual);
    }

    @Test
    public void null_lookup()
    {
        ObjectProvider master = new HiveMindObjectProvider(LOG);
        AnnotationProvider annotationProvider = mockAnnotationProvider();
        ObjectLocator locator = mockObjectLocator();
        Object actual = master.provide(NullService.class, annotationProvider, locator);
        assertNull(actual);
    }

}
