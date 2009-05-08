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

package org.chenillekit.demo.pages.templating;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.tapestry5.annotations.Component;
import org.apache.tapestry5.annotations.Mixins;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.corelib.components.Form;
import org.apache.tapestry5.corelib.components.OutputRaw;
import org.apache.tapestry5.corelib.components.Select;
import org.apache.tapestry5.corelib.components.Submit;
import org.apache.tapestry5.corelib.components.TextArea;
import org.apache.tapestry5.ioc.Messages;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.json.JSONObject;
import org.apache.tapestry5.services.RequestGlobals;

import org.chenillekit.demo.components.LeftSideMenu;
import org.chenillekit.demo.utils.TemplateEnum;

/**
 * @version $Id$
 */
public abstract class AbstractTemplateDemo
{
    @Inject
    private Messages messages;

    @Persist(value = "flash")
    @Property(read = false)
    private String textAreaValue;

    @Component(parameters = {"menuName=templating"})
    private LeftSideMenu menu;

    @Component
    private Form form;

    @Component(parameters = {"value=textAreaValue"})
    private TextArea textArea;

    @Component
    private Submit submit;

    @Component(parameters = {"value=templateString"})
    private OutputRaw outputRawTemplateString;

    @Component(parameters = {"value=evaluatedString"})
    private OutputRaw outputRawEvaluatedString;

    @Component(parameters = {"value=selectedTemplate", "model=templateSelectModel", "event=change",
            "onCompleteCallback=templateSelected"})
    @Mixins(value = {"ck/OnEvent"})
    private Select selectTemplate;

    @Persist(value = "flash")
    private TemplateEnum templateEnum;

    @Inject
    private RequestGlobals requestGlobals;

    public String getTextAreaValue()
    {
        return textAreaValue;
    }

    /**
     * send the renderable script source to screen.
     */
    JSONObject onSelectTemplate(TemplateEnum template) throws IOException
    {
        JSONObject json = new JSONObject();

        if (template == null)
            return null;


        String templateString = getTemplateString(template.getName(), true);
        if (templateString.length() == 0)
            json.put("template", String.format("template %s not found!", template.getName()));
        else
            json.accumulate("template", templateString);

        return json;
    }

    public TemplateEnum getTemplateEnum()
    {
        return templateEnum;
    }

    public void setTemplateEnum(TemplateEnum templateEnum)
    {
        this.templateEnum = templateEnum;
    }

    /**
     * get the renderable script source.
     */
    public String getTemplateString()
    {
        try
        {
            if (getTemplateEnum() != null)
                return getTemplateString(getTemplateEnum().getName(), true);

            return "";
        }
        catch (IOException e)
        {
            throw new RuntimeException(e);
        }
    }

    /**
     * prepare the script for ouput.
     *
     * @param name    name of the template
     * @param convert should converted for renderable output
     */
    private String getTemplateString(String name, boolean convert) throws IOException
    {
        InputStream templateStream = getTemplateStream(name);
        if (templateStream == null)
            return "";

        BufferedReader br = new BufferedReader(new InputStreamReader(templateStream));
        StringBuilder sb = new StringBuilder();
        String line;

        while ((line = br.readLine()) != null)
        {
            if (convert)
            {
                line = line.replaceAll("\"", "&Prime;").replaceAll("<", "&lt;").replaceAll(">", "&gt;");
                sb.append(line).append("<br/>");
            }
            else
                sb.append(line).append("\n");

        }

        br.close();
        return sb.toString();
    }

    /**
     * prepare the script for ouput.
     *
     * @param name name of the template
     */
    protected InputStream getTemplateStream(String name) throws IOException
    {
        return this.getClass().getClassLoader().getResourceAsStream("org/chenillekit/demo/pages/templating/" + name);
    }

    public abstract String getEvaluatedString() throws IOException;

    public RequestGlobals getRequestGlobals()
    {
        return requestGlobals;
    }

    public Messages getMessages()
    {
        return messages;
    }
}

