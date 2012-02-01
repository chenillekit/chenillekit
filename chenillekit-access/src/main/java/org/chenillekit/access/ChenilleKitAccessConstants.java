/*
 * Apache License
 * Version 2.0, January 2004
 * http://www.apache.org/licenses/
 *
 * Copyright 2008-2012 by chenillekit.org
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
 * @version $Id$
 */
public final class ChenilleKitAccessConstants
{
    public static final String REMEMBERED_ACTIVE_PAGE_NAME = "CKRememberedActivePageName";

    public static final String REMEMBERED_CONTAINING_PAGE_NAME = "CKRememberedContainingPageName";

    public static final String REMEMBERED_NESTED_COMPONENT_ID = "CKRememberedComponentId";

    public static final String REMEMBERED_EVENT_TYPE = "CKRememberedAtcivePageName";

    public static final String REMEMBERED_EVENT_CONTEXT = "CKRememberedActivationContext";

    public static final String REMEMBERED_ACTIVATION_CONTEXT = "CKRememberedActivationContext";

    public static final String REMEMBERED_PARAMS_TYPE = "CKRememberedParamsType";

    public static final String REMEMBERED_PARAMS_TYPE_PAGERENDER_VALUE = "pagerender";

    public static final String REMEMBERED_PARAMS_TYPE_ACTIONEVENT_VALUE = "actioneventr";

    /**
     * Cookie flag to check for a successful login done.
     */
    public static final String LOGIN_SUCCESSFUL_COOKIE_NAME = "CKAccessLogin";

    /**
     * Cookie value to indicate a successful login
     */
    public static final String LOGIN_SUCCESSFUL_COOKIE_NAME_OK = "OK";

    /**
     * Cookie value to indicate a unsuccessful login
     */
    public static final String LOGIN_SUCCESSFUL_COOKIE_NAME_KO = "KO";

    /**
     * The logical name of the login page
     */
    public static final String LOGIN_PAGE = "chenillekit.access-login-page";

    /**
     * The logical name of the fallback page
     */
    public static final String FALLBACK_PAGE = "chenillekit.access-fallback-page";

    /**
     * wich action should execute if user have no access rights.
     */
    public static final String ACCESS_DENIED_ACTION = "chenillekit.access-denied-action";
    public static final String JUMP_TO_LOGIN_PAGE = "chenillekit.access-jump-to-login-page";
    public static final String SEND_ERROR_401 = "chenillekit.access-send-error-401";
    public static final String TRIGGER_EVENT = "chenillekit.access-trigger-event";

    /**
     * Meta data key applied to pages that may only be accessed after a succesfull login.
     */
    public static final String RESTRICTED_PAGE = "chenillekit.access-restricted-page";
    public static final String RESTRICTED_PAGE_ROLE = "chenillekit.access-restricted-page-role"; // are these too verbose?
    public static final String RESTRICTED_PAGE_LOGICAL = "chenillekit.access-restricted-page-logical"; // are these too verbose?
    public static final String RESTRICTED_PAGE_GROUP = "chenillekit.access-restricted-page-group";
    public static final String RESTRICTED_EVENT_HANDLER_PREFIX = "chenillekit.access-restricted-handler";
    public static final String RESTRICTED_EVENT_HANDLER_ROLE_SUFFIX = "role";
	public static final String RESTRICTED_EVENT_HANDLER_LOGICAL_SUFFIX = "logical";
    public static final String RESTRICTED_EVENT_HANDLER_GROUPS_SUFFIX = "groups";

    public static final String NO_GROUP_RESTRICTION = "NONE";

    /**
     * contribution key for password encoder.
     */
    public static final String PASSWORD_ENCODER = "chenillekit.access-passwordencoder";

    public static final String NOT_AUTHENTICATED_ERROR_MESSAGE = "chenillekit.access-not-authenticated";


    /**
     * A root marker for all things Tapestry related. The remaining markers are children of the TAPESTRY marker.
     */
    public static final Marker CHENILLEKIT_ACCESS = MarkerFactory.getMarker("CHENILLEKIT-ACCESS");


    public static final String EVENT_NOT_ENOUGH_ACCESS_RIGHTS = "notEnoughAccessRights";
}
