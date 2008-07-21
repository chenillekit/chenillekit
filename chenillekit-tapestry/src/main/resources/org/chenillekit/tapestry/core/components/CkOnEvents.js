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

Ck.OnEvent = Class.create();
Ck.OnEvent.prototype = {
    initialize: function(eventName, elementId, stop, requestUrl, onCompleteCallback)
    {
        if (!$(elementId))
            throw(elementId + " doesn't exist!");

        this.element = $(elementId);
        this.stop = stop;
        this.onCompleteCallback = onCompleteCallback;
        this.requestUrl = requestUrl;

        Event.observe(this.element, eventName, this.reflectOnEvent.bind(this), false);
    },
    reflectOnEvent: function(myEvent)
    {
        var fieldValue;
        var formElement = this.element.form;
        var url = this.requestUrl;

        if(this.stop) Event.stop(myEvent);

        var sessionPos = url.toLowerCase().lastIndexOf(";jsessionid");

        if (typeof formElement != 'undefined')
            fieldValue = $F(this.element);

        if (typeof fieldValue != 'undefined')
        {
            if (sessionPos == -1)
                url += "/" + fieldValue;
            else
              url = url.substring(0, sessionPos) + "/" + fieldValue + url.substring(sessionPos);
        }

        new Ajax.Request(url, {
            method: 'post',
            onFailure: function(t)
            {
                alert('Error communication with the server: ' + t.responseText.stripTags());
            },
            onException: function(t, exception)
            {
                alert('Error communication with the server: ' + exception.stripTags());
            },
            onSuccess: function(t)
            {
                if (this.onCompleteCallback != "undefined")
                    eval(this.onCompleteCallback + "('" + t.responseText + "')");
            }.bind(this)
        });
    }
}


Ck.ButtonEvent = Class.create();
Ck.ButtonEvent.prototype =
{
    initialize: function(elementId, requestUrl)
    {
        if (!$(elementId))
            throw(elementId + " doesn't exist!");

        this.element = $(elementId);
        this.requestUrl = requestUrl;

        Event.observe(this.element, "click",
                this.fireEvent.bind(this, this.element),
                false);
    },
    fireEvent: function(myEvent)
    {
        document.location = this.requestUrl;
        return false;
    }
}