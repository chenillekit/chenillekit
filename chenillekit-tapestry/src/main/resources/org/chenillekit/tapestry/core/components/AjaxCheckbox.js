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

Ck.AjaxCheckbox = Class.create();
Ck.AjaxCheckbox.prototype = {
    initialize: function(elementId, requestUrl, onCompleteCallback)
    {
        this.elementId = elementId;
        this.requestUrl = requestUrl;
        this.onCompleteCallback = onCompleteCallback;

        Event.observe($(this.elementId), 'click', this._click.bindAsEventListener(this));
    },
    _click: function(theEvent)
    {
        new Ajax.Request(this.requestUrl + "/" + ($(this.elementId).getValue() == null ? "false" : "true"), {
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