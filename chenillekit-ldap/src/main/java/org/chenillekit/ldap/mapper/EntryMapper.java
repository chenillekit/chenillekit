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

package org.chenillekit.ldap.mapper;

import netscape.ldap.LDAPEntry;

/**
 * 
 * @author massimo
 */
public interface EntryMapper<T>
{
	/**
	 * 
	 * @param entry
	 * @return
	 */
	public T mapFromEntry(LDAPEntry entry);
	
	/**
	 * 
	 * @param type
	 * @return
	 */
	public LDAPEntry mapToEntry(T type);
}
