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

Ck.Cookie = {
    set: function(name, value, daysToExpire)
    {
        var expire = '';
        if (daysToExpire != undefined)
        {
            var d = new Date();
            d.setTime(d.getTime() + (86400000 * parseFloat(daysToExpire)));
            expire = '; expires=' + d.toGMTString();
        }
        return (document.cookie = encodeURI(name) + '=' + encodeURI(value || '') + expire);
    },
    get: function(name)
    {
        var cookie = document.cookie.match(new RegExp('(^|;)\\s*' + encodeURI(name) + '=([^;\\s]*)'));
        return (cookie ? decodeURI(cookie[2]) : null);
    },
    erase: function(name)
    {
        var cookie = Ck.Cookie.get(name) || true;
        Ck.Cookie.set(name, '', -1);
        return cookie;
    },
    accept: function()
    {
        if (typeof navigator.cookieEnabled == 'boolean')
        {
            return navigator.cookieEnabled;
        }
        Ck.Cookie.set('_test', '1');
        return (Ck.Cookie.erase('_test') = '1');
    }
};