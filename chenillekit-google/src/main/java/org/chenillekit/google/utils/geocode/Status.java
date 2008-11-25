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
 *
 */

package org.chenillekit.google.utils.geocode;

import org.chenillekit.google.utils.JSONException;
import org.chenillekit.google.utils.JSONObject;

/**
 * @version $Id$
 */
public class Status
{
    private int code;
    private String request;

    public Status(JSONObject json)
    {
        buildFromJSON(json);
    }

    private void buildFromJSON(JSONObject json)
    {
        try
        {
            code = json.getInt("code");
            request = json.getString("request");
        }
        catch (JSONException e)
        {
            throw new RuntimeException(e);
        }
    }

    public int getCode()
    {
        return code;
    }

    public String getRequest()
    {
        return request;
    }

}
