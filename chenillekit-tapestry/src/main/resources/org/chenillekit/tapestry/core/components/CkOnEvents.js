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

        if (typeof formElement != 'undefined')
            fieldValue = $F(this.element);

        new Ajax.Request(url, {
            method: 'post',
			parameters: {"value": fieldValue},
			onFailure: function(t)
            {
                alert('Error communication with the server: ' + t.responseText.stripTags());
            },
            onException: function(t, exception)
            {
                alert('Error communication with the server: ' + exception.message);
            },
            onSuccess: function(t)
            {
                if (this.onCompleteCallback != null)
				{
					var funcToEval = this.onCompleteCallback + "(" + t.responseText + ")";
					eval(funcToEval);
				}
			}.bind(this)
        });
    }
}

Ck.ButtonEvent = Class.create(
{
  initialize: function(elementId, requestUrl, confirmMessage)
  {
      if (!$(elementId))
          throw(elementId + " doesn't exist!");

      this.element = $(elementId);
      this.requestUrl = requestUrl;

      this.confirmMessage = confirmMessage;

      Event.observe(this.element, "click",
              this.fireEvent.bind(this),
              false);
  },
  fireEvent: function(event)
  {
  	if (this.confirmMessage.length < 2 || confirm(this.confirmMessage))
  		document.location = this.requestUrl;
  	else
  		event.stop();

      return false;
  }
});
