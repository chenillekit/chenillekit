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

    initialize: function(panelId, closed, options)
    {
        this.panelId = panelId;
        this.subject = this.panelId + "_subject";
        this.content = this.panelId + "_content";
        this.options = options;

        if (!closed)
            $(this.content).style.display = 'block';

        Event.observe($(this.subject), 'click', this.toggle.bind(this), false);
    },
    toggle: function (theEvent)
    {
        Effect.toggle($(this.content), 'slide', this.options);
    }
}
