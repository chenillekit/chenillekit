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

package org.chenillekit.demo.pages.tapcomp;

import org.apache.tapestry5.FieldTranslator;
import org.apache.tapestry5.MarkupWriter;
import org.apache.tapestry5.ValidationException;
import org.apache.tapestry5.annotations.Component;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.corelib.components.Form;
import org.apache.tapestry5.ioc.annotations.Inject;

import org.chenillekit.demo.components.LeftSideMenu;
import org.chenillekit.demo.data.Track;
import org.chenillekit.demo.services.MusicLibrary;
import org.chenillekit.tapestry.core.components.Hidden;

/**
 * @author <a href="mailto:homburgs@gmail.com">shomburg</a>
 * @version $Id$
 */
public class HiddenDemo
{
    @Persist
    @Property
    private long hiddenValue1 = 1l;

    @Persist
    @Property
    private String hiddenValue2 = "TestString";

    @Property
    @Inject
    private MusicLibrary musicLibrary;

    @Property
    @Persist
    private Track hiddenValue3;

    @Component(parameters = {"menuName=demo"})
    private LeftSideMenu menu;

    @Component
    private Form form;

    @Component(parameters = {"value=hiddenValue1"})
    private Hidden hidden1;

    @Component(parameters = {"value=hiddenValue2"})
    private Hidden hidden2;

    @Component(parameters = {"value=hiddenValue3", "translate=prop:trackTranslator"})
    private Hidden hidden3;

    /**
     * Tapestry render phase method.
     * Initialize temporary instance variables here.
     */
    private void setupRender(MarkupWriter writer)
    {
        hiddenValue3 = musicLibrary.getById(365);
    }


    public FieldTranslator getTrackTranslator()
    {
        return new FieldTranslator<Track>()
        {

            /**
             * Returns the type of the server-side value.
             *
             * @return a type
             */
            public Class<Track> getType()
            {
                return Track.class;
            }

            /**
             * Invoked after the client-submitted value has been {@link org.apache.tapestry5.Translator translated} to check that the value conforms
             * to expectations (often, in terms of minimum or maximum value). If and only if the value is approved by all
             * Validators is the value applied by the field.
             *
             * @throws org.apache.tapestry5.ValidationException
             *          if the value violates the constraint
             */
            public Track parse(String input) throws ValidationException
            {
                if (input == null || input.equals("0"))
                    return null;

                return musicLibrary.getById(Long.parseLong(input));
            }

            /**
             * Converts a server-side value to a client-side string. This allows for formatting of the value in a way
             * appropriate to the end user.
             *
             * @param value the server side value (which will not be null)
             *
             * @return client-side value to present to the user
             *
             * @see org.apache.tapestry5.Translator#toClient(Object)
             */
            public String toClient(Track value)
            {
                Long id = value.getId();

                if (id == null)
                    id = 0l;

                return String.valueOf(id);
            }

            /**
             * Invokes {@link org.apache.tapestry5.Translator#render(org.apache.tapestry5.Field , String, org.apache.tapestry5.MarkupWriter ,org.apache.tapestry5.services.FormSupport)}. This is
             * called at a point "inside" the tag, so that additional attributes may be added.  In many cases, the underlying
             * {@link org.apache.tapestry5.Validator} may write client-side JavaScript to enforce the constraint as well.
             *
             * @param writer markup writer to direct output to.
             *
             * @see org.apache.tapestry5.MarkupWriter#attributes(Object[])
             */
            public void render(MarkupWriter writer)
            {
            }
        };
    }
}
