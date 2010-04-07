/*
 * Apache License
 * Version 2.0, January 2004
 * http://www.apache.org/licenses/
 *
 * Copyright 2008-2010 by chenillekit.org
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 */
package org.chenillekit.ldap.tests;

import netscape.ldap.LDAPEntry;
import org.chenillekit.ldap.ChenilleKitLDAPTestModule;
import org.chenillekit.ldap.OnePropMapper;
import org.chenillekit.ldap.OneProperty;
import org.chenillekit.ldap.services.LDAPOperation;
import org.chenillekit.test.AbstractTestSuite;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Test;

import javax.naming.NamingException;
import java.util.List;

/**
 * @author massimo
 *
 */
public class LDAPOperationTest extends AbstractTestSuite
{
	private LDAPOperation ldapOperation;

	private OnePropMapper oneMapper;

    @BeforeSuite
    public final void setup()
    {
        super.setup_registry(ChenilleKitLDAPTestModule.class);
        ldapOperation = registry.getService(LDAPOperation.class);
        oneMapper = new OnePropMapper();
    }

    @Test
    public void search() throws NamingException
    {
        String baseDN = "o=Bund,c=DE";
        String filter = "(cn=Bund*)";
        String attribute = "mail";

        List<LDAPEntry> matches = ldapOperation.search(baseDN, filter, attribute);

        assertTrue(matches.size() >= 1);
    }

    @Test
    public void search_not_found() throws NamingException
    {
        String baseDN = "o=Bund,c=DE";
        String filter = "(cn=This_Should_Not_Be_In_That_LDAP_Tree)";
        String attribute = "mail";

        List<LDAPEntry> matches = ldapOperation.search(baseDN, filter, attribute);

        assertTrue(matches.isEmpty());
    }

    @Test
    public void search_mapper() throws NamingException
    {
        String baseDN = "o=Bund,c=DE";
        String filter = "(cn=Bund*)";

        List<OneProperty> matches = ldapOperation.search(baseDN, filter, oneMapper);

        assertTrue(matches.size() >= 1);
    }

    @Test
    public void search_mapper_not_found() throws NamingException
    {
        String baseDN = "o=Bund,c=DE";
        String filter = "(cn=This_Should_Not_Be_In_That_LDAP_Tree)";

        List<OneProperty> matches = ldapOperation.search(baseDN, filter, oneMapper);

        assertTrue(matches.isEmpty());
    }

    @Test
    public void lookup() throws NamingException
    {
    	String baseDN = "o=Bund,c=DE";
        String filter = "(cn=Bund*)";

        List<LDAPEntry> matches = ldapOperation.search(baseDN, filter);

        assertTrue(matches.size() >= 1);

        LDAPEntry match = matches.get(0);

        String matchDN = match.getDN();
        LDAPEntry entry = ldapOperation.lookup(matchDN);

        assertNotNull(entry);
    }

    @Test
    public void lookup_not_present() throws NamingException
    {
    	String matchDN = "cn=This_Should_Not_Be_In_That_LDAP_Tree,o=Bund,c=DE";

        LDAPEntry entry = ldapOperation.lookup(matchDN);

        assertNull(entry);
    }

    @Test
    public void lookup_mapper() throws NamingException
    {
    	String baseDN = "o=Bund,c=DE";
        String filter = "(cn=Bund*)";

        List<LDAPEntry> matches = ldapOperation.search(baseDN, filter);

        assertTrue(matches.size() >= 1);

        LDAPEntry match = matches.get(0);

        String matchDN = match.getDN();

        OneProperty one = ldapOperation.lookup(matchDN, oneMapper);

        assertNotNull(one);
    }

    @Test
    public void lookup_mapper_not_present() throws NamingException
    {
    	String matchDN = "cn=This_Should_Not_Be_In_That_LDAP_Tree,o=Bund,c=DE";

        OneProperty one = ldapOperation.lookup(matchDN, oneMapper);

        assertNull(one);
    }

}
