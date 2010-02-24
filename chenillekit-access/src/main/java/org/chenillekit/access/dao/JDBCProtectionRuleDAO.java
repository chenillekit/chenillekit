/*
 * Apache License
 * Version 2.0, January 2004
 * http://www.apache.org/licenses/
 *
 * Copyright 2010 by chenillekit.org
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

/**
 * @author <a href="mailto:shomburg@depot120.dpd.de">S.Homburg</a>
 * @version $Id$
 */
public class JDBCProtectionRuleDAO implements ProtectionRuleDAO<ProtectionRuleImpl>
{
    private final String tableName;
    private final Connection connection;
    private final PreparedStatement preparedStatement;

    public JDBCProtectionRuleDAO(Connection connection, String tableName)
    {
        this.connection = connection;
        this.tableName = tableName;

        try
        {
            preparedStatement = connection.prepareStatement(String.format("SELECT * FROM %s WHERE component_id = ?", tableName));
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
            preparedStatement.setString(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next())
            {
                protectionRule = new ProtectionRuleImpl();
                protectionRule.setGroups(resultSet.getString(2));
                protectionRule.setRoleWeight(resultSet.getInt(3));
            }

            return protectionRule;
        }
        catch (SQLException e)
        {
            throw new RuntimeException(e);
        }
    }
}
