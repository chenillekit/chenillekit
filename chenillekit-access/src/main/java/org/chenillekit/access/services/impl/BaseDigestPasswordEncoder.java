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
 * <p>Convenience base for digest password encoders.</p>
 *
 * @author colin sampaleanu
 * @version $Id$
 */
public abstract class BaseDigestPasswordEncoder extends BasePasswordEncoder
{
    private boolean encodeHashAsBase64 = false;

    public boolean getEncodeHashAsBase64()
    {
        return encodeHashAsBase64;
    }

    /**
     * The encoded password is normally returned as Hex (32 char) version of the hash bytes. Setting this
     * property to true will cause the encoded pass to be returned as Base64 text, which will consume 24 characters.
     *
     * @param encodeHashAsBase64 set to true for Base64 output
     */
    public void setEncodeHashAsBase64(boolean encodeHashAsBase64)
    {
        this.encodeHashAsBase64 = encodeHashAsBase64;
    }
}
