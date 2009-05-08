/*  Prototype-UI, version trunk
 *
 *  Prototype-UI is freely distributable under the terms of an MIT-style license.
 *  For details, see the PrototypeUI web site: http://www.prototype-ui.com/
 *
 *--------------------------------------------------------------------------*/

if(typeof Prototype == 'undefined' || !Prototype.Version.match("1.6"))
  throw("Prototype-UI library require Prototype library >= 1.6.0");

(function(p) {
  var b = p.Browser, n = navigator;

  if (b.WebKit) {
    b.WebKitVersion = parseFloat(n.userAgent.match(/AppleWebKit\/([\d\.\+]*)/)[1]);
    b.Safari2 = (b.WebKitVersion < 420);
  }

  if (b.IE) {
    b.IEVersion = parseFloat(n.appVersion.split(';')[1].strip().split(' ')[1]);
    b.IE6 = b.IEVersion == 6;
    b.IE7 = b.IEVersion == 7;
  }

  p.falseFunction = function() { return false };
  p.trueFunction  = function() { return true  };
})(Prototype);

/*
Namespace: UI

  Introduction:
    Prototype-UI is a library of user interface components based on the Prototype framework.
    Its aim is to easilly improve user experience in web applications.

    It also provides utilities to help developers.

  Guideline:
    - Prototype conventions are followed
    - Everything should be unobstrusive
    - All components are themable with CSS stylesheets, various themes are provided

  Warning:
    Prototype-UI is still under deep development, this release is targeted to developers only.
    All interfaces are subjects to changes, suggestions are welcome.

    DO NOT use it in production for now.

  Authors:
    - Sébastien Gruhier, <http://www.xilinus.com>
    - Samuel Lebeau, <http://gotfresh.info>
*/

var UI = {
  Abstract: { },
  Ajax: { }
};
Object.extend(Class.Methods, {
  extend: Object.extend.methodize(),

  addMethods: Class.Methods.addMethods.wrap(function(proceed, source) {
    // ensure we are not trying to add null or undefined
    if (!source) return this;

    // no callback, vanilla way
    if (!source.hasOwnProperty('methodsAdded'))
      return proceed(source);

    var callback = source.methodsAdded;
    delete source.methodsAdded;
    proceed(source);
    callback.call(source, this);
    source.methodsAdded = callback;

    return this;
  }),

  addMethod: function(name, lambda) {
    var methods = {};
    methods[name] = lambda;
    return this.addMethods(methods);
  },

  method: function(name) {
    return this.prototype[name].valueOf();
  },

  classMethod: function() {
    $A(arguments).flatten().each(function(method) {
      this[method] = (function() {
        return this[method].apply(this, arguments);
      }).bind(this.prototype);
    }, this);
    return this;
  },

  // prevent any call to this method
  undefMethod: function(name) {
    this.prototype[name] = undefined;
    return this;
  },

  // remove the class' own implementation of this method
  removeMethod: function(name) {
    delete this.prototype[name];
    return this;
  },

  aliasMethod: function(newName, name) {
    this.prototype[newName] = this.prototype[name];
    return this;
  },

  aliasMethodChain: function(target, feature) {
    feature = feature.camelcase();

    this.aliasMethod(target+"Without"+feature, target);
    this.aliasMethod(target, target+"With"+feature);

    return this;
  }
});
Object.extend(Number.prototype, {
  // Snap a number to a grid
  snap: function(round) {
    return parseInt(round == 1 ? this : (this / round).floor() * round);
  }
});
/*
Interface: String

*/

Object.extend(String.prototype, {
  camelcase: function() {
    var string = this.dasherize().camelize();
    return string.charAt(0).toUpperCase() + string.slice(1);
  },

  /*
    Method: makeElement
      toElement is unfortunately already taken :/

      Transforms html string into an extended element or null (when failed)

      > '<li><a href="#">some text</a></li>'.makeElement(); // => LI href#
      > '<img src="foo" id="bar" /><img src="bar" id="bar" />'.makeElement(); // => IMG#foo (first one)

    Returns:
      Extended element

  */
  makeElement: function() {
    var wrapper = new Element('div'); wrapper.innerHTML = this;
    return wrapper.down();
  }
});
Object.extend(Array.prototype, {
  /**
   * Array#isEmpty() -> Boolean
   * Convenient method to check wether or not array is empty
   * returns: true if array is empty, false otherwise
   **/
  isEmpty: function() {
    return !this.length;
  },

  /**
   * Array#at(index) -> Object
   * Returns the element at the given index or undefined if index is out of range.
   * A negative index counts from the end.
   **/
  at: function(index) {
    return this[index < 0 ? this.length + index : index];
  },

  /**
   * Array#removeAt(index) -> Object | undefined
   * Deletes item at the given index, which may be negative
   * returns: deleted item or undefined if index is out of range
   **/
  removeAt: function(index) {
    if (-index > this.length) return;
    return this.splice(index, 1)[0];
  },

  /**
   * Array#removeIf(iterator[, context]) -> Array
   * Deletes items for which iterator returns a truthy value, bound to optional context
   * returns: array of items deleted
   **/
  removeIf: function(iterator, context) {
    for (var i = this.length - 1, objects = [ ]; i >= 0; i--)
      if (iterator.call(context, this[i], i))
        objects.push(this.removeAt(i));
    return objects.reverse();
  },

  /**
   * Array#remove(object) -> Number
   * Deletes items that are identical to given object
   * returns: number of items deleted
   **/
  remove: function(object) {
    return this.removeIf(function(member) { return member === object }).length;
  },

  /**
   * Array#insert(index, object[, ...])
   * Inserts the given objects before the element with the given index (which may be negative)
   * returns: this
   **/
  insert: function(index) {
    if (index > this.length)
      this.length = index;
    else if (index < 0)
      index = this.length + index + 1;

    this.splice.apply(this, [ index, 0 ].concat($A(arguments).slice(1)));
    return this;
  }
});

// backward compatibility
Array.prototype.empty = Array.prototype.isEmpty;
Element.addMethods({
  getScrollDimensions: function(element) {
    element = $(element);
    return {
      width:  element.scrollWidth,
      height: element.scrollHeight
    }
  },

  getScrollOffset: function(element) {
    element = $(element);
    return Element._returnOffset(element.scrollLeft, element.scrollTop);
  },

  setScrollOffset: function(element, offset) {
    element = $(element);
    if (arguments.length == 3)
      offset = { left: offset, top: arguments[2] };
    element.scrollLeft = offset.left;
    element.scrollTop  = offset.top;
    return element;
  },

  // returns "clean" numerical style (without "px") or null if style can not be resolved
  // or is not numeric
  getNumStyle: function(element, style) {
    var value = parseFloat($(element).getStyle(style));
    return isNaN(value) ? null : value;
  },

  // with courtesy of Tobie Langel
  //   (http://tobielangel.com/2007/5/22/prototype-quick-tip)
  appendText: function(element, text) {
    element = $(element);
    element.appendChild(document.createTextNode(String.interpret(text)));
    return element;
  }
});

document.whenReady = (function() {
  var queue = [ ];

  document.observe('dom:loaded', function() {
    queue.invoke('call', document);
    queue.clear();
    document.whenReady = function(callback) { callback.bind(document).defer() };
  });

  return function(callback) { queue.push(callback) };
})();

Object.extend(document.viewport, {
  // Alias this method for consistency
  getScrollOffset: document.viewport.getScrollOffsets,

  setScrollOffset: function(offset) {
    Element.setScrollOffset(Prototype.Browser.WebKit ? document.body : document.documentElement, offset);
  },

  getScrollDimensions: function() {
    return Element.getScrollDimensions(Prototype.Browser.WebKit ? document.body : document.documentElement);
  }
});

document.whenReady(function() {
  window.$head = $(document.getElementsByTagName('head')[0]);
  window.$body = $(document.body);
});
/*
Interface: UI.Options
  Mixin to handle *options* argument in initializer pattern.

  TODO: find a better example than Circle that use an imaginary Point function,
        this example should be used in tests too.

  It assumes class defines a property called *options*, containing
  default options values.

  Instances hold their own *options* property after a first call to <setOptions>.

  Example:
    > var Circle = Class.create(UI.Options, {
    >
    >   // default options
    >   options: {
    >     radius: 1,
    >     origin: Point(0, 0)
    >   },
    >
    >   // common usage is to call setOptions in initializer
    >   initialize: function(options) {
    >     this.setOptions(options);
    >   }
    > });
    >
    > var circle = new Circle({ origin: Point(1, 4) });
    >
    > circle.options
    > // => { radius: 1, origin: Point(1,4) }

  Accessors:
    There are builtin methods to automatically write options accessors. All those
    methods can take either an array of option names nor option names as arguments.
    Notice that those methods won't override an accessor method if already present.

     * <optionsGetter> creates getters
     * <optionsSetter> creates setters
     * <optionsAccessor> creates both getters and setters

    Common usage is to invoke them on a class to create accessors for all instances
    of this class.
    Invoking those methods on a class has the same effect as invoking them on the class prototype.
    See <classMethod> for more details.

    Example:
    > // Creates getter and setter for the "radius" options of circles
    > Circle.optionsAccessor('radius');
    >
    > circle.setRadius(4);
    > // 4
    >
    > circle.getRadius();
    > // => 4 (circle.options.radius)

  Inheritance support:
    Subclasses can refine default *options* values, after a first instance call on setOptions,
    *options* attribute will hold all default options values coming from the inheritance hierarchy.
*/

(function() {
  UI.Options = {
    methodsAdded: function(klass) {
      klass.classMethod($w(' setOptions allOptions optionsGetter optionsSetter optionsAccessor '));
    },

    // Group: Methods

    /*
      Method: setOptions
        Extends object's *options* property with the given object
    */
    setOptions: function(options) {
      if (!this.hasOwnProperty('options'))
        this.options = this.allOptions();

      this.options = Object.extend(this.options, options || {});
    },

    /*
      Method: allOptions
        Computes the complete default options hash made by reverse extending all superclasses
        default options.

        > Widget.prototype.allOptions();
    */
    allOptions: function() {
      var superclass = this.constructor.superclass, ancestor = superclass && superclass.prototype;
      return (ancestor && ancestor.allOptions) ?
          Object.extend(ancestor.allOptions(), this.options) :
          Object.clone(this.options);
    },

    /*
      Method: optionsGetter
        Creates default getters for option names given as arguments.
        With no argument, creates getters for all option names.
    */
    optionsGetter: function() {
      addOptionsAccessors(this, arguments, false);
    },

    /*
      Method: optionsSetter
        Creates default setters for option names given as arguments.
        With no argument, creates setters for all option names.
    */
    optionsSetter: function() {
      addOptionsAccessors(this, arguments, true);
    },

    /*
      Method: optionsAccessor
        Creates default getters/setters for option names given as arguments.
        With no argument, creates accessors for all option names.
    */
    optionsAccessor: function() {
      this.optionsGetter.apply(this, arguments);
      this.optionsSetter.apply(this, arguments);
    }
  };

  // Internal
  function addOptionsAccessors(receiver, names, areSetters) {
    names = $A(names).flatten();

    if (names.empty())
      names = Object.keys(receiver.allOptions());

    names.each(function(name) {
      var accessorName = (areSetters ? 'set' : 'get') + name.camelcase();

      receiver[accessorName] = receiver[accessorName] || (areSetters ?
        // Setter
        function(value) { return this.options[name] = value } :
        // Getter
        function()      { return this.options[name]         });
    });
  }
})();
/*
  Class: UI.Dock
    EXPERIMENTAL.

    Creates a dock with a fisheye effect from an element.

    Assumptions:
      - Element is a UL, items are LI elements.
      - Images are IMG markups inside LI items
      - LI can contain label elements, which match a given selector (see <labelsSelector> option)

    Example:
      > new UI.Dock('dock', { hideLabels: true });

    Original source code from Safalra (Stephen Morley)
      http://www.safalra.com/web-design/javascript/mac-style-dock/.
    This is a Prototype "port"


*/
UI.Dock = Class.create(UI.Options, {

  // Group: Options
  options: {
    // Property: maxItemSize
    //   maximum size in pixel of images when magnified, default is 96.
    maxItemSize: 96,

    // Property: range
    //   number of items the magnify effect affects, default is 2.
    range: 2,

    // Property: hideLabels
    //   a boolean, if set to true labels are only visible when mouse is over, default is false.
    hideLabels: false,

    // Property: labelsSelector
    //   CSS3 selector to select labels element, default is ".label".
    labelsSelector: '.label'
  },

  initialize: function(element, options) {
    this.element = $(element);
    this.setOptions(options);

    this.scale = 0;
    this.create();
  },

  create: function() {
    this.createSchedulers();
    this.parseItems();
    this.observeElement();

    if (this.options.hideLabels)
      this.items.pluck('label').invoke('hide');

    this.options.itemSize = this.options.itemSize || this.items.first().size;
    var offset = this.options.maxItemSize - this.options.itemSize;

    this.items.pluck('element').invoke('setStyle', {
      top: "-"+offset+"px",
      position: "relative" }, this);

    this.element.style.height = this.options.itemSize + "px";
    this.redrawItems();
  },

  parseItems: function() {
    var selector = this.options.labelsSelector;

    this.items = this.element.select('LI').collect(function(LI, i) {
      LI._dockPosition = i;
      return {
        element: LI,
        image:   LI.down('img'),
        size:    parseInt(LI.down('img').readAttribute('width')),
        label:   LI.down(selector)
      }
    });
  },

  findEventItem: function(event) {
    var element = event.findElement('LI');
    return element && this.items[element._dockPosition];
  },

  createSchedulers: function() {
    this.magnifyScheduler = new PeriodicalExecuter(this.magnifyStep.bind(this), 0.01);
    this.magnifyScheduler.stop();
    this.closeScheduler = new PeriodicalExecuter(this.closeStep.bind(this), 0.01);
    this.closeScheduler.stop();
  },

  onMouseOver: function(event){
    var item = this.findEventItem(event);
    if (!item) return;

    if (this.options.hideLabels)
      this.shownLabel = item.label.show();
  },

  onMouseMove: function(event) {
    this.magnify();

    var item = this.findEventItem(event);
    if (!item) return;

    var index  = this.items.indexOf(item),
        across = (event.layerX || event.offsetX) / this.items[index].size;

    if (!across) return;

    this.items.each(function(item, i) {
      item.size = this.itemSize + (((i < index - this.range) || (i > index + this.range)) ? 0 :
        ((this.maxItemSize - this.itemSize) * (Math.cos(i - index - across + 0.5) + 1) / 2).ceil());
    }, this.options);

    this.redrawItems();
  },

  onMouseOut: function(event){
    if (this.closeTimeout || this.closeScheduler.timer)
      return;

    this.closeTimeout = this.close.bind(this).delay(0.05);

    if (this.options.hideLabels)
      this.shownLabel.hide();
  },

  magnify: function() {
    if (this.closeTimeout) {
      window.clearTimeout(this.closeTimeout);
      this.closeTimeout = false;
    }

    this.closeScheduler.stop();

    if (this.scale != 1 && !this.magnifyScheduler.timer)
      this.magnifyScheduler.registerCallback();
  },

  close: function() {
    this.closeTimeout = false;
    this.magnifyScheduler.stop();
    this.closeScheduler.registerCallback();
  },

  magnifyStep: function(scheduler){
    if (this.scale < 1) this.scale += 0.125;
    else {
      this.scale = 1;
      scheduler.stop();
    }
    this.redrawItems();
  },

  closeStep: function(scheduler){
    if (this.scale > 0) this.scale -= 0.125;
    else {
      this.scale = 0;
      scheduler.stop();
    }
    this.redrawItems();
  },

  observeElement: function() {
    this.element.observe('mouseover', this.onMouseOver.bind(this))
                .observe('mousemove', this.onMouseMove.bind(this))
                .observe('mouseout',  this.onMouseOut.bind(this));
  },

  redrawItems: function(){
    var itemSize  = this.options.itemSize,
        maxSize   = this.options.maxItemSize,
        totalSize = 0;

    this.items.each(function(item) {
      var size = itemSize + this.scale * (item.size - itemSize),
          image = item.image;
      image.setAttribute('width', size);
      image.setAttribute('height', size);
      image.style.marginTop = maxSize - size + 'px';
      if (item.label)
        item.label.style.width = size + 'px';
      totalSize += size;
    }, this);

    this.element.style.width = totalSize + 'px';
  }
});
/*
  Class: UI.IframeShim
    Handles IE6 bug when <select> elements overlap other elements with higher z-index

  Example:
    > // creates iframe and positions it under "contextMenu" element
    > this.iefix = new UI.IframeShim().positionUnder('contextMenu');
    > ...
    > document.observe('click', function(e) {
    >   if (e.isLeftClick()) {
    >     this.contextMenu.hide();
    >
    >     // hides iframe when left click is fired on a document
    >     this.iefix.hide();
    >   }
    > }.bind(this))
    > ...
*/

// TODO:
//
// Maybe it makes sense to bind iframe to an element
// so that it automatically calls positionUnder method
// when the element it's binded to is moved or resized
// Not sure how this might affect overall perfomance...

UI.IframeShim = Class.create(UI.Options, {

  options: {
    parent: document.body
  },

  /*
    Method: initialize
    Constructor

      Creates iframe shim and appends it to the body.
      Note that this method does not perform proper positioning and resizing of an iframe.
      To do that use positionUnder method

    Returns:
      this
  */
  initialize: function(options) {
    this.setOptions(options);
    this.element = new Element('iframe', {
      style: 'position:absolute;filter:progid:DXImageTransform.Microsoft.Alpha(opacity=1);',
      src: 'javascript:false;',
      frameborder: 0
    });
    $(this.options.parent || document.body).insert(this.element);
  },

  /*
    Method: hide
      Hides iframe shim leaving its position and dimensions intact

    Returns:
      this
  */
  hide: function() {
    this.element.hide();
    return this;
  },

  /*
    Method: show
      Show iframe shim leaving its position and dimensions intact

    Returns:
      this
  */
  show: function() {
    this.element.show();
    return this;
  },

  /*
    Method: positionUnder
      Positions iframe shim under the specified element
      Sets proper dimensions, offset, zIndex and shows it
      Note that the element should have explicitly specified zIndex

    Returns:
      this
  */
  positionUnder: function(element) {
    var element = $(element),
        offset = element.cumulativeOffset(),
        dimensions = element.getDimensions(),
        style = {
          left: offset[0] + 'px',
          top: offset[1] + 'px',
          width: dimensions.width + 'px',
          height: dimensions.height + 'px',
          zIndex: element.getStyle('zIndex') - 1
        };
    this.element.setStyle(style).show();

    return this;
  },

  /*
    Method: setBounds
      Sets element's width, height, top and left css properties using 'px' as units

    Returns:
      this
  */
  setBounds: function(bounds) {
    for (prop in bounds) {
      bounds[prop] = parseInt(bounds[prop]) + 'px';
    }
    this.element.setStyle(bounds);
    return this;
  },

  /*
    Method: setSize
      Sets element's width, height

    Returns:
      this
  */
  setSize: function(width, height) {
    this.element.style.width  = parseInt(width) + "px";
    this.element.style.height = parseInt(height) + "px";
    return this;
  },

  /*
    Method: setPosition
      Sets element's top and left

    Returns:
      this
  */
  setPosition: function(top, left) {
    this.element.style.top  = parseInt(top) + "px";
    this.element.style.left = parseInt(left) + "px";
    return this;
  },

  /*
    Method: destroy
      Completely removes the iframe shim from the document

    Returns:
      this
  */
  destroy: function() {
    if (this.element)
      this.element.remove();

    return this;
  }
});
/*
  Group: Drag
    UI provides Element#enableDrag method that allow elements to fire drag-related events.

    Events fired:
      - drag:started : fired when a drag is started (mousedown then mousemove)
      - drag:updated : fired when a drag is updated (mousemove)
      - drag:ended   : fired when a drag is ended (mouseup)

    Notice it doesn't actually move anything, drag behavior has to be implemented
    by attaching handlers to drag events.

    Drag-related informations:
      event.memo contains useful information about the drag occuring:
        - dx         : difference between pointer x position when drag started
                       and actual x position
        - dy         : difference between pointer y position when drag started
                       and actual y position
        - mouseEvent : the original mouse event, useful to know pointer absolute position,
                       or if key were pressed.

    Example, with event handling for a specific element:

    > // Now "resizable" will fire drag-related events
    > $('resizable').enableDrag();
    >
    > // Let's observe them
    > $('resizable').observe('drag:started', function(event) {
    >   this._dimensions = this.getDimensions();
    > }).observe('drag:updated', function(event) {
    >   var drag = event.memo;
    >
    >   this.setStyle({
    >     width:  this._dimensions.width  + drag.dx + 'px',
    >     height: this._dimensions.height + drag.dy + 'px'
    >   });
    > });

    Example, with event delegating on the whole document:

    > // All elements in the having the "draggable" class name will fire drag events.
    > $$('.draggable').invoke('enableDrag');
    >
    > document.observe('drag:started', function(event) {
    >   UI.logger.info('trying to drag ' + event.element().id);
    > }):
*/

Element.addMethods({
  enableDrag: function(element) {
    return $(element).writeAttribute('draggable');
  },

  disableDrag: function(element){
    return $(element).writeAttribute('draggable', null);
  },

  isDraggable: function(element) {
    return $(element).hasAttribute('draggable');
  }
});

(function() {
  var initPointer, draggedElement;

  document.observe('mousedown', function(event) {
    if (draggedElement = findDraggable(event.element())) {
      // prevent default browser action to avoid selecting text for instance
      event.preventDefault();
      initPointer = event.pointer();

      document.observe('mousemove', startDrag);
      document.observe('mouseup',   cancelDrag);
    }
  });

  function findDraggable(element) {
    while (element && element !== document) {
      if (element.hasAttribute('draggable')) return element;
      element = $(element.parentNode);
    }
  };

  function startDrag(event) {
    document.stopObserving('mousemove', startDrag)
            .stopObserving('mouseup',   cancelDrag)
            .observe('mousemove', drag)
            .observe('mouseup',   endDrag);

    fire('drag:started', event);
  };

  function cancelDrag(event) {
    document.stopObserving('mousemove', startDrag)
            .stopObserving('mouseup',   cancelDrag);
  };

  function drag(event) {
    fire('drag:updated', event);
  };

  function endDrag(event) {
    document.stopObserving('mousemove', drag)
            .stopObserving('mouseup',   endDrag);

    fire('drag:ended', event);
  };

  function fire(eventName, event) {
    var pointer = event.pointer();

    draggedElement.fire(eventName, {
      dx: pointer.x - initPointer.x,
      dy: pointer.y - initPointer.y,
      mouseEvent: event
    });
  };
})();

/*
Namespace: CSS

  Utility functions for CSS/StyleSheet files access

  Authors:
    - Sébastien Gruhier, <http://www.xilinus.com>
    - Samuel Lebeau, <http://gotfresh.info>
*/

var CSS = (function() {
  // Code based on:
  //   - IE5.5+ PNG Alpha Fix v1.0RC4 (c) 2004-2005 Angus Turnbull http://www.twinhelix.com
  //   - Whatever:hover - V2.02.060206 - hover, active & focus (c) 2005 - Peter Nederlof * Peterned - http://www.xs4all.nl/~peterned/
  function fixPNG() {
   parseStylesheet.apply(this, $A(arguments).concat(fixRule));
  };

  function parseStylesheet() {
    var patterns = $A(arguments);
    var method = patterns.pop();

    // To avoid flicking background
    // if (Prototype.Browser.IE)
    //   document.execCommand("BackgroundImageCache", false, true);
    // Parse all document stylesheets
    var styleSheets = $A(document.styleSheets);
    if (patterns.length > 0) {
      styleSheets = styleSheets.select(function(css) {
        return patterns.any(function(pattern) {
          return css.href && css.href.match(pattern)
          });
      });
    }
    styleSheets.each(function(styleSheet) {fixStylesheet.call(this, styleSheet, method)});
  };

  // Fixes a stylesheet
  function fixStylesheet(stylesheet, method) {
    // Parse import files
    if (stylesheet.imports)
      $A(stylesheet.imports).each(fixStylesheet);

    var href = stylesheet.href || document.location.href;
    var docPath = href.substr(0, href.lastIndexOf('/'));
	  // Parse all CSS Rules
    $A(stylesheet.rules || stylesheet.cssRules).each(function(rule) { method.call(this, rule, docPath) });
  };

  var filterPattern = 'progid:DXImageTransform.Microsoft.AlphaImageLoader(src="#{src}",sizingMethod="#{method}")';

  // Fixes a rule if it has a PNG background
  function fixRule(rule, docPath) {
    var bgImg = rule.style.backgroundImage;
    // Rule with PNG background image
    if (bgImg && bgImg != 'none' && bgImg.match(/^url[("']+(.*\.png)[)"']+$/i)) {
      var src = RegExp.$1;
      var bgRepeat = rule.style.backgroundRepeat;
      // Relative path
      if (src[0] != '/')
        src = docPath + "/" + src;
      // Apply filter
      rule.style.filter = filterPattern.interpolate({
        src:    src,
        method: bgRepeat == "no-repeat" ? "crop" : "scale" });
      rule.style.backgroundImage = "none";
    }
  };

  var preloadedImages = new Hash();

  function preloadRule(rule, docPath) {
    var bgImg = rule.style.backgroundImage;
    if (bgImg && bgImg != 'none'  && bgImg != 'initial' ) {
      if (!preloadedImages.get(bgImg)) {
        bgImg.match(/^url[("']+(.*)[)"']+$/i);
        var src = RegExp.$1;
        // Relative path
        if (!(src[0] == '/' || src.match(/^file:/) || src.match(/^https?:/)))
          src = docPath + "/" + src;
        preloadedImages.set(bgImg, true);
        var image = new Image();
        image.src = src;
      }
    }
  }

  return {
    /*
       Method: fixPNG
         Fix transparency of PNG background of document stylesheets.
         (only on IE version<7, otherwise does nothing)

         Warning: All png background will not work as IE filter use for handling transparency in PNG
         is not compatible with all background. It does not support top/left position (so no CSS sprite)

         I recommend to create a special CSS file with png that needs to be fixed and call CSS.fixPNG on this CSS

         Examples:
          > CSS.fixPNG() // To fix all css
          >
          > CSS.fixPNG("mac_shadow.css") // to fix all css files with mac_shadow.css so mainly only on file
          >
          > CSS.fixPNG("shadow", "vista"); // To fix all css files with shadow or vista in their names

       Parameters
         patterns: (optional) list of pattern to filter css files
    */
    fixPNG: (Prototype.Browser.IE && Prototype.Browser.IEVersion < 7) ? fixPNG : Prototype.emptyFunction,

    // By Tobie Langel (http://tobielangel.com)
    //   inspired by http://yuiblog.com/blog/2007/06/07/style/
    addRule: function(css) {
      var style = new Element('style', { type: 'text/css', media: 'screen' });
      $head.insert(style);
      if (style.styleSheet) style.styleSheet.cssText = css;
      else style.appendText(css);
      return style;
    },

    preloadImages: function() {
      // Does not work with FF3!!
      if (navigator.userAgent.match(/Firefox\/3/))
        return;

      parseStylesheet.apply(this, $A(arguments).concat(preloadRule));
    }
  };
})();
/*
  Interface: Date
  author: Minho Kim
*/
Object.extend(Date.prototype, {
  addDays: function(days) {
    return new Date(this.getFullYear(), this.getMonth(), this.getDate() + days, this.getHours(), this.getMinutes(), this.getSeconds(), this.getMilliseconds());
  },

  succ: function() {
    return this.addDays(1);
  },

  firstOfMonth: function() {
    return new Date(this.getFullYear(), this.getMonth(), 1);
  },

  endOfMonth: function() {
    return new Date(this.getFullYear(), this.getMonth() + 1, 0);
  },

  getDayOfYear: function() {
    return Math.ceil((this - new Date(this.getFullYear(), 0, 1)) / 86400000);
  },

  strftime: function(grammar) {
    var parts = { }, i18n = Date.default_i18n;
    var lambda = function(date, part) {
      switch (part) {
      // date
        case 'a': return i18n.WEEKDAYS_MEDIUM[date.getDay()];
        case 'A': return i18n.WEEKDAYS[date.getDay()];
        case 'b':
        case 'h': return i18n.MONTHS_SHORT[date.getMonth()];
        case 'B': return i18n.MONTHS[date.getMonth()];
        case 'C': return Math.floor(date.getFullYear() / 100);
        case 'd': return date.getDate().toPaddedString(2);
        case 'e': return date.getDate();
        case 'j': return date.getDayOfYear();
        case 'm': return (date.getMonth()+1).toPaddedString(2);
        case 'u': return date.getDay() || 7;
        case 'w': return date.getDay();
        case 'y': return date.getFullYear().toString().substring(2);
        case 'Y': return date.getFullYear();

        // time
        case 'H': return date.getHours().toPaddedString(2);
        case 'I': return (date.getHours() % 12).toPaddedString(2);
        case 'M': return date.getMinutes().toPaddedString(2);
        case 'p': return date.getHours() < 12 ? 'am' : 'pm';
        case 'S': return date.getSeconds().toPaddedString(2);

        // static
        case 'n': return '\n';
        case 't': return '\t';

        // combined
        case 'D': return date.strftime('%m/%d/%y');
        case 'r': return date.strftime('%I:%M:%S %p'); // time in a.m. and p.m. notation
        case 'R': return date.strftime('%H:%M:%S'); // time in 24 hour notation
        case 'T': return date.strftime('%H:%M:%S'); // current time, equal to %H:%M:%S

        // locale
        case 'c': return date.strftime(i18n.FORMAT_DATETIME);
        case 'x': return date.strftime(i18n.FORMAT_DATE);
        case 'X': return date.strftime(i18n.FORMAT_TIME);
      }
    };
    grammar.scan(/\w+/, function(e){
      var part = e.first();
      parts[part] = lambda(this, part);
    }.bind(this));
    return grammar.interpolate(parts, Date.STRFT_GRAMMER);
  },

  equalsDate: function(other) {
    return (this.getMonth() == other.getMonth() && this.getDate() == other.getDate() && this.getFullYear() == other.getFullYear());
  }
});

Object.extend(Date, {
  STRFT_GRAMMER : /(^|.|\r|\n)(\%(\w+))/,

  default_i18n: {
    MONTHS_SHORT: $w('Jan Feb Mar Apr May Jun Jul Aug Sep Oct Nov Dec'),
    MONTHS: $w('January February March April May June July August September October November December'),
    WEEKDAYS_MEDIUM: $w('Sun Mon Tue Wed Thu Fri Sat'),
    WEEKDAYS: $w('Sunday Monday Tuesday Wednesday Thursday Friday Saturday'),
    FORMAT_DATE: '%m/%d/%Y',
    FORMAT_TIME: '%H:%M:%S',
    FORMAT_DATETIME: '%x %X'
  },

  parseString: function(dateString, format) {
    var date = new Date(), i18n = Date.default_i18n;

    format=format.replace('%D','%m/%d/%y');
    format=format.replace('%T','%H:%M:%S').replace('%r','%I:%M:%S %p').replace('%R','%H:%M:%S');
    format=format.replace('%c',i18n.FORMAT_DATETIME).replace('%x',i18n.FORMAT_DATE).replace('%X',i18n.FORMAT_TIME);

    var tokens = format.match(/%./g);

    // the regex /\W+/ does not work for utf8 chars
    dateString.split(/[^A-Za-z0-9\u00A1-\uFFFF]+/).each(function(e, i) {
      switch (tokens[i]) {
        case '%a':
        case '%A':
        case '%u':
        case '%w': break;

        case '%b':
        case '%h': date.setMonth(i18n.MONTHS_SHORT.indexOf(e)); break;
        case '%B': date.setMonth(i18n.MONTHS.indexOf(e)); break;
        case '%C': break; //century
        case '%d':
        case '%e': date.setDate(parseInt(e,10)); break;
        case '%j': break; // day of year
        case '%m': date.setMonth(parseInt(e,10)-1); break;
        case '%w': date.setDay(parseInt(e,10)); break;
        case '%y':
          var year = parseInt(e,10);
          if (year<50)  year+=2000;
          if (year<100) year+=1900;
          date.setYear(year);
          break;
        case '%Y': date.setFullYear(parseInt(e,10)); break;

        // time
        case '%H': date.setHours(parseInt(e,10)); break;
        case '%I': date.setHours(parseInt(e,10)); break;
        case '%M': date.setMinutes(parseInt(e,10)); break;
        case '%p': if(e=='pm') date.setHours(date.getHours()+12); break;
        case '%S': date.setSeconds(parseInt(e,10)); break;
      }
    });
    return date;
  }
});
UI.Benchmark = {
  benchmark: function(lambda, iterations) {
    var date = new Date();
    (iterations || 1).times(lambda);
    return (new Date() - date) / 1000;
  }
};
/*
  Group: Drag
    UI provides Element#enableDrag method that allow elements to fire drag-related events.

    Events fired:
      - drag:started : fired when a drag is started (mousedown then mousemove)
      - drag:updated : fired when a drag is updated (mousemove)
      - drag:ended   : fired when a drag is ended (mouseup)

    Notice it doesn't actually move anything, drag behavior has to be implemented
    by attaching handlers to drag events.

    Drag-related informations:
      event.memo contains useful information about the drag occuring:
        - dx         : difference between pointer x position when drag started
                       and actual x position
        - dy         : difference between pointer y position when drag started
                       and actual y position
        - mouseEvent : the original mouse event, useful to know pointer absolute position,
                       or if key were pressed.

    Example, with event handling for a specific element:

    > // Now "resizable" will fire drag-related events
    > $('resizable').enableDrag();
    >
    > // Let's observe them
    > $('resizable').observe('drag:started', function(event) {
    >   this._dimensions = this.getDimensions();
    > }).observe('drag:updated', function(event) {
    >   var drag = event.memo;
    >
    >   this.setStyle({
    >     width:  this._dimensions.width  + drag.dx + 'px',
    >     height: this._dimensions.height + drag.dy + 'px'
    >   });
    > });

    Example, with event delegating on the whole document:

    > // All elements in the having the "draggable" class name will fire drag events.
    > $$('.draggable').invoke('enableDrag');
    >
    > document.observe('drag:started', function(event) {
    >   UI.logger.info('trying to drag ' + event.element().id);
    > }):
*/

Element.addMethods({
  enableDrag: function(element) {
    return $(element).writeAttribute('draggable');
  },

  disableDrag: function(element){
    return $(element).writeAttribute('draggable', null);
  },

  isDraggable: function(element) {
    return $(element).hasAttribute('draggable');
  }
});

(function() {
  var initPointer, draggedElement;

  document.observe('mousedown', function(event) {
    if (draggedElement = findDraggable(event.element())) {
      // prevent default browser action to avoid selecting text for instance
      event.preventDefault();
      initPointer = event.pointer();

      document.observe('mousemove', startDrag);
      document.observe('mouseup',   cancelDrag);
    }
  });

  function findDraggable(element) {
    while (element && element !== document) {
      if (element.hasAttribute('draggable')) return element;
      element = $(element.parentNode);
    }
  };

  function startDrag(event) {
    document.stopObserving('mousemove', startDrag)
            .stopObserving('mouseup',   cancelDrag)
            .observe('mousemove', drag)
            .observe('mouseup',   endDrag);

    fire('drag:started', event);
  };

  function cancelDrag(event) {
    document.stopObserving('mousemove', startDrag)
            .stopObserving('mouseup',   cancelDrag);
  };

  function drag(event) {
    fire('drag:updated', event);
  };

  function endDrag(event) {
    document.stopObserving('mousemove', drag)
            .stopObserving('mouseup',   endDrag);

    fire('drag:ended', event);
  };

  function fire(eventName, event) {
    var pointer = event.pointer();

    draggedElement.fire(eventName, {
      dx: pointer.x - initPointer.x,
      dy: pointer.y - initPointer.y,
      mouseEvent: event
    });
  };
})();
/*
  UI.State
    This helper allows to use back/forward browser buttons to switch between states and
    to bookmark an application in a given state. It uses fragment part of URL to store state name.
    A state is just a simple string, basic examples are "previewing", "editing" for a composition
    application.

    Use UI.State.change('previewing') to switch to the 'previewing' state.
    Use document.observe('state:changed', callback) to detect state changes.
    Inside the callback, event.memo.value gives the state name.

    On page load, state:changed is fired if the fragment part of the URL is not blank.
    If users gets back to the initial state, state:changed is fired with empty string as value.

    This is a simplified (and still buggy) version of YUI Browser History Manager.
    (http://yuiblog.com/blog/2007/02/21/browser-history-manager/)

    Seems to work in Safari 2/3, IE6, Firefox 1.5/2
    Opera 9.5 is now supported thanks to Terence Johnson.
    UI.State.isSupported is now the true function !!

    Warning: You should need to wait for document to be loaded before using this helper.
    Using document.whenReady is a good way to ensure this.

    FIXME: Classic anchors with hash (<a href="#top"> for instance) triggers state:changed on all
    browsers but IE, therefore using those anchors is NOT the good way to switch between states.

    Example:
      A simple REST catalog that works without Javascript.

      <div id="products_menu">
        <a href="/products/14-mug">Mug</a>
        <a href="/products/15-chair">Chair</a>
      </div>

      <div id="content">
        <!-- selected product -->
      </div>


      Server side: at route "/products[/id]"
        if javascript is wanted?
          render javascript instructions to replace $('content') with
          current product (if id is present) or index
        else
          render product (if id is present) or index in html within catalog layout
        end

      Javascript side:
        document.observe('state:changed', function(event) {
          var fragment = event.memo.value;
          // Back to initial (index) state
          if (fragment.empty()) new Ajax.Request('/products');
          // If fragment matches a product, request this product
          if (fragment.match(/\d+-\w+/)) new Ajax.Request('/products/'+fragment);
        });

        document.observe('mousedown', function(event) {
          var link = event.findElement('#products_menu a');
          if (link) {
            // prevent browser from following link
            event.stop();
            UI.State.change(link.readAttribute('href').sub('/products/', ''));
          }
        });


*/
(function(browser) {
  var watch, change, changed, fragment;

  // returns fragment part of the URL without "#"
  // "http://someplace.org/path/to#?something" => "something"
  function getFragment() {
    return top.location.hash.substr(1);
  }

  function setFragment(fragment) {
    top.location.hash = fragment || '';
  }

  function fragmentChanged(newFragment) {
    document.fire('state:changed', { value: newFragment, previousValue: fragment });
    fragment = newFragment;
  }

  document.whenReady(function() {
    var initialFragment = getFragment();
    if (initialFragment) fragmentChanged(initialFragment);

    if (browser.IE) {
      var iframe = new Element('iframe', { style: 'display: none' });
      $body.appendChild(iframe);

      var contentWindow = iframe.contentWindow;

      function writeFragmentToIFrame(fragment) {
        var doc = contentWindow.document;
        // opening and closing the document will cause IE to add an history entry
        doc.open();
        doc.write(fragment || '');
        doc.close();
      }

      function readFragmentFromIFrame() {
        return contentWindow.document.body.innerText;
      }

      // this first call wont add an history entry
      writeFragmentToIFrame(fragment);

      watch = function() {
        var newFragment = readFragmentFromIFrame();
        if (newFragment != fragment) {
          setFragment(newFragment);
          fragmentChanged(newFragment);
        }
      };

      change = function(newFragment) {
        if (newFragment != fragment) writeFragmentToIFrame(newFragment);
      };

    } else {
      // watching history.length is only useful for WebKit
      var counter = history.length, fragments = [ ];

      watch = function() {
        var newFragment = getFragment(), newCounter = history.length;

        if (newFragment != fragment) {
          fragmentChanged(newFragment);

        } else if (newCounter != counter) {
          fragmentChanged(fragments[newCounter - 1]);
          fragment = newFragment;
        }

        counter = newCounter;
      };

      change = function(newFragment) {
        setFragment(newFragment);

        if (browser.WebKit) {
          fragments[history.length] = newFragment;
        }
      };
    }
  });

  UI.State = {
    change: function(value) {
      change(value);
      changed = true;
    },

    isSupported: Prototype.trueFunction,

    // private method called by Opera < 9.5
    _watch: function() { watch() }
  };

  if (browser.Opera && parseFloat(navigator.appVersion) < 9.5) {
    // moving in history will cause Opera to try to reload this fake image...
    // therefore to call UI.State._watch.
    document.write('<img src="javascript:location.href=\'javascript:UI.State._watch();\';" style="position:absolute; top:-1000px" />');
  }

  setInterval(function() { if (changed) watch() }, 50);

})(Prototype.Browser);
/*
  Class: UI.IframeShim
    Handles IE6 bug when <select> elements overlap other elements with higher z-index

  Example:
    > // creates iframe and positions it under "contextMenu" element
    > this.iefix = new UI.IframeShim().positionUnder('contextMenu');
    > ...
    > document.observe('click', function(e) {
    >   if (e.isLeftClick()) {
    >     this.contextMenu.hide();
    >
    >     // hides iframe when left click is fired on a document
    >     this.iefix.hide();
    >   }
    > }.bind(this))
    > ...
*/

// TODO:
//
// Maybe it makes sense to bind iframe to an element
// so that it automatically calls positionUnder method
// when the element it's binded to is moved or resized
// Not sure how this might affect overall perfomance...

UI.IframeShim = Class.create(UI.Options, {

  options: {
    parent: document.body
  },

  /*
    Method: initialize
    Constructor

      Creates iframe shim and appends it to the body.
      Note that this method does not perform proper positioning and resizing of an iframe.
      To do that use positionUnder method

    Returns:
      this
  */
  initialize: function(options) {
    this.setOptions(options);
    this.element = new Element('iframe', {
      style: 'position:absolute;filter:progid:DXImageTransform.Microsoft.Alpha(opacity=1);',
      src: 'javascript:false;',
      frameborder: 0
    });
    $(this.options.parent || document.body).insert(this.element);
  },

  /*
    Method: hide
      Hides iframe shim leaving its position and dimensions intact

    Returns:
      this
  */
  hide: function() {
    this.element.hide();
    return this;
  },

  /*
    Method: show
      Show iframe shim leaving its position and dimensions intact

    Returns:
      this
  */
  show: function() {
    this.element.show();
    return this;
  },

  /*
    Method: positionUnder
      Positions iframe shim under the specified element
      Sets proper dimensions, offset, zIndex and shows it
      Note that the element should have explicitly specified zIndex

    Returns:
      this
  */
  positionUnder: function(element) {
    var element = $(element),
        offset = element.cumulativeOffset(),
        dimensions = element.getDimensions(),
        style = {
          left: offset[0] + 'px',
          top: offset[1] + 'px',
          width: dimensions.width + 'px',
          height: dimensions.height + 'px',
          zIndex: element.getStyle('zIndex') - 1
        };
    this.element.setStyle(style).show();

    return this;
  },

  /*
    Method: setBounds
      Sets element's width, height, top and left css properties using 'px' as units

    Returns:
      this
  */
  setBounds: function(bounds) {
    for (prop in bounds) {
      bounds[prop] = parseInt(bounds[prop]) + 'px';
    }
    this.element.setStyle(bounds);
    return this;
  },

  /*
    Method: setSize
      Sets element's width, height

    Returns:
      this
  */
  setSize: function(width, height) {
    this.element.style.width  = parseInt(width) + "px";
    this.element.style.height = parseInt(height) + "px";
    return this;
  },

  /*
    Method: setPosition
      Sets element's top and left

    Returns:
      this
  */
  setPosition: function(top, left) {
    this.element.style.top  = parseInt(top) + "px";
    this.element.style.left = parseInt(left) + "px";
    return this;
  },

  /*
    Method: destroy
      Completely removes the iframe shim from the document

    Returns:
      this
  */
  destroy: function() {
    if (this.element)
      this.element.remove();

    return this;
  }
});
/*
Class: UI.Logger
*/

/*
  Group: Logging Facilities
    Prototype UI provides a facility to log message with levels.
    Levels are in order "debug", "info", "warn" and "error".

    As soon as the DOM is loaded, a default logger is present in UI.logger.

    This logger is :
    * an <ElementLogger> if $('log') is present
    * a <ConsoleLogger> if window.console is defined
    * a <MemLogger> otherwise

    See <AbstractLogger> to learn how to use it.

    Example:

    > UI.logger.warn('something bad happenned !');
*/

// Class: AbstractLogger

UI.Abstract.Logger = Class.create({
  /*
    Property: level
      The log level, default value is debug  <br/>
  */
  level: 'debug'
});

(function() {
  /*
    Method: debug
      Logs with "debug" level

    Method: info
      Logs with "info" level

    Method: warn
      Logs with "warn" level

    Method: error
      Logs with "error" level
  */
  var levels = $w(" debug info warn error ");

  levels.each(function(level, index) {
    UI.Abstract.Logger.addMethod(level, function(message) {
      // filter lower level messages
      if (index >= levels.indexOf(this.level))
        this._log({ level: level, message: message, date: new Date() });
    });
  });
})();

/*
  Class: NullLogger
    Does nothing
*/
UI.NullLogger = Class.create(UI.Abstract.Logger, {
  _log: Prototype.emptyFunction
});

/*
  Class: MemLogger
    Logs in memory

    Property: logs
      An array of logs, objects with "date", "level", and "message" properties
*/
UI.MemLogger = Class.create(UI.Abstract.Logger, {
  initialize: function() {
    this.logs = [ ];
  },

  _log: function(log) {
    this.logs.push(log);
  }
});

/*
  Class: ConsoleLogger
    Logs using window.console
*/
UI.ConsoleLogger = Class.create(UI.Abstract.Logger, {
  _log: function(log) {
    console[log.level](log.message);
  }
});

/*
  Class: ElementLogger
    Logs in a DOM element
*/
UI.ElementLogger = Class.create(UI.Abstract.Logger, {
  /*
    Method: initialize
      Constructor, takes a DOM element to log into as argument
  */
  initialize: function(element) {
    this.element = $(element);
  },

  /*
    Property: format
      A format string, will be interpolated with "date", "level" and "message"

      Example:
        > "<p>(#{date}) #{level}: #{message}</p>"
  */
  format: '<p>(<span class="date">#{date}</span>) ' +
              '<span class="level">#{level}</span> : ' +
              '<span class="message">#{message}</span></p>',

  _log: function(log) {
    var entry = this.format.interpolate({
      level:   log.level.toUpperCase(),
      message: log.message.escapeHTML(),
      date:    log.date.toLocaleTimeString()
    });
    this.element.insert({ top: entry });
  }
});

document.whenReady(function() {
  if ($('log'))             UI.logger = new UI.ElementLogger('log');
  else if (window.console)  UI.logger = new UI.ConsoleLogger();
  else                      UI.logger = new UI.MemLogger();
});
UI.PullDown = Class.create(UI.Options, {
  options: {
    className:   '',
    shadow:      false,
    position:    'over',
    cloneWidth:   false,
    beforeShow:   null,
    afterShow:    null,
    beforeUpdate: null,
    afterUpdate:  null,
    afterCreate:  null
  },

  initialize: function(container, options){
    this.setOptions(options);
		this.container = $(container);

    this.element = new Element('div', {
      className: 'UI-widget-dropdown ' + this.options.className,
      style: 'z-index:999999;position:absolute;'
    }).hide();

    if (this.options.shadow)
      this.shadow = new UI.Shadow(this.element, {theme: this.options.shadow}).hide();
    else
      this.iframe = Prototype.Browser.IE ? new UI.IframeShim() : null;

    this.outsideClickHandler = this.outsideClick.bind(this);
    this.placeHandler        = this.place.bind(this);
    this.hideHandler         = this.hide.bind(this);
  },

  destroy: function(){
 		if (this.active)
      this.element.remove();
    this.element = null;
    this.stopObserving();
  },

  /*
    Method: insert
      Inserts a new Element to the PullDown

    Parameters:
      elem  - an DOM element

    Returns:
      this
   */
   insert: function(elem){
     return this.element.insert(elem);
   },

   /*
    Method: place
      Place the PullDown

    Parameters:
      none

    Returns:
     this
  */
  place: function(){
    this.element.clonePosition(this.container, {
      setHeight: false,
      setWidth:  this.options.cloneWidth,
      offsetTop: this.options.position == 'below' ? this.container.offsetHeight : 0
    });

    var w = this.element.getWidth();
    var h = this.element.getHeight();
    var t = parseInt(this.element.style.top);
    var l = parseInt(this.element.style.left);

    if (this.shadow)
      this.shadow.setBounds({top: t, left: l, width: w, height: h});
    if (this.iframe)
      this.iframe.setPosition(t, l).setSize(w, h);
    return this;
  },

  /*
    Method: show
    Show the PullDown

    Parameters:
      event  - (optional) Event fired the show

    Returns:
     this
  */
  show: function(event){
    if (this.active)
        return this;

    this.active = true;

    if (this.options.beforeShow)
        this.options.beforeShow(this);

    this.element.hide();

    if (this.iframe)
			this.iframe.show();

    document.body.insert(this.element);

    if (this.shadow)
      this.shadow.show();
    this.element.show();

    if (this.options.afterShow)
      this.options.afterShow(this);

    document.observe('mousedown',  this.outsideClickHandler);
		Event.observe(window,'scroll', this.placeHandler);
		Event.observe(window,'resize', this.hideHandler);

		return this;
  },

  outsideClick: function(event) {
    if (event.findElement('.UI-widget-dropdown'))
			return;
    //this.hide();
  },

  /*
    Method: hide
      Hide the PullDown

    Returns:
      this
  */
  hide: function(){
		if (this.active) {
      this.active = false;
      if (this.shadow)
        this.shadow.hide();

  		if(this.iframe)
  			this.iframe.hide();

      this.element.remove();

    }
    this.stopObserving();
		return this;
  },

  stopObserving: function() {
		Event.stopObserving(window,'resize', this.hideHandler);
		Event.stopObserving(window,'scroll', this.placeHandler);
    document.stopObserving('click', this.outsideClickHandler);
  }
});
/*
Class: UI.Shadow
  Add shadow around a DOM element. The element MUST BE in ABSOLUTE position.

  Shadow can be skinned by CSS (see mac_shadow.css or drop_shadow.css).
  CSS must be included to see shadow.

  A shadow can have two states: focused and blur.
  Shadow shifts are set in CSS file as margin and padding of shadow_container to add visual information.

  Example:
    > new UI.Shadow("element_id");
*/
UI.Shadow = Class.create(UI.Options, {
  options: {
    theme: "mac_shadow",
    focus: false,
    zIndex: 100,
    withIFrameShim: true
  },

  /*
    Method: initialize
      Constructor, adds shadow elements to the DOM if element is in the DOM.
      Element MUST BE in ABSOLUTE position.

    Parameters:
      element - DOM element
      options - Hashmap of options
        - theme (default: mac_shadow)
        - focus (default: true)
        - zIndex (default: 100)

    Returns:
      this
  */
  initialize: function(element, options) {
    this.setOptions(options);

    this.element = $(element);
    this.create();
    this.iframe = Prototype.Browser.IE && this.options.withIFrameShim ? new UI.IframeShim() : null;

    if (Object.isElement(this.element.parentNode))
      this.render();
  },

  /*
    Method: destroy
      Destructor, removes elements from the DOM
  */
  destroy: function() {
    if (this.shadow.parentNode)
      this.remove();
  },

  // Group: Size and Position
  /*
    Method: setPosition
      Sets top/left shadow position in pixels

    Parameters:
      top -  top position in pixel
      left - left position in pixel

    Returns:
      this
  */
  setPosition: function(top, left) {
    if (this.shadowSize) {
      var shadowStyle = this.shadow.style;
      top =  parseInt(top)  - this.shadowSize.top  + this.shadowShift.top;
      left = parseInt(left) - this.shadowSize.left + this.shadowShift.left;
      shadowStyle.top  = top + 'px';
      shadowStyle.left = left + 'px';
      if (this.iframe)
        this.iframe.setPosition(top, left);
    }
    return this;
  },

  /*
    Method: setSize
      Sets width/height shadow in pixels

    Parameters:
      width  - width in pixel
      height - height in pixel

    Returns:
      this
  */
  setSize: function(width, height) {
    if (this.shadowSize) {
      try {
        var w = Math.max(0, parseInt(width) + this.shadowSize.width - this.shadowShift.width) + "px";
        this.shadow.style.width = w;
        var h = Math.max(0, parseInt(height) - this.shadowShift.height) + "px";

        // this.shadowContents[1].style.height = h;
        this.shadowContents[1].childElements().each(function(e) {e.style.height = h});
        this.shadowContents.each(function(item){ item.style.width = w});
        if (this.iframe)
          this.iframe.setSize(width + this.shadowSize.width - this.shadowShift.width, height + this.shadowSize.height - this.shadowShift.height);
      }
      catch(e) {
        // IE could throw an exception if called to early
      }
    }
    return this;
  },

  /*
    Method: setBounds
      Sets shadow bounds in pixels

    Parameters:
      bounds - an Hash {top:, left:, width:, height:}

    Returns:
      this
  */
  setBounds: function(bounds) {
    return this.setPosition(bounds.top, bounds.left).setSize(bounds.width, bounds.height);
  },

  /*
    Method: setZIndex
      Sets shadow z-index

    Parameters:
      zIndex - zIndex value

    Returns:
      this
  */
  setZIndex: function(zIndex) {
    this.shadow.style.zIndex = zIndex;
    return this;
  },

   // Group: Render
  /*
    Method: show
      Displays shadow

    Returns:
      this
  */
  show: function() {
    this.render();
    this.shadow.show();
    if (this.iframe)
      this.iframe.show();
    return this;
  },

  /*
    Method: hide
      Hides shadow

    Returns:
      this
  */
  hide: function() {
    this.shadow.hide();
    if (this.iframe)
      this.iframe.hide();
    return this;
  },

  /*
    Method: remove
      Removes shadow from the DOM

    Returns:
      this
  */
  remove: function() {
    this.shadow.remove();
    return this;
  },

  // Group: Status
  /*
    Method: focus
      Focus shadow.

      Change shadow shift. Shift values are set in CSS file as margin and padding of shadow_container
      to add visual information of shadow status.

    Returns:
      this
  */
  focus: function() {
    this.options.focus = true;
    this.updateShadow();
    return this;
  },

  /*
    Method: blur
      Blurs shadow.

      Change shadow shift. Shift values are set in CSS file as margin and padding of shadow_container
      to add visual information of shadow status.

    Returns:
      this
  */
  blur: function() {
    this.options.focus = false;
    this.updateShadow();
    return this;
  },

  // Private Functions
  // Adds shadow elements to DOM, computes shadow size and displays it
  render: function() {
    if (this.element.parentNode && !Object.isElement(this.shadow.parentNode)) {
      this.element.parentNode.appendChild(this.shadow);
      this.computeSize();
      this.setBounds(Object.extend(this.element.getDimensions(), this.getElementPosition()));
      this.shadow.show();
    }
    return this;
  },

  // Creates HTML elements without inserting them into the DOM
  create: function() {
    var zIndex = this.element.getStyle('zIndex');
    if (!zIndex)
      this.element.setStyle({zIndex: this.options.zIndex});
    zIndex = (zIndex || this.options.zIndex) - 1;

    this.shadowContents = new Array(3);
    this.shadowContents[0] = new Element("div")
      .insert(new Element("div", {className: "shadow_center_wrapper"}).insert(new Element("div", {className: "n_shadow"})))
      .insert(new Element("div", {className: "shadow_right ne_shadow"}))
      .insert(new Element("div", {className: "shadow_left nw_shadow"}));

    this.shadowContents[1] = new Element("div")
      .insert(new Element("div", {className: "shadow_center_wrapper c_shadow"}))
      .insert(new Element("div", {className: "shadow_right e_shadow"}))
      .insert(new Element("div", {className: "shadow_left w_shadow"}));
    this.centerElements = this.shadowContents[1].childElements();

    this.shadowContents[2] = new Element("div")
      .insert(new Element("div", {className: "shadow_center_wrapper"}).insert(new Element("div", {className: "s_shadow"})))
      .insert(new Element("div", {className: "shadow_right se_shadow"}))
      .insert(new Element("div", {className: "shadow_left sw_shadow"}));

    this.shadow = new Element("div", {className: "shadow_container " + this.options.theme,
                                      style: "position:absolute; top:-10000px; left:-10000px; display:none; z-index:" + zIndex })
      .insert(this.shadowContents[0])
      .insert(this.shadowContents[1])
      .insert(this.shadowContents[2]);
  },

  // Compute shadow size
  computeSize: function() {
    if (this.focusedShadowShift)
      return;
    this.shadow.show();

    // Trick to get shadow shift designed in CSS as padding
    var content = this.shadowContents[1].select("div.c_shadow").first();
    this.unfocusedShadowShift = {};
    this.focusedShadowShift = {};

    $w("top left bottom right").each(function(pos) {this.unfocusedShadowShift[pos] = content.getNumStyle("padding-" + pos) || 0}.bind(this));
    this.unfocusedShadowShift.width  = this.unfocusedShadowShift.left + this.unfocusedShadowShift.right;
    this.unfocusedShadowShift.height = this.unfocusedShadowShift.top + this.unfocusedShadowShift.bottom;

    $w("top left bottom right").each(function(pos) {this.focusedShadowShift[pos] = content.getNumStyle("margin-" + pos) || 0}.bind(this));
    this.focusedShadowShift.width  = this.focusedShadowShift.left + this.focusedShadowShift.right;
    this.focusedShadowShift.height = this.focusedShadowShift.top + this.focusedShadowShift.bottom;

    this.shadowShift = this.options.focus ? this.focusedShadowShift : this.unfocusedShadowShift;

    // Get shadow size
    this.shadowSize  = {top:    this.shadowContents[0].childElements()[1].getNumStyle("height"),
                        left:   this.shadowContents[0].childElements()[1].getNumStyle("width"),
                        bottom: this.shadowContents[2].childElements()[1].getNumStyle("height"),
                        right:  this.shadowContents[0].childElements()[2].getNumStyle("width")};

    this.shadowSize.width  = this.shadowSize.left + this.shadowSize.right;
    this.shadowSize.height = this.shadowSize.top + this.shadowSize.bottom;

    // Remove padding
    content.setStyle("padding:0; margin:0");
    this.shadow.hide();
  },

  // Update shadow size (called when it changes from focused to blur and vice-versa)
  updateShadow: function() {
    this.shadowShift = this.options.focus ? this.focusedShadowShift : this.unfocusedShadowShift;
    var shadowStyle = this.shadow.style, pos  = this.getElementPosition(), size = this.element.getDimensions();

    shadowStyle.top  =  pos.top    - this.shadowSize.top   + this.shadowShift.top   + 'px';
    shadowStyle.left  = pos.left   - this.shadowSize.left  + this.shadowShift.left  + 'px';
    shadowStyle.width = size.width + this.shadowSize.width - this.shadowShift.width + "px";
    var h = size.height - this.shadowShift.height + "px";
    this.centerElements.each(function(e) {e.style.height = h});

    var w = size.width + this.shadowSize.width - this.shadowShift.width+ "px";
    this.shadowContents.each(function(item) { item.style.width = w });
  },

  // Get element position in integer values
  getElementPosition: function() {
    return {top: this.element.getNumStyle("top"), left: this.element.getNumStyle("left")}
  }
});

// Set theme and focus as read/write accessor
document.whenReady(function() { CSS.fixPNG("shadow") });
/*
Class: UI.Window
  Main class to handle windows inside a web page.

  Example:
    > new UI.Window({ theme: 'bluglighting' }).show()
*/


/*
<div class="STitle">Options</div>
*/

UI.Window = Class.create(UI.Options, {
  // Group: Options
  options: {

    // Property: theme
    //   window theme, uses the window manager theme as default
    theme:         null,

    // Property: shadowTheme
    //   window shadow theme, uses the window manager one as default
    //   Only useful if <shadow> options is true, see <UI.Shadow> for details
    shadowTheme:   null,

    // Property: id
    //   id ot the window, generated by default
    id:            null,

    // Property: windowManager
    //   window manager that manages this window,
    //   uses UI.defaultWM as default
    windowManager: null,

    top:           null,
    left:          null,
    width:         200,
    height:        300,
    minHeight:     100,
    minWidth:      200,
    maxHeight:     null,
    maxWidth:      null,
    altitude:      "front",

    // Property: resizable
    //   true by default
    resizable:     true,

    // Property: draggable
    //   true by default
    draggable:     true,

    // Property: wired
    //   draw wires around window when dragged, false by default
    wired:         false,

    // Property: show
    //   Function used to show the window, default is Element.show
    show: Element.show,

    // Property: hide
    //   Function used to hide the window, default is Element.hide.
    hide: Element.hide,

    // Property: superflousEffects
    //   uses superflous effects when resizing or moving window.
    //   it's true if Scriptaculous' Effect is defined, false otherwise
    superflousEffects: !Object.isUndefined(window.Effect),

    // Property: shadow
    //   draw shadow around the window, default is false
    shadow:            false,

    // Property: activeOnClick
    //   When set to true, a click on an blurred window content activates it,
    //   default is true
    activeOnClick:     true,

    // Grid
    gridX:  1,
    gridY:  1,

    // Buttons and actions (false to disable)

    // Property: close
    //   Window method name as string, or false to disable close button
    //   Default is 'destroy'
    close:    'destroy',

    // Property: minimize
    //   Window method name as string, or false to disable minimize button
    //   Default is 'toggleFold'
    minimize: 'toggleFold',

    // Property: maximize
    //   Window method name as string, or false to disable maximize button
    //   Default is 'toggleMaximize'
    maximize: 'toggleMaximize'
  },

  // Group: Attributes

  /*
    Property: id
      DOM id of the window's element

    Property: element
      DOM element containing the window

    Property: windowManager
      Window manager that manages the window

    Property: content
      Window content element

    Property: header
      Window header element

    Property: footer
      Window footer element

    Property: visible
      true if window is visible

    Property: focused
      true if window is focused

    Property: folded
      true if window is folded

    Property: maximized
      true if window is maximized
  */

  /*
    Group: Events
    List of events fired by a window
  */

  /*
    Property: created
      Fired after creating the window

    Property: destroyed
      Fired after destroying the window

    Property: showing
      Fired when showing a window

    Property: shown
      Fired after showing effect

    Property: hiding
      Fired when hiding a window

    Property: hidden
      Fired after hiding effect

    Property: focused
      Fired after focusing the window

    Property: blurred
      Fired after bluring the window

    Property: maximized
      Fired after maximizing the window

    Property: restored
      Fired after restoring the window from its maximized state

    Property: fold
      Fired after unfolding the window

    Property: unfold
      Fired after folding the window

    Property: altitude:changed
      Fired when window altitude has changed (z-index)

    Property: size:changed
      Fired when window size has changed

    Property: position:changed
      Fired when window position has changed

    Property: move:started
      Fired when user has started a moving a window, position:changed are then fired continously

    Property: move:ended
      Fired when user has finished moving a window

    Property: resize:started
      Fired when user has started resizing window, size:changed are then fired continuously

    Property: resize:ended
      Fired when user has finished resizing window

  */

  // Group: Contructor

  /*
    Method: initialize
      Constructor, should not be called directly, it's called by new operator (new Window())
      The window is not open and nothing has been added to the DOM yet

    Parameters:
      options - (Hash) list of optional parameters

    Returns:
      this
  */
  initialize: function(options) {
    this.setOptions(options);
    this.windowManager = this.options.windowManager || UI.defaultWM;
    this.create();
    this.id = this.element.id;
    this.windowManager.register(this);
    this.render();
    if (this.options.activeOnClick)
      this.overlay.setStyle({ zIndex: this.lastZIndex + 1 }).show();
  },

  /*
    Method: destroy
      Destructor, closes window, cleans up DOM and memory
  */
  destroy: function($super) {
    this.hide();
    if (this.centerOptions)
      Event.stopObserving(this.windowManager.scrollContainer, "scroll", this.centerOptions.handler);
    this.windowManager.unregister(this);
    this.fire('destroyed');
  },

  // Group: Event handling

  /*
    Method: fire
      Fires a window custom event automatically namespaced in "window:" (see Prototype custom events).
      The memo object contains a "window" property referring to the window.

    Example:
      > UI.Window.addMethods({
      >   iconify: function() {
      >     // ... your iconifying code here ...
      >     this.fire('iconified');
      >     // chain friendly
      >     return this;
      >   }
      > });
      >
      > document.observe('window:iconified', function(event) {
      >   alert("Window with id " + event.memo.window.id + " has just been iconified");
      > });

    Parameters:
      eventName - an event name
      memo - a memo object

    Returns:
      fired event
  */
  fire: function(eventName, memo) {
    memo = memo || { };
    memo.window = this;
    return (this.savedElement || this.element).fire('window:' + eventName, memo);
  },

   /*
     Method: observe
       Observe a window event with a handler function automatically bound to the window

     Parameters:
       eventName - an event name
       handler - a handler function

     Returns:
       this
  */
  observe: function(eventName, handler) {
    this.element.observe('window:' + eventName, handler.bind(this));
    return this;
  },


  // Group: Actions

  /*
    Method: show
      Opens the window (appends it to the DOM)

    Parameters:
      modal - open the window in a modal mode (default false)

    Returns:
      this
 */
  show: function(modal) {
    if (this.visible) return this;

    this.fire('showing');
    this.effect('show');

    if (modal) {
      this.windowManager.startModalSession(this);
      this.modalSession = true;
    }

    this.addElements();
    this.visible = true;

    new PeriodicalExecuter(function(executer) {
      if (!this.element.visible()) return;
      this.fire('shown');
      executer.stop();
    }.bind(this), 0.1);

    return this;
  },

  /*
    Method: hide
       Hides the window, (removes it from the DOM)

     Returns:
       this
  */
  hide: function() {
    if (!this.visible) return this;

    this.fire('hiding');
    this.effect('hide');

    if (this.modalSession) {
      this.windowManager.endModalSession(this);
      this.modalSession = false;
    }

    this.windowManager.hide(this);

    new PeriodicalExecuter(function(executer) {
      if (this.element.visible()) return;
      this.visible = false;
      this.element.remove();
      this.fire('hidden');
      executer.stop();
    }.bind(this), 0.1);

    return this;
  },

  close: function() {
    return this.action('close');
  },

  /*
    Method: activate
      Brings window to the front and sets focus on it

     Returns:
       this
  */
  activate: function() {
    return this.bringToFront().focus();
  },

  /*
    Method: bringToFront
      Brings window to the front (but does not set focus on it)

     Returns:
       this
  */
  bringToFront: function() {
    return this.setAltitude('front');
  },

  /*
    Method: sendToBack
      Sends window to the back (without changing its focus)

     Returns:
       this
  */
  sendToBack: function() {
    return this.setAltitude('back');
  },

  /*
    Method: focus
      Focuses the window (without bringing window to the front)

     Returns:
       this
  */
  focus: function() {
    if (this.focused) return this;

    this.windowManager.focus(this);
    // Hide the overlay that catch events
    this.overlay.hide();
    // Add focused class name
    this.element.addClassName(this.options.theme + '_focused');

    this.focused = true;
    this.fire('focused');
    return this;
  },

  /*
    Method: blur
      Blurs the window (without changing windows order)

     Returns:
       this
  */
  blur: function() {
    if (!this.focused) return this;

    this.windowManager.blur(this);
    this.element.removeClassName(this.options.theme + '_focused');

    // Show the overlay to catch events
    if (this.options.activeOnClick)
      this.overlay.setStyle({ zIndex: this.lastZIndex + 1 }).show();

    this.focused = false;
    this.fire('blurred');
    return this;
  },

  /*
    Method: maximize
      Maximizes window inside its viewport (managed by WindowManager)
      Makes window take full size of its viewport

     Returns:
       this
  */
  maximize: function() {
    if (this.maximized) return this;

    // Get bounds has to be before  this.windowManager.maximize for IE!! this.windowManager.maximize remove overflow
    // and it breaks this.getBounds()
    var bounds = this.getBounds();
    if (this.windowManager.maximize(this)) {
      this.disableButton('minimize').setResizable(false).setDraggable(false);

      this.activate();
      this.maximized = true;
      this.savedArea = bounds;
      var newBounds = Object.extend(this.windowManager.viewport.getDimensions(), { top: 0, left: 0 });
      this[this.options.superflousEffects && !Prototype.Browser.IE ? "morph" : "setBounds"](newBounds);
      this.fire('maximized');
      return this;
    }
  },

  /*
    Function: restore
      Restores a maximized window to its initial size

     Returns:
       this
  */
  restore: function() {
    if (!this.maximized) return this;

    if (this.windowManager.restore(this)) {
      this[this.options.superflousEffects  && !Prototype.Browser.IE ? "morph" : "setBounds"](this.savedArea);
      this.enableButton("minimize").setResizable(true).setDraggable(true);

      this.maximized = false;
      this.fire('restored');
      return this;
    }
  },

  /*
    Function: toggleMaximize
      Maximizes/Restores window inside it's viewport (managed by WindowManager)

     Returns:
       this
  */
  toggleMaximize: function() {
    return this.maximized ? this.restore() : this.maximize();
  },

  /*
    Function: adapt
      Adapts window size to fit its content

     Returns:
       this
  */
  adapt: function() {
    var dimensions = this.content.getScrollDimensions();
    if (this.options.superflousEffects)
      this.morph(dimensions, true);
    else
      this.setSize(dimensions.width, dimensions.height, true);
    return this;
  },

  /*
    Method: fold
      Folds window content

     Returns:
       this
  */
  fold: function() {
    if (!this.folded) {
      var size = this.getSize(true);
      this.folded = true;
      this.savedInnerHeight = size.height;

      if (this.options.superflousEffects)
        this.morph({ width: size.width, height: 0 }, true);
      else
        this.setSize(size.width, 0, true);

      this.setResizable(false);
      this.fire("fold");
    }
    return this;
  },

  /*
    Method: unfold
      Unfolds window content

     Returns:
       this
  */
  unfold: function() {
    if (this.folded) {
      var size = this.getSize(true);
      this.folded = false;

      if (this.options.superflousEffects)
        this.morph({ width: size.width, height: this.savedInnerHeight }, true);
      else
        this.setSize(size.width, this.savedInnerHeight, true);

      this.setResizable(true);
      this.fire("unfold");
    }
    return this;
  },

  /*
    Method: toggleFold
      Folds/Unfolds window content

     Returns:
       this
  */
  toggleFold: function() {
    return this.folded ? this.unfold() : this.fold();
  },

  /*
    Method: setHeader
      Sets window header, equivalent to this.header.update(...) but allows chaining

     Returns:
       this
  */
  setHeader: function(header) {
    this.header.update(header);
    return this;
  },

  /*
    Method: setContent
      Sets window content, equivalent to this.content.update(...) but allows chaining

     Returns:
       this
  */
  setContent: function(content) {
    this.content.update(content);
    return this;
  },

  /*
    Method: setFooter
      Sets window footer, equivalent to this.footer.update(...) but allows chaining

     Returns:
       this
  */
  setFooter: function(footer) {
    this.footer.update(footer);
    return this;
  },

  /*
    Method: setAjaxContent
      Sets window content using Ajax request

     Parameters:
        url - Ajax URL
        options - Ajax Updater options (see http://prototypejs.org/api/ajax/options and
          http://prototypejs.org/api/ajax/updater)

     Returns:
       this
  */
  setAjaxContent: function(url, options) {
    if (!options)
      options = {};

    // bind all callbacks to the window
    Object.keys(options).each(function(name) {
      if (Object.isFunction(options[name]))
        options[name] = options[name].bind(this);
    }, this);

    var onComplete = options.onComplete;
    options.onComplete = (function(response, json) {
      this.setContent(response.responseText);
      if (Object.isFunction(onComplete)) onComplete(response, json);
    }).bind(this);

    new Ajax.Request(url, options);
    return this;
  },

  // Group: Size and Position

  /*
    Method: getPosition
      Returns top/left position of a window (in pixels)

     Returns:
       an Hash {top:, left:}
  */
  getPosition: function() {
    return { left: this.options.left, top: this.options.top };
  },

  /*
    Method: setPosition
      Sets top/left position of a window (in pixels)

    Parameters
      top:  top position in pixel
      left: left position in pixel

    Returns:
      this
  */
  setPosition: function(top, left) {
    var pos = this.computePosition(top, left);
    this.options.top  = pos.top;
    this.options.left = pos.left;

    var elementStyle  = this.element.style;
    elementStyle.top  = pos.top + 'px';
    elementStyle.left = pos.left + 'px';

    this.fire('position:changed');
    return this;
  },

  /*
    Method: center
      Centers the window within its viewport

    Returns:
      this
  */
  center: function(options) {
    var size          = this.getSize(),
        windowManager = this.windowManager,
        viewport      = windowManager.viewport;
        viewportArea  = viewport.getDimensions(),
        offset        = viewport.getScrollOffset();

    if (options && options.auto) {
      this.centerOptions = Object.extend({ handler: this.recenter.bind(this) }, options);
      Event.observe(this.windowManager.scrollContainer,"scroll", this.centerOptions.handler);
      Event.observe(window,"resize", this.centerOptions.handler);
    }

    options = Object.extend({
      top:  (viewportArea.height - size.height) / 2,
      left: (viewportArea.width  - size.width)  / 2
    }, options || {});

    return this.setPosition(options.top + offset.top, options.left + offset.left);
  },

  /*
    Method: getSize
      Returns window width/height dimensions (in pixels)

    Parameters
      innerSize: returns content size if true, window size if false (defaults to false)

    Returns:
      Hash {width:, height:}
  */
  getSize: function(innerSize) {
    if (innerSize)
      return { width:  this.options.width  - this.borderSize.width,
               height: this.options.height - this.borderSize.height };
    else
      return { width: this.options.width, height: this.options.height };
  },

  /*
    Method: setSize
      Sets window width/height dimensions (in pixels), fires size:changed

    Parameters
      width:  width (in pixels)
      height: height (in pixels)
      innerSize: if true change set content size, else set window size (defaults to false)

    Returns:
      this
  */
  setSize: function(width, height, innerSize) {
    var size = this.computeSize(width, height, innerSize);
    var elementStyle = this.element.style, contentStyle = this.content.style;

    this.options.width  = size.outerWidth;
    this.options.height = size.outerHeight;

    elementStyle.width = size.outerWidth + "px", elementStyle.height = size.outerHeight + "px";
    contentStyle.width = size.innerWidth + "px", contentStyle.height = size.innerHeight + "px";
    this.overlay.style.height = size.innerHeight + "px";

    this.fire('size:changed');
 	  return this;
  },

  /*
    Method: getBounds
      Returns window bounds (in pixels)

    Parameters
      innerSize: returns content size if true, window size otherwise

    Returns:
      an Hash {top:, left:, width:, height:}
  */
  getBounds: function(innerSize) {
    return Object.extend(this.getPosition(), this.getSize(innerSize));
  },

  /*
    Method: setBounds
      Sets window bounds (in pixels), fires position:changed and size:changed

    Parameters
      bounds: Hash {top:, left:, width:, height:} where all values are optional
      innerSize: sets content size if true, window size otherwise

    Returns:
      Hash {top:, left:, width:, height:}
  */
  setBounds: function(bounds, innerSize) {
    return this.setPosition(bounds.top, bounds.left)
               .setSize(bounds.width, bounds.height, innerSize);
  },

  morph: function(bounds, innerSize) {
    bounds = Object.extend(this.getBounds(innerSize), bounds || {});

    if (this.centerOptions && this.centerOptions.auto)
       bounds = Object.extend(bounds, this.computeRecenter(bounds));

    if (innerSize) {
      bounds.width  += this.borderSize.width;
      bounds.height += this.borderSize.height;
    }

    this.animating = true;

    new UI.Window.Effects.Morph(this, bounds, {
      duration: 0.5,
      afterFinish: function() { this.animating = false }.bind(this)
    });

    Object.extend(this.options, bounds);

    return this;
  },

  /*
    Method: getAltitude
      Returns window altitude, an integer between 0 and the number of windows,
      the higher the altitude number - the higher the window position.
  */
  getAltitude: function() {
    return this.windowManager.getAltitude(this);
  },

  /*
    Method: setAltitude
      Sets window altitude, fires 'altitude:changed' if altitude was changed
  */
  setAltitude: function(altitude) {
    if (this.windowManager.setAltitude(this, altitude))
      this.fire('altitude:changed');
    return this;
  },

  /*
    Method: setResizable
      TODO
  */
  setResizable: function(resizable) {
    this.options.resizable = resizable;

    var toggleClassName = (resizable ? 'add' : 'remove') + 'ClassName';

    this.element[toggleClassName]('resizable')
      .select('div:[class*=_sizer]').invoke(resizable ? 'show' : 'hide');
    if (resizable)
      this.createResizeHandles();

    this.element.select('div.se').first()[toggleClassName]('se_resize_handle');

    return this;
  },

  /*
    Method: setDraggable
      TODO
  */
  setDraggable: function(draggable) {
    this.options.draggable = draggable;
    this.element[(draggable ? 'add' : 'remove') + 'ClassName']('draggable');
    return this;
  },

  // Group: Theme
  /*
    Method: getTheme
      Returns window theme name
  */
  getTheme: function() {
    return this.options.theme || this.windowManager.getTheme();
  },

  /*
    Method: setTheme
      Sets window theme
  */
  setTheme: function(theme, windowManagerTheme) {
    this.element.removeClassName(this.getTheme()).addClassName(theme);
    // window has it's own theme
    if (!windowManagerTheme)
      this.options.theme = theme;

    return this;
  },

  /*
    Method: getShadowTheme
      Returns shadow theme name
  */
  getShadowTheme: function() {
    return this.options.shadowTheme || this.windowManager.getShadowTheme();
  }
});

UI.Window.addMethods(UI.Window.Buttons);
UI.Window.addMethods(UI.Window.Shadow);
UI.Window.optionsAccessor($w(" minWidth minHeight maxWidth maxHeight gridX gridY altitude "));
// Private functions for window.js
UI.Window.addMethods({
  style: "position: absolute; top: 0; left: 0; width: 100%; height: 100%; border: 0;",

  action: function(name) {
    var action = this.options[name];
    if (action)
      Object.isString(action) ? this[action]() : action.call(this, this);
  },

  create: function() {
    function createDiv(className, options) {
      return new Element('div', Object.extend({ className: className }, options));
    };

    // Main div
    this.element = createDiv("ui-window " + this.getTheme(), {
      id: this.options.id,
      style: "top:-10000px; left:-10000px"
    });

    // Create HTML window code
    this.header  = createDiv('n move_handle').enableDrag();
    this.content = createDiv('content').appendText(' ');
    this.footer  = createDiv('s move_handle').enableDrag();

    var header   = createDiv('nw').insert(createDiv('ne').insert(this.header));
    var content  = createDiv('w').insert(createDiv('e', {style: "position:relative"}).insert(this.content));
    var footer   = createDiv('sw').insert(createDiv('se' + (this.options.resizable ?  " se_resize_handle" : "")).insert(this.footer));

    this.element.insert(header).insert(content).insert(footer).identify('ui-window');
    this.header.observe('mousedown', this.activate.bind(this));

    this.setDraggable(this.options.draggable);
    this.setResizable(this.options.resizable);

    this.overlay = new Element('div', { style: this.style + "display: none" })
        .observe('mousedown', this.activate.bind(this));

    if (this.options.activeOnClick)
      this.content.insert({ before: this.overlay });
  },

  createWiredElement: function() {
    this.wiredElement = this.wiredElement || new Element("div", {
      className: this.getTheme() + "_wired",
      style:    "display: none; position: absolute; top: 0; left: 0"
    });
  },

  createResizeHandles: function() {
    $w(" n  w  e  s  nw  ne  sw  se ").each(function(id) {
      this.insert(new Element("div", {
        className:   id + "_sizer resize_handle",
        drag_prefix: id }).enableDrag());
    }, this.element);
    this.createResizeHandles = Prototype.emptyFunction;
  },

  // First rendering, pre-compute window border size
  render: function() {
    this.addElements();

    this.computeBorderSize();
    this.updateButtonsOrder();
    this.element.hide().remove();

    // this.options contains top, left, width and height keys
    return this.setBounds(this.options);
  },

  // Adds window elements to the DOM
  addElements: function() {
    this.windowManager.container.appendChild(this.element);
  },

  // Set z-index to all window elements
  setZIndex: function(zIndex) {
    if (this.zIndex != zIndex) {
      this.zIndex = zIndex;
      [ this.element ].concat(this.element.childElements()).each(function(element) {
        element.style.zIndex = zIndex++;
      });
      this.lastZIndex = zIndex;
    }
    return this;
  },

  effect: function(name, element, options) {
    var effect = this.options[name] || Prototype.emptyFunction;
    effect(element || this.element, options || {});
  },

  // re-compute window border size
  computeBorderSize: function() {
    if (this.element) {
      if (Prototype.Browser.IEVersion >= 7)
        this.content.style.width = "100%";
      var dim = this.element.getDimensions(), pos = this.content.positionedOffset();
      this.borderSize = {  top:    pos[1],
                           bottom: dim.height - pos[1] - this.content.getHeight(),
                           left:   pos[0],
                           right:  dim.width - pos[0] - this.content.getWidth() };
      this.borderSize.width  = this.borderSize.left + this.borderSize.right;
      this.borderSize.height = this.borderSize.top  + this.borderSize.bottom;
      if (Prototype.Browser.IEVersion >= 7)
        this.content.style.width = "auto";
    }
  },

  computeSize: function(width, height, innerSize) {
    var innerWidth, innerHeight, outerWidth, outerHeight;
	  if (innerSize) {
	    outerWidth  =  width  + this.borderSize.width;
	    outerHeight =  height + this.borderSize.height;
    } else {
	    outerWidth  =  width;
	    outerHeight =  height;
    }
    // Check grid value
    if (!this.animating) {
      outerWidth = outerWidth.snap(this.options.gridX);
      outerHeight = outerHeight.snap(this.options.gridY);

      // Check min size
      if (!this.folded) {
        if (outerWidth < this.options.minWidth)
          outerWidth = this.options.minWidth;

        if (outerHeight < this.options.minHeight)
          outerHeight = this.options.minHeight;
      }

      // Check max size
      if (this.options.maxWidth && outerWidth > this.options.maxWidth)
        outerWidth = this.options.maxWidth;

      if (this.options.maxHeight && outerHeight > this.options.maxHeight)
        outerHeight = this.options.maxHeight;
    }

    if (this.centerOptions && this.centerOptions.auto)
      this.recenter();

    innerWidth  = outerWidth - this.borderSize.width;
    innerHeight = outerHeight - this.borderSize.height;
    return {
      innerWidth: innerWidth, innerHeight: innerHeight,
      outerWidth: outerWidth, outerHeight: outerHeight
    };
  },

  computePosition: function(top, left) {
    if (this.centerOptions && this.centerOptions.auto)
      return this.computeRecenter(this.getSize());                                                                                                            ;

    return {
      top:  this.animating ? top  : top.snap(this.options.gridY),
      left: this.animating ? left : left.snap(this.options.gridX)
    };
  },

  computeRecenter: function(size) {
    var viewport   = this.windowManager.viewport,
        area       = viewport.getDimensions(),
        offset     = viewport.getScrollOffset(),
        center     = {
          top:  Object.isUndefined(this.centerOptions.top)  ? (area.height - size.height) / 2 : this.centerOptions.top,
          left: Object.isUndefined(this.centerOptions.left) ? (area.width  - size.width)  / 2 : this.centerOptions.left
        };

    return {
      top:  parseInt(center.top + offset.top),
      left: parseInt(center.left + offset.left)
    };
  },

  recenter: function(event) {
    var pos = this.computeRecenter(this.getSize());
    this.setPosition(pos.top, pos.left);
  }
});
UI.URLWindow = Class.create(UI.Window, {
  options: {
    url: 'about:blank'
  },

  afterClassCreate: function() {
    this.undefMethod('setAjaxContent');
  },

  initialize: function($super, options) {
    $super(options);
    this.createIFrame();
  },

  destroy: function($super){
    this.iframe.src = null;
    $super();
  },

  getUrl: function() {
    return this.iframe.src;
  },

  setUrl: function(url, options) {
    this.iframe.src = url;
    return this;
  },

  createIFrame: function($super) {
    this.iframe = new Element('iframe', {
      style: this.style,
      frameborder: 0,
      src: this.options.url,
      name: this.element.id + "_frame",
      id:  this.element.id + "_frame"
    });

    this.content.insert(this.iframe);
  }
});
if (!Object.isUndefined(window.Effect)) {
  UI.Window.Effects = UI.Window.Effects || {};
  UI.Window.Effects.Morph = Class.create(Effect.Base, {
    initialize: function(window, bounds) {
      this.window = window;
      var options = Object.extend({
        fromBounds: this.window.getBounds(),
        toBounds:   bounds,
        from:       0,
        to:         1
      }, arguments[2] || { });
      this.start(options);
    },

    update: function(position) {
      var t = this.options.fromBounds.top + (this.options.toBounds.top   - this.options.fromBounds.top) * position;
      var l = this.options.fromBounds.left + (this.options.toBounds.left - this.options.fromBounds.left) * position;

      var ow = this.options.fromBounds.width + (this.options.toBounds.width - this.options.fromBounds.width) * position;
      var oh = this.options.fromBounds.height + (this.options.toBounds.height - this.options.fromBounds.height) * position;

      this.window.setBounds({top: t,  left: l, width: ow, height: oh})
    }
  });
}
UI.Window.addMethods({
  startDrag: function(handle) {
    this.initBounds = this.getBounds();
    this.activate();

    if (this.options.wired) {
      this.createWiredElement();
      this.wiredElement.style.cssText = this.element.style.cssText;
      this.element.hide();
      this.saveElement = this.element;
      this.windowManager.container.appendChild(this.wiredElement);
      this.element = this.wiredElement;
    }

    handle.hasClassName('resize_handle') ? this.startResize(handle) : this.startMove();
  },

  endDrag: function() {
    this.element.hasClassName('resized') ? this.endResize() : this.endMove();

    if (this.options.wired) {
      this.saveElement.style.cssText = this.wiredElement.style.cssText;
      this.wiredElement.remove();
      this.element = this.saveElement;
      this.saveElement = false;
    }
  },

  startMove: function() {
    // method used to drag
    this.drag = this.moveDrag;
    this.element.addClassName('moved');
    this.fire('move:started');
  },

  endMove: function() {
    this.element.removeClassName('moved');
    this.fire('move:ended');
  },

  startResize: function(handle) {
    this.drag = this[handle.readAttribute('drag_prefix')+'Drag'];
    this.element.addClassName('resized');
    this.fire('resize:started');
  },

  endResize: function() {
    this.element.removeClassName('resized');
    this.fire('resize:ended');
  },

  moveDrag: function(dx, dy) {
    this.setPosition(this.initBounds.top + dy, this.initBounds.left + dx);
  },

  swDrag: function(dx, dy) {
    var initBounds = this.initBounds;
    this.setSize(initBounds.width - dx, initBounds.height + dy)
        .setPosition(initBounds.top,
                     initBounds.left + (initBounds.width - this.getSize().width));
  },

  seDrag: function(dx, dy) {
    this.setSize(this.initBounds.width + dx, this.initBounds.height + dy);
  },

  nwDrag: function(dx, dy) {
    var initBounds = this.initBounds;
    this.setSize(initBounds.width - dx, initBounds.height - dy)
        .setPosition(initBounds.top + (initBounds.height - this.getSize().height),
                     initBounds.left + (initBounds.width - this.getSize().width));
  },

  neDrag: function(dx, dy) {
    var initBounds = this.initBounds;
    this.setSize(initBounds.width + dx, initBounds.height - dy)
        .setPosition(initBounds.top + (initBounds.height - this.getSize().height),
                     initBounds.left);
  },

  wDrag: function(dx, dy) {
    var initBounds = this.initBounds;
    this.setSize(initBounds.width - dx, initBounds.height)
        .setPosition(initBounds.top,
                     initBounds.left + (initBounds.width - this.getSize().width));
  },

  eDrag: function(dx, dy) {
    this.setSize(this.initBounds.width + dx, this.initBounds.height);
  },

  nDrag: function(dx, dy) {
    var initBounds = this.initBounds;
    this.setSize(initBounds.width, initBounds.height - dy)
        .setPosition(initBounds.top + (initBounds.height - this.getSize().height),
                     initBounds.left);
  },

  sDrag: function(dx, dy) {
    this.setSize(this.initBounds.width, this.initBounds.height + dy);
  }
});
UI.Window.addMethods({
  methodsAdded: function(base) {
    base.aliasMethodChain('create',  'buttons');
    base.aliasMethodChain('destroy', 'buttons');
  },

  createWithButtons: function() {
    this.createWithoutButtons();

    if (!this.options.resizable) {
      this.options.minimize = false;
      this.options.maximize = false;
    }

    this.buttons = new Element("div", { className: "buttons" })
      .observe('click',     this.onButtonsClick.bind(this))
      .observe('mouseover', this.onButtonsHover.bind(this))
      .observe('mouseout',  this.onButtonsOut.bind(this));

    this.element.insert(this.buttons);

    this.defaultButtons.each(function(button) {
      if (this.options[button] !== false)
        this.addButton(button);
    }, this);
  },

  destroyWithButtons: function() {
    this.buttons.stopObserving();
    this.destroyWithoutButtons();
  },

  defaultButtons: $w(' minimize maximize close '),

  getButtonElement: function(buttonName) {
    return this.buttons.down("." + buttonName);
  },

  // Controls close, minimize, maximize, etc.
  // action can be either a string or a function
  // if action is a string, it is the method name that will be called
  // else the function will take the window as first parameter.
  // if not given action will be taken in window's options
  addButton: function(buttonName, action) {
    this.buttons.insert(new Element("a", { className: buttonName, href: "#"}));

    if (action)
      this.options[buttonName] = action;

    return this;
  },

  removeButton: function(buttonName) {
    this.getButtonElement(buttonName).remove();
    return this;
  },

  disableButton: function(buttonName) {
    this.getButtonElement(buttonName).addClassName("disabled");
    return this;
  },

  enableButton: function(buttonName) {
    this.getButtonElement(buttonName).removeClassName("disabled");
    return this;
  },

  onButtonsClick: function(event) {
    var element = event.findElement('a:not(.disabled)');

    if (element) this.action(element.className);
    event.stop();
  },

  onButtonsHover: function(event) {
    this.buttons.addClassName("over");
  },

  onButtonsOut: function(event) {
    this.buttons.removeClassName("over");
  },

  updateButtonsOrder: function() {
    var buttons = this.buttons.childElements();

    buttons.inject(new Array(buttons.length), function(array, button) {
      array[parseInt(button.getStyle("padding-top"))] = button.setStyle("padding: 0");
      return array;
    }).each(function(button) { this.buttons.insert(button) }, this);
  }
});
UI.Window.addMethods({
  methodsAdded: function(base) {
    (function(methods) {
      $w(methods).each(function(m) { base.aliasMethodChain(m, 'shadow') });
    })(' create addElements setZIndex setPosition setSize setBounds ');
  },

  showShadow: function() {
    if (this.shadow) {
      this.shadow.hide();
      this.effect('show', this.shadow.shadow);
    }
    if (this.iframe)
      this.iframe.show();
  },

  hideShadow: function() {
    if (this.shadow)
      this.effect('hide', this.shadow.shadow);
    if (this.iframe)
      this.iframe.hide();
  },

  removeShadow: function() {
    if (this.shadow)
      this.shadow.remove();
  },

  focusShadow: function() {
    if (this.shadow)
      this.shadow.focus();
  },

  blurShadow: function() {
    if (this.shadow)
      this.shadow.blur();
  },

  // Private Functions
  createWithShadow: function() {
    this.createWithoutShadow();

    this.observe('showing', this.showShadow)
        .observe('hiding',  this.hideShadow)
        .observe('hidden',  this.removeShadow)
        .observe('focused', this.focusShadow)
        .observe('blurred', this.blurShadow);

    if (this.options.shadow)
      this.shadow = new UI.Shadow(this.element, {theme: this.getShadowTheme(), withIFrameShim: false});
    this.iframe = Prototype.Browser.IE ? new UI.IframeShim({parent: this.windowManager.container}) : null;
  },

  addElementsWithShadow: function() {
    this.addElementsWithoutShadow();
    if (this.shadow) {
      this.shadow.setBounds(this.options).render();
    }
  },

  setZIndexWithShadow: function(zIndex) {
    if (this.zIndex != zIndex) {
      if (this.shadow)
        this.shadow.setZIndex(zIndex - 1);
      this.setZIndexWithoutShadow(zIndex);
      this.zIndex = zIndex;
    }
    return this;
  },

  setPositionWithShadow: function(top, left) {
    this.setPositionWithoutShadow(top, left);
    if (this.shadow) {
      var pos = this.getPosition();
      this.shadow.setPosition(pos.top, pos.left);
    }
    if (this.iframe) {
      var pos = this.getPosition();
      this.iframe.setPosition(pos.top, pos.left);
    }
    return this;
  },

  setSizeWithShadow: function(width, height, innerSize) {
    this.setSizeWithoutShadow(width, height, innerSize);
    if (this.shadow) {
      var size = this.getSize();
      this.shadow.setSize(size.width, size.height);
    }
    if (this.iframe) {
      var size = this.getSize();
      this.iframe.setSize(size.width, size.height);
    }
    return this;
  },

  setBoundsWithShadow: function(bounds, innerSize) {
    this.setBoundsWithoutShadow(bounds, innerSize);
    if (this.shadow)
      this.shadow.setBounds(this.getBounds());
    if (this.iframe)
      this.iframe.setBounds(this.getBounds());
  }
});
/*
Class: UI.Dialog
  Main class to handle dialog. Dialog are Windows with buttons like Ok/Cancel for having nicer alert/confirm javascript dialog.

  Buttons are fully configurable and designed by CSS

  Alert and confirm are already designed

  To open a alert dialog just do:
  > new UI.Dialog({buttons: UI.Dialog.okButton}).center().setHeader("Open a new sesssion").setContent("your content").show(true);
  >

  To open a cofirm dialog just do:
  > new UI.Dialog({buttons: UI.Dialog.okCancelButton}).center().setHeader("Open a new sesssion").setContent("your content").show(true);
  >
  > // Or without options as it's by default
  > new UI.Dialog().center().setHeader("Open a new sesssion").setContent("your content").show(true);

*/

UI.Dialog = Class.create(UI.Window, {
  options: {
    buttons:       null, // default = ok/cancel button
    beforeSelect:  Prototype.trueFunction,
    close:         false,
    resizable:     false,
    activeOnClick: false
  },

  // Group: Attributes

  /*
    Property: buttons
      Array of buttons attributes. A button is defined with an hash with
      - title
      - className
      - callback (optional) called when a user click on a button (not disabled)

      Example for alert Dialog
      > [{title: 'Ok',     className: 'ok',     callback: function(win) { win.destroy(); }}]
      >
      >

    Property: beforeSelect
      function calls when a user click on a button (not disabled). If beforeSelect returns false, button callbacks is not executed
  */


  // Group: Instance Methods

  /*
    Method: disableButton
      Disables a button (add disabled css class name)
      The button will not be clickable anymore

    Parameters:
      index - Button index (start from 0)

    Returns:
      this
  */
  disableDialogButton: function(index) {
    var button = this.getDialogButton(index);
    if (button)
      button.addClassName("disabled");
    return this;
  },

  /*
    Method: enableButton
      Enables a button (remove disabled css class name)
      The button is now clickable

    Parameters:
      index - Button index (start from 0)

    Returns:
      this
  */
  enableDialogButton: function(index) {
    var button = this.getDialogButton(index);
    if (button)
      button.removeClassName("disabled");
    return this;
  },

  /*
    Method: getButton
      Gets button element

    Parameters:
      index - Button index (start from 0)

    Returns:
      button element or null if bad index
  */
  getDialogButton: function(index) {
    var buttons = this.buttonContainer.childElements();
    if (index >= 0 && index < buttons.length)
      return buttons[index];
    else
      return null;
  },

  // Override create to add dialog buttons
  create: function($super) {
    $super();

    // Create buttons
    this.buttonContainer = this.createButtons();
    // Add a new content for dialog content
    this.dialogContent   = new Element('div', {className:'ui-dialog-content'});

    this.content.update(this.dialogContent);
    this.content.insert(this.buttonContainer);
  },

  addElements: function($super) {
    $super();
    // Pre compute buttons height
    this.buttonsHeight = this.buttonContainer.getHeight() || this.buttonsHeight;
    this.setDialogSize();
  },

  setContent: function(content, withoutButton) {
    this.dialogContent.update(content);

    // Remove button if need be
    if (withoutButton && this.buttonContainer.parentNode)
      this.buttonContainer.remove();
    else
      this.content.insert(this.buttonContainer);

    // Update dialog size
    this.setDialogSize();
    return this;
  },

  onSelect: function(e) {
    var element = e.element();
    if (element.callback && !element.hasClassName('disabled')) {
      if (this.options.beforeSelect(element))
        element.callback(this);
    }
  },

  createButtons: function(dialogButtons) {
    var buttonContainer = new Element('div', { className: 'ui-dialog-buttons'});
    (this.options.buttons || UI.Dialog.okCancelButtons).each(function(item){
      var button;
      if (item.separator)
        button = new Element('span', {className: 'separator'});
      else
        button = new Element('button', {title: item.title,
                                        className: (item.className || '') + (item.disabled ? ' disabled' : '')})
                   .observe('click', this.onSelect.bind(this))
                   .update(item.title);

       buttonContainer.insert(button);
       button.callback = item.callback;
    }.bind(this));
    return buttonContainer;
  },

  setDialogSize: function () {
    // Do not compute dialog size if window is not completly ready
    if (!this.borderSize)
      return;

    this.buttonsHeight = this.buttonContainer.getHeight() || this.buttonsHeight;
    var style = this.dialogContent.style, size  = this.getSize(true);
    style.width = size.width + "px", style.height = size.height - this.buttonsHeight + "px";
  },

  setSize: function($super, width, height, innerSize) {
  	$super(width, height, innerSize);
    this.setDialogSize();
    return this;
  }
});


UI.Dialog =  Object.extend(UI.Dialog, {
  okButton        : [{title: 'Ok',     className: 'ok',     callback: function(win) { win.destroy(); }}],
  okCancelButtons : [{title: 'Ok',     className: 'ok',     callback: function(win) { win.destroy(); }},
                     {title: 'Cancel', className: 'cancel', callback: function(win) { win.destroy(); }}]
});

/*
Class: UI.WindowManager
  Window Manager.
  A default instance of this class is created in UI.defaultWM.

  Example:
    > new UI.WindowManger({
    >   container: 'desktop',
    >   theme: 'mac_os_x'
    > });
*/

UI.WindowManager = Class.create(UI.Options, {
  options: {
    container:   null, // will default to document.body
    zIndex:      0,
    theme:       "alphacube",
    shadowTheme: "mac_shadow",
    showOverlay: Element.show,
    hideOverlay: Element.hide,
    positionningStrategy: function(win, area) {
      UI.WindowManager.DumbPositionningStrategy(win, area);
    }
  },

  initialize: function(options) {
    this.setOptions(options);

    this.container = $(this.options.container || document.body);

    if (this.container === $(document.body)) {
      this.viewport = document.viewport;
      this.scrollContainer = window;
    } else {
      this.viewport = this.scrollContainer = this.container;
    }

    this.container.observe('drag:started', this.onStartDrag.bind(this))
                  .observe('drag:updated', this.onDrag.bind(this))
                  .observe('drag:ended',   this.onEndDrag.bind(this));

    this.stack = new UI.WindowManager.Stack();
    this.modalSessions = 0;

    this.createOverlays();
    this.resizeEvent = this.resize.bind(this);

    Event.observe(window, "resize", this.resizeEvent);
  },

  destroy: function() {
    this.windows().invoke('destroy');
    this.stack.destroy();
    Event.stopObserving(window, "resize", this.resizeEvent);
  },

  /*
    Method: setTheme
      Changes window manager's theme, all windows that don't have a own theme
      will have this new theme.

    Parameters:
      theme - theme name

    Example:
      > UI.defaultWM.setTheme('bluelighting');
  */
  setTheme: function(theme) {
    this.stack.windows.select(function(w) {
      return !w.options.theme;
    }).invoke('setTheme', theme, true);
    this.options.theme = theme;
    return this;
  },

  register: function(win) {
    if (this.getWindow(win.id)) return;

    this.handlePosition(win);
    this.stack.add(win);
    this.restartZIndexes();
  },

  unregister: function(win) {
    this.stack.remove(win);

    if (win == this.focusedWindow)
      this.focusedWindow = null;
  },

  /*
    Method: getWindow
      Find the window containing a given element.

    Example:
      > $$('.ui-window a.close').invoke('observe', 'click', function() {
      >   UI.defaultWM.getWindow(this).close();
      > });

    Parameters:
      element - element or element identifier

    Returns:
      containing window or null
  */
  getWindow: function(element) {
    element = $(element);

    if (!element) return;

    if (!element.hasClassName('ui-window'))
      element = element.up('.ui-window');

    var id = element.id;
    return this.stack.windows.find(function(win) { return win.id == id });
  },

  /*
    Method: windows
      Returns an array of all windows handled by this window manager.
      First one is the back window, last one is the front window.

    Example:
      > UI.defaultWM.windows().invoke('destroy');
  */
  windows: function() {
    return this.stack.windows.clone();
  },

  /*
    Method: getFocusedWindow
      Returns the focused window
  */
  getFocusedWindow: function() {
    return this.focusedWindow;
  },

  // INTERNAL

  // Modal mode
  startModalSession: function(win) {
    if (!this.modalSessions) {
      this.removeOverflow();
      this.modalOverlay.className = win.getTheme() + "_overlay";
      this.container.appendChild(this.modalOverlay);

      if (!this.modalOverlay.opacity)
        this.modalOverlay.opacity = this.modalOverlay.getOpacity();
      this.modalOverlay.setStyle("height: " + this.viewport.getHeight() + "px");

      this.options.showOverlay(this.modalOverlay, {from: 0, to: this.modalOverlay.opacity});
      if (this.iframe) {
        this.iframe.setBounds({top: 0, left: 0, width: this.viewport.getWidth(), height: this.viewport.getHeight()});
        this.iframe.show();
      }
    }
    this.modalOverlay.setStyle({ zIndex: win.zIndex - 1 });
    this.modalSessions++;
  },

  endModalSession: function(win) {
    this.modalSessions--;
    if (this.modalSessions) {
      this.modalOverlay.setStyle({ zIndex: this.stack.getPreviousWindow(win).zIndex - 1 });
    } else {
      this.resetOverflow();
      this.options.hideOverlay(this.modalOverlay, { from: this.modalOverlay.opacity, to: 0 });
      if (this.iframe)
        this.iframe.hide();
    }
  },

  moveHandleSelector:   '.ui-window.draggable .move_handle',
  resizeHandleSelector: '.ui-window.resizable .resize_handle',

  onStartDrag: function(event) {
    var handle = event.element(),
        isMoveHandle   = handle.match(this.moveHandleSelector),
        isResizeHandle = handle.match(this.resizeHandleSelector);

    // ensure dragged element is a window handle !
    if (isResizeHandle || isMoveHandle) {
      event.stop();

      // find the corresponding window
      var win = this.getWindow(event.findElement('.ui-window'));

      // render drag overlay
      this.container.insert(this.dragOverlay.setStyle({ zIndex: this.getLastZIndex() }));

      win.startDrag(handle);
      this.draggedWindow = win;
    }
  },

  onDrag: function(event) {
    if (this.draggedWindow) {
      event.stop();
      this.draggedWindow.drag(event.memo.dx, event.memo.dy);
    }
  },

  onEndDrag: function(event) {
    if (this.draggedWindow) {
      event.stop();
      this.dragOverlay.remove();
      this.draggedWindow.endDrag();
      this.draggedWindow = null;
    }
  },

  maximize: function(win) {
    this.removeOverflow();
    this.maximizedWindow = win;
    return true;
  },

  restore: function(win) {
    if (this.maximizedWindow) {
      this.resetOverflow();
      this.maximizedWindow = false;
    }
    return true;
  },

  removeOverflow: function() {
    var container = this.container;
    // Remove overflow, save overflow and scrolloffset values to restore them when restore window
    container.savedOverflow = container.style.overflow || "auto";
    container.savedOffset = this.viewport.getScrollOffset();
    container.style.overflow = "hidden";

    this.viewport.setScrollOffset({ top:0, left:0 });

    if (this.container == document.body && Prototype.Browser.IE)
      this.cssRule = CSS.addRule("html { overflow: hidden }");
  },

  resetOverflow: function() {
    var container = this.container;
    // Restore overflow ans scrolloffset
    if (container.savedOverflow) {
      if (this.container == document.body && Prototype.Browser.IE)
        this.cssRule.remove();

      container.style.overflow = container.savedOverflow;
      this.viewport.setScrollOffset(container.savedOffset);

      container.savedOffset = container.savedOverflow = null;
    }
  },

  hide: function(win) {
    var previous = this.stack.getPreviousWindow(win);
    if (previous) previous.focus();
  },

  restartZIndexes: function(){
    // Reset zIndex
    var zIndex = this.getZIndex() + 1; // keep a zIndex free for overlay divs
    this.stack.windows.each(function(w) {
      w.setZIndex(zIndex);
      zIndex = w.lastZIndex + 1;
    });
  },

  getLastZIndex: function() {
    return this.stack.getFrontWindow().lastZIndex + 1;
  },

  overlayStyle: "position: absolute; top: 0; left: 0; display: none; width: 100%;",

  createOverlays: function() {
    this.modalOverlay = new Element("div", { style: this.overlayStyle });
    this.dragOverlay  = new Element("div", { style: this.overlayStyle+"height: 100%" });
    this.iframe       = Prototype.Browser.IE ? new UI.IframeShim() : null;
  },

  focus: function(win) {
    // Blur the previous focused window
    if (this.focusedWindow)
      this.focusedWindow.blur();
    this.focusedWindow = win;
  },

  blur: function(win) {
    if (win == this.focusedWindow)
      this.focusedWindow = null;
  },

  setAltitude: function(win, altitude) {
    var stack = this.stack;

    if (altitude === "front") {
      if (stack.getFrontWindow() === win) return;
      stack.bringToFront(win);
    } else if (altitude === "back") {
      if (stack.getBackWindow() === win) return;
      stack.sendToBack(win);
    } else {
      if (stack.getPosition(win) == altitude) return;
      stack.setPosition(win, altitude);
    }

    this.restartZIndexes();
    return true;
  },

  getAltitude: function(win) {
    return this.stack.getPosition(win);
  },

  resize: function(event) {
    var area = this.viewport.getDimensions();

    if (this.maximizedWindow)
      this.maximizedWindow.setSize(area.width, area.height);

    if (this.modalOverlay.visible())
      this.modalOverlay.setStyle("height:" + area.height + "px");
  },

  handlePosition: function(win) {
    // window has its own position, nothing needs to be done
    if (Object.isNumber(win.options.top) && Object.isNumber(win.options.left))
      return;

    var strategy = this.options.positionningStrategy,
        area     = this.viewport.getDimensions();

    Object.isFunction(strategy) ? strategy(win, area) : strategy.position(win, area);
  }
});

UI.WindowManager.DumbPositionningStrategy = function(win, area) {
  size = win.getSize();

  var top  = area.height - size.height,
      left = area.width  - size.width;

  top  = top  < 0 ? 0 : Math.random() * top;
  left = left < 0 ? 0 : Math.random() * left;

  win.setPosition(top, left);
};

UI.WindowManager.optionsAccessor('zIndex', 'theme', 'shadowTheme');

UI.WindowManager.Stack = Class.create(Enumerable, {
  initialize: function() {
    this.windows = [ ];
  },

  each: function(iterator) {
    this.windows.each(iterator);
  },

  add: function(win, position) {
    this.windows.splice(position || this.windows.length, 0, win);
  },

  remove: function(win) {
    this.windows = this.windows.without(win);
  },

  sendToBack: function(win) {
    this.remove(win);
    this.windows.unshift(win);
  },

  bringToFront: function(win) {
    this.remove(win);
    this.windows.push(win);
  },

  getPosition: function(win) {
    return this.windows.indexOf(win);
  },

  setPosition: function(win, position) {
    this.remove(win);
    this.windows.splice(position, 0, win);
  },

  getFrontWindow: function() {
    return this.windows.last();
  },

  getBackWindow: function() {
    return this.windows.first();
  },

  getPreviousWindow: function(win) {
    return (win == this.windows.first()) ? null : this.windows[this.windows.indexOf(win) - 1];
  }
});

document.whenReady(function() {
  UI.defaultWM = new UI.WindowManager();
});
/*
  Class: UI.ContextMenu
    Creates a context menu when instantiated.
    Shows menu when right button (ctrl + left in Opera) is clicked on a certain element.
    Hides menu when left button is cliked.
    Allows to attach certain behavior to certain menu elements (links).

  Example:
    > var contextLinks = [{
    >   name: 'Save',
    >   className: 'back',
    >   callback: Document.save
    > }, {
    >   name: 'Save as...',
    >   submenu: [{
    >     name: 'Excel (.xls)',
    >     className: 'xls',
    >     callback: Document.saveAsXls
    >   }, {
    >     name: 'Word (.doc)',
    >     className: 'doc',
    >     callback: Document.saveAsDoc
    >   }, {
    >     name: 'Acrobat Reader',
    >     className: 'pdf',
    >     callback: Document.saveAsPdf
    >   }]
    > }];
    >
    > ...
    >
    > new UI.ContextMenu({
    >   selector: '#context_area', // element to attach right click event to
    >   showEffect: true, // indicates whether Effect.Appear is used when menu is shown
    >   menuItems: contextLinks // array of links to be used when building menu
    > });
*/

UI.ContextMenu = Class.create(UI.Options, {
  // Group: Options
  options: {
    // Property: className
    //   class to be applied to menu element, default is 'ui-context_menu'
    className: 'ui-context_menu',

    // Property: beforeShow
    //   beforeShow: function to be called before menu element is shown,
    //   default is empty function.
    beforeShow: Prototype.emptyFunction,

    // Property: beforeHide
    //   function to be called before menu element is hidden,
    //   default is empty function.
    beforeHide: Prototype.emptyFunction,

    // Property: beforeSelect
    //   function to be called before menu item is clicked,
    //   default is empty function.
    beforeSelect: Prototype.emptyFunction,

    // Property: zIndex
    //  z-index to be applied to a menu element, default is 900
    zIndex: 900,

    pageOffset: 25,

    // Property: showEffect
    // showEffect: true will force menu to "fade in" when shown,
    // default is false
    showEffect: false,

    // Property: hideEffect
    // showEffect: true will force menu to "fade out" when hidden,
    // default is false
    hideEffect: false,

    // Property: shadow
    // name of a shadow theme or false, default is 'mac_shadow'
    shadow: "mac_shadow"
  },

  // Group: Constructor

  /*
    Method: initialize
      Constructor function, should not be called directly

    Parameters:
      options - (Hash) list of optional parameters

    Returns:
      this
  */
  initialize: function(options) {
    this.setOptions(options);

    if (Object.isUndefined(Effect)) {
      this.options.showEffect = this.options.hideEffect = false;
    }

    this.iframe = Prototype.Browser.IE ? new UI.IframeShim() : null;
    this.create();

    this.shadow = this.options.shadow
      ? UI.ContextMenu.shadow || new UI.Shadow(this.element, {theme: this.options.shadow}).focus().hide()
      : null;

    if (this.shadow)
      UI.ContextMenu.shadow = this.shadow;

    this.initObservers();
  },

  // Group: Methods

  create: function() {
    this.element = new Element('div', {
      className: this.options.className,
      style: 'display: none'
    });
    this.element.insert(this.createList(this.options.menuItems));
    $(document.body).insert(this.element.observe('contextmenu', Event.stop));
  },

  createList: function(items) {
    var list = new Element('ul');

    items.each(function(item){
      list.insert(
        new Element('li', {className: item.separator ? 'separator' : ''}).insert(
          !item.separator
            ? Object.extend(new Element('a', {
                href: '#',
                title: item.name,
                className: (item.className || '')
                  + (item.disabled ? ' disabled' : '')
                  + (item.submenu ? ' submenu' : '')
              }), { _callback: item.callback })
              .observe('click', item.callback ? this.onSelect.bind(this) : Event.stop)
              .observe('contextmenu', Event.stop)
              .update(item.name)
              .insert(
                item.submenu
                  ? this.createList(item.submenu).wrap({
                      className: this.options.className, style: 'display:none'
                    })
                  : ''
              )
            : ''
        )
      )
    }.bind(this));

    return list;
  },

  initObservers: function() {
    var contextEvent = Prototype.Browser.Opera ? 'click' : 'contextmenu';

    document.observe('click', function(e) {
      if (this.element.visible() && !e.isRightClick()) {
        this.options.beforeHide();
        if (this.iframe)
          this.iframe.hide();
        this.hide();
      }
    }.bind(this));

    $$(this.options.selector).invoke('observe', contextEvent, function(e) {
      if (Prototype.Browser.Opera && !e.ctrlKey) return;
      this.show(e);
    }.bind(this));

    this.element.select('a.submenu')
      .invoke('observe', 'mouseover', function(e) {
        if (this.hasClassName('disabled')) return;
        this.down('.menu').setStyle({
          top: 0,
          left: this.getWidth() + 'px'
        }).show();
      })
      .invoke('observe', 'mouseout', function(e) {
        this.down('.menu').hide();
      });

    if (this.shadow)
      this.shadow.shadow.observe('contextmenu', Event.stop);
  },

  /*
    Method: show

    Parameters:
      e - Event object (optional)

    Returns:
      this
  */
  show: function(e) {
    if (e) e.stop();

    this.options.beforeShow();
    this.fire('showing');

    if (UI.ContextMenu.shownMenu) {
      UI.ContextMenu.shownMenu.hide();
    }
    UI.ContextMenu.shownMenu = this;

    this.position(e);

    if (this.options.showEffect) {
      Effect.Appear(this.element, {
        duration: 0.25,
        afterFinish: function() { this.fire('shown') }.bind(this)
      })
    }
    else {
      this.element.show();
      this.fire('shown');
    }

    this.event = e;
    return this;
  },

  /*
    Method: position
      Takes event object and positions menu element to match event's pointer coordinates
      Optionally positions shadow and iframe elements

    Returns:
      this
  */
  position: function(e) {
    var x = Event.pointer(e).x,
        y = Event.pointer(e).y,
        vpDim = document.viewport.getDimensions(),
        vpOff = document.viewport.getScrollOffset(),
        elDim = this.element.getDimensions(),
        elOff = {
          left: ((x + elDim.width + this.options.pageOffset) > vpDim.width
            ? (vpDim.width - elDim.width - this.options.pageOffset) : x),
          top: ((y - vpOff.top + elDim.height) > vpDim.height && (y - vpOff.top) > elDim.height
            ? (y - elDim.height) : y)
          },
        elBounds = Object.clone(Object.extend(elOff, elDim));

    for (prop in elOff) {
      elOff[prop] += 'px';
    }
    this.element.setStyle(elOff).setStyle({zIndex: this.options.zIndex});

    if (this.iframe) {
      this.iframe.setBounds(elBounds).show();
    }

    if (this.shadow) {
      this.shadow.setBounds(elBounds).show();
    }

    return this;
  },

  /*
    Method: hide

    Returns:
      this
  */
  hide: function() {

    this.options.beforeHide();

    if (this.iframe)
      this.iframe.hide();

    if (this.shadow)
      this.shadow.hide();

    if (this.options.hideEffect) {
      Effect.Fade(this.element, {
        duration: 0.25,
        afterFinish: function() { this.fire('hidden') }.bind(this)
      })
    }
    else {
      this.element.hide();
      this.fire('hidden')
    }

    return this;
  },
  /*
    Method: onSelect

    Parameters:
      e - current Event object (left click on a menu item)
  */
  onSelect: function(e) {
    if (e.target._callback && !e.target.hasClassName('disabled')) {
      this.options.beforeSelect();
      this.fire('selected');
      this.hide();
      e.target._callback(e, this.event);
    }
  },

  fire: function(eventName) {
    this.element.fire('contextmenu:' + eventName);
  }
});
/*
  Credits:
  - Idea: Facebook + Apple Mail
  - Guillermo Rauch: Original MooTools script
  - InteRiders: Prototype version  <http://interiders.com/>
*/

Object.extend(Event, {
  KEY_SPACE: 32,
  KEY_COMA:  188
});

UI.AutoComplete = Class.create(UI.Options, {
  // Group: Options
  options: {
    className: "pui-autocomplete",         // CSS class name prefix
    max: {selection: 10, selected:false},  // Max values fort autocomplete,
                                           // selection : max item in pulldown menu
                                           // selected  : max selected items (false = no limit)
    url: false,                            // Url for ajax completion
    delay: 0.2,                            // Delay before running ajax request
    shadow: false,                         // Shadow theme name (false = no shadow)
    highlight: false,                      // Highlight search string in list
    tokens: false,                         // Tokens used to automatically adds a new entry (ex tokens:[KEY_COMA, KEY_SPACE] for coma and spaces)
    unique: true                           // Do not display in suggestion a selected value
  },

  initialize: function(element, options) {
    this.setOptions(options);
    if(typeof(this.options.tokens) == 'number')
      this.options.tokens = $A([this.options.tokens]);
    this.element = $(element);

    this.render();
	this.updateInputSize();
    this.nbSelected = 0;
    this.list = [];

    this.keydownHandler  = this.keydown.bindAsEventListener(this);
    document.observe('keydown', this.keydownHandler);
  },

  destroy:function() {
    this.autocompletion.destroy();
    this.input.stopObserving();
    document.stopObserving('keypress', this.keydownHandler);
    this.container.remove();
    this.element.show();
  },

  init: function(tokens) {
    tokens = tokens || this.options.tokens;
    var values = this.input.value.split(",");
    values.each(function(text) {if (!text.empty()) this.add(text)}.bind(this));
    this.input.clear();

    return this;
  },

  add: function(text, value, options) {
    // No more than max
    if (!this.canAddMoreItems())
      return;

    // Create a new li
    var li = new Element("li", Object.extend({className: this.getClassName("box")}, options || {}));
    li.observe("click",     this.focus.bindAsEventListener(this, li))
      .observe("mouseover", this.over.bindAsEventListener(this, li))
      .observe("mouseout",  this.out.bindAsEventListener(this, li));

    // Close button
    var close = new Element('a', {'href': '#', 'class': 'closebutton'});
    li.insert(new Element("span").update(text).insert(close));
    if (value)
      li.writeAttribute("pui-autocomplete:value", value);

    close.observe("click", this.remove.bind(this, li));

    this.input.parentNode.insert({before: li});
    this.nbSelected++;
    this.updateSelectedText().updateHiddenField();

    this.updateInputSize();
    if (!this.canAddMoreItems())
      this.hideAutocomplete().fire("selection:max_reached");
    else
      this.hideAutocomplete().fire("input:empty");

    return this.fire("element:added", {element: li, text: text, value: value});
  },

  remove: function(element) {
    element.stopObserving();

    element.remove();
    this.nbSelected--;
    this.updateSelectedText().updateHiddenField();

    this.updateInputSize();
    this.input.focus();
    return this.fire("element:removed", {element: element});
  },

  removeLast: function() {
    var element = this.container.select("li." + this.getClassName("box")).last();
    if (element)
      this.remove(element);
  },

  removeSelected: function(event) {
    if (event.element().readAttribute("type") != "text" && event.keyCode == Event.KEY_BACKSPACE) {
      this.container.select("li." + this.getClassName("box")).each(function(element) {
        if (this.isSelected(element))
          this.remove(element);
      }.bind(this));
      if (event)
        event.stop();
    }
    return this;
  },

  focus: function(event, element) {
    if (event)
      event.stop();

    // Multi selection with shift
    if (event && !event.shiftKey)
      this.deselectAll();

    element = element || this.input;
    if (element == this.input && !this.input.readAttribute("focused")) {
      this.input.writeAttribute("focused", true);
      this.input.focus();
      this.displayCompletion();
    }
    else {
      this.out(event, element);
      element.addClassName(this.getClassName("selected"));

      // Blur input field
      if (element != this.input)
        this.blur();
    }
    return this.fire("element:focus", {element: element});
  },

  blur: function(event, element) {
    if (event)
      event.stop();

    if (!element)
      this.input.blur();

    this.hideAutocomplete();
    return this.fire("element:blur", {element: element});
  },

  over: function(event, element) {
    if (!this.isSelected(element))
      element.addClassName(this.getClassName("over"));
    if (event)
      event.stop();
    return this.fire("element:over", {element: element});
  },

  out: function(event, element) {
    if (!this.isSelected(element))
      element.removeClassName(this.getClassName("over"));
    if (event)
      event.stop();
    return this.fire("element:out", {element: element});
  },

  isSelected: function(element) {
    return element.hasClassName(this.getClassName("selected"));
  },

  deselectAll: function() {
   this.container.select("li." + this.getClassName("box")).invoke("removeClassName", this.getClassName("selected"));
   return this;
  },

  setAutocompleteList: function(list) {
    this.list = list;
    return this;
  },

  /*
    Method: fire
      Fires a autocomplete custom event automatically namespaced in "autocomplete:" (see Prototype custom events).
      The memo object contains a "autocomplete" property referring to the autocomplete.


    Parameters:
      eventName - an event name
      memo      - a memo object

    Returns:
      fired event
  */
  fire: function(eventName, memo) {
    memo = memo || { };
    memo.autocomplete = this;
    return this.input.fire('autocomplete:' + eventName, memo);
  },

  /*
    Method: observe
      Observe a autocomplete event with a handler function automatically bound to the autocomplete

    Parameters:
      eventName - an event name
      handler   - a handler function

    Returns:
      this
  */
  observe: function(eventName, handler) {
    this.input.observe('autocomplete:' + eventName, handler.bind(this));
    return this;
  },

  /*
    Method: stopObserving
      Unregisters a autocomplete event, it must take the same parameters as this.observe (see Prototype stopObserving).

    Parameters:
      eventName - an event name
      handler   - a handler function

    Returns:
      this
  */
  stopObserving: function(eventName, handler) {
	  this.input.stopObserving('autocomplete:' + eventName, handler);
	  return this;
  },

  // PRIVATE METHOD
  // Move selection. element = nil (highlight first),  "previous"/"next" or selected element
  moveSelection: function(event, element) {
    var current = null;
    // Seletc first
    if (!this.current)
      current = this.autocompletionContainer.firstDescendant();
    else if (element == "next") {
      current = this.current[element]() || this.autocompletionContainer.firstDescendant();
    }
    else if (element == "previous") {
      current = this.current[element]() || this.autocompletionContainer.childElements().last();
    }
    else
      current = element;

    if (this.current)
      this.current.removeClassName(this.getClassName("current"));

    this.current = current;

    if (this.current)
      this.current.addClassName(this.getClassName("current"));
  },

  // Add current selected element from completion to input
  addCurrentSelected: function() {
    if (this.current) {
      // Get selected text
      var index = this.autocompletionContainer.childElements().indexOf(this.current);
      // Clear input
      this.current = null;
      this.input.value = "";

      this.add(this.selectedList[index].text, this.selectedList[index].value);

      // Refocus input
      (function() {this.input.focus()}.bind(this)).defer();
      // Clear completion (force render)
      this.displayCompletion();
    }
  },

  // Display message (info or progress)
  showMessage: function(text) {
    if (text) {
      if (this.hideTimer) {
        clearTimeout(this.hideTimer);
        this.hideTimer = false;
      }
      // udate text
      this.message.update(text);
      this.message.show();
      // Hidden auto complete suggestion
      this.autocompletionContainer.hide();
      this.showAutocomplete();
    }
    else
      this.hideAutocomplete();
  },

  // Run ajax request to get completion values
  runRequest: function(search) {
    this.autocompletionContainer.hide();
    this.fire("request:started");

    new Ajax.Request(this.options.url, {parameters: {search: search, max: this.options.max.selection, "selected[]": this.selectedValues()}, onComplete: function(transport) {
      this.setAutocompleteList(transport.responseText.evalJSON());
      this.timer = null;
      this.fire("request:completed");
      this.displayCompletion();
    }.bind(this)});
  },

  // Get a "namespaced" class name
  getClassName: function(className) {
    return  this.options.className + "-" + className;
  },

  // Key down (for up/down and return key)
  keydown: function(event) {
    if (event.element() != this.input)
      return;

    this.ignoreKeyup = false;
    // Check max
    if (this.options.max.selected && this.nbSelected == this.options.max.selected)
      this.ignoreKeyup = true;

    // Check tokens
    if (this.options.tokens){
      var tokenFound = this.options.tokens.find(function(token){
        return event.keyCode == token;
      });
      if (tokenFound) {
        var value = this.input.value.strip();
        this.ignoreKeyup = true;
        var value = this.input.value;
        this.input.clear();
        if (!value.empty())
          this.add(value);
      }
    }
    switch(event.keyCode) {
     case Event.KEY_UP:
       this.moveSelection(event, 'previous');
       this.ignoreKeyup = true;
       break;
     case Event.KEY_DOWN:
       this.moveSelection(event, 'next');
       this.ignoreKeyup = true;
       break;
     case Event.KEY_RETURN:
       this.addCurrentSelected();
       this.ignoreKeyup = true;
       break;
     case Event.KEY_BACKSPACE:
       if (this.input.getCaretPosition() == 0)
         this.removeLast();
       break;
    }
    if (this.ignoreKeyup) {
      event.stop();
      return false;
    }
    else
      return true;
  },

  // Key to handle completion
  keyup: function(event) {
    if (this.ignoreKeyup) {
      this.ignoreKeyup = false;
      return true;
    }
    else {
      this.updateHiddenField();
      this.displayCompletion(event);
      return true;
    }
  },

  // Update input filed size to fit available width space
  updateInputSize: function() {
    // Get added elements width
    var top;
    var w = this.container.select("li." + this.getClassName("box")).inject(0, function(sum, element) {
      // First element
      if (Object.isUndefined(top))
        top = element.cumulativeOffset().top;
      // New line
      else if (top != element.cumulativeOffset().top) {
        top = element.cumulativeOffset().top;
        sum = 0;
      }
      return sum + element.getWidth() + element.getMarginDimensions().width + element.getBorderDimensions().width;
    });
    var margin = this.container.getMarginDimensions().width + this.container.getBorderDimensions().width + this.container.getPaddingDimensions().width;
    var width = this.container.getWidth() - w - margin;

    if (width < 50)
      width =   this.container.getWidth() - margin;

    this.input.parentNode.style.width = width + "px";
    this.input.style.width = width + "px";
  },

  // Display completion. It could display info or progress message if need be. Info when input field is empty
  // progress when ajax request is running
  displayCompletion: function(event) {
    var value = this.input.value.strip();
    this.current = null;
    if (!this.canAddMoreItems())
      return;

    if (!value.empty()) {
      // Run ajax reqest if need be
      if (event && this.options.url) {
        if (this.timer)
          clearTimeout(this.timer);
        this.timer = this.runRequest.bind(this, value).delay(this.options.delay);
      }
      else {
        this.message.hide();
        if (this.options.url)
          this.selectedList = this.list;
        else {
          this.selectedList = this.list.findAll(function(entry) {return entry.text.match(value)}).slice(0, this.options.max.selection);
          if (this.options.unique) {
            var selected= this.selectedValues();
            if (! selected.empty())
              this.selectedList = this.selectedList.findAll(function(entry) {return !selected.include(entry.value)});
          }
        }
        this.autocompletionContainer.update("");
        if (this.selectedList.empty()) {
          this.hideAutocomplete().fire('selection:empty');
        }
        else {
          this.selectedList.each(function(entry) {
            var li = new Element("li").update(this.options.highlight ? entry.text.gsub(value, "<em>" + value + "</em>") : entry.text);
            li.observe("mouseover", this.moveSelection.bindAsEventListener(this, li))
              .observe("mousedown", this.addCurrentSelected.bindAsEventListener(this));
            this.autocompletionContainer.insert(li);
          }.bind(this));
          this.autocompletionContainer.show();
          this.moveSelection("next");
          this.showAutocomplete();
        }
      }
    }
    else {
      this.hideAutocomplete().fire("input:empty");
    }
  },

  showAutocomplete: function(event){
    this.autocompletion.show(event).place(this.container);
    return this;
  },

  hideAutocomplete: function(){
    if (!this.hideTimer)
      this.hideTimer = (function() {
        this.autocompletionContainer.hide();
        this.autocompletion.hide();
        this.hideTimer = false;
      }).bind(this).defer();
    return this;
  },

  // Create HTML code
  render: function() {
    // GENERATED HTML CODE:
    // <ul class="pui-autocomplete-holder">
    //   <li class="pui-autocomplete-input">
    //     <input type="text"/>
    //   </li>
    // </ul>
    // <div class="pui-autocomplete-result">
    //   <div class="pui-autocomplete-message"></div>
    //   <ul class="pui-autocomplete-results">
    //   </ul>
    // </div>
    //
    this.input = this.element.cloneNode(true);
    this.input.writeAttribute("autocomplete", "off");
    this.input.name = "";

    this.input.observe("focus",    this.focus.bindAsEventListener(this, this.input))
              .observe("blur",     this.blur.bindAsEventListener(this, this.input))
              .observe("keyup",    this.keyup.bindAsEventListener(this));
    this.container = new Element('ul', {className: this.getClassName("holder")})
                       .insert(new Element("li", {className: this.getClassName("input")}).insert(this.input));

    this.autocompletionContainer = new Element("ul",{className: this.getClassName("results")}).hide();

    this.message  = new Element("div", {className: this.getClassName("message")}).hide();
    this.hidden = new Element("input",{type: 'hidden', name: this.element.name});
    this.element.insert({before: this.container}).insert({before: this.hidden});
    this.element.remove();

    this.autocompletion = new UI.PullDown(this.container, {
      className: this.getClassName("result"),
      shadow: this.options.shadow,
      position: 'below',
      cloneWidth: true
    });

    this.autocompletion.insert(this.message).insert(this.autocompletionContainer);
  },

  canAddMoreItems: function() {
    return !(this.options.max.selected && this.nbSelected == this.options.max.selected);
  },

  updateSelectedText: function() {
    var selected = this.container.select("li." + this.getClassName("box"));
    var content = selected.collect(function(element) {return element.down("span").firstChild.textContent});
    var separator = this.getSeparatorChar();
    this.selectedText = content.empty() ? false : content.join(separator);

    return this;
  },

  updateHiddenField: function() {
    var separator = this.getSeparatorChar();
    this.hidden.value = this.selectedText ? $A([this.selectedText, this.input.value]).join(separator) : this.input.value;
  },

  selectedValues: function() {
    var selected = this.container.select("li." + this.getClassName("box"));
    return  selected.collect(function(element) {return element.readAttribute("pui-autocomplete:value")});
  },

  getSeparatorChar: function() {
    var separator = this.options.tokens ? this.options.tokens.first() : " ";
    if (separator == Event.KEY_COMA)
      separator = ',';
    if (separator == Event.KEY_SPACE)
      separator = ' ';
    return separator;
  }
});

Element.addMethods({
  getCaretPosition: function(element) {
    if (element.createTextRange) {
      var r = document.selection.createRange().duplicate();
        r.moveEnd('character', element.value.length);
        if (r.text === '') return element.value.length;
        return element.value.lastIndexOf(r.text);
    } else return element.selectionStart;
  },

  getAttributeDimensions: function(element, attribut ) {
    var dim = $w('top bottom left right').inject({}, function(dims, key) {
      dims[key] = element.getNumStyle(attribut + "-" + key + (attribut == "border" ? "-width" : ""));
      return dims;
    });
    dim.width  = dim.left + dim.right;
    dim.height = dim.top + dim.bottom;
    return dim;
  },

  getBorderDimensions:  function(element) {return element.getAttributeDimensions("border")},
  getMarginDimensions:  function(element) {return element.getAttributeDimensions("margin")},
  getPaddingDimensions: function(element) {return element.getAttributeDimensions("padding")}
});
/*
  Interface: Date
  author: Minho Kim
*/
Object.extend(Date.prototype, {
  addDays: function(days) {
    return new Date(this.getFullYear(), this.getMonth(), this.getDate() + days, this.getHours(), this.getMinutes(), this.getSeconds(), this.getMilliseconds());
  },

  succ: function() {
    return this.addDays(1);
  },

  firstOfMonth: function() {
    return new Date(this.getFullYear(), this.getMonth(), 1);
  },

  endOfMonth: function() {
    return new Date(this.getFullYear(), this.getMonth() + 1, 0);
  },

  getDayOfYear: function() {
    return Math.ceil((this - new Date(this.getFullYear(), 0, 1)) / 86400000);
  },

  strftime: function(grammar) {
    var parts = { }, i18n = Date.default_i18n;
    var lambda = function(date, part) {
      switch (part) {
      // date
        case 'a': return i18n.WEEKDAYS_MEDIUM[date.getDay()];
        case 'A': return i18n.WEEKDAYS[date.getDay()];
        case 'b':
        case 'h': return i18n.MONTHS_SHORT[date.getMonth()];
        case 'B': return i18n.MONTHS[date.getMonth()];
        case 'C': return Math.floor(date.getFullYear() / 100);
        case 'd': return date.getDate().toPaddedString(2);
        case 'e': return date.getDate();
        case 'j': return date.getDayOfYear();
        case 'm': return (date.getMonth()+1).toPaddedString(2);
        case 'u': return date.getDay() || 7;
        case 'w': return date.getDay();
        case 'y': return date.getFullYear().toString().substring(2);
        case 'Y': return date.getFullYear();

        // time
        case 'H': return date.getHours().toPaddedString(2);
        case 'I': return (date.getHours() % 12).toPaddedString(2);
        case 'M': return date.getMinutes().toPaddedString(2);
        case 'p': return date.getHours() < 12 ? 'am' : 'pm';
        case 'S': return date.getSeconds().toPaddedString(2);

        // static
        case 'n': return '\n';
        case 't': return '\t';

        // combined
        case 'D': return date.strftime('%m/%d/%y');
        case 'r': return date.strftime('%I:%M:%S %p'); // time in a.m. and p.m. notation
        case 'R': return date.strftime('%H:%M:%S'); // time in 24 hour notation
        case 'T': return date.strftime('%H:%M:%S'); // current time, equal to %H:%M:%S

        // locale
        case 'c': return date.strftime(i18n.FORMAT_DATETIME);
        case 'x': return date.strftime(i18n.FORMAT_DATE);
        case 'X': return date.strftime(i18n.FORMAT_TIME);
      }
    };
    grammar.scan(/\w+/, function(e){
      var part = e.first();
      parts[part] = lambda(this, part);
    }.bind(this));
    return grammar.interpolate(parts, Date.STRFT_GRAMMER);
  },

  equalsDate: function(other) {
    return (this.getMonth() == other.getMonth() && this.getDate() == other.getDate() && this.getFullYear() == other.getFullYear());
  }
});

Object.extend(Date, {
  STRFT_GRAMMER : /(^|.|\r|\n)(\%(\w+))/,

  default_i18n: {
    MONTHS_SHORT: $w('Jan Feb Mar Apr May Jun Jul Aug Sep Oct Nov Dec'),
    MONTHS: $w('January February March April May June July August September October November December'),
    WEEKDAYS_MEDIUM: $w('Sun Mon Tue Wed Thu Fri Sat'),
    WEEKDAYS: $w('Sunday Monday Tuesday Wednesday Thursday Friday Saturday'),
    FORMAT_DATE: '%m/%d/%Y',
    FORMAT_TIME: '%H:%M:%S',
    FORMAT_DATETIME: '%x %X'
  },

  parseString: function(dateString, format) {
    var date = new Date(), i18n = Date.default_i18n;

    format=format.replace('%D','%m/%d/%y');
    format=format.replace('%T','%H:%M:%S').replace('%r','%I:%M:%S %p').replace('%R','%H:%M:%S');
    format=format.replace('%c',i18n.FORMAT_DATETIME).replace('%x',i18n.FORMAT_DATE).replace('%X',i18n.FORMAT_TIME);

    var tokens = format.match(/%./g);

    // the regex /\W+/ does not work for utf8 chars
    dateString.split(/[^A-Za-z0-9\u00A1-\uFFFF]+/).each(function(e, i) {
      switch (tokens[i]) {
        case '%a':
        case '%A':
        case '%u':
        case '%w': break;

        case '%b':
        case '%h': date.setMonth(i18n.MONTHS_SHORT.indexOf(e)); break;
        case '%B': date.setMonth(i18n.MONTHS.indexOf(e)); break;
        case '%C': break; //century
        case '%d':
        case '%e': date.setDate(parseInt(e,10)); break;
        case '%j': break; // day of year
        case '%m': date.setMonth(parseInt(e,10)-1); break;
        case '%w': date.setDay(parseInt(e,10)); break;
        case '%y':
          var year = parseInt(e,10);
          if (year<50)  year+=2000;
          if (year<100) year+=1900;
          date.setYear(year);
          break;
        case '%Y': date.setFullYear(parseInt(e,10)); break;

        // time
        case '%H': date.setHours(parseInt(e,10)); break;
        case '%I': date.setHours(parseInt(e,10)); break;
        case '%M': date.setMinutes(parseInt(e,10)); break;
        case '%p': if(e=='pm') date.setHours(date.getHours()+12); break;
        case '%S': date.setSeconds(parseInt(e,10)); break;
      }
    });
    return date;
  }
});
Element.addMethods({
  center: function( container, element ) {
    element = $(element);
    container = $(container);
    var cBorders = container.borderDimensions(), eBorders = element.borderDimensions();
    var height = container.getHeight()-(cBorders.top + cBorders.bottom);
    var width = container.getWidth()-(cBorders.left + cBorders.right);

    var setX = ((width-element.getWidth()-(eBorders.left + eBorders.right))/2);
    var setY = ((height-element.getHeight()-(eBorders.top + eBorders.bottom))/2);

    setX = (setX < 0) ? 0 : setX;
    setY = (setY < 0) ? 0 : setY;
    container.relativize();

    return element.setStyle({ top: setY + 'px', left: setX + 'px' });
  },

  borderDimensions: function( element ) {
    return $w('top bottom left right').inject({}, function(dims, key) {
      dims[key] = parseFloat(element.getStyle('border-' + key + '-width') || 0);
      return dims;
    });
  }
});

/*
  Class: UI.Calendar

  Author: Minho Kim
*/
UI.Calendar = Class.create(UI.Options, {
  options: {
    theme: 'mac_os_x',
    format: '%m/%d/%Y',
    startWeekday: 0,
    startDate: new Date()
  },

  initialize: function(element, options) {
    this.setOptions(options);

    this.element = $(element);
    this.element.identify();

    this.container = new Element('div').addClassName('ui_calendar_container');
    this.container.setStyle({ position: 'relative' });
    this.element.addClassName(this.options.theme).insert({ top: this.container });

    this.initDate(this.options.startDate);
    this.buildTable();
    this.buildSelector();
    this.update(this.date);
  },

  // Group: Event handling

  /*
    Method: fire
      Fires a calendar custom event automatically namespaced in "calendar:" (see Prototype custom events).
      The memo object contains a "calendar" property referring to the calendar.

    Parameters:
      eventName - an event name
      memo      - a memo object

    Returns:
      fired event
  */
  fire: function(eventName, memo) {
    memo = memo || { };
    memo.calendar = this;
    return this.element.fire('calendar:' + eventName, memo);
  },

  /*
    Method: observe
      Observe a calendar event with a handler function automatically bound to the calendar

    Parameters:
      eventName - an event name
      handler   - a handler function

    Returns:
      this
  */
  observe: function(eventName, handler) {
    this.element.observe('calendar:' + eventName, handler.bind(this));
    return this;
  },

  /*
    Method: stopObserving
      Unregisters a calendar event, it must take the same parameters as this.observe (see Prototype stopObserving).

    Parameters:
      eventName - an event name
      handler   - a handler function

    Returns:
      this
  */
  stopObserving: function(eventName, handler) {
	  this.element.stopObserving('calendar:' + eventName, handler);
	  return this;
  },

  generateId: function(name) {
    return this.element.id + name;
  },

  initDate: function(date) {
    this.date = this.convertDate(date);
  },

  convertDate: function(date) {
    if (!date) return null;
    return Object.isString(date) ? Date.parseString(date, this.options.format) : date;
  },

  update: function(newDate) {
    this.updateDaysRow();
    this.date = newDate;
    var today = new Date();
    this.headerSpan.innerHTML = UI.Calendar.Options.MONTHS[this.date.getMonth()] + ' ' + this.date.getFullYear();

    var days = $R(this.startDay(this.date), this.lastDay(this.date));
    if (days.size() < 42) {
      days = $R(this.startDay(this.date), this.lastDay(this.date).addDays(42 - days.size()));
    }
    days = $A(days);

    var day, cell, classNames, monthDate, index, l;
    for (index = 0, l = days.length; index < l; ++index) {
      day = days[index];
      cell = this.cells[index];
      classNames = [];
      cell.date = day;

      monthDate = day.getDate();

      if (day.getMonth() != this.date.getMonth()) {
        classNames.push('non_current');
        cell.innerHTML = monthDate;
      } else {
        cell.innerHTML = '<a href="#">' + monthDate + '</a>';
        if (this.selectedDay && this.selectedDay.equalsDate(day)) classNames.push('selected');
      }
      if (today.equalsDate(day)) classNames.push('today');
      if (cell.hasClassName('weekend')) classNames.push('weekend');
      if (cell.hasClassName('first')) classNames.push('first');
      if (cell.hasClassName('last')) classNames.push('last');

      cell.className = classNames.join(' ');
    }
  },

  updateDaysRow: function() {
    var dayNames = this.dayNames();
    this.daysRow.update('');
    $R(0, 6).each(function(n){
      this.daysRow.insert({
        bottom: new Element('th', {'class': 'dayname'}).update(dayNames[n].truncate(2,''))
      });
    }.bind(this));
  },

  onCellClick: function(event) {
    event.stop();
    var element = event.element();
    if (element.tagName == 'A') element = element.up('td');
    if (element.hasClassName('non_current')) return;
    var day = element.date;
    this.selectedDay = day;

    $w('selected selected_next selected_prev').each(function(e){ this.table.select('.'+e).invoke('removeClassName', e); }.bind(this));

    element.addClassName('selected');
    var next = element.next(), prev = element.previous();
    if (next) next.addClassName('selected_next');
    if (prev) prev.addClassName('selected_prev');

    this.fire('click', {
      date: day,
      formattedDate: day.strftime(this.options.format)
    });
  },

  onMonthClick: function(event) {
    event.stop();
    this.selector.setStyle({
      position: 'absolute',
      top: 0,
      left: 0,
      'float': 'left',
      zIndex: 3
    });
    this._showSelector();
  },

  _showMask: function() {
    // Stupid hack because IE browsers do not support css width/height: 100% sometimes
    if (Prototype.Browser.IE) {
      var borderDimensions = this.container.borderDimensions();
      if (!this.containerWidth) {
        this.containerWidth = this.container.getWidth()-(borderDimensions.left + borderDimensions.right);
      }
      if (!this.containerHeight) {
        this.containerHeight = this.container.getHeight()-(borderDimensions.top + borderDimensions.bottom);
      }
      this.mask.setStyle({
        width  : this.containerWidth + 'px',
        height : this.containerHeight + 'px'
      });
    }
    this.mask.show();
  },

  _hideSelector: function() {
    this.mask.hide();
    this.selector.hide();
  },

  _showSelector: function() {
    this.container.center(this.selector);
    this._showMask();
    this.selector.show();
  },

  buildTable: function() {
    this.table = new Element('table').addClassName('ui_calendar');
    this.table.setStyle({ position: 'relative' });

    this.buildHeader(this.date);

    var tbody = new Element('tbody');

    $R(1, 42).inGroupsOf(7).each(function(week, index) {
      var row = new Element('tr');
      week.each(function(day, i){
        var cell = new Element('td');
        if (i == 0 || i == 6) cell.addClassName('weekend');
        if (i == 0) cell.addClassName('first');
        if (i == 6) cell.addClassName('last');
        row.insert({ bottom: cell});
      });
      tbody.insert({ bottom: row });
    }.bind(this));

    this.cells = tbody.select('td');
    this.cells.invoke('observe', 'click', this.onCellClick.bind(this));
    this.table.insert({ bottom: tbody });
    this.container.insert({ top: this.table });
  },

  buildSelector: function() {
    this.selector = new Element('div').addClassName('selector').hide();

    this.mask = new Element('div').hide().addClassName('ui_calendar_mask').setOpacity(0.3);

    this.container.insert({ bottom: this.selector })
                  .insert({ bottom: this.mask });

    this.selector.insert({
      top: new Element('label', {
        'for': this.generateId('_select')
      }).update(UI.Calendar.Options.LABEL_MONTH)
    });

    var select = new Element('select');
    for (var i = 0; i < 12; i++) {
      select.insert({ bottom: new Element('option', { value: i }).update(UI.Calendar.Options.MONTHS[i]) });
    }

    this.selector.insert({
      bottom: select
    }).insert({
      bottom: new Element('label', {
        'for' :  this.generateId('_input')
      }).update(UI.Calendar.Options.LABEL_YEAR)
    });

    var input = new Element('input', {
      type: 'text',
      size: 4, maxLength: 4,
      value: this.date.getFullYear()
    });

    this.selector.insert({ bottom: input });

    var createButton = function(name, onClick) {
      return new Element('span').addClassName('ui_calendar_button')
                                .insert({ top: new Element('button', {type: 'button'}).update(name)
                                                                                      .observe('click', onClick.bind(this)) });
    };

    var btnCn = createButton('Cancel', function(e) {
      this._hideSelector();
    }.bind(this));

    var btnOk = createButton('OK', function(e) {
      this._hideSelector();
      this.update(new Date(input.value, select.value, 1));
    }.bind(this));

    this.selector.insert({
      bottom: new Element('div', {
        textAlign: 'center',
        width: '100%'
      }).addClassName('ui_calendar_button_div')
        .insert({ bottom: btnCn })
        .insert({ bottom: btnOk })
    });
  },

  buildHeader: function(date) {
    var header = new Element('thead');
    this.daysRow = new Element('tr', {
      id: this.generateId('_days_row')
    });

    var initMonthLink = function(link, direction, self) {
      link
        .observe('click', function(e) {
          e.stop();
          self[direction+'Month'].call(self);
        })
        .observe('mousedown', function(e) {
          var p = new PeriodicalExecuter(function(pe){
            self[direction+'Month'].call(self);
          }, .25);
          document.observe('mouseup', function(e){ p.stop(); });
        });
    };

    $w('prev next').each(function(d){
      this[d + 'Link'] = new Element('a').addClassName(d);
      initMonthLink(this[d + 'Link'], d, this);
    }.bind(this));

    this.headerSpan = new Element('a', {
      href: '#'
    }).update(UI.Calendar.Options.MONTHS[date.getMonth()] + ' ' + date.getFullYear()).observe('click', this.onMonthClick.bind(this));

    var headerDiv = new Element('div').addClassName('header')
                                      .insert({ bottom: this.prevLink })
                                      .insert({ bottom: this.headerSpan })
                                      .insert({ bottom: this.nextLink });

    this.updateDaysRow();

    header.insert({ bottom: new Element('tr').insert({ top: new Element('th', {colspan: 7}).update(headerDiv).addClassName('monthname') }) })
          .insert({ bottom: this.daysRow });

    this.table.insert({ top: header });
  },

  nextMonth: function() {
    this.date.setMonth(this.date.getMonth() + 1);
    this.update(this.date);
  },

  prevMonth: function() {
    this.date.setMonth(this.date.getMonth() - 1);
    this.update(this.date);
  },

  startDay: function(date) {
    var startDate = date.firstOfMonth();
  	startDate.setDate(-(startDate.getDay() % 7));
  	startDate.setDate(startDate.getDate() + 1 + parseInt(this.options.startWeekday));
    return startDate;
  },

  lastDay: function(date) {
    var endDate = date.endOfMonth();
  	endDate.setDate(endDate.getDate() + 6 - (endDate.getDay() % 7));
    return endDate;
  },

  dayNames: function() {
    var days = UI.Calendar.Options.WEEKDAYS.slice(this.options.startWeekday);
    for (var i = 0; i < this.options.startWeekday; i++) days.push(UI.Calendar.Options.WEEKDAYS[i]);
    return days;
  },

  setStartWeekday: function(start) {
    this.options.startWeekday = start;
    this.update(this.date);
  }
});

UI.Calendar.Options = {
	MONTHS_SHORT: $w('Jan Feb Mar Apr May Jun Jul Aug Sep Oct Nov Dec'),
	MONTHS: $w('January February March April May June July August September October November December'),
	WEEKDAYS_1CHAR: $w('S M T W T F S'),
	WEEKDAYS_SHORT: $w('Su Mo Tu We Th Fr Sa'),
	WEEKDAYS_MEDIUM: $w('Sun Mon Tue Wed Thu Fri Sat'),
	WEEKDAYS: $w('Sunday Monday Tuesday Wednesday Thursday Friday Saturday'),

	FORMAT_DATE: '%m/%d/%Y',
	FORMAT_TIME: '%H:%M:%S',
	FORMAT_DATETIME: '%x %X',

	LABEL_MONTH: "Month",
	LABEL_YEAR: "Year"
};
/*
  Class: UI.Carousel

  Main class to handle a carousel of elements in a page. A carousel :
    * could be vertical or horizontal
    * works with liquid layout
    * is designed by CSS

  Assumptions:
    * Elements should be from the same size

  Example:
    > ...
    > <div id="horizontal_carousel">
    >   <div class="previous_button"></div>
    >   <div class="container">
    >     <ul>
    >       <li> What ever you like</li>
    >     </ul>
    >   </div>
    >   <div class="next_button"></div>
    > </div>
    > <script>
    > new UI.Carousel("horizontal_carousel");
    > </script>
    > ...
*/
UI.Carousel = Class.create(UI.Options, {
  // Group: Options
  options: {
	// Property: direction
	//   Can be horizontal or vertical, horizontal by default
    direction               : "horizontal",

    // Property: previousButton
    //   Selector of previous button inside carousel element, ".previous_button" by default,
    //   set it to false to ignore previous button
    previousButton          : ".previous_button",

    // Property: nextButton
    //   Selector of next button inside carousel element, ".next_button" by default,
    //   set it to false to ignore next button
    nextButton              : ".next_button",

    // Property: container
    //   Selector of carousel container inside carousel element, ".container" by default,
    container               : ".container",

    // Property: scrollInc
    //   Define the maximum number of elements that gonna scroll each time, auto by default
    scrollInc               : "auto",

    // Property: disabledButtonSuffix
    //   Define the suffix classanme used when a button get disabled, to '_disabled' by default
    //   Previous button classname will be previous_button_disabled
    disabledButtonSuffix : '_disabled',

    // Property: overButtonSuffix
    //   Define the suffix classanme used when a button has a rollover status, '_over' by default
    //   Previous button classname will be previous_button_over
    overButtonSuffix : '_over'
  },

  /*
    Group: Attributes

      Property: element
        DOM element containing the carousel

      Property: id
        DOM id of the carousel's element

      Property: container
        DOM element containing the carousel's elements

      Property: elements
        Array containing the carousel's elements as DOM elements

      Property: previousButton
        DOM id of the previous button

      Property: nextButton
        DOM id of the next button

      Property: posAttribute
        Define if the positions are from left or top

      Property: dimAttribute
        Define if the dimensions are horizontal or vertical

      Property: elementSize
        Size of each element, it's an integer

      Property: nbVisible
        Number of visible elements, it's a float

      Property: animating
        Define whether the carousel is in animation or not
  */

  /*
    Group: Events
      List of events fired by a carousel

      Notice: Carousel custom events are automatically namespaced in "carousel:" (see Prototype custom events).

      Examples:
        This example will observe all carousels
        > document.observe('carousel:scroll:ended', function(event) {
        >   alert("Carousel with id " + event.memo.carousel.id + " has just been scrolled");
        > });

        This example will observe only this carousel
        > new UI.Carousel('horizontal_carousel').observe('scroll:ended', function(event) {
        >   alert("Carousel with id " + event.memo.carousel.id + " has just been scrolled");
        > });

      Property: previousButton:enabled
        Fired when the previous button has just been enabled

      Property: previousButton:disabled
        Fired when the previous button has just been disabled

      Property: nextButton:enabled
        Fired when the next button has just been enabled

      Property: nextButton:disabled
        Fired when the next button has just been disabled

      Property: scroll:started
        Fired when a scroll has just started

      Property: scroll:ended
        Fired when a scroll has been done,
        memo.shift = number of elements scrolled, it's a float

      Property: sizeUpdated
        Fired when the carousel size has just been updated.
        Tips: memo.carousel.currentSize() = the new carousel size
  */

  // Group: Constructor

  /*
    Method: initialize
      Constructor function, should not be called directly

    Parameters:
      element - DOM element
      options - (Hash) list of optional parameters

    Returns:
      this
  */
  initialize: function(element, options) {
    this.setOptions(options);
    this.element = $(element);
    this.id = this.element.id;
    this.container   = this.element.down(this.options.container).firstDescendant();
    this.elements    = this.container.childElements();
    this.previousButton = this.options.previousButton == false ? null : this.element.down(this.options.previousButton);
    this.nextButton = this.options.nextButton == false ? null : this.element.down(this.options.nextButton);

    this.posAttribute = (this.options.direction == "horizontal" ? "left" : "top");
    this.dimAttribute = (this.options.direction == "horizontal" ? "width" : "height");

    this.elementSize = this.computeElementSize();
    this.nbVisible = Math.ceil(this.currentSize() / this.elementSize);

    var scrollInc = this.options.scrollInc;
    if (scrollInc == "auto")
      scrollInc = Math.floor(this.nbVisible);
    [ this.previousButton, this.nextButton ].each(function(button) {
      if (!button) return;
      var className = (button == this.nextButton ? "next_button" : "previous_button") + this.options.overButtonSuffix;
      button.clickHandler = this.scroll.bind(this, (button == this.nextButton ? -1 : 1) * scrollInc * this.elementSize);
      button.observe("click", button.clickHandler)
            .observe("mouseover", function() {button.addClassName(className)}.bind(this))
            .observe("mouseout",  function() {button.removeClassName(className)}.bind(this));
    }, this);
    this.updateButtons();
  },

  // Group: Destructor

  /*
    Method: destroy
      Cleans up DOM and memory
  */
  destroy: function($super) {
    [ this.previousButton, this.nextButton ].each(function(button) {
      if (!button) return;
        button.stopObserving("click", button.clickHandler);
    }, this);
	  this.element.remove();
	  this.fire('destroyed');
  },

  // Group: Event handling

  /*
    Method: fire
      Fires a carousel custom event automatically namespaced in "carousel:" (see Prototype custom events).
      The memo object contains a "carousel" property referring to the carousel.

    Example:
      > document.observe('carousel:scroll:ended', function(event) {
      >   alert("Carousel with id " + event.memo.carousel.id + " has just been scrolled");
      > });

    Parameters:
      eventName - an event name
      memo      - a memo object

    Returns:
      fired event
  */
  fire: function(eventName, memo) {
    memo = memo || { };
    memo.carousel = this;
    return this.element.fire('carousel:' + eventName, memo);
  },

  /*
    Method: observe
      Observe a carousel event with a handler function automatically bound to the carousel

    Parameters:
      eventName - an event name
      handler   - a handler function

    Returns:
      this
  */
  observe: function(eventName, handler) {
    this.element.observe('carousel:' + eventName, handler.bind(this));
    return this;
  },

  /*
    Method: stopObserving
      Unregisters a carousel event, it must take the same parameters as this.observe (see Prototype stopObserving).

    Parameters:
      eventName - an event name
      handler   - a handler function

    Returns:
      this
  */
  stopObserving: function(eventName, handler) {
	  this.element.stopObserving('carousel:' + eventName, handler);
	  return this;
  },

  // Group: Actions

  /*
    Method: checkScroll
      Check scroll position to avoid unused space at right or bottom

    Parameters:
      position       - position to check
      updatePosition - should the container position be updated ? true/false

    Returns:
      position
  */
  checkScroll: function(position, updatePosition) {
    if (position > 0)
      position = 0;
    else {
      var limit = this.elements.last().positionedOffset()[this.posAttribute] + this.elementSize;
      var carouselSize = this.currentSize();

      if (position + limit < carouselSize)
        position += carouselSize - (position + limit);
      position = Math.min(position, 0);
    }
    if (updatePosition)
      this.container.style[this.posAttribute] = position + "px";

    return position;
  },

  /*
    Method: scroll
      Scrolls carousel from maximum deltaPixel

    Parameters:
      deltaPixel - a float

    Returns:
      this
  */
  scroll: function(deltaPixel) {
    if (this.animating)
      return this;

    // Compute new position
    var position =  this.currentPosition() + deltaPixel;

    // Check bounds
    position = this.checkScroll(position, false);

    // Compute shift to apply
    deltaPixel = position - this.currentPosition();
    if (deltaPixel != 0) {
      this.animating = true;
      this.fire("scroll:started");

      var that = this;
      // Move effects
      this.container.morph("opacity:0.5", {duration: 0.2, afterFinish: function() {
        that.container.morph(that.posAttribute + ": " + position + "px", {
          duration: 0.4,
          delay: 0.2,
          afterFinish: function() {
            that.container.morph("opacity:1", {
              duration: 0.2,
              afterFinish: function() {
                that.animating = false;
                that.updateButtons()
                  .fire("scroll:ended", { shift: deltaPixel / that.currentSize() });
              }
            });
          }
        });
      }});
    }
    return this;
  },

  /*
    Method: scrollTo
      Scrolls carousel, so that element with specified index is the left-most.
      This method is convenient when using carousel in a tabbed navigation.
      Clicking on first tab should scroll first container into view, clicking on a fifth - fifth one, etc.
      Indexing starts with 0.

    Parameters:
      Index of an element which will be a left-most visible in the carousel

    Returns:
      this
  */
  scrollTo: function(index) {
    if (this.animating || index < 0 || index > this.elements.length || index == this.currentIndex() || isNaN(parseInt(index)))
      return this;
    return this.scroll((this.currentIndex() - index) * this.elementSize);
  },

  /*
    Method: updateButtons
      Update buttons status to enabled or disabled
      Them status is defined by classNames and fired as carousel's custom events

    Returns:
      this
  */
  updateButtons: function() {
	  this.updatePreviousButton();
    this.updateNextButton();
    return this;
  },

  updatePreviousButton: function() {
    if (!this.previousButton)
      return;
    var position = this.currentPosition();
    var previousClassName = "previous_button" + this.options.disabledButtonSuffix;

    if (this.previousButton.hasClassName(previousClassName) && position != 0) {
      this.previousButton.removeClassName(previousClassName);
      this.fire('previousButton:enabled');
    }
    if (!this.previousButton.hasClassName(previousClassName) && position == 0) {
	    this.previousButton.addClassName(previousClassName);
      this.fire('previousButton:disabled');
    }
  },

  updateNextButton: function() {
    if (!this.nextButton)
      return;
    var lastPosition = this.currentLastPosition();
    var size = this.currentSize();
    var nextClassName = "next_button" + this.options.disabledButtonSuffix;

    if (this.nextButton.hasClassName(nextClassName) && lastPosition != size) {
      this.nextButton.removeClassName(nextClassName);
      this.fire('nextButton:enabled');
    }
    if (!this.nextButton.hasClassName(nextClassName) && lastPosition == size) {
	    this.nextButton.addClassName(nextClassName);
      this.fire('nextButton:disabled');
    }
  },

  // Group: Size and Position

  /*
    Method: computeElementSize
      Return elements size in pixel, height or width depends on carousel orientation.

    Returns:
      an integer value
  */
  computeElementSize: function() {
    return this.elements.first().getDimensions()[this.dimAttribute];
  },

  /*
    Method: currentIndex
      Returns current visible index of a carousel.
      For example, a horizontal carousel with image #3 on left will return 3 and with half of image #3 will return 3.5
      Don't forget that the first image have an index 0

    Returns:
      a float value
  */
  currentIndex: function() {
    return - this.currentPosition() / this.elementSize;
  },

  /*
    Method: currentLastPosition
      Returns the current position from the end of the last element. This value is in pixel.

    Returns:
      an integer value, if no images a present it will return 0
  */
  currentLastPosition: function() {
    if (this.container.childElements().empty())
      return 0;
    return this.currentPosition() +
           this.elements.last().positionedOffset()[this.posAttribute] +
           this.elementSize;
  },

  /*
    Method: currentPosition
      Returns the current position in pixel.
      Tips: To get the position in elements use currentIndex()

    Returns:
      an integer value
  */
  currentPosition: function() {
    return this.container.getNumStyle(this.posAttribute);
  },

  /*
    Method: currentSize
      Returns the current size of the carousel in pixel

    Returns:
      Carousel's size in pixel
  */
  currentSize: function() {
    return this.container.parentNode.getDimensions()[this.dimAttribute];
  },

  /*
    Method: updateSize
      Should be called if carousel size has been changed (usually called with a liquid layout)

    Returns:
      this
  */
  updateSize: function() {
    this.nbVisible = this.currentSize() / this.elementSize;
    var scrollInc = this.options.scrollInc;
    if (scrollInc == "auto")
      scrollInc = Math.floor(this.nbVisible);

    [ this.previousButton, this.nextButton ].each(function(button) {
      if (!button) return;
      button.stopObserving("click", button.clickHandler);
      button.clickHandler = this.scroll.bind(this, (button == this.nextButton ? -1 : 1) * scrollInc * this.elementSize);
      button.observe("click", button.clickHandler);
    }, this);

    this.checkScroll(this.currentPosition(), true);
    this.updateButtons().fire('sizeUpdated');
    return this;
  }
});
/*
  Class: UI.Ajax.Carousel

  Gives the AJAX power to carousels. An AJAX carousel :
    * Use AJAX to add new elements on the fly

  Example:
    > new UI.Ajax.Carousel("horizontal_carousel",
    >   {url: "get-more-elements", elementSize: 250});
*/
UI.Ajax.Carousel = Class.create(UI.Carousel, {
  // Group: Options
  //
  //   Notice:
  //     It also include of all carousel's options
  options: {
	// Property: elementSize
	//   Required, it define the size of all elements
    elementSize : -1,

	// Property: url
	//   Required, it define the URL used by AJAX carousel to request new elements details
    url         : null
  },

  /*
    Group: Attributes

      Notice:
        It also include of all carousel's attributes

      Property: elementSize
        Size of each elements, it's an integer

      Property: endIndex
        Index of the last loaded element

      Property: hasMore
        Flag to define if there's still more elements to load

      Property: requestRunning
        Define whether a request is processing or not

      Property: updateHandler
        Callback to update carousel, usually used after request success

      Property: url
        URL used to request additional elements
  */

  /*
    Group: Events
      List of events fired by an AJAX carousel, it also include of all carousel's custom events

      Property: request:started
        Fired when the request has just started

      Property: request:ended
        Fired when the request has succeed
  */

  // Group: Constructor

  /*
    Method: initialize
      Constructor function, should not be called directly

    Parameters:
      element - DOM element
      options - (Hash) list of optional parameters

    Returns:
      this
  */
  initialize: function($super, element, options) {
    if (!options.url)
      throw("url option is required for UI.Ajax.Carousel");
    if (!options.elementSize)
      throw("elementSize option is required for UI.Ajax.Carousel");

    $super(element, options);

    this.endIndex = 0;
    this.hasMore  = true;

    // Cache handlers
    this.updateHandler = this.update.bind(this);
    this.updateAndScrollHandler = function(nbElements, transport, json) {
	    this.update(transport, json);
	    this.scroll(nbElements);
	  }.bind(this);

    // Run first ajax request to fill the carousel
    this.runRequest.bind(this).defer({parameters: {from: 0, to: Math.ceil(this.nbVisible) - 1}, onSuccess: this.updateHandler});
  },

  // Group: Actions

  /*
    Method: runRequest
      Request the new elements details

    Parameters:
      options - (Hash) list of optional parameters

    Returns:
      this
  */
  runRequest: function(options) {
    this.requestRunning = true;
    new Ajax.Request(this.options.url, Object.extend({method: "GET"}, options));
    this.fire("request:started");
    return this;
  },

  /*
    Method: scroll
      Scrolls carousel from maximum deltaPixel

    Parameters:
      deltaPixel - a float

    Returns:
      this
  */
  scroll: function($super, deltaPixel) {
    if (this.animating || this.requestRunning)
      return this;

    var nbElements = (-deltaPixel) / this.elementSize;
    // Check if there is not enough
    if (this.hasMore && nbElements > 0 && this.currentIndex() + this.nbVisible + nbElements - 1 > this.endIndex) {
      var from = this.endIndex + 1;
      var to   = Math.ceil(from + this.nbVisible - 1);
      this.runRequest({parameters: {from: from, to: to}, onSuccess: this.updateAndScrollHandler.curry(deltaPixel).bind(this)});
      return this;
    }
    else
      $super(deltaPixel);
  },

  /*
    Method: update
      Update the carousel

    Parameters:
      transport - XMLHttpRequest object
      json      - JSON object

    Returns:
      this
  */
  update: function(transport, json) {
    this.requestRunning = false;
    this.fire("request:ended");
    if (!json)
      json = transport.responseJSON;
    this.hasMore = json.more;

    this.endIndex = Math.max(this.endIndex, json.to);
    this.elements = this.container.insert({bottom: json.html}).childElements();
    return this.updateButtons();
  },

  // Group: Size and Position

  /*
    Method: computeElementSize
      Return elements size in pixel

    Returns:
      an integer value
  */
  computeElementSize: function() {
    return this.options.elementSize;
  },

  /*
    Method: updateSize
      Should be called if carousel size has been changed (usually called with a liquid layout)

    Returns:
      this
  */
  updateSize: function($super) {
    var nbVisible = this.nbVisible;
    $super();
    // If we have enough space for at least a new element
    if (Math.floor(this.nbVisible) - Math.floor(nbVisible) >= 1 && this.hasMore) {
      if (this.currentIndex() + Math.floor(this.nbVisible) >= this.endIndex) {
        var nbNew = Math.floor(this.currentIndex() + Math.floor(this.nbVisible) - this.endIndex);
        this.runRequest({parameters: {from: this.endIndex + 1, to: this.endIndex + nbNew}, onSuccess: this.updateHandler});
      }
    }
    return this;
  },

  updateNextButton: function($super) {
    if (!this.nextButton)
      return;
    var lastPosition = this.currentLastPosition();
    var size = this.currentSize();
    var nextClassName = "next_button" + this.options.disabledButtonSuffix;

    if (this.nextButton.hasClassName(nextClassName) && lastPosition != size) {
      this.nextButton.removeClassName(nextClassName);
      this.fire('nextButton:enabled');
    }
    if (!this.nextButton.hasClassName(nextClassName) && lastPosition == size && !this.hasMore) {
	    this.nextButton.addClassName(nextClassName);
      this.fire('nextButton:disabled');
    }
  }
});
