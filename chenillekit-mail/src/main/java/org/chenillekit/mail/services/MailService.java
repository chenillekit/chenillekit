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

import java.io.File;
import javax.activation.DataSource;

import org.apache.commons.mail.Email;

import org.chenillekit.mail.MailMessageHeaders;

/**
 * SMTP tool for sending emails based on <a href="http://jakarta.apache.org/commons/email">commons-email</a>.
 *
 * @version $Id$
 */
public interface MailService<T extends Email>
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
	 *
	 * @deprecated use one of the other two methods: <code>sendPlainTextEmail</code> or
	 *             <code>sendHtmlMail</code>.
	 */
	@Deprecated
	boolean sendEmail(T email);

	/**
	 * send a HTML message.
	 *
	 * @param headers  the mail headers
	 * @param htmlBody the mail body (HTML based)
	 *
	 * @return true if mail successfull send
	 */
	boolean sendHtmlMail(MailMessageHeaders headers, String htmlBody);

	/**
	 * send a HTML message.
	 *
	 * @param headers	 the mail headers
	 * @param htmlBody	the mail body (HTML based)
	 * @param attachments array of files to attach at this mail
	 *
	 * @return true if mail successfull send
	 */
	boolean sendHtmlMail(MailMessageHeaders headers, String htmlBody, File... attachments);

	/**
	 * send a HTML message.
	 *
	 * @param headers	 the mail headers
	 * @param htmlBody	the mail body (HTML based)
	 * @param dataSources array of data sources to attach at this mail
	 *
	 * @return true if mail successfull send
	 */
	boolean sendHtmlMail(MailMessageHeaders headers, String htmlBody, DataSource... dataSources);

	/**
	 * send a plain text message.
	 *
	 * @param headers the mail headers
	 * @param body	the mail body (text based)
	 *
	 * @return true if mail successfull send
	 */
	boolean sendPlainTextMail(MailMessageHeaders headers, String body);

	/**
	 * send a plain text message.
	 *
	 * @param headers	 the mail headers
	 * @param body		the mail body (text based)
	 * @param attachments array of files to attach at this mail
	 *
	 * @return true if mail successfull send
	 */
	boolean sendPlainTextMail(MailMessageHeaders headers, String body, File... attachments);

	/**
	 * send a plain text message.
	 *
	 * @param headers	 the mail headers
	 * @param body		the mail body (text based)
	 * @param dataSources array of data sources to attach at this mail
	 *
	 * @return true if mail successfull send
	 */
	boolean sendPlainTextMail(MailMessageHeaders headers, String body, DataSource... dataSources);
}
