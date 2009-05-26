/**
 * 
 */
package org.chenillekit.ldap.services.internal;

import netscape.ldap.LDAPAttributeSet;
import netscape.ldap.LDAPModificationSet;

import org.slf4j.Logger;

/**
 * @author massimo
 *
 */
public class WriteServiceImpl implements WriteService
{
	private final Logger logger;
	
	private final LDAPSource ldapSource;
	
	/**
	 * 
	 * @param logger
	 * @param ldapSource
	 */
	public WriteServiceImpl(Logger logger, LDAPSource ldapSource)
	{
		this.ldapSource = ldapSource;
		this.logger = logger;
	}

	/*
	 * (non-Javadoc)
	 * @see org.chenillekit.ldap.services.internal.WriteService#addEntry(java.lang.String, netscape.ldap.LDAPAttributeSet)
	 */
	public void addEntry(String dn, LDAPAttributeSet attributes)
	{
		// TODO Auto-generated method stub
		throw new RuntimeException("NOT YET IMPLEMENTED");
	}

	/*
	 * (non-Javadoc)
	 * @see org.chenillekit.ldap.services.internal.WriteService#deleteEntry(java.lang.String)
	 */
	public void deleteEntry(String dn)
	{
		// TODO Auto-generated method stub
		throw new RuntimeException("NOT YET IMPLEMENTED");
	}

	/*
	 * (non-Javadoc)
	 * @see org.chenillekit.ldap.services.internal.WriteService#modifyEntry(java.lang.String, netscape.ldap.LDAPModificationSet)
	 */
	public void modifyEntry(String dn, LDAPModificationSet modifications)
	{
		// TODO Auto-generated method stub
		throw new RuntimeException("NOT YET IMPLEMENTED");
	}

	/*
	 * (non-Javadoc)
	 * @see org.chenillekit.ldap.services.internal.WriteService#renameEntry(java.lang.String, java.lang.String)
	 */
	public void renameEntry(String oldDn, String newDn)
	{
		// TODO Auto-generated method stub
		throw new RuntimeException("NOT YET IMPLEMENTED");
	}
	

}
