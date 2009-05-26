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
package org.chenillekit.ldap.services.internal;

import java.util.List;

import netscape.ldap.LDAPEntry;

/**
 * @version $Id: LdapSearchService.java 167 2008-07-23 22:05:16Z shomburg $
 */
public interface ReadService
{
    /**
     * get the result object of an attribute based on baseDN and filter.
     *
     * @param baseDN     like <em>uid=sven.homburg,cn=depot120.dpd.de</em>
     * @param filter     like <em>(objectclass=*)</em>
     * @param attributes string array which attributes should be returned like <em>uid,userpassword</em>
     *
     * @return
     */
    public List<LDAPEntry> search(String baseDN, String filter, String... attributes);
    
    /**
     * Get the specified DN as a LDAPEntry
     * 
     * @param dn the LDAP DN look for 
     * @return the {@link LDAPEntry} associated with the DN
     */
    public LDAPEntry lookup(String dn);
}
