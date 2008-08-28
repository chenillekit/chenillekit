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

/**
 * <p>MD5 implementation of PasswordEncoder.</p>
 * <p>If a <code>null</code> password is presented, it will be treated as an empty <code>String</code> ("")
 * password.</p>
 * <P>As MD5 is a one-way hash, the salt can contain any characters.</p>
 * <p/>
 * This is a convenience class that extends the
 * {@link MessageDigestPasswordEncoder} and passes MD5 as the algorithm to use.
 *
 * @author Ray Krueger
 * @author colin sampaleanu
 * @author Ben Alex
 * @version $Id$
 */
public class Md5PasswordEncoder extends MessageDigestPasswordEncoder
{

    public Md5PasswordEncoder()
    {
        super("MD5");
    }
}
