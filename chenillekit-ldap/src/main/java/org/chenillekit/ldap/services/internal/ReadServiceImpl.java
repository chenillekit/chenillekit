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

import java.util.List;

import netscape.ldap.LDAPEntry;
import netscape.ldap.LDAPException;
import netscape.ldap.LDAPSearchResults;
import netscape.ldap.LDAPv2;
import netscape.ldap.LDAPv3;

import org.apache.commons.lang.ArrayUtils;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.ioc.annotations.Symbol;
import org.apache.tapestry5.ioc.internal.util.CollectionFactory;
import org.chenillekit.ldap.ChenilleKitLDAPConstants;
import org.slf4j.Logger;

/**
 * @version $Id: LdapSearchServiceImpl.java 167 2008-07-23 22:05:16Z shomburg $
 */
public class ReadServiceImpl implements ReadService
{
    private final Logger logger;
    
    private final int ldapVersion;
    
    private final LDAPSource ldapSource;

    public ReadServiceImpl(Logger logger,
    						LDAPSource ldapSource,
    						@Inject
    						@Symbol(ChenilleKitLDAPConstants.LDAP_VERSION)
    						int ldapVersion)
    {
        this.logger = logger;
        this.ldapSource = ldapSource;
        this.ldapVersion = ldapVersion;
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
            if (logger.isDebugEnabled())
                logger.debug("BaseDN: " + baseDN + " / Filter: " + filter + " / Attributes: " + ArrayUtils.toString(attributes, "null"));

            int scope = LDAPv3.SCOPE_SUB;
            if (ldapVersion == 2)
                scope = LDAPv2.SCOPE_SUB;

            LDAPSearchResults results = ldapSource.openSession().search(baseDN, scope, filter, attributes, false);
            // We cycle through the NamingEnumeration
            // that is returned by the search.
            while (results.hasMoreElements())
                returnList.add(results.next());

            return returnList;
        }
        catch (LDAPException e)
        {
        	// TODO More sane error reporting...
            throw new RuntimeException(e);
        }
    }
    
    /*
     * (non-Javadoc)
     * @see org.chenillekit.ldap.services.internal.SearcherService#lookup(java.lang.String)
     */
    public LDAPEntry lookup(String dn)
    {
    	try
    	{
    		if (logger.isDebugEnabled())
    			logger.debug("Lookin up " + dn);
    		
    		LDAPEntry entry = ldapSource.openSession().read(dn);
    		
    		return entry;
    	}
    	catch (LDAPException exc)
    	{
    		throw new RuntimeException(exc);
    	}
	}

	
}
