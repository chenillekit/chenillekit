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

package org.chenillekit.tapestry.core.tests;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.EncoderException;
import org.apache.commons.codec.net.BCodec;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

/**
 * @author <a href="mailto:shomburg@depot120.dpd.de">S.Homburg</a>
 * @version $Id$
 */
public class TestStringCoding extends Assert
{
	private BCodec bCodec;

	@BeforeClass
	public void initTest()
	{
		bCodec = new BCodec();
	}

	@Test
	public void testEncoding() throws EncoderException
	{
		assertEquals(bCodec.encode("x6nx5"), "=?UTF-8?B?eDZueDU=?=");
	}

	@Test
	public void testDecoding() throws DecoderException
	{
		assertEquals(bCodec.decode("=?UTF-8?B?eDZueDU=?="), "x6nx5");
	}
}
