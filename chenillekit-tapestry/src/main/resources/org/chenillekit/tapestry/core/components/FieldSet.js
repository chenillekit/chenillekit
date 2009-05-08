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

/**
 * closeable fieldset
 */
Ck.FieldSet = Class.create();
Ck.FieldSet.prototype = {

    contentClass : 'ck_fieldset_content',

    initialize: function(elementId, closed)
    {
        this.fieldset = $(elementId).cleanWhitespace();
        this.fieldSetChild = this.fieldset.getElementsByClassName(this.contentClass)[0].cleanWhitespace();
        this.closed = closed;

        if (this.closed)
            this.fieldSetChild.style.display = 'none';

        this.fieldSetLegend = this.fieldset.getElementsByTagName('legend')[0];
        Event.observe(this.fieldSetLegend, "click", this.toggle.bindAsEventListener(this));
    },
    toggle: function(theEvent)
    {
        Effect.toggle(this.fieldSetChild, 'blind');
    }
}