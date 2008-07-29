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
import javax.naming.NamingException;

import org.chenillekit.ldap.ChenilleKitLDAPTestModule;
import org.chenillekit.test.AbstractTestSuite;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Test;

/**
 * @author <a href="mailto:shomburg@hsofttec.com">S.Homburg</a>
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

    @Test
    public void test_simple_search() throws NamingException
    {
        List<Attribute[]> matches = searcherService.search("o=Bund,c=DE", "(cn=Bund*)", "mail");
        for (Attribute[] match : matches)
            System.err.println(match[0].get());

        assertTrue(matches.size() >= 1);
    }
}
