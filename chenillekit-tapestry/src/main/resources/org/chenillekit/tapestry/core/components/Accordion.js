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

Ck.Accordion = Class.create();
Ck.Accordion.prototype = {
    initialize: function(container)
    {
        if (!$(container))
            throw(container + " doesn't exist!");

        this.container = $(container);
        this.currentAccordion = null;

        this.options = Object.extend({
            duration: 0.2
        }, arguments[1] || {});

        var accordions = $A(this.container.getElementsByClassName('ck_accordionToggle'));
        accordions.each(function(accordion)
        {
            Event.observe(accordion, 'click', this.activate.bindAsEventListener(this), false);
        }, this);
    },

    //
    //  de-/activate an accordion
    //
    activate : function(myEvent)
    {
        var accordion = Event.element(myEvent);
        var content = accordion.next();

        if (this.currentAccordion != null)
        {
            var currentAccordionContent = this.currentAccordion.next();
            if (this.currentAccordion != accordion)
                Effect.SlideUp(currentAccordionContent, {duration: this.options.duration, queue: 'end'});
        }

        if (this.currentAccordion == accordion)
            Effect.SlideUp(content, {duration: this.options.duration});
        else
            Effect.SlideDown(content, {duration: this.options.duration});


        this.currentAccordion = (this.currentAccordion == accordion ? null : accordion)
    }
}
