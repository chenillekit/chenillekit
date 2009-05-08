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
 * ClickSubmit attaches a handler to the specified element. This handler
 * responds to the 'onclick' event, and when triggered, submits the containing
 * form. Note that it expects (requries) the element to be a descendant
 * of a form, and the containing form will be the target for submission.
 *
 * @author Chris Lewis Dec 29, 2007 <chris@thegodcode.net>
 * @version $Id$
 */
Ck.ClickSubmit = Class.create();
Ck.ClickSubmit.prototype = {
    initialize: function(e)
    {
        $(e).ancestors().each(
                function(ae)
                {
                    if (ae.tagName == 'FORM')
                    {
                        Event.observe(e, 'click', function()
                        {
                            ae.submit();
                        });
                    }
                }
                );
    }
}