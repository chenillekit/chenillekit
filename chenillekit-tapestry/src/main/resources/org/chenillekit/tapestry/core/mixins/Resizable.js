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

/*
 *    script.aculo.us resizable-1.1.js, Sat Dec 22th 2007
 *
 *    Orginal: http://script.aculo.us/
 *
 *    Scriptaculous extension 2007 "Vasil Popovski" => vas_popovski@hotmail.com
 *    Extension based on Scriptaculous dragdrop.js by Thomas Fuchs
 *
 *  This extenssion is freely distributable under the terms of an MIT-style license.
 */

var Resizables = {
    resizers: [],
    observers: [],

    register: function(resizable)
    {
        if (this.resizers.length == 0)
        {
            this.eventMouseUp = this.endResize.bindAsEventListener(this);
            this.eventMouseMove = this.updateResize.bindAsEventListener(this);
            this.eventKeypress = this.keyPress.bindAsEventListener(this);

            Event.observe(document, "mouseup", this.eventMouseUp);
            Event.observe(document, "mousemove", this.eventMouseMove);
            Event.observe(document, "keypress", this.eventKeypress);
        }
        this.resizers.push(resizable);
    },

    unregister: function(resizable)
    {
        this.resizers = this.resizers.reject(function(r)
        {
            return r == resizable
        });
        if (this.resizers.length == 0)
        {
            Event.stopObserving(document, "mouseup", this.eventMouseUp);
            Event.stopObserving(document, "mousemove", this.eventMouseMove);
            Event.stopObserving(document, "keypress", this.eventKeypress);
        }
    },

    activate: function(resizable)
    {
        if (resizable.options.delay)
        {
            this._timeout = setTimeout(function()
            {
                Resizables._timeout = null;
                window.focus();
                Resizables.activeResizable = resizable;
            }.bind(this), resizable.options.delay);
        }
        else
        {
            window.focus(); // allows keypress events if window isn't currently focused, fails for Safari
            this.activeResizable = resizable;
        }
    },
    deactivate: function()
    {
        this.activeResizable = null;
    },
    updateResize: function(event)
    {
        if (!this.activeResizable) return;
        var pointer = [Event.pointerX(event), Event.pointerY(event)];
        if (this._lastPointer && (this._lastPointer.inspect() == pointer.inspect())) return;
        this._lastPointer = pointer;
        this.activeResizable.updateResize(event, pointer);
    },
    endResize: function(event)
    {
        if (this._timeout)
        {
            clearTimeout(this._timeout);
            this._timeout = null;
        }
        if (!this.activeResizable) return;
        this._lastPointer = null;
        this.activeResizable.endResize(event);
        this.activeResizable = null;
    },

    keyPress: function(event)
    {
        if (this.activeResizable)
            this.activeResizable.keyPress(event);
    },

    addObserver: function(observer)
    {
        this.observers.push(observer);
        this._cacheObserverCallbacks();
    },

    removeObserver: function(element)
    {  // element instead of observer fixes mem leaks
        this.observers = this.observers.reject(function(o)
        {
            return o.element == element
        });
        this._cacheObserverCallbacks();
    },

    notify: function(eventName, resizable, event)
    {  // 'onStart', 'onEnd', 'onDrag'
        if (this[eventName + 'Count'] > 0)
            this.observers.each(function(o)
            {
                if (o[eventName]) o[eventName](eventName, resizable, event);
            });
        if (resizable.options[eventName]) resizable.options[eventName](resizable, event);
    },

    _cacheObserverCallbacks: function()
    {
        ['onStart','onEnd','onResize'].each(function(eventName)
        {
            Resizables[eventName + 'Count'] = Resizables.observers.select(
                    function(o)
                    {
                        return o[eventName];
                    }
                    ).length;
        });
    }
}

var Resizable = Class.create();
Resizable._resizing = {};

Resizable.prototype = {
    initialize: function(element)
    {
        var defaults = {
            handle: false,

            endeffect: function(element)
            {
                var toOpacity = typeof element._opacity == 'number' ? element._opacity : 1.0;
                new Effect.Opacity(element, {duration:0.2, from:0.7, to:toOpacity,
                    queue: {scope:'_resizable', position:'end'},
                    afterFinish: function()
                    {
                        Resizable._resizing[element] = false
                    }
                });
            },
            zindex: 1000,
            revert: false,
            snap: false,  // false, or xy or [x,y] or function(x,y){ return [x,y] }
            delay: 0,
            persist: true
        };

        if (!arguments[1] || typeof arguments[1].endeffect == 'undefined')
            Object.extend(defaults, {
                starteffect: function(element)
                {
                    element._opacity = Element.getOpacity(element);
                    Resizable._resizing[element] = true;
                    new Effect.Opacity(element, {duration:0.2, from:element._opacity, to:0.7});
                }
            });

        var options = Object.extend(defaults, arguments[1] || {});
        this.element = $(element);
        this.element.style.margin = 0;

        if (options.handle && (typeof options.handle == 'string'))
            this.handle = this.element.down('.' + options.handle, 0);

        if (!this.handle) this.handle = $(options.handle);
        if (!this.handle) this.handle = this.element;

        this.handle.style.width = this.element.getWidth() + "px";
//        this.element.style.marginBottom = "5px";
        //        Element.makePositioned(this.handle); // fix IE

        Element.makePositioned(this.element); // fix IE
        this.delta = this.currentDelta();
        this.options = options;
        this.resizing = false;

        this.eventMouseDown = this.initResize.bindAsEventListener(this);
        Event.observe(this.handle, "mousedown", this.eventMouseDown);

        Resizables.register(this);
    },
    reverteffect: function(element, horizontal, vertical)
    {
        var horiz = this._edim[0] - horizontal;
        var vert = this._edim[1] - vertical;
        new Effect.ReSize(element, {direction:'vert', amount:vert});
        new Effect.ReSize(element, {direction:'horizontal', amount:horiz});
    },

    destroy: function()
    {
        Event.stopObserving(this.handle, "mousedown", this.eventMouseDown);
        Resizables.unregister(this);
    },

    currentDelta: function()
    {
        return([
            parseInt(Element.getStyle(this.element, 'left') || '0'),
            parseInt(Element.getStyle(this.element, 'top') || '0')]);
    },
    initResize: function(event)
    {
        if (typeof Resizable._resizing[this.element] != 'undefined' &&
            Resizable._resizing[this.element]) return;
        if (Event.isLeftClick(event))
        {
            // abort on form elements, fixes a Firefox issue
            //            var src = Event.element(event);
            //            if ((tag_name = src.tagName.toUpperCase()) && (
            //                    tag_name == 'INPUT' ||
            //                    tag_name == 'SELECT' ||
            //                    tag_name == 'OPTION' ||
            //                    tag_name == 'BUTTON' ||
            //                    tag_name == 'TEXTAREA')) return;

            var pointer = [Event.pointerX(event), Event.pointerY(event)];
            this._initialX = pointer[0];
            this._initialY = pointer[1];
            var dim = Element.getDimensions(this.element);

            this._edim = [dim.width,dim.height];
            this._min = [1,1];
            this._max = [0,0];
            var pos = Position.cumulativeOffset(this.element);
            this.offset = [0,1].map(function(i)
            {
                return (pointer[i] - pos[i])
            });

            if (this.options.preserveRatio)
            {
                this._ratio = dim.width / dim.height;
            }

            if (this.options.bind || this.options.flip)
            {
                this._parentDim = Element.getDimensions(this.element.parentNode);
                this._cop = Position.cumulativeOffset(this.element.parentNode);
                this._coe = Position.cumulativeOffset(this.element);
                this.elementOffset = [this._coe[0] - this._cop[0], this._coe[1] - this._cop[1]];
                if (this.options.flip)
                {
                    var margins = [parseInt(Element.getStyle(this.element, 'margin-left')) || "0",
                        parseInt(Element.getStyle(this.element, 'margin-top')) || "0"];

                    this.element.setStyle({overflow:'hidden'});
                    this._mc = [(this._edim[0] - (this._initialX - (this._coe[0]))),
                        (this._edim[1] - (this._initialY - (this._coe[1])))];
                    this._inf = Position.positionedOffset(this.element);
                //ie fix:
                    this._inf[0] -= this.element.parentNode != document.body ? this._cop[0] : 0;
                    this._inf[1] -= this.element.parentNode != document.body ? this._cop[1] : 0;
                }
            }
            if (this.options.min)
            {
                if (this.options.min instanceof Array)
                {
                    this._min = this._min.map(function(v, i)
                    {
                        return (this.options.min[i] > 0 ? this.options.min[i] : 1);
                    }.bind(this));
                }
                else
                    this._min = this._min.map(function(v, i)
                    {
                        return (this.options.min > 0 ? this.options.min : 1);
                    }.bind(this));
            }
            if (this.options.max)
            {
                if (this.options.max instanceof Array)
                {
                    this._max = this._max.map(function(v, i)
                    {
                        return (this.options.max[i] >= this._min[i]) ? this.options.max[i] : 0;
                    }.bind(this));
                }
                else
                    this._max = this._max.map(function(v, i)
                    {
                        return (this.options.max >= this._min[i]) ? this.options.max : 0;
                    }.bind(this));
            }

            Resizables.activate(this);
            Event.stop(event);
        }
    },

    startResize: function(event)
    {
        this.resizing = true;
        if (this.options.zindex)
        {
            this.originalZ = parseInt(Element.getStyle(this.element, 'z-index') || 0);
            this.element.style.zIndex = this.options.zindex;
        }
        if (this.options.ghosting)
        {
            // If element has margin-left/right/top/bottom set all sort of problems occurs regarding the elements starting position
            // and final position especially in IE (more problems when also snap and ghosting are initiated together)
            // (similar happens in Draggable - might need fixing there as well??)
            // next few lines SOLVE the problem (works for every combination: position:absolute+offset || relative+margins etc.)
            this._clone = this.element.cloneNode(true);
            this.element.parentNode.insertBefore(this._clone, this.element);
            var style = this._clone.style;
            Position.absolutize(this._clone);
      // partial IE margin fix (if another element is below it in IE it looses its position i.e. offset on bottom is reset)
            if (navigator.appName.indexOf('Microsoft') != -1 && parseInt(Element.getStyle(this.element, 'margin-top')) > 0)
            {
                this.element.style.top = style.marginTop;
            }
      //
            style.margin = '0px';
        }
        Resizables.notify('onStart', this, event);

        if (this.options.starteffect) this.options.starteffect(this.element);
    },
    updateResize: function(event, pointer)
    {
        if (!this.resizing) this.startResize(event);

        Resizables.notify('onResize', this, event);
        this.draw(pointer);

        this.handle.style.width = this.element.getWidth() + "px";

        if (this.options.change) this.options.change(this);

        Event.stop(event);
    },

    finishResize: function(event, success)
    {
        this.resizing = false;
        if (this.options.ghosting)
        {
            if (navigator.appName.indexOf('Microsoft') != -1 && parseInt(Element.getStyle(this.element, 'margin-top')) > 0)
                this.element.style.top = this._clone.style.marginTop;
            Element.remove(this._clone);
            this._clone = null;
        }
        Resizables.notify('onEnd', this, event);
        var revert = this.options.revert;
        if (revert && typeof revert == 'function') revert = revert(this.element);

        if (revert && this.reverteffect)
        {
            var dim = Element.getDimensions(this.element);
            this.reverteffect(this.element, dim.width, dim.height);//d[1]-this.delta[1], d[0]-this.delta[0]
        }
        if (this.options.zindex)
            this.element.style.zIndex = this.originalZ;

        if (this.options.endeffect)
            this.options.endeffect(this.element);

        if (this.options.persist)
        {
            var lastDim = Element.getDimensions(this.element);
            Cookie.set(this.element.id + ".width", lastDim.width, 7);
            Cookie.set(this.element.id + ".height", lastDim.height, 7);
        }

        Resizables.deactivate(this);
    },

    keyPress: function(event)
    {
        if (event.keyCode != Event.KEY_ESC) return;
        this.finishResize(event, false);
        Event.stop(event);
    },

    endResize: function(event)
    {
        if (!this.resizing) return;
        this.finishResize(event, true);
        Event.stop(event);
    },

    draw: function(point)
    {
        var pos = Position.cumulativeOffset(this.element);
        var d = this.currentDelta();
        pos[0] -= d[0];
        pos[1] -= d[1];

        var p = [0,1].map(function(i)
        {
            return (point[i] - pos[i] - this.offset[i])
        }.bind(this));

        var l_width = p[0] + this._edim[0] - d[0];
        var l_height = p[1] + this._edim[1] - d[1];

        p[0] = (l_width > this._min[0]) ? l_width : this._min[0];
        p[1] = (l_height > this._min[1]) ? l_height : this._min[1];

        if (this.options.snap)
        {
            if (typeof this.options.snap == 'function')
            {
                p = this.options.snap(p[0], p[1], this);
            }
            else
            {
                if (this.options.snap instanceof Array)
                {
                    p = p.map(function(v, i)
                    {
                        // IF Javascript alert activated in IE throws error if one of the snap values is 0 : [20,0]
                        // or if this map functions returns 0 for i-th element
                        // Same happens in Draggable (needs to be patched??)
                        var dim = Math.round(v / this.options.snap[i]) * this.options.snap[i];
                        return (this.options.snap[i] > 0) ? ((dim > this._min[i]) ? dim : this._min[i]) : this._edim[i];
                    }.bind(this))
                }
                else
                {
                    p = p.map(function(v, i)
                    {
                        var dim = Math.round(v / this.options.snap) * this.options.snap - d[i];
                        return (this.options.snap > 0) ? ((dim > this._min[i]) ? dim : this._min[i]) : this._edim[i]
                    }.bind(this))
                }
            }
        }

        if (this.options.bind)
        {
            if (this._parentDim.width <= p[0] + this.elementOffset[0])
                p[0] = this._parentDim.width - this.elementOffset[0] - 2;
            if (this._parentDim.height <= p[1] + this.elementOffset[1])
                p[1] = this._parentDim.height - this.elementOffset[1] - 2;
        }

        if (this.options.min)
        {
            p[0] = p[0] > this._min[0] ? p[0] : this._min[0];
            p[1] = p[1] > this._min[1] ? p[1] : this._min[1];
        }
        if (this.options.max)
        {
            p[0] = p[0] < this._max[0] ? p[0] : (this._max[0] > 0 ? this._max[0] : p[0]);
            p[1] = p[1] < this._max[1] ? p[1] : (this._max[1] > 0 ? this._max[1] : p[1]);
        }

        var style = this.element.style;
        if (this.options.flip)
        {
            var r1 = (point[0] + this._mc[0]);
            var r2 = (point[1] + this._mc[1]);
            if (r1 <= this._coe[0])
            {
                style.left = r1 - this._cop[0] + "px";//this._mc[0]
                p[0] = this._coe[0] - this.element.offsetLeft;
            }
            else
                style.left = this._inf[0] + 'px';
            if (r2 <= this._coe[1])
            {
                style.top = r2 - this._cop[1] + "px";//this._mc[0]
                p[1] = this._coe[1] - this.element.offsetTop;
            }
            else
                style.top = this._inf[1] + 'px';
        }

        if (this.options.preserveRatio)
        {
            p[0] = this._ratio * p[1];
        }

        if ((!this.options.constraint) || (this.options.constraint == 'horizontal'))
        {
            style.width = p[0] + "px";
        }
        if ((!this.options.constraint) || (this.options.constraint == 'vertical'))
        {
            style.height = p[1] + "px";
        }
        if (style.visibility == "hidden") style.visibility = ""; // fix gecko rendering
    }
}

// script.aculo.us EffectResize.js

// Copyright(c) 2007 - Frost Innovation AS, http://ajaxwidgets.com
//
// EffectResize.js is freely distributable under the terms of an MIT-style license.
// For details, see the script.aculo.us web site: http://script.aculo.us/

/* Helper Effect for resizing elements...
 */
Effect.ReSize = Class.create();
Object.extend(Object.extend(Effect.ReSize.prototype, Effect.Base.prototype), {
    initialize: function(element)
    {
        this.element = element;
        if (!this.element) throw(Effect._elementDoesNotExistError);
        var options = Object.extend({ amount: 100, direction: 'vert', toSize:null }, arguments[1] || {});
        if (options.direction == 'vert')
            this.originalSize = options.originalSize || parseInt(this.element.style.height);
        else
            this.originalSize = options.originalSize || parseInt(this.element.style.width);

        if (options.toSize != null)
            options.amount = options.toSize - this.originalSize;

        this.start(options);
    },
    setup: function()
    {
        // Prevent executing on elements not in the layout flow
        if (this.element.getStyle('display') == 'none')
            this.cancel();
    },
    update: function(position)
    {
        if (this.options.direction == 'vert')
            this.element.setStyle({height: this.originalSize + (this.options.amount * position) + 'px'});
        else
            this.element.setStyle({width: this.originalSize + (this.options.amount * position) + 'px'});
    },
    finish: function()
    {
        if (this.options.direction == 'vert')
            this.element.setStyle({height: this.originalSize + this.options.amount + 'px'});
        else
            this.element.setStyle({width: this.originalSize + this.options.amount + 'px'});
    }
});