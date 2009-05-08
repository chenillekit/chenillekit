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

package org.chenillekit.image;

import java.util.Map;
import java.util.Properties;

import org.apache.tapestry5.ioc.MappedConfiguration;
import org.apache.tapestry5.ioc.ServiceBinder;

import com.google.code.kaptcha.Producer;
import com.google.code.kaptcha.impl.DefaultKaptcha;
import com.google.code.kaptcha.util.Config;
import org.chenillekit.image.services.impl.ImageServiceImpl;

/**
 * @version $Id$
 */
public class ChenilleKitImageModule
{
    public static void bind(ServiceBinder binder)
    {
        binder.bind(ImageServiceImpl.class);
    }

	public static Producer buildKaptchaProducer(Map<String, Properties> configuration)
	{
		DefaultKaptcha producer = new DefaultKaptcha();
		producer.setConfig(new Config(configuration.get(ChenilleKitImageConstants.KAPATCHA_CONFIG_KEY)));
		return producer;
	}

	/**
	 * contribute configuration to freemarker template service.
	 */
	public static void contributeKaptchaProducer(MappedConfiguration<String, Properties> configuration)
	{
		Properties properties = new Properties();
//		properties.setProperty("kaptcha.border", "yes"); // Border around kaptcha. Legal values are yes or no. 	yes
//		properties.setProperty("kaptcha.border.color", "black"); //Color of the border. Legal values are r,g,b (and optional alpha) or white,black,blue. 	black
//		properties.setProperty("kaptcha.border.thickness", "1"); //Thickness of the border around kaptcha. Legal values are > 0. 	1
//		properties.setProperty("kaptcha.image.width", "200"); //Width in pixels of the kaptcha image. 	200
//		properties.setProperty("kaptcha.image.height", "50"); //Height in pixels of the kaptcha image. 	50
//		properties.setProperty("kaptcha.producer.impl", "com.google.code.kaptcha.impl.DefaultKaptcha"); //The image producer. 	com.google.code.kaptcha.impl.DefaultKaptcha
//		properties.setProperty("kaptcha.textproducer.impl", "com.google.code.kaptcha.text.impl.DefaultTextCreator"); //The text producer. 	com.google.code.kaptcha.text.impl.DefaultTextCreator
//		properties.setProperty("kaptcha.textproducer.char.string", "abcde2345678gfynmnpwx"); //The characters that will create the kaptcha. 	abcde2345678gfynmnpwx
//		properties.setProperty("kaptcha.textproducer.char.length", "5"); //The number of characters to display. 	5
//		properties.setProperty("kaptcha.textproducer.font.names", "Arial, Courier"); //A list of comma separated font names. 	Arial, Courier
//		properties.setProperty("kaptcha.textproducer.font.size", "40"); //The size of the font to use. 	40px.
//		properties.setProperty("kaptcha.textproducer.font.color", "black"); //The color to use for the font. Legal values are r,g,b. 	black
//		properties.setProperty("kaptcha.noise.impl", "com.google.code.kaptcha.impl.DefaultNoise"); //The noise producer. 	com.google.code.kaptcha.impl.DefaultNoise
//		properties.setProperty("kaptcha.noise.color", "black"); //The noise color. Legal values are r,g,b. 	black
//		properties.setProperty("kaptcha.obscurificator.impl", "com.google.code.kaptcha.impl.WaterRipple"); //The obscurificator implementation. 	com.google.code.kaptcha.impl.WaterRipple
//		properties.setProperty("kaptcha.background.impl", "com.google.code.kaptcha.impl.DefaultBackground"); //The background implementation. 	com.google.code.kaptcha.impl.DefaultBackground
////		properties.setProperty("kaptcha.background.clear.from", "lightgray"); //Starting background color. Legal values are r,g,b. 	light grey
//		properties.setProperty("kaptcha.background.clear.to", "white"); //Ending background color. Legal values are r,g,b. 	white
//		properties.setProperty("kaptcha.word.impl", "com.google.code.kaptcha.text.impl.DefaultWordRenderer"); //The word renderer implementation. 	com.google.code.kaptcha.text.impl.DefaultWordRenderer
//		properties.setProperty("kaptcha.session.key", "KAPTCHA_SESSION_KEY"); //The value for the kaptcha is generated and is put into the HttpSession. This is the key value for that item in the session. 	KAPTCHA_SESSION_KEY
//		properties.setProperty("kaptcha.session.date", "KAPTCHA_SESSION_DATE"); //The date the kaptcha is generated is put into the HttpSession. This is the key value for that item in the session. 	KAPTCHA_SESSION_DATE

		configuration.add(ChenilleKitImageConstants.KAPATCHA_CONFIG_KEY, properties);
	}

}
