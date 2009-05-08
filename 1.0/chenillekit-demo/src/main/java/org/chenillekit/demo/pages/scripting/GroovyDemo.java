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

package org.chenillekit.demo.pages.scripting;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Map;

import org.apache.tapestry5.SelectModel;
import org.apache.tapestry5.annotations.Component;
import org.apache.tapestry5.annotations.Mixins;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.corelib.components.Form;
import org.apache.tapestry5.corelib.components.If;
import org.apache.tapestry5.corelib.components.OutputRaw;
import org.apache.tapestry5.corelib.components.Select;
import org.apache.tapestry5.corelib.components.Submit;
import org.apache.tapestry5.ioc.Messages;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.ioc.internal.util.CollectionFactory;
import org.apache.tapestry5.json.JSONObject;
import org.apache.tapestry5.services.Context;
import org.apache.tapestry5.util.EnumSelectModel;

import org.chenillekit.demo.components.LeftSideMenu;
import org.chenillekit.demo.utils.GroovyScriptEnum;
import org.chenillekit.scripting.services.ScriptingService;

/**
 * @version $Id$
 */
public class GroovyDemo
{
    @Inject
    private ScriptingService scriptingService;

    @Inject
    private Context context;

    @Inject
    private Messages messages;

    @Property
    @Persist
    private GroovyScriptEnum selectedScript;

    @Persist("flash")
    @Property
    private Object scriptResult;

    @Component(parameters = {"menuName=scripting"})
    private LeftSideMenu menu;

    @Component
    private Form form;

    @Component
    private Submit buttonSubmit;

    @Component(parameters = {"value=selectedScript", "model=scriptSelectModel", "event=change",
            "onCompleteCallback=scriptSelected"})
    @Mixins(value = {"ck/OnEvent"})
    private Select selectScript;

    @Component(parameters = {"test=ognl:scriptResult != null"})
    private If ifScriptResult;

    @Component(parameters = {"value=scriptResult"})
    private OutputRaw outputRaw;

    @Component(parameters = {"value=scriptString"})
    private OutputRaw outputScriptSource;


    /**
     * Tapestry form submit event method.
     * <p/>
     * evaluates the choosen script and writes result into property <em>scriptResult</em>.
     */
    public void onSuccess()
    {
        Map<String, Object> scriptParameters = CollectionFactory.newMap();
        scriptParameters.put("web_context", context);
        try
        {
            if (selectedScript != null)
                scriptResult = scriptingService.eval("groovy", getScriptString(selectedScript.getName(), false), scriptParameters);
        }
        catch (RuntimeException ex)
        {
            scriptResult = ex.getLocalizedMessage();
        }
        catch (IOException e)
        {
            scriptResult = e.getLocalizedMessage();
        }
    }

    /**
     * get the script select model.
     */
    public SelectModel getScriptSelectModel()
    {
        return new EnumSelectModel(GroovyScriptEnum.class, messages);
    }

    /**
     * send the renderable script source to screen.
     */
    JSONObject onChangeFromSelectScript(String script) throws IOException
    {
        JSONObject json = new JSONObject();

        if (script == null || script.length() == 0)
            return null;


		GroovyScriptEnum gScript = GroovyScriptEnum.valueOf(script);
		String scriptString = getScriptString(gScript.getName(), true);
        if (scriptString.length() == 0)
            json.put("script", String.format("script %s not found!", script));
        else
            json.accumulate("script", scriptString);

        return json;
    }

    /**
     * get the renderable script source.
     */
    public String getScriptString()
    {
        try
        {
            if (selectedScript != null)
                return getScriptString(selectedScript.getName(), true);

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
     * @param scriptName name of the script
     * @param convert    should converted for renderable output
     */
    private String getScriptString(String scriptName, boolean convert) throws IOException
    {
        InputStream scriptStream = this.getClass().getClassLoader().getResourceAsStream("org/chenillekit/demo/pages/scripting/" + scriptName);
        if (scriptStream == null)
            return "";

        BufferedReader br = new BufferedReader(new InputStreamReader(scriptStream));
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
}