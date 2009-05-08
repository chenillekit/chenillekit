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

package org.chenillekit.access;

/**
 *
 *
 * @version $Id$
 */
public class ChenilleKitAccessException extends RuntimeException
{
	/**
	 * Serial version
	 */
	private static final long serialVersionUID = -6022592655403552535L;

	/**
	 * Default blank constructor
	 */
	public ChenilleKitAccessException()
	{
	}

	/**
	 * @param message
	 */
	public ChenilleKitAccessException(String message)
	{
		super(message);
	}

	/**
	 * @param cause
	 */
	public ChenilleKitAccessException(Throwable cause)
	{
		super(cause);
	}

	/**
	 * @param message
	 * @param cause
	 */
	public ChenilleKitAccessException(String message, Throwable cause)
	{
		super(message, cause);
	}

}
