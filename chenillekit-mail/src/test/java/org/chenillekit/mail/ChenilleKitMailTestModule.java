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

package org.chenillekit.mail;

import org.apache.tapestry5.ioc.MappedConfiguration;
import org.apache.tapestry5.ioc.annotations.SubModule;

/**
 * @author <a href="mailto:homburgs@googlemail.com">shomburg</a>
 * @version $Id$
 */
@SubModule(value = {ChenilleKitMailModule.class})
public class ChenilleKitMailTestModule
{
    public static void contributeApplicationDefaults(MappedConfiguration<String, String> contribution)
    {
        contribution.add(ChenilleKitMailConstants.SMTP_PORT, "9999");
        contribution.add(ChenilleKitMailConstants.SMTP_DEBUG, "true");
    }
}
