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

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.Map;

import org.apache.tapestry5.SelectModel;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Service;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.ioc.internal.util.CollectionFactory;
import org.apache.tapestry5.json.JSONObject;
import org.apache.tapestry5.util.EnumSelectModel;

import org.chenillekit.demo.utils.FreeMarkerTemplateEnum;
import org.chenillekit.template.services.TemplateService;

/**
 * @version $Id$
 */
public class FreeMarkerDemo extends AbstractTemplateDemo
{
    @Persist(value = "flash")
    private FreeMarkerTemplateEnum selectedTemplate;

    @Inject
    @Service(value = "FreeMarkerService")
    private TemplateService templateService;

    public FreeMarkerTemplateEnum getSelectedTemplate()
    {
        return selectedTemplate;
    }

    public void setSelectedTemplate(FreeMarkerTemplateEnum selectedTemplate)
    {
        this.selectedTemplate = selectedTemplate;
        setTemplateEnum(selectedTemplate);
    }

    /**
     * send the renderable script source to screen.
     */
    JSONObject onChangeFromSelectTemplate(FreeMarkerTemplateEnum template) throws IOException
    {
        return super.onSelectTemplate(template);
    }

    /**
     * get the template select model.
     */
    public SelectModel getTemplateSelectModel()
    {
        return new EnumSelectModel(FreeMarkerTemplateEnum.class, getMessages());
    }

    /**
     * get the renderable script source.
     */
    public String getEvaluatedString() throws IOException
    {
        if (selectedTemplate == null)
            return "";

        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        Map parameterMap = CollectionFactory.newMap();
        parameterMap.put("myText", getTextAreaValue());
        parameterMap.put("sysDate", new Date());
        parameterMap.put("ipAdress", getRequestGlobals().getHTTPServletRequest().getRemoteAddr());
        templateService.mergeDataWithStream(getTemplateStream(selectedTemplate.getName()), bos, parameterMap);

        return new String(bos.toByteArray());
    }
}