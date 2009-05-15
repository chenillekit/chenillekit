/**
 * 
 */
package org.chenillekit.ldap.services;

import java.util.List;

import netscape.ldap.LDAPEntry;

/**
 * @author massimo
 *
 */
public interface LDAPOperation
{
	/**
	 * 
	 * @param baseDN
	 * @param filter
	 * @param attributes
	 * @return
	 */
	public List<LDAPEntry> lookUp(String baseDN, String filter, String... attributes);

}
