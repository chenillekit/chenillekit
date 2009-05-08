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

var SidePanel = new Class.create();
SidePanel.prototype =
{
    initialize: function(panelId, sizeElement)
    {
        this.panelId = $(panelId).cleanWhitespace();
        this.menuPanel = this.panelId.getElementsByClassName('tap5c_sidepanel-panel')[0];
        this.menuContent = this.panelId.getElementsByClassName('tap5c_sidepanel-content')[0];
        this.pinnerBar = this.panelId.getElementsByClassName('tap5c_sidepanel-pinner-bar')[0];
        this.pinner = this.panelId.getElementsByClassName('tap5c_sidepanel-pinner')[0];
        this.panelWidth = this.panelId.getWidth();
        this.panelPoxX = this.panelId.offsetLeft;
        this.isVisible = false;
        this.isPinned = (Ck.Cookie.get(panelId) == 'pinned');
        this.pinnerBar.style.width = this.menuContent.getWidth() + "px";
        this.pinner.style.marginLeft = this.menuContent.getWidth() - 20 + "px";

        this.toggglePinner();

        if (this.isPinned)
        {
            this.menuContent.style.display = 'block';
            this.isVisible = true;
        }

        if (sizeElement != 'null')
        {
            this.panelId.style.height = $(sizeElement).getHeight() + "px";
            this.menuContent.style.height = $(sizeElement).getHeight() + "px";
        }

        Event.observe(this.menuPanel, "click", this.toggle.bindAsEventListener(this));
        Event.observe(this.pinner, "click", this.pinningContent.bindAsEventListener(this));
        Event.observe(document, "keyup", this.doToggle.bindAsEventListener(this));
    },
    toggglePinner:function()
    {
        if (this.isPinned)
            this.pinner.style.backgroundPosition = '0 -135px';
        else
            this.pinner.style.backgroundPosition = '0 -150px';
    },
    pinningContent: function(e)
    {
        if (!this.isPinned)
            Ck.Cookie.set(this.panelId.id, 'pinned', 7);
        else
            Ck.Cookie.set(this.panelId.id, '', 7);

        this.isPinned = !this.isPinned;
        this.toggglePinner();
    },
    doToggle: function(e)
    {
        if (e.altKey && e.keyCode == '49')
        {
            this.toggle(e);
            Event.stop(e);
        }
        if (e.altKey && e.keyCode == '50')
        {
            this.pinningContent(e);
            Event.stop(e);
        }
    },
    toggle
            :
            function(e)
            {
                if (this.menuContent.style.display != 'none')
                    this.close(e)
                else
                    this.open(e)
            }
    ,
    close: function(e)
    {
        if (this.menuContent.style.display != 'none')
        {
            this.menuContent.style.display = 'none';
//            Effect.BlindUpFromRight(this.menuContent);
            this.isVisible = false;
        }
    }
    ,
    open: function(e)
    {
        if (this.menuContent.style.display == 'none')
        {
            this.menuContent.style.display = 'block';
//            Effect.BlindDownFromRight(this.menuContent);
            this.isVisible = true;
        }
    }
}

Effect.BlindUpFromRight = function(element)
{
    element = $(element);
    element.makeClipping();
    return new Effect.Scale(element, 0,
            Object.extend({ scaleContent: false,
                scaleY: false,
                restoreAfterFinish: true,
                afterFinishInternal: function(effect)
                {
                    effect.element.hide();
                    effect.element.undoClipping();
                }
            }, arguments[1] || {})
            );
}

Effect.BlindDownFromRight = function(element)
{
    element = $(element);
    var elementDimensions = element.getDimensions();
    return new Effect.Scale(element, 100, Object.extend({
        scaleContent: false,
        scaleY: false,
        scaleFrom: 0,
        scaleMode: {originalHeight: elementDimensions.height, originalWidth: elementDimensions.width},
        restoreAfterFinish: true,
        afterSetup: function(effect)
        {
            effect.element.makeClipping();
            effect.element.setStyle({width: '0px'});
            effect.element.show();
        },
        afterFinishInternal: function(effect)
        {
            effect.element.undoClipping();
        }
    }, arguments[1] || {}));
}

