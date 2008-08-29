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

/**
 * some constants for chenillekit secure module.
 *
 * @author <a href="mailto:mlusetti@gmail.com">M.Lusetti</a>
 * @version $Id$
 */
public class ChenilleKitAccessConstants
{
	public static final String WEB_USER_IMPLEMENTATION = "webuser.implementation";

    /**
     * The logical name of the login page
     */
    public static final String LOGIN_PAGE = "chenillekit.access-login-page";

    /**
     * Meta data key applied to pages that may only be accessed after a succesfull login.
     */
    public static final String PRIVATE_PAGE = "chenillekit.access-private-page";
    public static final String PRIVATE_PAGE_ROLE = "chenillekit.access-private-page-role";
    public static final String PRIVATE_PAGE_GROUP = "chenillekit.access-private-page-group";

    /**
     * contribution key for password encoder.
     */
    public static final String PASSWORD_ENCODER = "chenillekit.access-passwordencoder";
}
