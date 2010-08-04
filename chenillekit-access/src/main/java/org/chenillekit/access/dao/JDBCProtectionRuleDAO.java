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

package org.chenillekit.access.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.slf4j.Logger;

/**
 * @version $Id$
 */
public class JDBCProtectionRuleDAO implements ProtectionRuleDAO<ProtectionRuleImpl>
{
	private final Logger logger;
    private final PreparedStatement preparedStatement;

    public JDBCProtectionRuleDAO(Logger logger, Connection connection, String tableName)
    {
		this.logger = logger;
		
        try
        {
            preparedStatement = connection.prepareStatement(String.format("SELECT * FROM %s WHERE COMPONENT_ID = ?", tableName));
        }
        catch (SQLException e)
        {
            throw new RuntimeException(e);
        }
    }

    /**
     * retrieve the record that holds the protection informations for a page/method.
     *
     * @param id ID of page/method
     * @return protection informations
     */
    public ProtectionRuleImpl retrieveProtectionRule(String id)
    {
        ProtectionRuleImpl protectionRule = null;

        try
        {
			if (logger.isDebugEnabled())
				logger.debug("searching for component id {}", id);
			
            preparedStatement.setString(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next())
            {
                protectionRule = new ProtectionRuleImpl();
                protectionRule.setGroups(resultSet.getString("GROUPS"));
                protectionRule.setRoleWeight(resultSet.getInt("ROLE_WEIGHT"));
            }

            return protectionRule;
        }
        catch (SQLException e)
        {
            throw new RuntimeException(e);
        }
    }
}
