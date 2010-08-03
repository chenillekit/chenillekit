/*
 * Apache License
 * Version 2.0, January 2004
 * http://www.apache.org/licenses/
 *
 * Copyright 2008-2010 by chenillekit.org
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 */

Tapestry.Initializer.ckwindow = function(ckOptions)
{
    var win = new Window(ckOptions.windowoptions);
    if (ckOptions.hasbody){
      win.setContent(ckOptions.contentid);
    }
    if (ckOptions.show){
      if (ckOptions.center){
        win.showCenter(ckOptions.modal);
      }else{
        win.show(ckOptions.modal);
      }
    }
    $T(ckOptions.clientid).ck_window = win;
};