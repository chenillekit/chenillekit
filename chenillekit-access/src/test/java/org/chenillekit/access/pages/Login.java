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

package org.chenillekit.access.pages;

import org.apache.tapestry5.annotations.Component;
import org.apache.tapestry5.annotations.Log;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.corelib.components.PasswordField;
import org.apache.tapestry5.corelib.components.TextField;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.slf4j.Logger;


/**
 * @version $Id$
 */
public class Login
{
    @Inject
    private Logger logger;

    @Persist
    @Property
    private String userName;

    @Property
    private String password;

    @Component(parameters = {"value=userName"})
    private TextField inputUserName;

    @Component(parameters = {"value=password"})
    private PasswordField inputPassword;

    @Log
    void onSuccess()
    {
//        authService.doAuthenticate(userName, password);
    }
}
