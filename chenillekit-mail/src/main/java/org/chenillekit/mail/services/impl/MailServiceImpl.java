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

import java.util.Map;

import org.apache.commons.configuration.Configuration;
import org.apache.commons.mail.Email;
import org.apache.commons.mail.EmailException;
import org.apache.tapestry5.ioc.Resource;

import org.chenillekit.core.services.ConfigurationService;
import org.chenillekit.mail.ChenilleKitMailConstants;
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

	/**
	 * send an email.
	 */
	public boolean sendEmail(Email email)
	{
		boolean sended = true;

		try
		{
			email.setHostName(smtpServer);

			if (smtpUser != null && smtpUser.length() > 0)
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
