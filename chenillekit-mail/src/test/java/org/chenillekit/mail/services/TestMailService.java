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

package org.chenillekit.mail.services;

import com.dumbster.smtp.SimpleSmtpServer;
import org.apache.commons.mail.EmailAttachment;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.MultiPartEmail;
import org.apache.tapestry5.ioc.internal.util.ClasspathResource;
import org.chenillekit.mail.ChenilleKitMailTestModule;
import org.chenillekit.mail.MailMessageHeaders;
import org.chenillekit.test.AbstractTestSuite;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import javax.activation.FileDataSource;
import javax.mail.MessagingException;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMultipart;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;

/**
 * @version $Id$
 */
public class TestMailService extends AbstractTestSuite
{
	private static final int SMTP_PORT = 4444;

	private Logger logger = LoggerFactory.getLogger(TestMailService.class);

	private SimpleSmtpServer smtpServer;

	@BeforeSuite
	public final void setup_registry()
	{
		super.setup_registry(ChenilleKitMailTestModule.class);
	}

	@BeforeTest
	public final void setup_smtpserver()
	{
		logger.info("starting fake SMTP server at port {}", SMTP_PORT);
		smtpServer = SimpleSmtpServer.start(SMTP_PORT);
		logger.info("SimpleSmtpServer started");
	}

	@AfterTest
	public final void smtp_shutdown()
	{
		if (this.smtpServer != null && !this.smtpServer.isStopped())
		{
			this.smtpServer.stop();
		}
	}

	@Test
	public void test_multipartemail_sending() throws EmailException, MessagingException
	{
		MultiPartEmail email = new MultiPartEmail();
		email.setSubject("Test Mail 2");
		email.addTo("homburgs@gmail.com");
		email.setFrom("homburgs@gmail.com");
		email.setMsg("This is a dummy message text!");
		email.addPart("This is a dummy message part 1!", "text/plain");

		MimeMultipart mmp = new MimeMultipart();
		MimeBodyPart mbp = new MimeBodyPart();

		mbp.setText("This is a dummy MimeBodyPart 1!");

		mmp.addBodyPart(mbp);
		email.addPart(mmp);

		EmailAttachment attachment = new EmailAttachment();
		attachment.setDescription("dummy.txt");
		attachment.setURL(new ClasspathResource("dummy.txt").toURL());
		email.attach(attachment);

		MailService mailService = registry.getService(MailService.class);
		boolean sended = mailService.sendEmail(email);

		assertTrue(sended, "sended");
	}

	@Test
	public void send_plaintext_mail()
	{
		MailMessageHeaders headers = new MailMessageHeaders();

		headers.setFrom("sender@example.com");
		headers.addTo("receiver@example.com");

		MailService mailService = registry.getService(MailService.class);
		boolean sended = mailService.sendPlainTextMail(headers, "THIS IS THE BODY");

		assertTrue(sended, "sended");
	}

	@Test
	public void send_plaintext_fileattachment_mail() throws URISyntaxException
	{
		MailMessageHeaders headers = new MailMessageHeaders();

		headers.setFrom("sender@example.com");
		headers.addTo("receiver@example.com");

		File file = new File(new ClasspathResource("dummy.txt").toURL().toURI());

		MailService mailService = registry.getService(MailService.class);
		boolean sended = mailService.sendPlainTextMail(headers, "THIS IS THE BODY", file);

		assertTrue(sended, "sended");
	}

	@Test
	public void send_plaintext_datasourceattachment_mail() throws URISyntaxException
	{
		MailMessageHeaders headers = new MailMessageHeaders();

		headers.setFrom("sender@example.com");
		headers.addTo("receiver@example.com");

		FileDataSource file = new FileDataSource(new File(new ClasspathResource("dummy.txt").toURL().toURI()));

		MailService mailService = registry.getService(MailService.class);
		boolean sended = mailService.sendPlainTextMail(headers, "THIS IS THE BODY", file);

		assertTrue(sended, "sended");
	}

	@Test
	public void send_html_mail() throws IOException, URISyntaxException
	{
		MailMessageHeaders headers = new MailMessageHeaders();

		headers.setFrom("sender@example.com");
		headers.addTo("receiver@example.com");

		File tmp = new File(new ClasspathResource("dummy.txt").toURL().toURI());

		MailService mailService = registry.getService(MailService.class);
		boolean sended = mailService.sendHtmlMail(headers, "<html><head><title>HTML title message</title></head><body><p>I'm the Body</body></html>", tmp);

		assertTrue(sended, "sended");
	}

	@Test
	public void send_html_datasourceattachment_mail() throws IOException, URISyntaxException
	{
		MailMessageHeaders headers = new MailMessageHeaders();

		headers.setFrom("sender@example.com");
		headers.addTo("receiver@example.com");

		FileDataSource file1 = new FileDataSource(new File(new ClasspathResource("dummy.txt").toURL().toURI()));
		FileDataSource file2 = new FileDataSource(new File(new ClasspathResource("log4j.xml").toURL().toURI()));
		FileDataSource file3 = new FileDataSource(new File(new ClasspathResource("smtp.properties").toURL().toURI()));

		MailService mailService = registry.getService(MailService.class);
		boolean sended = mailService.sendHtmlMail(headers,
												  "<html><head><title>HTML title message</title></head><body><p>I'm the Body</body></html>",
												  file1, file2, file3);

		assertTrue(sended, "sended");
	}

	@Test
	public void send_plaintext_iso88591_mail() throws UnsupportedEncodingException
	{
		MailMessageHeaders headers = new MailMessageHeaders();

		headers.setFrom("sender@example.com");
		headers.addTo("receiver@example.com");
		headers.setCharset("ISO8859-1");

		MailService mailService = registry.getService(MailService.class);
		boolean sended = mailService.sendPlainTextMail(headers, "Hallo, Ihr alten KnackwÃ¼rste! Ich heiÃ\u009Fe Euch Willkommen, aber ganz schÃ¶n Ã¼bel!");

		assertTrue(sended, "sended");
	}

	@Test
	public void send_plaintext_utf8_mail()
	{
		MailMessageHeaders headers = new MailMessageHeaders();

		headers.setFrom("sender@example.com");
		headers.addTo("receiver@example.com");
		headers.setCharset("UTF-8");

		MailService mailService = registry.getService(MailService.class);
		boolean sended = mailService.sendPlainTextMail(headers, "Hallo, Ihr alten Knackwürste! " +
				"Ich heiße Euch Willkommen, aber ganz schön übel!");

		assertTrue(sended, "sended");
	}
}
