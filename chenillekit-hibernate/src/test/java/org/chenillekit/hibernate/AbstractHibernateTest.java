package org.chenillekit.hibernate;

import org.chenillekit.test.AbstractTestSuite;
import org.hibernate.Session;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;

/**
 * @version $Id$
 */
public class AbstractHibernateTest extends AbstractTestSuite
{
    protected Session session;

    @BeforeTest
    public void setup()
    {
        super.setup_registry(ChenillekitHibernateTestModule.class);
        session = registry.getService("Session", Session.class);
    }

    @AfterTest
    public void afterTest()
    {
        session.getSessionFactory().close();
    }
}
