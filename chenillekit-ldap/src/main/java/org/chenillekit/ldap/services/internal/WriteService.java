/*
 * Apache License
 * Version 2.0, January 2004
 * http://www.apache.org/licenses/
 *
 * Copyright 2009 by chenillekit.org
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 */
package org.chenillekit.ldap.services.internal;

import netscape.ldap.LDAPAttributeSet;
import netscape.ldap.LDAPModificationSet;

/**
 * 
 * @author massimo
 *
 */
public interface WriteService
{
	/**
	 * 
	 * @param dn
	 * @param attributes
	 */
	public void addEntry(String dn, LDAPAttributeSet attributes);
	
	/**
	 * 
	 * @param dn
	 * @param modifications
	 */
	public void modifyEntry(String dn, LDAPModificationSet modifications);
	
	/**
	 * 
	 * @param dn
	 */
	public void deleteEntry(String dn);
	
	/**
	 * 
	 * @param oldDn
	 * @param newDn
	 */
	public void renameEntry(String oldDn, String newDn);

}
