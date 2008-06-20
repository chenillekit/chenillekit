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

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.tapestry5.Link;
import org.apache.tapestry5.internal.services.LinkFactory;
import org.apache.tapestry5.ioc.internal.util.Defense;
import org.apache.tapestry5.ioc.services.SymbolSource;
import org.apache.tapestry5.runtime.Component;
import org.apache.tapestry5.services.ApplicationStateManager;
import org.apache.tapestry5.services.ComponentClassResolver;
import org.apache.tapestry5.services.ComponentSource;
import org.apache.tapestry5.services.Dispatcher;
import org.apache.tapestry5.services.MetaDataLocator;
import org.apache.tapestry5.services.Request;
import org.apache.tapestry5.services.Response;
import org.chenillekit.access.ChenilleKitAccessConstants;
import org.chenillekit.access.annotations.Restricted;
import org.chenillekit.access.utils.WebSessionUser;
import org.slf4j.Logger;

/**
 * @author <a href="mailto:homburgs@gmail.com">S.Homburg</a>
 * @version $Id$
 */
public class AccessController implements Dispatcher
{
//    private final AuthService authService;

    private final String loginPage;
    private final ApplicationStateManager asm;

    private final ComponentClassResolver componentResolver;
    private final ComponentSource componentSource;
    private final MetaDataLocator locator;
    // Warn... this is an internal interface!
    private final LinkFactory linkFactory;
    private final Class<? extends WebSessionUser> webSessionUserImplmentation;

    private final Logger logger;

    // A beast that recognizes all the elements of a path in a single go.
    // We skip the leading slash, then take the next few terms (until a dot or a colon)
    // as the page name.  Then there's a sequence that sees a dot
    // and recognizes the nested component id (which may be missing), which ends
    // at the colon, or at the slash (or the end of the string).  The colon identifies
    // the event name (the event name is also optional).  A valid path will always have
    // a nested component id or an event name (or both) ... when both are missing, then the
    // path is most likely a page render request.  After the optional event name,
    // the next piece is the action context, which is the remainder of the path.

    private final Pattern PATH_PATTERN = Pattern.compile(

            "^/" +      // The leading slash is recognized but skipped
                    "(((\\w+)/)*(\\w+))" + // A series of folder names leading up to the page name, forming the logical page name
                    "(\\.(\\w+(\\.\\w+)*))?" + // The first dot separates the page name from the nested component id
                    "(\\:(\\w+))?" + // A colon, then the event type
                    "(/(.*))?", //  A slash, then the action context
                                Pattern.COMMENTS);

    // Constants for the match groups in the above pattern.
    private static final int LOGICAL_PAGE_NAME = 1;
    private static final int NESTED_ID = 6;
    private static final int EVENT_NAME = 9;
    private static final int CONTEXT = 11;


    public AccessController(ApplicationStateManager stateManager, ComponentClassResolver resolver,
                            ComponentSource componentSource, MetaDataLocator locator,
                            Logger logger, LinkFactory linkFactory, SymbolSource symbols,
                            Class<? extends WebSessionUser> webSessionUserImplmentation)
    {
        Defense.notNull(webSessionUserImplmentation, "webSessionUserImplmentation");
        this.asm = stateManager;
        this.componentResolver = resolver;
        this.componentSource = componentSource;
        this.locator = locator;
        this.logger = logger;
        this.linkFactory = linkFactory;
        this.webSessionUserImplmentation = webSessionUserImplmentation;
        this.loginPage = symbols.valueForSymbol(ChenilleKitAccessConstants.LOGIN_PAGE);
    }

    /**
     * Analyzes the incoming request and performs an appropriate operation for each.
     *
     * @param request  the request object
     * @param response the response object
     *
     * @return true if a response was delivered, false if the servlet container should be allowed to handle the request
     */
    public boolean dispatch(Request request, Response response) throws IOException
    {
        if (logger.isTraceEnabled())
            logger.trace("Checking security/access constraints on: {}", request.getPath());

        Matcher matcher = PATH_PATTERN.matcher(request.getPath());

        if (!matcher.matches())
            return false;

//        /**
//         * We need to get the Tapestry page requested by the user.
//         * So we parse the path extracted from the request
//         */
//        String path = request.getPath();
//        if (path.equals(""))
//            return false;
//
//        int nextslashx = path.length();
//        String pageName;
//
//        while (true)
//        {
//            pageName = path.substring(1, nextslashx);
//            if (!pageName.endsWith("/") && componentResolver.isPageName(pageName))
//                break;
//            nextslashx = path.lastIndexOf('/', nextslashx - 1);
//            if (nextslashx <= 1)
//                return false;
//        }
        
        String logicalPageName = matcher.group(LOGICAL_PAGE_NAME);

        String nestedComponentId = null; //matcher.group(NESTED_ID);
        String eventType = null; //matcher.group(EVENT_NAME);

        if (!hasAccess(logicalPageName, nestedComponentId, eventType))
        {
            // WARN  linkFactory is an internal interace...
            Link link = linkFactory.createPageLink(loginPage, false);
            response.sendRedirect(link);
            return true; // Make sure to leave the chain
        }

        return false;
    }

    /**
     * Check the rights of the user for the page requested
     *
     * @param pageName    name of the page
     * @param componentId component id (not used yet)
     * @param eventType   event type (not used yet)
     *
     * @return if true then leave the chain
     */
    private boolean hasAccess(String pageName, String componentId, String eventType)
    {
        boolean canAccess = false;
        
        if (logger.isTraceEnabled())
            logger.trace("check access for pageName/componentId/eventType: {}/{}/{}",
                         new Object[]{pageName, componentId, eventType});
        
        /* Is the requested page private ? */
        Component page = null;
        boolean found = false;
        while ( !found )
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
                                        logger.trace("Nuovo pagename: " + pageName);
                        }
                        else
                        {
                                throw iae;
                        }
                }
        }

        boolean pagePrivate = locator.findMeta(ChenilleKitAccessConstants.PRIVATE_PAGE, page.getComponentResources(), Boolean.class);
        
        if (pagePrivate)
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
        		
        		// Let's see if it can access it
        		canAccess = hasGroup && hasRole;
        	}
        	
        }

        return canAccess;
        

//      Component page = componentSource.getPage(pageName);
//      Restricted restrictedPage = page.getClass().getAnnotation(Restricted.class);
//      if (restrictedPage == null)
//          return true;

//      if (logger.isTraceEnabled())
//          logger.trace("page '{}' is restricted", pageName);
      
//      /* Is the user already authentified ? */
//      WebSessionUser webSessionUser = asm.getIfExists(webSessionUserImplmentation);
//      if (webSessionUser != null)
//      {
//          boolean hasRole = false;
//          boolean hasGroup = false;
//
//          for (int pageRole : restrictedPage.roles())
//          {
//              for (int userRole : webSessionUser.getRoles())
//              {
//                  if (userRole >= pageRole)
//                  {
//                      hasRole = true;
//                      break;
//                  }
//              }
//          }
//
//          if (!hasRole)
//          {
//              for (String pageGroup : restrictedPage.groups())
//              {
//                  for (String userGroup : webSessionUser.getGroups())
//                  {
//                      if (userGroup.equalsIgnoreCase(pageGroup))
//                      {
//                          hasGroup = true;
//                          break;
//                      }
//                  }
//              }
//          }
//
//          if (hasRole || hasGroup)
//              canAccess = true;
//      }
    }
}
