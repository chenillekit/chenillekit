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

package org.chenillekit.google.utils;

/**
 * the configuration holder for proxy values.
 *
 * @author <a href="mailto:homburgs@googlemail.com">S.Homburg</a>
 * @version $Id: ProxyConfig.java 367 2008-02-06 09:59:36Z homburgs $
 */
public class ProxyConfig
{
    private String _host;
    private int _port;
    private String _userName;
    private String _password;

    public ProxyConfig(String host, int port, String userName, String password)
    {
        _host = host;
        _port = port;
        _userName = userName;
        _password = password;
    }

    public String getHost()
    {
        return _host;
    }

    public int getPort()
    {
        return _port;
    }

    public String getUserName()
    {
        return _userName;
    }

    public String getPassword()
    {
        return _password;
    }
}
