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

import java.io.File;
import java.util.Map;
import javax.activation.DataSource;
import javax.activation.FileDataSource;

import org.apache.commons.configuration.Configuration;
import org.apache.commons.mail.Email;
import org.apache.commons.mail.EmailAttachment;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.HtmlEmail;
import org.apache.commons.mail.MultiPartEmail;
import org.apache.commons.mail.SimpleEmail;
import org.apache.tapestry5.ioc.Resource;

import org.chenillekit.core.services.ConfigurationService;
import org.chenillekit.mail.ChenilleKitMailConstants;
import org.chenillekit.mail.MailMessageHeaders;
import org.chenillekit.mail.services.MailService;
import org.slf4j.Logger;

/**
 * simple SMTP tool for sending emails based on <a href="http://jakarta.apache.org/commons/email">commons-email</a>.
 *
 * @version $Id$
 */
public class MailServiceImpl implements MailService<Email>
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

	public MailServiceImpl(Logger logger,
						   ConfigurationService configurationService,
						   Map<String, Resource> configuration)
	{
		this.logger = logger;

		Resource servicePorpertiesResource = configuration.get(ChenilleKitMailConstants.PROPERTIES_KEY);
		if (servicePorpertiesResource == null || !servicePorpertiesResource.exists())
			throw new RuntimeException(String.format("'%s' does not exists!", servicePorpertiesResource));

		Configuration serviceConfiguration = configurationService.getConfiguration(servicePorpertiesResource);

		this.smtpServer = serviceConfiguration.getString(ChenilleKitMailConstants.SMTP_HOST, "localhost");
		this.smtpPort = serviceConfiguration.getInt(ChenilleKitMailConstants.SMTP_PORT, 25);
		this.smtpUser = serviceConfiguration.getString(ChenilleKitMailConstants.SMTP_USER);
		this.smtpPassword = serviceConfiguration.getString(ChenilleKitMailConstants.SMTP_PASSWORD);
		this.smtpDebug = serviceConfiguration.getBoolean(ChenilleKitMailConstants.SMTP_DEBUG, false);
		this.smtpSSL = serviceConfiguration.getBoolean(ChenilleKitMailConstants.SMTP_SSL, false);
		this.smtpTLS = serviceConfiguration.getBoolean(ChenilleKitMailConstants.SMTP_TLS, false);
		this.smtpSslPort = serviceConfiguration.getInt(ChenilleKitMailConstants.SMTP_SSLPORT, 465);
	}


	private void setEmailStandardData(Email email)
	{
		email.setHostName(smtpServer);

		if (smtpUser != null && smtpUser.length() > 0)
			email.setAuthentication(smtpUser, smtpPassword);

		email.setDebug(smtpDebug);
		email.setSmtpPort(smtpPort);
		email.setSSL(smtpSSL);
		email.setSslSmtpPort(String.valueOf(smtpSslPort));
		email.setTLS(smtpTLS);
	}

	private EmailAttachment getAttachment(File file)
	{
		// Create the attachment
		EmailAttachment attachment = new EmailAttachment();
		attachment.setPath(file.getAbsolutePath());
		attachment.setDisposition(EmailAttachment.ATTACHMENT);
		attachment.setDescription(file.getName());
		attachment.setName(file.getName());

		return attachment;
	}

	private void setMailMessageHeaders(Email email, MailMessageHeaders headers)
			throws EmailException
	{
		email.setFrom(headers.getFrom());

		email.setSubject(headers.getSubject());

		for (String to : headers.getTo())
		{
			email.addTo(to);
		}

		for (String cc : headers.getCc())
		{
			email.addCc(cc);
		}

		for (String bcc : headers.getBcc())
		{
			email.addBcc(bcc);
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
//			email.setHostName(smtpServer);
//
//			if (smtpUser != null && smtpUser.length() > 0)
//				email.setAuthentication(smtpUser, smtpPassword);
//
//			email.setDebug(smtpDebug);
//			email.setSmtpPort(smtpPort);
//			email.setSSL(smtpSSL);
//			email.setSslSmtpPort(String.valueOf(smtpSslPort));
//			email.setTLS(smtpTLS);

			setEmailStandardData(email);

			email.send();
		}
		catch (EmailException e)
		{
			logger.error(e.getLocalizedMessage(), e);
			sended = false;
		}

		return sended;
	}

	/**
	 * send a HTML message.
	 *
	 * @param headers  the mail headers
	 * @param htmlBody the mail body (HTML based)
	 *
	 * @return true if mail successfull send
	 */
	public boolean sendHtmlMail(MailMessageHeaders headers, String htmlBody)
	{
		return sendHtmlMail(headers, htmlBody, (DataSource[]) null);
	}

	/**
	 * send a HTML message.
	 *
	 * @param headers	 the mail headers
	 * @param htmlBody	the mail body (HTML based)
	 * @param attachments array of files to attach at this mail
	 *
	 * @return true if mail successfull send
	 */
	public boolean sendHtmlMail(MailMessageHeaders headers, String htmlBody, File... attachments)
	{
		DataSource[] dataSources = null;

		if (attachments != null)
		{
			dataSources = new DataSource[attachments.length];
			for (int x = 0; x < attachments.length; x++)
				dataSources[x] = new FileDataSource(attachments[x]);
		}

		return sendHtmlMail(headers, htmlBody, dataSources);
	}

	/**
	 * send a HTML message.
	 *
	 * @param headers	 the mail headers
	 * @param htmlBody	the mail body (HTML based)
	 * @param dataSources array of data sources to attach at this mail
	 *
	 * @return true if mail successfull send
	 */
	public boolean sendHtmlMail(MailMessageHeaders headers, String htmlBody, DataSource... dataSources)
	{
		try
		{
			HtmlEmail email = new HtmlEmail();

			setEmailStandardData(email);

			setMailMessageHeaders(email, headers);

			for (DataSource dataSource : dataSources)
				email.attach(dataSource, dataSource.getName(), dataSource.getName());

			email.setHtmlMsg(htmlBody);

			String msgId = email.send();

			return true;
		}
		catch (EmailException e)
		{
			// FIXME Handle gracefully
			throw new RuntimeException(e);
		}
	}

	/**
	 * send a plain text message.
	 *
	 * @param headers the mail headers
	 * @param body	the mail body (text based)
	 *
	 * @return true if mail successfull send
	 */
	public boolean sendPlainTextMail(MailMessageHeaders headers, String body)
	{
		return sendPlainTextMail(headers, body, (DataSource[]) null);
	}

	/**
	 * send a plain text message.
	 *
	 * @param headers	 the mail headers
	 * @param body		the mail body (text based)
	 * @param attachments array of files to attach at this mail
	 *
	 * @return true if mail successfull send
	 */
	public boolean sendPlainTextMail(MailMessageHeaders headers, String body, File... attachments)
	{
		DataSource[] dataSources = null;

		if (attachments != null)
		{
			dataSources = new DataSource[attachments.length];
			for (int x = 0; x < attachments.length; x++)
				dataSources[x] = new FileDataSource(attachments[x]);
		}

		return sendPlainTextMail(headers, body, dataSources);
	}


	/**
	 * send a plain text message.
	 *
	 * @param headers	 the mail headers
	 * @param body		the mail body (text based)
	 * @param dataSources array of data sources to attach at this mail
	 *
	 * @return true if mail successfull send
	 */
	public boolean sendPlainTextMail(MailMessageHeaders headers, String body, DataSource... dataSources)
	{
		try
		{
			Email email = new SimpleEmail();

			if (dataSources != null && dataSources.length > 0)
			{
				MultiPartEmail multiPart = new MultiPartEmail();

				for (DataSource dataSource : dataSources)
					multiPart.attach(dataSource, dataSource.getName(), dataSource.getName());

				email = multiPart;
			}

			setEmailStandardData(email);

			setMailMessageHeaders(email, headers);

			email.setMsg(body);

			String msgId = email.send();

			return true;
		}
		catch (EmailException e)
		{
			// FIXME Handle gracefully
			throw new RuntimeException(e);
		}
	}

}
