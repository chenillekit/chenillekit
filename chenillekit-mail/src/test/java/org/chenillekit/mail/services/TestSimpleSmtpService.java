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

import javax.mail.MessagingException;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMultipart;

import org.apache.commons.mail.EmailAttachment;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.MultiPartEmail;
import org.apache.commons.mail.SimpleEmail;
import org.apache.tapestry5.ioc.internal.util.ClasspathResource;

import com.dumbster.smtp.SimpleSmtpServer;
import org.chenillekit.mail.ChenilleKitMailTestModule;
import org.chenillekit.test.AbstractTestSuite;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Test;

/**
 * @author <a href="mailto:homburgs@googlemail.com">shomburg</a>
 * @version $Id$
 */
public class TestSimpleSmtpService extends AbstractTestSuite
{
    private SimpleSmtpServer smtpServer;

    @BeforeSuite
    public final void setup_registry()
    {
        super.setup_registry(ChenilleKitMailTestModule.class);
        smtpServer = SimpleSmtpServer.start(25);
    }

    @Test
    public void test_simpleemail_sending() throws EmailException
    {
        SimpleEmail email = new SimpleEmail();
        email.setSubject("Test Mail 1");
        email.addTo("homburgs@gmail.com");
        email.setFrom("homburgs@gmail.com");
        email.setMsg("This is a dummy message text!");

        SmtpService service = registry.getService(SmtpService.class);
        service.sendEmail(email);

        assertTrue(smtpServer.getReceivedEmailSize() == 1);
    }

    @Test(dependsOnMethods = "test_simpleemail_sending")
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
        attachment.setDescription("smtp.properties");
        attachment.setURL(new ClasspathResource("smtp.properties").toURL());
        email.attach(attachment);

        SmtpService service = registry.getService(SmtpService.class);
        service.sendEmail(email);

        assertTrue(smtpServer.getReceivedEmailSize() == 2);
    }
}
