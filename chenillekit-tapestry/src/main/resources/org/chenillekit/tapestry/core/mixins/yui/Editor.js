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

Ck.YuiEditor = Class.create();
Ck.YuiEditor.prototype = {
	initialize: function(elementId, imageUploadUrl, options)
	{
		this.elementId = elementId;
		this.options = Object.extend({
			dompath: true,
			animate: true
		}, options || {});

		var editor = new YAHOO.widget.Editor(this.elementId, this.options);

		if (imageUploadUrl != 'null')
			yuiImgUploader(editor, elementId, imageUploadUrl, 'fileElement');

		editor.render();
	}
}


