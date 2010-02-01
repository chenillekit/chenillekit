/*
 * Apache License
 * Version 2.0, January 2004
 * http://www.apache.org/licenses/
 *
 * Copyright 2008-2009 by chenillekit.org
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 */

package org.chenillekit.access.internal;

import java.util.ArrayList;
import java.util.List;

import org.apache.tapestry5.EventContext;
import org.apache.tapestry5.ioc.services.TypeCoercer;

/**
 * {@link EventContext} used in case to redirect the user to the
 * precedently requested page.
 * 
 * @id $id$
 */
public class ChenilleKitAccessEventContext implements EventContext
{
	private final Object[] values;
	
	private final TypeCoercer coercer;
	
	
	public ChenilleKitAccessEventContext(TypeCoercer coercer, Object[] values)
	{
		this.coercer = coercer;
		this.values = values;
		
		if (coercer == null || values == null)
			throw new IllegalStateException("TypeCoercer nor object values can be null");
	}

	/* (non-Javadoc)
	 * @see org.apache.tapestry5.EventContext#get(java.lang.Class, int)
	 */
	public <T> T get(Class<T> desiredType, int index)
	{
		if (index < 0 || index >= values.length)
			throw new RuntimeException("Index is going to be between 0 and " + values.length + " (values.length)");
		
		return coercer.coerce(values[index], desiredType);
	}

	/* (non-Javadoc)
	 * @see org.apache.tapestry5.EventContext#getCount()
	 */
	public int getCount()
	{
		return values.length;
	}

	/* (non-Javadoc)
	 * @see org.apache.tapestry5.EventContext#toStrings()
	 */
	public String[] toStrings()
	{
		List<String> res = new ArrayList<String>();
		
		for (int i = 0; i < values.length; i++)
		{
			res.add(coercer.coerce(values[i], String.class));
		}
		
		return res.toArray(new String[res.size()]);
	}

}
