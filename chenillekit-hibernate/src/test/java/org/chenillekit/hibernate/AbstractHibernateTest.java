package org.chenillekit.hibernate;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.AnnotationConfiguration;

import org.testng.Assert;

/**
 * @version $Id$
 */
public class AbstractHibernateTest extends Assert
{
    protected SessionFactory sessionFactory;

    public void setup()
    {
        AnnotationConfiguration configuration = new AnnotationConfiguration();
        configuration.addPackage("org.chenillekit.hibernate.entities");
        configuration.configure();
        sessionFactory = configuration.buildSessionFactory();
    }
}
