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

package org.chenillekit.tapestry.core;

/**
 * some constants for t5components library.
 *
 * @version $Id$
 */
public class ChenilleKitCoreConstants
{
    public static String __THUMBNAL_DIRECTORY__ = "thumbnails";
    public static String __CR__ = System.getProperty("line.separator");
    public static String __VERSION__;
    public static String URI_PREFIX = "uri";
    public static String URI_PATH_PREFIX = "/" + URI_PREFIX + "/";

    private static ChenilleKitCoreConstants ourInstance = new ChenilleKitCoreConstants();

    public static ChenilleKitCoreConstants getInstance()
    {
        return ourInstance;
    }

    private ChenilleKitCoreConstants()
    {
        __VERSION__ = this.getClass().getPackage().getImplementationVersion();
        if (__VERSION__ == null)
            __VERSION__ = "unknown";
    }
}

