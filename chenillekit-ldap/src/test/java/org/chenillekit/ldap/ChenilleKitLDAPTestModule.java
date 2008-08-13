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

package org.chenillekit.ldap;

import org.apache.tapestry5.ioc.MappedConfiguration;
import org.apache.tapestry5.ioc.Resource;
import org.apache.tapestry5.ioc.annotations.SubModule;
import org.apache.tapestry5.ioc.internal.util.ClasspathResource;
import org.apache.tapestry5.ioc.services.ClassFactory;

import org.chenillekit.core.ChenilleKitCoreModule;
import org.chenillekit.ldap.services.SearcherService;

/**
 * @author <a href="mailto:homburgs@googlemail.com">shomburg</a>
 * @version $Id: ChenilleKitMailTestModule.java 132 2008-07-27 22:18:54Z homburgs@gmail.com $
 */
@SubModule(value = {ChenilleKitCoreModule.class, ChenilleKitLDAPModule.class})
public class ChenilleKitLDAPTestModule
{
    public static void contributeSimpleLdapSearcherService(ClassFactory classFactory,
                                                           MappedConfiguration<String, Resource> configuration)
    {
        Resource cpResource = new ClasspathResource(classFactory.getClassLoader(), "ldap.properties");
        configuration.add(SearcherService.CONFIG_KEY, cpResource);
    }
}
