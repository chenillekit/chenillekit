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

import java.lang.reflect.Field;

import org.apache.tapestry5.internal.services.RequestPageCache;
import org.apache.tapestry5.ioc.internal.util.Defense;
import org.apache.tapestry5.runtime.Component;
import org.apache.tapestry5.services.ApplicationStateManager;
import org.apache.tapestry5.services.ComponentSource;
import org.apache.tapestry5.services.MetaDataLocator;

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


    /**
     * We check for page/component and event type access rights.
     * <p/>
     * first we check the access rights for the requested page,
     * if access granted, we step down to the next level, the components.
     *
     * @see org.chenillekit.access.services.AccessValidator#hasAccess(java.lang.String, java.lang.String, java.lang.String)
     */
    public boolean hasAccess(String pageName, String componentId, String eventType)
    {
        boolean hasAccess = true;

        if (logger.isDebugEnabled())
            logger.debug("check access for pageName/componentId/eventType: {}/{}/{}",
                         new Object[]{pageName, componentId, eventType});

        Component page = getPage(pageName);
        
        // Con component qui prendere le meta attraverso il locator
        
        // Con i meta dati controllare l'accessibilita`
        
        if (page != null)
        {
            hasAccess = checkForPageAccess(page);
            if (hasAccess)
            {
                Field[] fields = page.getClass().getDeclaredFields();
                for (Field field : fields)
                {
                    if (field.isAnnotationPresent(Restricted.class) &&
                            field.isAnnotationPresent(org.apache.tapestry5.annotations.Component.class))
                    {
                        if (logger.isInfoEnabled())
                            logger.info("found restricted component '{}' in page '{}'", field.getName(), pageName);

                        Component pageComponent = page.getComponentResources().getEmbeddedComponent(field.getName());
                        System.err.println("pageComponent: " + pageComponent.getComponentResources().getCompleteId());
                    }
                }
            }
        }

        return hasAccess;
    }

    /**
     * check for page restriction, and if page restricted we check for users access rights.
     *
     * @param page the page(component) object
     *
     * @return true if user has access or the page is not restricted
     */
    private boolean checkForPageAccess(Component page)
    {
        boolean hasAccess = true;

        Restricted pagePrivate = page.getClass().getAnnotation(Restricted.class);
        if (pagePrivate != null)
        {
            WebSessionUser webSessionUser = asm.getIfExists(webSessionUserImplmentation);
            if (webSessionUser == null)
                return false;

            boolean hasRole = hasUserRequiredRole(webSessionUser.getRoleWeigh(), pagePrivate.roles());
            boolean hasGroup = hasUserRequiredGroup(webSessionUser.getGroups(), pagePrivate.groups());

            if (logger.isInfoEnabled())
            {
                logger.info("Page '{}' - hasRole = {} / hasGroup = {}",
                            new Object[]{page.getComponentResources().getPageName(), hasRole, hasGroup});
            }

            // Let's see if it can access it
            hasAccess = hasGroup && hasRole;
        }

        return hasAccess;
    }

    /**
     * get the page component.
     *
     * @param pageName the name of page
     *
     * @return may be null if not found
     */
    private Component getPage(String pageName)
    {
        Component component = null;

        // This should be unnecessary...
        boolean found = false;
        while (!found)
        {
            try
            {
                component = componentSource.getPage(pageName);
                found = true;
            }
            catch (IllegalArgumentException iae)
            {
                if (pageName.lastIndexOf('/') != -1)
                {
                    pageName = pageName.substring(0, pageName.lastIndexOf('/'));
                    if (logger.isTraceEnabled())
                        logger.trace("New pagename: {}", pageName);
                }
                else
                {
                    throw iae;
                }
            }
        }

        return component;
    }

    /**
     * check if user has required role to access page/component/event.
     *
     * @param userRoles     role weigh the user have
     * @param requiredRoles role weigh required for page/component/event access
     *
     * @return true if user fulfill the required role
     */
    private boolean hasUserRequiredRole(int userRoleWeigh, int requiredRoleWeigh)
    {
        return userRoleWeigh >= requiredRoleWeigh ? true : false;
    }

    /**
     * check if user has required group to access page/component/event.
     *
     * @param userGroups     groups the user have
     * @param requiredGroups groups required for page/component/event access
     *
     * @return true if user has the required group
     */
    private boolean hasUserRequiredGroup(String[] userGroups, String[] requiredGroups)
    {
        boolean hasGroup = false;

        /**
         * if no group required
         */
        if (requiredGroups.length == 0)
            return true;

        for (String requiredGroup : requiredGroups)
        {
            for (String userGroup : userGroups)
            {
                if (userGroup.equalsIgnoreCase(requiredGroup))
                {
                    hasGroup = true;
                    break;
                }
            }

            if (hasGroup)
                break;
        }

        return hasGroup;
    }
}
