/**
 * 
 */
package org.chenillekit.ldap.services.impl;

import java.util.ArrayList;
import java.util.List;

import netscape.ldap.LDAPEntry;

import org.chenillekit.ldap.mapper.EntryMapper;
import org.chenillekit.ldap.services.LDAPOperation;
import org.chenillekit.ldap.services.internal.SearcherService;

/**
 * @author massimo
 *
 */
public class LDAPOperationImpl implements LDAPOperation
{
	private final SearcherService search;
	
	public LDAPOperationImpl(SearcherService search)
	{
		this.search = search;
	}

	/*
	 * (non-Javadoc)
	 * @see org.chenillekit.ldap.services.LDAPOperation#search(java.lang.String, java.lang.String, java.lang.String[])
	 */
	public List<LDAPEntry> search(String baseDN, String filter, String... attributes)
	{
		try
		{
			return search.search(baseDN, filter, attributes);
		}
		catch (RuntimeException re)
		{
			return new ArrayList<LDAPEntry>();
		}
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.chenillekit.ldap.services.LDAPOperation#search(java.lang.String, java.lang.String, org.chenillekit.ldap.mapper.EntryMapper)
	 */
	public <T> List<T> search(String baseDN, String filter, EntryMapper<T> mapper)
	{
		try
		{
			List<LDAPEntry> entries = search.search(baseDN, filter);
			List<T> res = new ArrayList<T>();
			
			for (LDAPEntry ldapEntry : entries)
			{
				res.add(mapper.mapFromEntry(ldapEntry));
			}
			
			return res;
		}
		catch (RuntimeException re)
		{
			return new ArrayList<T>();
		}
	}

	/*
	 * (non-Javadoc)
	 * @see org.chenillekit.ldap.services.LDAPOperation#lookup(java.lang.String)
	 */
	public LDAPEntry lookup(String dn)
	{
		try
		{
			return search.lookup(dn);
		}
		catch (RuntimeException exc)
		{
			return null;
		}
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.chenillekit.ldap.services.LDAPOperation#lookup(java.lang.String, org.chenillekit.ldap.mapper.EntryMapper)
	 */
	public <T> T lookup(String dn, EntryMapper<T> mapper)
	{
		try
		{
			T res =  mapper.mapFromEntry(search.lookup(dn));
			return res;
		}
		catch (RuntimeException exc)
		{
			return null;
		}
	}	

}
