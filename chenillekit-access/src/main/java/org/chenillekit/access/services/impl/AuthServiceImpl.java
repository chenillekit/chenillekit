/*
 *  Apache License
 *  Version 2.0, January 2004
 *  http://www.apache.org/licenses/
 *
 *  Copyright 2008 by chenillekit.org
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 */

package org.chenillekit.access.services.impl;

import org.chenillekit.access.services.AuthService;
import org.chenillekit.access.services.PasswordEncoder;
import org.slf4j.Logger;

/**
 * @author <a href="mailto:homburgs@googlemail.com">shomburg</a>
 * @version $Id$
 */
public class AuthServiceImpl implements AuthService
{
    private final Logger logger;
    private PasswordEncoder passwordEncoder;

    public AuthServiceImpl(final Logger logger, final PasswordEncoder passwordEncoder)
    {
        this.logger = logger;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * User authentication.
     *
     * @param userName name of the user
     * @param password users password
     */
    public Object doAuthenticate(String userName, String password)
    {
        String encodedPassword = passwordEncoder.encodePassword(password, null);

        if (logger.isTraceEnabled())
            logger.trace("encodedPassword: " + encodedPassword);

        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }
}
