/*
 * Apache License
 * Version 2.0, January 2004
 * http://www.apache.org/licenses/
 *
 * Copyright 2008-2011 by chenillekit.org
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 */

package org.chenillekit.ldap.services.internal;

import netscape.ldap.LDAPConnection;
import netscape.ldap.LDAPException;


/**
 * @author massimo
 */
public interface LDAPSource
{
	/**
	 * @return
	 *
	 * @throws LDAPException TODO
	 */
	public LDAPConnection openSession() throws LDAPException;

}
