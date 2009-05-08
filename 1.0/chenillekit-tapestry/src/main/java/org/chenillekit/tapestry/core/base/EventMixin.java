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

package org.chenillekit.tapestry.core.base;

import java.util.List;

/**
 * @version $Id: EventMixin.java 499 2008-03-07 18:21:45Z homburgs $
 */
public interface EventMixin
{
    /**
     * get the event name.
     *
     * @return the event name
     */
    String getEventName();

    /**
     * get the conext parameter(s)
     *
     * @return conext parameter(s)
     */
    List<?> getContext();
}
