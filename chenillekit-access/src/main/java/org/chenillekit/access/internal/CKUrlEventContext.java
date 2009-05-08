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

package org.chenillekit.access.internal;

import org.apache.tapestry5.EventContext;
import org.apache.tapestry5.internal.URLEventContext;
import org.apache.tapestry5.services.ContextValueEncoder;

/**
 * This is class is actually a copy of the {@link URLEventContext} Tapestry5 internal
 * class which exists just cause the orginal one is in the <em>.internal</em> package.
 * <pre>
 * Original author note: When the orignal one will be elevated to public
 * (if that will be the case) i will delete this one.
 * </pre>
 *
 * @version $Id$
 */
public class CKUrlEventContext implements EventContext
{
	private final ContextValueEncoder valueEncoder;
	private final String[] elements;

	public CKUrlEventContext(ContextValueEncoder valueEncoder, String[] elements)
	{
		this.valueEncoder = valueEncoder;
		this.elements = elements;
	}

	/* (non-Javadoc)
	 * @see org.apache.tapestry5.EventContext#get(java.lang.Class, int)
	 */
	public <T> T get(Class<T> desiredType, int index)
	{
		return valueEncoder.toValue(desiredType, elements[index]);
	}

	/* (non-Javadoc)
	 * @see org.apache.tapestry5.EventContext#getCount()
	 */
	public int getCount()
	{
		return elements == null ? 0 : elements.length;
	}

}
