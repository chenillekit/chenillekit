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

Ck.Rounded = new Class.create();
Ck.Rounded.prototype =
{
    initialize: function(elementId, bk, color, size)
    {
        this.selector = $(elementId);
        this.bk = bk;
        this.color = color;
        this.size = size;
    },
    round:function()
    {
        this.addTop(this.selector, this.bk, this.color, this.size);
        this.addBottom(this.selector, this.bk, this.color, this.size);
    },
    roundedTop:function ()
    {
        this.addTop(this.selector, this.bk, this.color, this.size);
    },
    roundedBottom:function ()
    {
        this.addBottom(this.selector, this.bk, this.color, this.size);
    },
    addTop: function (el, bk, color, size)
    {
        var i;
        var d = document.createElement("b");
        var cn = "ck_r";
        var lim = 4;
        if (size && size == "small")
        {
            cn = "ck_rs";
            lim = 2
        }
        d.className = "ck_rtop";
        d.style.backgroundColor = bk;
        for (i = 1; i <= lim; i++)
        {
            var x = document.createElement("b");
            x.className = cn + i;
            x.style.backgroundColor = color;
            d.appendChild(x);
        }
        el.insertBefore(d, el.firstChild);
    },
    addBottom:function (el, bk, color, size)
    {
        var i;
        var d = document.createElement("b");
        var cn = "ck_r";
        var lim = 4;
        if (size && size == "small")
        {
            cn = "ck_rs";
            lim = 2
        }
        d.className = "ck_rbottom";
        d.style.backgroundColor = bk;
        for (i = lim; i > 0; i--)
        {
            var x = document.createElement("b");
            x.className = cn + i;
            x.style.backgroundColor = color;
            d.appendChild(x);
        }
        el.appendChild(d, el.firstChild);
    }
}
