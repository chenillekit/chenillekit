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

package org.chenillekit.core.streams;

import java.io.StringReader;

/**
 * Wraps a String as an InputStream.
 * <p/>
 * stolen from org.apache.tools.ant.filters.StringInputStream
 *
 * @version $Id$
 */
public class StringInputStream extends ReaderInputStream
{
    /**
     * Composes a stream from a String
     *
     * @param source The string to read from. Must not be <code>null</code>.
     */
    public StringInputStream(String source)
    {
        super(new StringReader(source));
    }

    /**
     * Composes a stream from a String with the specified encoding
     *
     * @param source   The string to read from. Must not be <code>null</code>.
     * @param encoding The encoding scheme.  Also must not be <CODE>null</CODE>.
     */
    public StringInputStream(String source, String encoding)
    {
        super(new StringReader(source), encoding);
    }
}
