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
package org.chenillekit.mail.services;

import org.apache.commons.mail.Email;

/**
 * e-mailer tool send text based mail away based on <a href="http://jakarta.apache.org/commons/email">commons-email</a>.
 *
 * @author <a href="mailto:shomburg@hsofttec.com">S.Homburg</a>
 * @version $Id$
 */
public interface SmtpService<T extends Email>
{
    static final String CONFIG_KEY = "smtp.properties";
    static final String PK_SMTP_SERVER = "smtp.server";
    static final String PK_SMTP_PORT = "smtp.port";
    static final String PK_SMTP_USER = "smtp.user";
    static final String PK_SMTP_PASSWORD = "smtp.password";
    static final String PK_SMTP_DEBUG = "smtp.debug";
    static final String PK_SMTP_SSL = "smtp.ssl";
    static final String PK_SMTP_SSLPORT = "smtp.sslport";
    static final String PK_SMTP_TLS = "smtp.tls";

    /**
     * send an email.
     */
    boolean sendEmail(T email);
}
