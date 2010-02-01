/**
 * 
 */
package org.chenillekit.ldap.services;

import java.util.List;

import org.chenillekit.ldap.mapper.EntryMapper;

import netscape.ldap.LDAPAttribute;
import netscape.ldap.LDAPEntry;

/**
 * @author massimo
 *
 */
public interface LDAPOperation
{
	/**
	 * Search the <code>baseDN</code> for entries which satisfy <code>filter</code> and eventually
	 * return only the selected </code>attributes</code>
	 * 
	 * @param baseDN the base DN where to start to search (scope sub)
	 * @param filter the LDAP filter (LDAP syntax) 
	 * @param attributes the attributes requested, normally this would fetch all attributes
	 * @return a {@link List} of {@link LDAPEntry} or an empty {@link List} in case no result are
	 * available
	 */
	public List<LDAPEntry> search(String baseDN, String filter, String... attributes);
	
	/**
	 * Search the <code>baseDN</code> for entries which satify <code>filter</code> and add them
	 * to a {@link List} of type mapped <code>mapper</code>.
	 * 
	 * @param <T>
	 * @param baseDN the base DN where to start to search (scope sub)
	 * @param filter the LDAP filter (LDAP syntax)
	 * @param mapper the {@link EntryMapper} for the particular type.
	 * @return a {@link List} of type T or and empty list in case no result are available.
	 */
	public <T> List<T> search(String baseDN, String filter, EntryMapper<T> mapper);
	
	/**
	 * Look up a specific DN.
	 * 
	 * @param dn the LDAP DN to look up
	 * @return the {@link LDAPEntry} associated with the DN or <code>null</code> in case no entry
	 * is present at the specified DN
	 */
	public LDAPEntry lookup(String dn);
	
	/**
	 * Lookup a specific DN and map it to a particular type.
	 * 
	 * @param <T>
	 * @param dn the LDAP DN to look up
	 * @param mapper the {@link EntryMapper} used to map LDAP attributes to beans properties
	 * @return the type T which represent the LDAP entry or <code>null</code> in case no entry
	 * is present at the specified DN 
	 */
	public <T> T lookup(String dn, EntryMapper<T> mapper);
	
	/**
	 * Insert an <code>element</code> into the LDAP tree at the specified DN
	 * (<code>RDN + baseDN</code>) using the <code>mapper</code> to map Java
	 * beans properties to LDAP attributes.
	 *  
	 * @param <T> the type of element
	 * @param dn the position where to insert the element in the LDAP tree
	 * @param element the type of element to insert into the LDAP tree
	 * @param mapper the {@link EntryMapper} used to build the {@link LDAPEntry}
	 */
	public <T> void add(String dn, T element, EntryMapper<T> mapper);
	
	/**
	 * Modify the {@link LDAPAttribute} of the {@link LDAPEntry} present at the
	 * specified <code>DN</code> using the properties of the <code>element</code>
	 * with the mapping provided by the <code>mapper</code>
	 * 
	 * @param <T>
	 * @param dn the position where to find the {@link LDAPEntry} to modify
	 * @param element the <code>element</code> where to take new values from
	 * @param mapper the {@link EntryMapper} used to map properties to attribute
	 */
	public <T> void modify(String dn, T element, EntryMapper<T> mapper);
	
	/**
	 * Rename an element ({@link LDAPEntry}), used also to change position (move around) 
	 * inside the whole LDAP tree. The two DNs have to be absolute DN not just the RDN.
	 *  
	 * @param oldDn the DN (<code>RDN + baseDN</code>) of the element to be renamed
	 * @param newDn the DN (<code>RDN + baseDN</code>) of the element after the operation
	 */
	public void rename(String oldDn, String newDn);
	
	/**
	 * Remove the LDAP element ({@link LDAPEntry}) at the specificed DN. 
	 * 
	 * @param dn the DN (<code>RDN + baseDN</code>) of the element to be removed
	 */
	public void delete(String dn);

}
