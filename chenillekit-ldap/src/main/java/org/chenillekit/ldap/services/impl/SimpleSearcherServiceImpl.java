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

package org.chenillekit.ldap.services.impl;

import java.util.List;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.ioc.annotations.Symbol;
import org.apache.tapestry5.ioc.internal.util.CollectionFactory;
import org.apache.tapestry5.ioc.services.RegistryShutdownListener;

import netscape.ldap.LDAPConnection;
import netscape.ldap.LDAPEntry;
import netscape.ldap.LDAPException;
import netscape.ldap.LDAPSearchResults;
import netscape.ldap.LDAPv2;
import netscape.ldap.LDAPv3;
import org.chenillekit.ldap.ChenilleKitLDAPConstants;
import org.chenillekit.ldap.services.SearcherService;
import org.slf4j.Logger;

/**
 * @version $Id: LdapSearchServiceImpl.java 167 2008-07-23 22:05:16Z shomburg $
 */
public class SimpleSearcherServiceImpl implements SearcherService, RegistryShutdownListener
{
    private Logger logger;
    private LDAPConnection ldapConnection;
    private final String ldapHostName;
    private final String ldapAuthDN;
    private final String ldapPwd;
    private final int ldapPort;
    private final int ldapVersion;

    public SimpleSearcherServiceImpl(Logger logger,

                                     @Inject
                                     @Symbol(ChenilleKitLDAPConstants.LDAP_VERSION)
                                     int ldapVersion,

                                     @Inject
                                     @Symbol(ChenilleKitLDAPConstants.LDAP_HOSTNAME)
                                     String ldapHostName,

                                     @Inject
                                     @Symbol(ChenilleKitLDAPConstants.LDAP_HOSTPORT)
                                     int ldapPort,

                                     @Inject
                                     @Symbol(ChenilleKitLDAPConstants.LDAP_AUTHDN)
                                     String ldapAuthDN,

                                     @Inject
                                     @Symbol(ChenilleKitLDAPConstants.LDAP_AUTHPWD)
                                     String ldapPwd)
    {
        this.logger = logger;
        this.ldapVersion = ldapVersion;
        this.ldapHostName = ldapHostName;
        this.ldapPort = ldapPort;
        this.ldapAuthDN = ldapAuthDN;
        this.ldapPwd = ldapPwd;

        initService();
    }

    /**
     * read and check all service parameters.
     */
    private void initService()
    {
        if (StringUtils.isEmpty(ldapHostName))
            throw new RuntimeException("property '" + ChenilleKitLDAPConstants.LDAP_HOSTNAME + "' cant be empty!");

        ldapConnection = new LDAPConnection();
    }

    /**
     * get the result object of an attribute based on baseDN and filter.
     *
     * @param baseDN     like <em>uid=sven.homburg,cn=depot120.dpd.de</em>
     * @param filter     like <em>(objectclass=*)</em>
     * @param attributes string array wich attributes shoud returnd like <em>uid,userpassword</em>
     *
     * @return
     */
    public List<LDAPEntry> search(String baseDN, String filter, String... attributes)
    {
        List<LDAPEntry> returnList = CollectionFactory.newList();

        try
        {
            connect();

            if (logger.isInfoEnabled())
                logger.info("BaseDN: " + baseDN + " / Filter: " + filter + " / Attributes: " + ArrayUtils.toString(attributes, "null"));

            int scope = LDAPv3.SCOPE_SUB;
            if (ldapVersion == 2)
                scope = LDAPv2.SCOPE_SUB;

            LDAPSearchResults results = ldapConnection.search(baseDN, scope, filter, attributes, false);
            // We cycle through the NamingEnumeration
            // that is returned by the search.
            while (results.hasMoreElements())
                returnList.add(results.next());

            return returnList;
        }
        catch (LDAPException e)
        {
            throw new RuntimeException(e);
        }
    }

    /**
     * connect and/or authenticate at LDAP server.
     *
     * @throws LDAPException
     */
    private synchronized void connect() throws LDAPException
    {
        if (!ldapConnection.isConnected())
        {
            if (logger.isInfoEnabled())
                logger.info("connecting server: {}", ldapHostName);

            ldapConnection.connect(ldapVersion, ldapHostName, ldapPort, ldapAuthDN, ldapPwd);
        }

        if (!ldapConnection.isAuthenticated())
        {
            if (logger.isInfoEnabled())
                logger.info("authenticate at server: {}", ldapHostName);

            ldapConnection.authenticate(ldapVersion, ldapAuthDN, ldapPwd);
        }
    }

    /**
     * Invoked when the registry shuts down, giving services a chance to perform any final operations. Service
     * implementations should not attempt to invoke methods on other services (via proxies) as the service proxies may
     * themselves be shutdown.
     */
    public void registryDidShutdown()
    {
        try
        {
            if (ldapConnection != null && ldapConnection.isConnected())
            {
                if (logger.isInfoEnabled())
                    logger.info("disconnecting from server {}", ldapHostName);

                ldapConnection.disconnect();
            }
        }
        catch (LDAPException e)
        {
            throw new RuntimeException(e);
        }
    }
}
