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
package org.chenillekit.ldap.services;

import java.util.List;
import javax.naming.directory.Attribute;

/**
 * @author <a href="mailto:shomburg@hsofttec.com">shomburg</a>
 * @version $Id: LdapSearchService.java 167 2008-07-23 22:05:16Z shomburg $
 */
public interface SearcherService
{
    public static String CONFIG_KEY = "ldap.properties";
    public static String PROPKEY_INITIAL_CONTEXT_FACTORY = "ldap.initial.context.factory";
    public static String PROPKEY_PROVIDER_URL = "ldap.provider.url";
    public static String PROPKEY_SECURITY_AUTHENTICATION = "ldap.security.authentication";
    public static String PROPKEY_SECURITY_PRINCIPAL = "ldap.security.principal";
    public static String PROPKEY_SECURITY_CREDENTIALS = "ldap.security.credentials";

    /**
     * get the result object of an attribute based on baseDN and filter.
     *
     * @param baseDN     like <em>uid=sven.homburg,cn=depot120.dpd.de</em>
     * @param filter     like <em>(objectclass=*)</em>
     * @param attributes string array wich attributes shoud returnd like <em>uid,userpassword</em>
     *
     * @return
     */
    public List<Attribute[]> search(String baseDN, String filter, String ... attributes);
}
