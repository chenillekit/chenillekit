/**
 * 
 */
package org.chenillekit.ldap.services;

import java.util.Enumeration;
import java.util.List;

import javax.naming.NamingException;

import netscape.ldap.LDAPEntry;

import org.chenillekit.ldap.ChenilleKitLDAPTestModule;
import org.chenillekit.ldap.OnePropMapper;
import org.chenillekit.ldap.OneProperty;
import org.chenillekit.ldap.services.LDAPOperation;
import org.chenillekit.ldap.services.internal.ReadService;
import org.chenillekit.test.AbstractTestSuite;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Test;

/**
 * @author massimo
 *
 */
public class TestLDAPOperation extends AbstractTestSuite
{
	private LDAPOperation ldapOperation;
	
	private OnePropMapper oneMapper;

    @BeforeSuite
    public final void setup()
    {
        super.setup_registry(ChenilleKitLDAPTestModule.class);
        ldapOperation = registry.getService(LDAPOperation.class);
        oneMapper = new OnePropMapper();
    }
    
    @Test
    public void search() throws NamingException
    {
        String baseDN = "o=Bund,c=DE";
        String filter = "(cn=Bund*)";
        String attribute = "mail";
        
        List<LDAPEntry> matches = ldapOperation.search(baseDN, filter, attribute);

        assertTrue(matches.size() >= 1);
    }
    
    @Test
    public void search_not_found() throws NamingException
    {
        String baseDN = "o=Bund,c=DE";
        String filter = "(cn=This_Should_Not_Be_In_That_LDAP_Tree)";
        String attribute = "mail";
        
        List<LDAPEntry> matches = ldapOperation.search(baseDN, filter, attribute);

        assertTrue(matches.isEmpty());
    }
    
    @Test
    public void search_mapper() throws NamingException
    {
        String baseDN = "o=Bund,c=DE";
        String filter = "(cn=Bund*)";
        
        List<OneProperty> matches = ldapOperation.search(baseDN, filter, oneMapper);

        assertTrue(matches.size() >= 1);
    }
    
    @Test
    public void search_mapper_not_found() throws NamingException
    {
        String baseDN = "o=Bund,c=DE";
        String filter = "(cn=This_Should_Not_Be_In_That_LDAP_Tree)";
        
        List<OneProperty> matches = ldapOperation.search(baseDN, filter, oneMapper);

        assertTrue(matches.isEmpty());
    }
    
    @Test
    public void lookup() throws NamingException
    {
    	String baseDN = "o=Bund,c=DE";
        String filter = "(cn=Bund*)";
        
        List<LDAPEntry> matches = ldapOperation.search(baseDN, filter);
        
        assertTrue(matches.size() >= 1);
        
        LDAPEntry match = matches.get(0);
        
        String matchDN = match.getDN();
        LDAPEntry entry = ldapOperation.lookup(matchDN);
        
        assertNotNull(entry);
    }
    
    @Test
    public void lookup_not_present() throws NamingException
    {
    	String matchDN = "cn=This_Should_Not_Be_In_That_LDAP_Tree,o=Bund,c=DE";
        
        LDAPEntry entry = ldapOperation.lookup(matchDN);
        
        assertNull(entry);
    }
    
    @Test
    public void lookup_mapper() throws NamingException
    {
    	String baseDN = "o=Bund,c=DE";
        String filter = "(cn=Bund*)";
        
        List<LDAPEntry> matches = ldapOperation.search(baseDN, filter);
        
        assertTrue(matches.size() >= 1);
        
        LDAPEntry match = matches.get(0);
        
        String matchDN = match.getDN();
        
        OneProperty one = ldapOperation.lookup(matchDN, oneMapper);
        
        assertNotNull(one);
    }
    
    @Test
    public void lookup_mapper_not_present() throws NamingException
    {
    	String matchDN = "cn=This_Should_Not_Be_In_That_LDAP_Tree,o=Bund,c=DE";
        
        OneProperty one = ldapOperation.lookup(matchDN, oneMapper);
        
        assertNull(one);
    }

}
