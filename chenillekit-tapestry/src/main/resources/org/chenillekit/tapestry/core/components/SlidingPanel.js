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

if (typeof Prototype == 'undefined')
    throw("slidingpanel.js requires including prototype.js library");

Ck.SlidingPanel = Class.create();
Ck.SlidingPanel.prototype =
{
    subjectClassName : 'ck_slidingPanelSubject',

    initialize: function(panel, closed)
    {
        this.panel = $(panel);
        this.subject = this.panel.getElementsByClassName(this.subjectClassName)[0];
        this.content = this.subject.next().cleanWhitespace();

        if (!closed)
            this.content.style.display = 'block';

        Event.observe(this.subject, 'click', this.toggle.bindAsEventListener(this));
    },
    toggle: function (theEvent)
    {
        Effect.toggle(this.content, 'slide');
    }
}
