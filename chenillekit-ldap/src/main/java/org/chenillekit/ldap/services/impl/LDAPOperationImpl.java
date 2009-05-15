/**
 * 
 */
package org.chenillekit.ldap.services.impl;

import java.util.List;

import netscape.ldap.LDAPEntry;

import org.chenillekit.ldap.services.LDAPOperation;

/**
 * @author massimo
 *
 */
public class LDAPOperationImpl implements LDAPOperation
{

	public List<LDAPEntry> lookUp(String baseDN, String filter, String... attributes)
	{
		return null;
	}
	

}
