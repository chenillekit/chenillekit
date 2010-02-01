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

package org.chenillekit.image.services.impl;

import java.awt.image.BufferedImage;
import java.util.Properties;

import com.google.code.kaptcha.impl.DefaultKaptcha;
import com.google.code.kaptcha.util.Config;
import org.chenillekit.image.services.CaptchaProducer;

/**
 * implements the <a href="http://code.google.com/p/kaptcha">Kaptcha</a> library.
 *
 * @version $Id$
 */
public class KaptchaProducerImpl implements CaptchaProducer
{
	private DefaultKaptcha producer;

	public KaptchaProducerImpl(Properties kaptchaConfig)
	{
		producer = new DefaultKaptcha();

		if (kaptchaConfig == null)
			kaptchaConfig = new Properties();

		producer.setConfig(new Config(kaptchaConfig));
	}

	/**
	 * create the captch from given string.
	 */
	public BufferedImage createImage(String captchaString)
	{
		return producer.createImage(captchaString);
	}

	/**
	 * create a random String.
	 */
	public String createText()
	{
		return producer.createText();
	}
}
