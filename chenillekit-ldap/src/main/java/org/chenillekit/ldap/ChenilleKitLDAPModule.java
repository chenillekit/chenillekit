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
import org.apache.tapestry5.ioc.ServiceResources;
import org.apache.tapestry5.ioc.services.RegistryShutdownHub;

import org.chenillekit.ldap.services.internal.SearcherService;
import org.chenillekit.ldap.services.internal.SimpleSearcherServiceImpl;

/**
 * @version $Id: ChenilleKitMailModule.java 132 2008-07-27 22:18:54Z homburgs@gmail.com $
 */
public class ChenilleKitLDAPModule
{
    public static SearcherService buildSimpleLdapSearcherService(ServiceResources resources,
                                                                 RegistryShutdownHub shutdownHub)
    {
        SimpleSearcherServiceImpl service = resources.autobuild(SimpleSearcherServiceImpl.class);
        shutdownHub.addRegistryShutdownListener(service);
        return service;
    }

    /**
     * Contributes factory defaults that may be overridden.
     */
    public static void contributeFactoryDefaults(MappedConfiguration<String, String> contribution)
    {
        contribution.add(ChenilleKitLDAPConstants.LDAP_VERSION, "3");
        contribution.add(ChenilleKitLDAPConstants.LDAP_HOSTNAME, "");
        contribution.add(ChenilleKitLDAPConstants.LDAP_HOSTPORT, "389");
        contribution.add(ChenilleKitLDAPConstants.LDAP_AUTHDN, "");
        contribution.add(ChenilleKitLDAPConstants.LDAP_AUTHPWD, "");
    }
}
