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

Ck.ThumbNail = Class.create();
Ck.ThumbNail.prototype = {
    initialize: function(elementId, assetUrl)
    {
        this.elementId = elementId;
        this.assetUrl = assetUrl;
        this.elementContent = $(this.elementId).src;

        $(this.elementId).onclick = this.showOriginalImage.bindAsEventListener(this);
        $(this.elementId).onmouseout = this.hideOriginalImage.bindAsEventListener(this);
    },
    showOriginalImage:function()
    {
        if ($(this.elementId).src != this.assetUrl)
            $(this.elementId).setAttribute("src", this.assetUrl);
    },
    hideOriginalImage:function()
    {
        $(this.elementId).src = this.elementContent
    }
}
