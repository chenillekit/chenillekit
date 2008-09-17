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

/**
 *
 */

package org.chenillekit.access;

import org.slf4j.Marker;
import org.slf4j.MarkerFactory;

/**
 * some constants for chenillekit secure module.
 *
 * @author <a href="mailto:mlusetti@gmail.com">M.Lusetti</a>
 * @version $Id$
 */
public final class ChenilleKitAccessConstants
{
	public static final String WEB_USER_IMPLEMENTATION = "webuser.implementation";

    /**
     * The logical name of the login page
     */
    public static final String LOGIN_PAGE = "chenillekit.access-login-page";

    /**
     * Meta data key applied to pages that may only be accessed after a succesfull login.
     */
    public static final String RESTRICTED_PAGE_ROLE = "chenillekit.access-restricted-page-role"; // are these too verbose?
    public static final String RESTRICTED_PAGE_GROUP = "chenillekit.access-restricted-page-group";
    public static final String RESTRICTED_EVENT_HANDLER_PREFIX = "chenillekit.access-restricted-handler";
    public static final String RESTRICTED_EVENT_HANDLER_ROLE_SUFFIX = "role";
    public static final String RESTRICTED_EVENT_HANDLER_GROUPS_SUFFIX = "groups";
    
    public static final String NO_RESTRICTION = "NONE";

    /**
     * contribution key for password encoder.
     */
    public static final String PASSWORD_ENCODER = "chenillekit.access-passwordencoder";
    
    /**
     * A root marker for all things Tapestry related. The remaining markers are children of the TAPESTRY marker.
     */
    public static final Marker CHENILLEKIT_ACCESS = MarkerFactory.getMarker("CHENILLEKIT-ACCESS");
}
