/**
 * 
 */
package org.chenillekit.ldap;

import netscape.ldap.LDAPEntry;

import org.chenillekit.ldap.mapper.EntryMapper;

/**
 * @author massimo
 *
 */
public class OnePropMapper implements EntryMapper<OneProperty>
{

	public OneProperty mapFromEntry(LDAPEntry entry)
	{
		OneProperty one = new OneProperty();
		one.setTheProperty(entry.getAttribute("mail").getStringValueArray()[0]);
		
		return one;
	}

	public LDAPEntry mapToEntry(OneProperty type)
	{
		// TODO Auto-generated method stub
		return null;
	}
}
