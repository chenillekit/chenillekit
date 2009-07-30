/**
 * 
 */
package org.chenillekit.ldap.services.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import netscape.ldap.LDAPAttribute;
import netscape.ldap.LDAPEntry;
import netscape.ldap.LDAPModification;
import netscape.ldap.LDAPModificationSet;

import org.chenillekit.ldap.mapper.EntryMapper;
import org.chenillekit.ldap.services.LDAPOperation;
import org.chenillekit.ldap.services.internal.ReadService;
import org.chenillekit.ldap.services.internal.WriteService;
import org.slf4j.Logger;

/**
 * @author massimo
 *
 */
public class LDAPOperationImpl implements LDAPOperation
{
	private final ReadService reader;
	private final WriteService writer;
	
	private final Logger logger;
	
	public LDAPOperationImpl(ReadService reader, WriteService writer, Logger logger)
	{
		this.reader = reader;
		this.writer = writer;
		this.logger = logger;
	}

	/*
	 * (non-Javadoc)
	 * @see org.chenillekit.ldap.services.LDAPOperation#search(java.lang.String, java.lang.String, java.lang.String[])
	 */
	public List<LDAPEntry> search(String baseDN, String filter, String... attributes)
	{
		try
		{
			return reader.search(baseDN, filter, attributes);
		}
		catch (RuntimeException re)
		{
			logger.error("Unable to perform search operation: " + re.getMessage(), re);
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
			List<LDAPEntry> entries = reader.search(baseDN, filter);
			List<T> res = new ArrayList<T>();
			
			for (LDAPEntry ldapEntry : entries)
			{
				res.add(mapper.mapFromEntry(ldapEntry));
			}
			
			return res;
		}
		catch (RuntimeException re)
		{
			logger.error("Unable to perform search operation: " + re.getMessage(), re);
			
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
			return reader.lookup(dn);
		}
		catch (RuntimeException exc)
		{
			logger.error("Unable to perform lookup operation: " + exc.getMessage(), exc);
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
			T res =  mapper.mapFromEntry(reader.lookup(dn));
			return res;
		}
		catch (RuntimeException exc)
		{
			logger.error("Unable to perform search operation: " + exc.getMessage(), exc);
			return null;
		}
	}

	/* (non-Javadoc)
	 * @see org.chenillekit.ldap.services.LDAPOperation#add(java.lang.String, java.lang.Object, org.chenillekit.ldap.mapper.EntryMapper)
	 */
	public <T> void add(String dn, T element, EntryMapper<T> mapper)
	{
		// XXX Note the here we switch from element to entry to attributeSet to entry again
		LDAPEntry entry = mapper.mapToEntry(element);
		
		writer.addEntry(dn, entry.getAttributeSet());
	}

	/* (non-Javadoc)
	 * @see org.chenillekit.ldap.services.LDAPOperation#delete(java.lang.String)
	 */
	public void delete(String dn)
	{
		writer.deleteEntry(dn);
	}

	/* (non-Javadoc)
	 * @see org.chenillekit.ldap.services.LDAPOperation#modify(java.lang.String, java.lang.Object, org.chenillekit.ldap.mapper.EntryMapper)
	 */
	public <T> void modify(String dn, T element, EntryMapper<T> mapper)
	{
		LDAPEntry oriEntry = reader.lookup(dn);
		
		Map<String, String[]> orisAttr = new HashMap<String, String[]>();
		
		for (int i = 0; i < oriEntry.getAttributeSet().size(); i++)
		{
			LDAPAttribute attr = oriEntry.getAttributeSet().elementAt(i);
			orisAttr.put(attr.getName(), attr.getStringValueArray());
		}
		
		LDAPEntry modEntry = mapper.mapToEntry(element);
		
		LDAPModificationSet modSet = new LDAPModificationSet();
		
		for (int i = 0; i < modEntry.getAttributeSet().size(); i++)
		{
			LDAPAttribute modificationAttribute = modEntry.getAttributeSet().elementAt(i);
			
			String name = modificationAttribute.getName();
			String[] values = modificationAttribute.getStringValueArray();
			
			if (orisAttr.containsKey(name))
			{
				modSet.add(LDAPModification.REPLACE, new LDAPAttribute(name, values));
				orisAttr.remove(name);
			}
			else if (values != null && values.length > 0)
			{
				modSet.add(LDAPModification.ADD, new LDAPAttribute(name, values));
			}
		}
		
		for (String attrName : orisAttr.keySet())
		{
//			modSet.add(LDAPModification.DELETE, new LDAPAttribute(attrName, orisAttr.get(attrName)));
			if (logger.isDebugEnabled())
				logger.debug("Delting attribute " + attrName);
		}
		
		if (logger.isDebugEnabled())
			logger.debug("Total number of modifications requested for " + dn + " is " + modSet.size());
		
		writer.modifyEntry(dn, modSet);
	}

	/* (non-Javadoc)
	 * @see org.chenillekit.ldap.services.LDAPOperation#rename(java.lang.String, java.lang.String)
	 */
	public void rename(String oldDn, String newDn)
	{
		writer.renameEntry(oldDn, newDn);
	}	
	
}
