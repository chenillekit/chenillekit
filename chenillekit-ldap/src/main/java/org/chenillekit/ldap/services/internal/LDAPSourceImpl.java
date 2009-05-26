/*
 * Apache License
 * Version 2.0, January 2004
 * http://www.apache.org/licenses/
 *
 * Copyright 2008-2009 by chenillekit.org
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 */

package org.chenillekit.ldap.services.internal;

import netscape.ldap.LDAPConnection;
import netscape.ldap.LDAPException;

import org.apache.commons.lang.StringUtils;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.ioc.annotations.Symbol;
import org.chenillekit.ldap.ChenilleKitLDAPConstants;
import org.slf4j.Logger;

/**
 * @author massimo
 *
 */
public class LDAPSourceImpl implements LDAPSource
{
	private final Logger logger;
	
	private final LDAPConnection ldapConnection;
	
    private final String ldapHostName;
    private final String ldapAuthDN;
    private final String ldapPwd;
    private final int ldapPort;
    private final int ldapVersion;
    
    public LDAPSourceImpl(Logger logger,

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
    	
    	if (StringUtils.isEmpty(ldapHostName))
            throw new RuntimeException("property '" + ChenilleKitLDAPConstants.LDAP_HOSTNAME + "' cant be empty!");
    	
    	this.ldapHostName = ldapHostName;
    	
    	this.ldapPort = ldapPort;
    	this.ldapAuthDN = ldapAuthDN;
    	this.ldapPwd = ldapPwd;

    	ldapConnection = new LDAPConnection();
    }
    
    /**
     * connect and/or authenticate at LDAP server.
     *
     * @throws LDAPException
     */
    private synchronized void checkAndconnect() throws LDAPException
    {
        if (!ldapConnection.isConnected())
        {
            if (logger.isDebugEnabled())
                logger.debug("connecting server: {}", ldapHostName);

            ldapConnection.connect(ldapVersion, ldapHostName, ldapPort, ldapAuthDN, ldapPwd);
        }

        if (!ldapConnection.isAuthenticated())
        {
            if (logger.isDebugEnabled())
                logger.debug("authenticate at server: {}", ldapHostName);

            ldapConnection.authenticate(ldapVersion, ldapAuthDN, ldapPwd);
        }
    }
    
    /*
     * (non-Javadoc)
     * @see org.chenillekit.ldap.services.internal.LDAPSource#openSession()
     */
    public LDAPConnection openSession() throws LDAPException
    {
    	checkAndconnect();
    	
		return ldapConnection;
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
                if (logger.isDebugEnabled())
                    logger.debug("disconnecting from server {}", ldapHostName);

                ldapConnection.disconnect();
            }
        }
        catch (LDAPException e)
        {
            throw new RuntimeException(e);
        }
	}
    
    
}
