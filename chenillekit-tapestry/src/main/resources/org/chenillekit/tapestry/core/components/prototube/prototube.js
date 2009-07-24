/**
	ProtoTube v1b BETA 27.04.2009
	Copyright (c) 2008 Filippo Buratti; info [at] cssrevolt.com [dot] com; http://www.filippoburatti.net/

	Permission is hereby granted, free of charge, to any person obtaining a copy
	of this software and associated documentation files (the "Software"), to deal
	in the Software without restriction, including without limitation the rights
	to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
	copies of the Software, and to permit persons to whom the Software is
	furnished to do so, subject to the following conditions:

	The above copyright notice and this permission notice shall be included in
	all copies or substantial portions of the Software.

	THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
	IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
	FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
	AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
	LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
	OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
	THE SOFTWARE.
*/
var ProtoTube = Class.create({
	
	initialize: function(element, options) {
		
		this.element 	= $(element);
		var url 		= this.element.readAttribute('href');
		this.videoID 	=	url.replace(/^[^v]+v.(.{11}).*/,"$1");
		this.body = $$('body')[0];
		this.html = $$('html')[0];
		
		Prototype.Browser.IE6 = Prototype.Browser.IE && parseInt(navigator.userAgent.substring(navigator.userAgent.indexOf("MSIE")+5)) == 6;
		
		this.options 	= {	
			overlay: true, // if false embed the video player directly		
			// overlay and preview options
			duration: 0.5, // overlay appare/fade effect
			opacity: 0.8,  // overlay opacity
			imagePreview: true, // show video thumb
			imageID: 1, // 0,1,2,3			
			// player configuration
			playerWidth: 425,
			playerHeight: 350,
			fs: 1, // fullscreen button
			autoplay:0,
			loop:0,
			hd:0, // High definition
			showinfo:0, // show video title and rating before start 
			rel:1, // show related video at end			
			// You Tube url
			youtubeVideoUrl: 'http://www.youtube.com/v/',
			youtubeImageUrl: 'http://img.youtube.com/vi/'
		};
		Object.extend(this.options, options || {});
			
		this.options.overlay ? this.setupPreviewOverlay() : this.directEmbed();	
		
	},
	
	setupPreviewOverlay: function() {
 		 	if (this.options.imagePreview){ this.getImagePreview(); }		
			this.addOverlayMarkup();
			Event.observe(this.element, "click", this.showProtoTube.bindAsEventListener(this));		
			Event.observe(this.overlay, 'click', this.hideProtoTube.bindAsEventListener(this));
			Event.observe(this.ProtoTube, 'click',  function(event) { Event.stop(event) });
	},
	
	directEmbed: function() {			
		this.getVideoEmbed(this.element);	
	},
	
	addOverlayMarkup: function(){
		this.ProtoTube = new Element('div', {className:'prototube'});
		this.wrapper = new Element('div').update('<p>Loading video from You Tube...</p>');
		this.ProtoTube.insert(this.wrapper);
		this.overlay = new Element('div', {className:'overlay'});
		this.overlay.insert(this.ProtoTube);
		this.body.insert(this.overlay.hide());
	},

	showProtoTube: function (event) {
		Event.stop(event);
		this.toggleTroubleElements('hidden');
		this.getVideoEmbed(this.wrapper);
		this.ProtoTube.setStyle({ width: this.options.playerWidth+"px", height: this.options.playerHeight+"px", marginTop: "-"+ this.options.playerHeight/2 +"px", marginLeft: "-"+ this.options.playerWidth/2 +"px" });	
		if (Prototype.Browser.IE6) { this.prepareIE('100%', 'hidden'); }
		new Effect.Appear(this.overlay, { duration: this.options.duration,  from: 0.0, to: this.options.opacity });
	},
	
	hideProtoTube: function() {	
		new Effect.Fade(
				this.overlay, { 
					duration: this.options.duration, 
					afterFinish:function() { 
						this.toggleTroubleElements('visible');
						if (Prototype.Browser.IE6) { this.prepareIE("auto", "auto"); }
					}.bind(this)
				});
		},
	
	getVideoEmbed: function(wrapper) {
		var wrapperID = wrapper.identify();
		var flashvars = {
			fs: this.options.fs,
			autoplay: this.options.autoplay,
			loop: this.options.loop,
			hd: this.options.hd,
			showinfo: this.options.showinfo,
			rel: this.options.rel
	
		};
    	var params = { 
			allowScriptAccess: "always",
			allowFullScreen: true,
			menu: false
		};
    	var atts = { };
    	swfobject.embedSWF(this.options.youtubeVideoUrl+this.videoID, wrapperID, this.options.playerWidth, this.options.playerHeight, "9", null, flashvars, params, atts);
	},
	
	getImagePreview: function() {	
		this.image =  new Image();	
		this.element.update(this.image);
		this.image.src  = this.options.youtubeImageUrl + this.videoID + "/" + this.options.imageID + ".jpg";				
	},
	
	toggleTroubleElements: function(visibility) {
		$$('select', 'object', 'embed').each(function(node) { node.style.visibility = visibility });
	},
	
	prepareIE: function(height, overflow) {
		this.body.setStyle({ height: height, overflow: overflow });	
		this.html.setStyle({ height: height, overflow: overflow }); 
	}

});