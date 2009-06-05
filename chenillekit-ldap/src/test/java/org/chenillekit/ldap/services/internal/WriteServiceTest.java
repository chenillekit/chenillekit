/**
 * 
 */
package org.chenillekit.ldap.services.internal;

import netscape.ldap.LDAPConnection;
import netscape.ldap.LDAPEntry;

import org.chenillekit.ldap.ChenilleKitLDAPTestModule;
import org.chenillekit.test.AbstractTestSuite;
import org.easymock.classextension.EasyMock;
import org.slf4j.Logger;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

/**
 * 
 * 
 */
public class WriteServiceTest extends AbstractTestSuite
{
	private WriteService writeService;
	
	private WriteServiceImpl writeServiceImpl;
	private Logger mockLogger;
	private LDAPSource mockSource;
	
	@BeforeTest
    public final void setup_registry()
    {
        super.setup_registry(ChenilleKitLDAPTestModule.class);
        writeService = registry.getService(WriteService.class);
    }
	
	@BeforeMethod
	public final void setup_mocks()
	{
		mockLogger = EasyMock.createMock(Logger.class);
		mockSource = EasyMock.createMock(LDAPSource.class);
		
		writeServiceImpl = new WriteServiceImpl(mockLogger, mockSource);
	}

	@Test
	public void add_entry() throws Exception
	{
		LDAPConnection mockConn = EasyMock.createMock(LDAPConnection.class);
		
		EasyMock.expect(mockSource.openSession()).andReturn(mockConn);
		
		EasyMock.replay(mockSource);
		
		writeServiceImpl.addEntry("uid=duffy_duck", null);
		
		EasyMock.verify(mockSource);
	}
	
	@Test
	public void delete_entry_npe() throws Exception
	{
		try
		{
			mockSource.openSession().delete("uid=null");
			
			writeServiceImpl.deleteEntry("uid=null");
		}
		catch (NullPointerException npe)
		{
			return;
		}
		
		fail();
	}
}
