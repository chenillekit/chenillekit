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

import java.util.Enumeration;
import java.util.List;
import javax.naming.NamingException;

import netscape.ldap.LDAPEntry;
import org.chenillekit.ldap.ChenilleKitLDAPTestModule;
import org.chenillekit.ldap.services.internal.SearcherService;
import org.chenillekit.test.AbstractTestSuite;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Test;

/**
 * @version $Id$
 */
public class TestSimpleSearcher extends AbstractTestSuite
{
    private SearcherService searcherService;

    @BeforeSuite
    public final void setup_registry()
    {
        super.setup_registry(ChenilleKitLDAPTestModule.class);
        searcherService = registry.getService(SearcherService.class);
    }

    @Test(threadPoolSize = 4, invocationCount = 50, successPercentage = 98)
    public void test_simple_search() throws NamingException
    {
        String baseDN = "o=Bund,c=DE";
        String filter = "(cn=Bund*)";
        String attribute = "mail";
        List<LDAPEntry> matches = searcherService.search(baseDN, filter, attribute);
        for (LDAPEntry match : matches)
        {
            Enumeration values = match.getAttribute(attribute).getStringValues();
            while (values.hasMoreElements())
            {
                String value = (String) values.nextElement();
                System.err.println(String.format("value of attribute '%s': %s", attribute, value));
            }
        }

        assertTrue(matches.size() >= 1);
    }
}
