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
package org.chenillekit.access.services.impl;

import org.apache.tapestry5.ioc.internal.util.Defense;
import org.apache.tapestry5.runtime.Component;
import org.apache.tapestry5.services.ApplicationStateManager;
import org.apache.tapestry5.services.ComponentSource;
import org.apache.tapestry5.services.MetaDataLocator;

import org.chenillekit.access.ChenilleKitAccessConstants;
import org.chenillekit.access.annotations.Restricted;
import org.chenillekit.access.services.AccessValidator;
import org.chenillekit.access.utils.WebSessionUser;
import org.slf4j.Logger;

/**
 * @author <a href="mailto:mlusetti@gmail.com">M.Lusetti</a>
 * @version $Id$
 */
public class AccessValidatorImpl implements AccessValidator
{
    private final ApplicationStateManager asm;
    private final ComponentSource componentSource;
    private final MetaDataLocator locator;
    private final Class<? extends WebSessionUser> webSessionUserImplmentation;

    private final Logger logger;


    public AccessValidatorImpl(ApplicationStateManager stateManager,
                               ComponentSource componentSource, MetaDataLocator locator,
                               Logger logger,
                               Class<? extends WebSessionUser> webSessionUserImplmentation)
    {
        Defense.notNull(webSessionUserImplmentation, "webSessionUserImplmentation");
        this.asm = stateManager;
        this.componentSource = componentSource;
        this.locator = locator;
        this.logger = logger;
        this.webSessionUserImplmentation = webSessionUserImplmentation;
    }


    /* (non-Javadoc)
      * @see org.chenillekit.access.services.AccessValidator#hasAccess(java.lang.String, java.lang.String, java.lang.String)
      */
    public boolean hasAccess(String pageName, String componentId, String eventType)
    {
        boolean canAccess = true;

        if (logger.isDebugEnabled())
            logger.debug("check access for pageName/componentId/eventType: {}/{}/{}",
                         new Object[]{pageName, componentId, eventType});

        /* Is the requested page private ? */
        Component page = null;

        // This should be unnecessary...
        boolean found = false;
        while (!found)
        {
            try
            {
                page = componentSource.getPage(pageName);
                found = true;
            }
            catch (IllegalArgumentException iae)
            {
                if (pageName.lastIndexOf('/') != -1)
                {
                    pageName = pageName.substring(0, pageName.lastIndexOf('/'));
                    if (logger.isTraceEnabled())
                        logger.trace("New pagename: " + pageName);
                }
                else
                {
                    throw iae;
                }
            }
        }

        Restricted pagePrivate = page.getClass().getAnnotation(Restricted.class);

        if (pagePrivate != null)
        {
            WebSessionUser webSessionUser = asm.getIfExists(webSessionUserImplmentation);
            if (webSessionUser != null)
            {
                int role = Integer.parseInt(page.getComponentResources()
                        .getComponentModel()
                        .getMeta(ChenilleKitAccessConstants.PRIVATE_PAGE_ROLE));
                String group = page.getComponentResources()
                        .getComponentModel()
                        .getMeta(ChenilleKitAccessConstants.PRIVATE_PAGE_GROUP);

                boolean hasRole = false;
                boolean hasGroup = false;
                // We will see if this will need a changes...
                for (int i = 0; i < webSessionUser.getRoles().length; i++)
                {
                    int userRole = webSessionUser.getRoles()[i];
                    if (userRole >= role)
                    {
                        hasRole = true;
                        break;
                    }
                }
                for (int i = 0; i < webSessionUser.getGroups().length; i++)
                {
                    String userGroup = webSessionUser.getGroups()[i];
                    if (userGroup.equals(group))
                    {
                        hasGroup = true;
                        break;
                    }
                }

                logger.info("hasRole = " + hasRole);
                logger.info("hasGroup = " + hasGroup);

                // Let's see if it can access it
                canAccess = hasGroup && hasRole;
            }

        }

        return canAccess;
    }

}
