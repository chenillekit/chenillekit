/*
 * Apache License
 * Version 2.0, January 2004
 * http://www.apache.org/licenses/
 *
 * Copyright 2008-2010 by chenillekit.org
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 */

package org.chenillekit.access.integration.app1.services;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

import org.apache.tapestry5.SymbolConstants;
import org.apache.tapestry5.ioc.MappedConfiguration;
import org.apache.tapestry5.ioc.OrderedConfiguration;
import org.apache.tapestry5.ioc.ServiceBinder;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.ioc.annotations.SubModule;
import org.apache.tapestry5.ioc.annotations.Symbol;
import org.apache.tapestry5.services.ApplicationStateManager;

import org.chenillekit.access.ChenilleKitAccessConstants;
import org.chenillekit.access.ChenilleKitAccessModule;
import org.chenillekit.access.integration.app1.services.impl.NoOpAppServerLoginService;
import org.chenillekit.access.integration.app1.services.impl.UserAuthServiceImpl;
import org.chenillekit.access.integration.app1.services.impl.UserEqualPassCheck;
import org.chenillekit.access.services.AppServerLoginService;
import org.chenillekit.access.services.AuthenticationServiceFilter;

/**
 * @version $Id$
 */
@SubModule(ChenilleKitAccessModule.class)
public class TestAppWithRootModule
{
	/**
	 * Bind extra services.
	 *
	 * @param binder object to bind services to
	 */
	public static void bind(ServiceBinder binder)
	{
		binder.bind(AppServerLoginService.class, NoOpAppServerLoginService.class);
	}

	/**
	 * @param configuration
	 */
	public static void contributeAuthenticationService(OrderedConfiguration<AuthenticationServiceFilter> configuration,
													   ApplicationStateManager stateManager)
	{
		configuration.add("SAMPLE", new UserAuthServiceImpl(stateManager));
		configuration.add("EQUALTEST", new UserEqualPassCheck(stateManager), "before:*");
	}

	/**
	 * @param configuration
	 */
	public static void contributeApplicationDefaults(MappedConfiguration<String, String> configuration)
	{
		configuration.add(ChenilleKitAccessConstants.LOGIN_PAGE, "login");
		configuration.add(ChenilleKitAccessConstants.ACCESS_DENIED_ACTION, ChenilleKitAccessConstants.JUMP_TO_LOGIN_PAGE);
		configuration.add(SymbolConstants.PRODUCTION_MODE, "false");

		configuration.add("jdbcDriverClassName", "org.h2.Driver");
		configuration.add("jdbcConnectionUrl", "jdbc:h2:./target/testDB");
		configuration.add("userName", "sa");
		configuration.add("passWord", "");
		configuration.add("tableName", "permission");
	}

	public static Connection buildConnection(@Inject @Symbol("jdbcDriverClassName") String jdbcDriverClassName,
											 @Inject @Symbol("jdbcConnectionUrl") String jdbcConnectionUrl,
											 @Inject @Symbol("userName") String userName,
											 @Inject @Symbol("passWord") String passWord,
											 @Inject @Symbol("tableName") String tableName)
	{
		try
		{
			Class.forName(jdbcDriverClassName);
			Connection connection = DriverManager.getConnection(jdbcConnectionUrl, userName, passWord);

			Statement statement = connection.createStatement();
			statement.execute("DROP TABLE IF EXISTS " + tableName);
			statement.execute("CREATE TABLE " + tableName + " (COMPONENT_ID VARCHAR(99), GROUPS VARCHAR(255), ROLE INTEGER)");
			statement.execute("INSERT INTO " + tableName + " (COMPONENT_ID, GROUPS, ROLE) " +
					"VALUES ('ManagedRestrictedPage', 'admin', 10)," +
					"('ManagedRestrictedPage.event1', 'admin', 10)," +
					"('ManagedRestrictedPage.event2', 'admin', 100)");

			return connection;
		}
		catch (ClassNotFoundException e)
		{
			throw new RuntimeException(e);
		}
		catch (SQLException e)
		{
			throw new RuntimeException(e);
		}
	}
}
