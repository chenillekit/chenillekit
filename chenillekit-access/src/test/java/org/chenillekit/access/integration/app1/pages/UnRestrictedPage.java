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

package org.chenillekit.access.integration.app1.pages;

import java.util.Arrays;
import java.util.List;

import org.apache.tapestry5.annotations.Component;
import org.apache.tapestry5.annotations.InjectPage;
import org.apache.tapestry5.annotations.OnEvent;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.corelib.components.Errors;
import org.apache.tapestry5.corelib.components.Form;
import org.apache.tapestry5.corelib.components.Submit;
import org.apache.tapestry5.corelib.components.TextField;

import org.chenillekit.access.annotations.Restricted;

/**
 * @version $Id$
 */
public class UnRestrictedPage
{
    @Property
    private String testValue;

    @Component
    private Errors testErrors;

    @Component
    private Form testForm;

    @Component(parameters = {"value=testValue"})
    private TextField testInput;

    @Component
    private Submit testSubmit;

    @InjectPage
    private Invisible invisible;

    private List<String> context = null;


    void onActivate(List<String> context)
    {
		if (context != null && context.size() > 0)
        	this.context = context;
    }

    List<String> onPassivate()
    {
		return this.context;
    }

    @OnEvent(component = "testRightsRole", value = "action")
    @Restricted(role = 8)
    Object onActionFromTestRightsRole()
    {
        return invisible;
    }

    @Restricted(groups = {"ADMINS"})
    Object onActionFromTestRights()
    {
        return invisible;
    }

    @Restricted(groups = {"ADMINS"})
    @OnEvent(component = "testRightsOnEvent")
    Object thisThrowRuntimeException()
    {
        return invisible;
    }

    @Restricted(groups = {"ADMIN"})
    void onActionFromTestRightsContext(List<String> context)
    {
		this.context = context;
    }

    @Restricted(groups = {"DUMMY"})
    void onActionFromTestForm()
    {
    }

    public List<String> getActionContext()
    {
        return Arrays.asList("first", "second", "third", "forth");
    }

    public String getContextIfPresent()
    {
        return this.context == null || this.context.isEmpty() ?
                "NO CONTEXT" :
                Arrays.toString(context.toArray());
    }

    void onNotEnoughAccessRights()
    {
        System.err.println("onNotEnoughAccessRights");
        testForm.recordError("onNotEnoughAccessRights");
    }
}