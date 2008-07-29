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

import java.util.Hashtable;
import java.util.List;
import javax.naming.Context;
import javax.naming.NameNotFoundException;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.Attribute;
import javax.naming.directory.DirContext;
import javax.naming.directory.InitialDirContext;
import javax.naming.directory.SearchControls;
import javax.naming.directory.SearchResult;

import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.SystemUtils;
import org.apache.tapestry5.ioc.Resource;
import org.apache.tapestry5.ioc.internal.util.CollectionFactory;
import org.apache.tapestry5.ioc.internal.util.Defense;
import org.apache.tapestry5.ioc.services.RegistryShutdownListener;

import org.chenillekit.ldap.services.SearcherService;
import org.slf4j.Logger;

/**
 * @author <a href="mailto:shomburg@hsofttec.com">shomburg</a>
 * @version $Id: LdapSearchServiceImpl.java 167 2008-07-23 22:05:16Z shomburg $
 */
public class SimpleSearcherServiceImpl implements SearcherService, RegistryShutdownListener
{
    private Logger logger;
    private final Resource configResource;
    private DirContext dirContext;

    public SimpleSearcherServiceImpl(Logger logger, Resource configResource)
    {
        Defense.notNull(configResource, "configResource");

        this.logger = logger;
        this.configResource = configResource;

        if (!this.configResource.exists())
            throw new RuntimeException(String.format("config resource '%s' not found!", this.configResource.toString()));

        initService(this.configResource);
    }

    /**
     * read and check all service parameters.
     */
    private void initService(Resource configResource)
    {
        try
        {
            Configuration configuration = new PropertiesConfiguration(configResource.toURL());
            Hashtable<String, String> env = new Hashtable<String, String>();
            env.put(Context.INITIAL_CONTEXT_FACTORY, configuration.getString(PROPKEY_INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory"));
            env.put(Context.PROVIDER_URL, configuration.getString(PROPKEY_PROVIDER_URL));
            env.put(Context.SECURITY_AUTHENTICATION, configuration.getString(PROPKEY_SECURITY_AUTHENTICATION));
            env.put(Context.SECURITY_PRINCIPAL, configuration.getString(PROPKEY_SECURITY_PRINCIPAL));           // specify the username
            env.put(Context.SECURITY_CREDENTIALS, configuration.getString(PROPKEY_SECURITY_CREDENTIALS));       // specify the password
            dirContext = new InitialDirContext(env);
        }
        catch (ConfigurationException e)
        {
            throw new RuntimeException(e);
        }
        catch (NamingException e)
        {
            throw new RuntimeException(e);
        }
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
    public List<Attribute[]> search(String baseDN, String filter, String ... attributes)
    {
        List<Attribute[]> returnList = CollectionFactory.newList();
        Attribute[] resultObject;

        try
        {
            if (dirContext == null)
                throw new RuntimeException("DirContext is not instantiatet");

            if (logger.isInfoEnabled())
                logger.info("BaseDN: " + baseDN + " / Filter: " + filter + " / Attributes: " + ArrayUtils.toString(attributes, "null"));

            SearchControls sc = new SearchControls();
            sc.setSearchScope(SearchControls.SUBTREE_SCOPE);
            NamingEnumeration<SearchResult> ne;

            // Here we actually perform the search.
            ne = dirContext.search(baseDN, filter, sc);

            // We cycle through the NamingEnumeration
            // that is returned by the search.
            while (ne.hasMore())
            {
                // Retrieve the result as a SearchResult
                // and print it (not very pretty). There are
                // methods for extracting the attributes and
                // values without printing, as well.
                SearchResult sr = ne.next();

                resultObject = new Attribute[attributes.length];
                for (int i = 0; i < attributes.length; i++)
                {
                    String attribute = attributes[i];
                    resultObject[i] = sr.getAttributes().get(attribute);
                }
                returnList.add(resultObject);
            }


            return returnList;
        }
        catch (NameNotFoundException e)
        {
            if (logger.isWarnEnabled())
                logger.warn(e.getMessage() + SystemUtils.LINE_SEPARATOR + "BaseDN: '" + baseDN + "' / Filter: '" + filter + "'");

            return null;
        }
        catch (NamingException e)
        {
            throw new RuntimeException(e);
        }
    }

    /**
     * Invoked when the registry shuts down, giving services a chance to perform any final operations. Service
     * implementations should not attempt to invoke methods on other services (via proxies) as the service proxies may
     * themselves be shutdown.
     */
    public void registryDidShutdown()
    {
        if (logger.isInfoEnabled())
            logger.info("shutting down the dir context");

        try
        {
            if (dirContext != null)
                dirContext.close();
        }
        catch (NamingException e)
        {
            throw new RuntimeException(e);
        }
    }
}
