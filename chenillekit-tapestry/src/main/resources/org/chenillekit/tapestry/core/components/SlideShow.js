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
 * @author Chris Lewis Nov 8, 2007 <chris@thegodcode.net>
 * @version $Id$
 */
Ck.SlideShow = Class.create();

/*
 * Slide transitions are responsible for managing the effect
 * and action of changing the current slide in the slideshow.
 * A transition is just an object with a single method:
 *
 *     transition(from, to)
 *
 * This method takes two arguments: 'from' and 'to'. These
 * are references to the slide being moved out of view (from)
 * and the one being moved into view (to).
 * It is possible that the 'from' will be null. When this
 * happens it means that the slide show is just starting
 * and no slides have yet been rendered.
 * Following are a few canned transitions.
 */
Ck.SlideShow.Txs = {};
Ck.SlideShow.Txs.Crossfade = Class.create();
Ck.SlideShow.Txs.Crossfade.prototype = {
	initialize: function(opts)
	{
		this.opts = opts;
	},

	transition: function(from, to)
	{
		if (from != null)
		{
			Effect.Fade(from, this.opts);
		}
		Effect.Appear(to, this.opts);
	}
}

Ck.SlideShow.Txs.Grow = Class.create();
Ck.SlideShow.Txs.Grow.prototype = {
	initialize: function(opts)
	{
		this.opts = opts;
	},

	transition: function(from, to)
	{
		if (from != null)
		{
			Effect.Shrink(from, this.opts);
		}
		Effect.Grow(to, this.opts);
	}
}

/* Prebuilt transitions for easy use. */
Ck.SlideShow.Tx = {};
Ck.SlideShow.Tx.Crossfade = new Ck.SlideShow.Txs.Crossfade({ duration: 1 });
Ck.SlideShow.Tx.Grow = new Ck.SlideShow.Txs.Grow({ duration: 1 });
Ck.SlideShow.Tx.Abrupt = {
	transition: function(from, to)
	{
		if (from != null)
		{
			from.hide();
		}
		to.show();
	}
}


Ck.SlideShow.prototype = {

	/**
	 * Resulting state for 'this':
	 * slides - slide collection (html elements)
	 * interval - the interval for cycling slides
	 * loop - flag showing if the show will loop
	 * pauseOnHover - flag showing if slide cycling should pause when the user is hovering
	 * slideIndex - current slide index
	 * tx - object providing the slide transition
	 * pe - PeriodicalExecuter timing the slide changes
	 *
	 * @param e The DOM id of the slideshow parent element.
	 * @param options Transfer object with slideshow configurations.
	 */
	initialize: function(e, options)
	{
		//gc-slideshow-slides
		this.element = $(e);
		this.slides = $(e).firstDescendant().childElements();
		this.componentHeight = 0;
		this.componentWidth = 0;
		this.slides.each(function(_slide)
		{
			_slide.hide().addClassName('ck-slide');

			if (_slide.height > this.componentHeight)
				this.componentHeight = _slide.height;

			if (_slide.width > this.componentWidth)
				this.componentWidth = _slide.width;
		}.bind(this));

		if (options.calculateElementSize)
			this.element.setStyle({'height': this.componentHeight + "px", 'width': this.componentWidth + "px"});

		this.slideIndex = 0;
		this.loop = options.loop;
		this.interval = options.interval;
		this.controls = options.controls;
		this.pauseOnHover = options.pauseOnHover;

		if (this.pauseOnHover)
		{
			Event.observe($(e), 'mouseover', this.pause.bind(this));
			Event.observe($(e), 'mouseout', this.play.bind(this));
		}

		//TODO clean this up
		if (this.controls) new Ck.SlideShowControls(this);

		// This is a reference to an object as a string, so we must eval().
		this.tx = eval(options.transition);
		// Fire this here so the first slide renders immediately.
		this.tx.transition(null, this.slides[0]);

		this.play();
	},

	play: function()
	{
		//TODO should not have to find the 'false' arg explicitly here
		this.pe = new PeriodicalExecuter(this.next.bind(this, false), this.interval);
	},

	isPaused: function()
	{
		return this.pe == null;
	},

	pause: function()
	{
		this.pe.stop();
		this.pe = null;
	},

	toggle: function()
	{
		(this.isPaused() ? this.play : this.pause).call(this);
	},

	next: function(skipTx)
	{
		var _currentSlide = this.slides[this.slideIndex];
		if (++this.slideIndex == this.slides.size())
		{
			if (! this.loop)
			{
				this.pe.stop();
				return;
			}
			else
			{
				this.slideIndex = 0;
			}
		}
		this._flipSlide(_currentSlide, this.slides[this.slideIndex], skipTx);
	},

	previous: function(skipTx)
	{
		var _currentSlide = this.slides[this.slideIndex];
		if (--this.slideIndex < 0)
		{
			this.slideIndex = this.slides.size() - 1;
		}
		this._flipSlide(_currentSlide, this.slides[this.slideIndex], skipTx);
	},

	_flipSlide: function(from, to, skipTx)
	{
		//TODO when someone is manually advancing slides, the timer should stop
		(skipTx ? Ck.SlideShow.Tx.Abrupt.transition : this.tx.transition).call(this, from, to);
	}

}


Ck.SlideShowControls = Class.create();
Ck.SlideShowControls.prototype = {
	initialize: function(e, options)
	{
		this.player = e;

		//TODO resolve the control element in a saner way (based on SS id?)
		var ctlE = $(this.player.element.id + '-ctls');
		Event.observe($(e.element), 'mouseover', function()
		{
			ctlE.show()
		});
		Event.observe($(e.element), 'mouseout', function()
		{
			ctlE.hide()
		});

		//TODO need a RELATIVE (and maybe cleaner) way?
		var btns = [ $(this.player.element.id + '-ctl-prev'),
			$(this.player.element.id + '-ctl-play_pause'),
			$(this.player.element.id + '-ctl-next') ];

		Event.observe(btns[0], 'click', this.player.previous.bind(this.player));
		Event.observe(btns[1], 'click', this.player.toggle.bind(this.player));
		Event.observe(btns[2], 'click', this.player.next.bind(this.player, true));
	}
}