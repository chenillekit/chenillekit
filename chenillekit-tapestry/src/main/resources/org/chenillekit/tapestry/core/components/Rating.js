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
 * The "class" definition for the JS component properties of a
 * graphical slider widget.
 *
 * Depends on the prototype library provided by T5.
 */
var RatingField = Class.create();

RatingField.prototype = {

    selectedImagePath: null,
    unselectedImagePath: null,

    /**
     * pseudo constructor initializes object props
     */
    initialize: function(divId, selectedImagePath, unselectedImagePath)
    {

        this.selectedImagePath = selectedImagePath;
        this.unselectedImagePath = unselectedImagePath;
        this.values = new Array();

        var choiceBlocks = $(divId).getElementsByClassName('ck_rating_choice');
        for (var i = 0; i < choiceBlocks.length; i++)
        {
            var div = choiceBlocks[i];
            var input = div.getElementsBySelector('input')[0];
            var label = div.getElementsBySelector('label')[0];
            var value = new RatingChoice(div, input, label, this);
            this.values.push(value);
        }

		// init the images
        this.change();
    },

    change: function()
    {
        var selectedIndex = -1
        for (var i = this.values.length - 1; i >= 0; i--)
        {
            this.values[i].refresh();
            if (this.values[i].selected())
                selectedIndex = i;
            if (selectedIndex > i)
                this.values[i].refresh(true);
        }
    }

}


var RatingChoice = Class.create();

/**
 * Representing the div, input, label, and image for a rating choice.
 */
RatingChoice.prototype = {

    initialize: function(div, input, label, parent)
    {
        this.div = div;
        this.input = input;
        this.label = label;
        this.parent = parent;

        this.image = document.createElement('img');

        this.div.appendChild(this.image);
        this.refresh();

        Event.observe(this.input, 'change', function()
        {
            // call out to the parent obj
            this.parent.change();
        }.bindAsEventListener(this));

        Event.observe(this.image, 'mouseup', function()
        {
            this.input.checked = true;
            this.parent.change();
        }.bindAsEventListener(this));

    },

    selected: function()
    {
        return this.input.checked;
    },

    /**
     * Determines if this value has been selected and updates the image
     * value accordingly.
     */
    refresh: function(force)
    {
        if (this.selected() || force)
        {
            this.image.src = this.parent.selectedImagePath;
        }
        else
        {
            this.image.src = this.parent.unselectedImagePath;
        }
    }
}
