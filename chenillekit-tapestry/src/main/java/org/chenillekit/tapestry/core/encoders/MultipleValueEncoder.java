/*
 * Apache License
 * Version 2.0, January 2004
 * http://www.apache.org/licenses/
 *
 * Copyright 1996-2008 by Sven Homburg
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 */

package org.chenillekit.tapestry.core.encoders;

import java.util.List;

/**
 * @version $Id: MultipleValueEncoder.java 367 2008-02-06 09:59:36Z homburgs $
 */
public interface MultipleValueEncoder<V>
{
    String toClient(V value);

    List<V> toValue(String[] clientValue);
}
