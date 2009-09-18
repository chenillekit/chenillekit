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
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Iterator;

import javax.mail.MessagingException;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMultipart;

import org.apache.commons.mail.EmailAttachment;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.MultiPartEmail;
import org.apache.commons.mail.SimpleEmail;
import org.apache.tapestry5.ioc.internal.util.ClasspathResource;
import org.chenillekit.mail.ChenilleKitMailTestModule;
import org.chenillekit.mail.MailMessageHeaders;
import org.chenillekit.test.AbstractTestSuite;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.dumbster.smtp.SimpleSmtpServer;

/**
 * @version $Id$
 */
public class TestMailService extends AbstractTestSuite
{
	private static final int SMTP_PORT = 4444;
	
	private Logger logger = LoggerFactory.getLogger(TestMailService.class);
	
	private SimpleSmtpServer smtpServer;
	
//	@BeforeSuite
	public final void setup_registry()
	{
		super.setup_registry(ChenilleKitMailTestModule.class);
	}

//    @BeforeTest
    public final void setup_smtpserver()
    {   
        logger.info("starting fake SMTP server at port {}", SMTP_PORT);
		smtpServer = SimpleSmtpServer.start(SMTP_PORT);
		logger.info("SimpleSmtpServer started");
    }
    
//    @AfterTest
    public final void smtp_shutdown()
    {
    	if ( this.smtpServer != null && !this.smtpServer.isStopped() )
    	{
    		this.smtpServer.stop();
    	}
    }

//    @Test
    public void test_simpleemail_sending() throws EmailException
    {
        SimpleEmail email = new SimpleEmail();
        email.setSubject("Test Mail 1");
        email.addTo("homburgs@gmail.com");
        email.setFrom("homburgs@gmail.com");
        email.setMsg("This is a dummy message text!");

        MailService mailService = registry.getService(MailService.class);

        mailService.sendEmail(email);

        assertTrue(smtpServer.getReceivedEmailSize() == 1);
    }

//    @Test
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
        mailService.sendEmail(email);
        
        assertTrue(smtpServer.getReceivedEmailSize() == 1);
    }

//    @Test
    public void send_plain_mail()
    {
    	MailMessageHeaders headers = new MailMessageHeaders();

    	headers.setFrom("sender@example.com");
    	headers.addTo("receiver@example.com");

    	MailService mailService = registry.getService(MailService.class);
    	mailService.sendPlainTextMail(headers, "THIS IS THE BODY");
    	
    	assertTrue(smtpServer.getReceivedEmailSize() == 1);
    }

//    @Test
    public void send_html_mail() throws IOException, URISyntaxException
    {
    	MailMessageHeaders headers = new MailMessageHeaders();

    	headers.setFrom("sender@example.com");
    	headers.addTo("receiver@example.com");

    	File tmp = new File(new ClasspathResource("dummy.txt").toURL().toURI());

    	MailService mailService = registry.getService(MailService.class);
    	mailService.sendHtmlMail(headers, "<html><head><title>HTML title message</title></head><body><p>I'm the Body</body></html>", tmp);
    	
    	Iterator<?> iter = smtpServer.getReceivedEmail();

    	while (iter.hasNext()) {
			Object object = (Object) iter.next();
			System.err.println(" " + object);
		}
    }
}
