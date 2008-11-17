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

import org.apache.commons.mail.Email;
import org.apache.commons.mail.EmailException;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.ioc.annotations.Symbol;

import org.chenillekit.mail.ChenilleKitMailConstants;
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
    private final String smtpServer;
    private final int smtpPort;
    private final String smtpUser;
    private final String smtpPassword;
    private final boolean smtpDebug;
    private final boolean smtpSSL;
    private final boolean smtpTLS;
    private final int smtpSslPort;

    public SimpleSmtpServiceImpl(Logger logger,

                                 @Inject
                                 @Symbol(ChenilleKitMailConstants.SMTP_HOST)
                                 String smtpServer,

                                 @Symbol(ChenilleKitMailConstants.SMTP_PORT)
                                 int smtpPort,

                                 @Inject
                                 @Symbol(ChenilleKitMailConstants.SMTP_USER)
                                 String smtpUser,

                                 @Inject
                                 @Symbol(ChenilleKitMailConstants.SMTP_PASSWORD)
                                 String smtpPassword,

                                 @Symbol(ChenilleKitMailConstants.SMTP_DEBUG)
                                 boolean smtpDebug,

                                 @Symbol(ChenilleKitMailConstants.SMTP_SSL)
                                 boolean smtpSSL,

                                 @Symbol(ChenilleKitMailConstants.SMTP_TLS)
                                 boolean smtpTLS,

                                 @Symbol(ChenilleKitMailConstants.SMTP_SSLPORT)
                                 int smtpSslPort)
    {
        this.logger = logger;
        this.smtpServer = smtpServer;
        this.smtpPort = smtpPort;
        this.smtpUser = smtpUser;
        this.smtpPassword = smtpPassword;
        this.smtpDebug = smtpDebug;
        this.smtpSSL = smtpSSL;
        this.smtpTLS = smtpTLS;
        this.smtpSslPort = smtpSslPort;
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
            email.setSslSmtpPort(String.valueOf(smtpSslPort));
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
