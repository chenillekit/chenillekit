/*
 * Apache License
 * Version 2.0, January 2004
 * http://www.apache.org/licenses/
 *
 * Copyright 2008-2010 by chenillekit.org
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 */

package org.chenillekit.access.dao;

/**
 * @version $Id: ProtectionRule.java 628 2010-02-24 11:05:45Z homburgs $
 */
public interface ProtectionRule
{
    /**
     * get the groups.
     */
    String[] getGroups();

    /**
     * get the role weight.
     */
    int getRoleWeight();
}
