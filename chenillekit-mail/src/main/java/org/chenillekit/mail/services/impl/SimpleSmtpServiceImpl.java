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
package org.chenillekit.mail.services.impl;

import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.commons.mail.Email;
import org.apache.commons.mail.EmailException;
import org.apache.tapestry5.ioc.Resource;
import org.apache.tapestry5.ioc.internal.util.Defense;

import org.chenillekit.mail.services.SmtpService;
import org.slf4j.Logger;

/**
 * simple SMTP tool for sending emails based on <a href="http://jakarta.apache.org/commons/email">commons-email</a>.
 *
 * @author <a href="mailto:homburgs@gmail.com">S.Homburg</a>
 * @version $Id$
 */
public class SimpleSmtpServiceImpl implements SmtpService<Email>
{
    private final Logger logger;
    private final Resource configResource;
    private String smtpServer;
    private int smtpPort;
    private int smtpSSLPort;
    private String smtpUser;
    private String smtpPassword;
    private boolean smtpDebug;
    private boolean smtpSSL;
    private boolean smtpTLS;

    public SimpleSmtpServiceImpl(Logger logger, Resource configResource)
    {
        Defense.notNull(configResource, "configResource");

        this.logger = logger;
        this.configResource = configResource;

        if (!this.configResource.exists())
            throw new RuntimeException(String.format("config resource '%s' not found!", this.configResource.toURL().toString()));

        initService(configResource);
    }

    /**
     * read and check all service parameters.
     */
    private void initService(Resource configResource)
    {
        try
        {
            Configuration configuration = new PropertiesConfiguration(configResource.toURL());
            smtpServer = configuration.getString(PK_SMTP_SERVER);
            smtpPort = configuration.getInt(PK_SMTP_PORT, 25);
            smtpUser = configuration.getString(PK_SMTP_USER);
            smtpPassword = configuration.getString(PK_SMTP_PASSWORD);
            smtpDebug = configuration.getBoolean(PK_SMTP_DEBUG, false);
            smtpSSL = configuration.getBoolean(PK_SMTP_SSL, false);
            smtpTLS = configuration.getBoolean(PK_SMTP_TLS, false);
            smtpSSLPort = configuration.getInt(PK_SMTP_SSLPORT, 465);

            if (smtpServer == null || smtpServer.length() == 0)
                throw new RuntimeException(String.format("key %s has no value", PK_SMTP_SERVER));
        }
        catch (ConfigurationException e)
        {
            throw new RuntimeException(e);
        }
    }

    /**
     * send an email.
     */
    public boolean sendEmail(Email email)
    {
        boolean sended = true;

        try
        {
            email.setHostName(smtpServer);
            email.setAuthentication(smtpUser, smtpPassword);
            email.setDebug(smtpDebug);
            email.setSmtpPort(smtpPort);
            email.setSSL(smtpSSL);
            email.setSslSmtpPort(String.valueOf(smtpSSLPort));
            email.setTLS(smtpTLS);
            email.setTLS(smtpTLS);
            email.send();
        }
        catch (EmailException e)
        {
            logger.error(e.getLocalizedMessage(), e);
            sended = false;
        }

        return sended;
    }
}
