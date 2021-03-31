package cn.beingyi.mysql.utils;

import cn.beingyi.mysql.core.ReadyCore;

import java.sql.*;


public class JDBCUtils {
    
    static {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
    
    public static Connection getConnection(ReadyCore readyCore) throws Exception {
        Connection connection = DriverManager.getConnection(readyCore.getReady().getDbUrl(), readyCore.getReady().getUserName(), readyCore.getReady().getPassWord());
        return connection;
    }
    
    
    public static void closeResource(ResultSet resultSet, Statement statement, Connection connection) {
        closeResultSet(resultSet);
        closeStatement(statement);
        closeConnection(connection);
    }


    public static void closeResource(ResultSet resultSet,Statement statement) {
        closeResultSet(resultSet);
        closeStatement(statement);
    }
    
    public static void closeResource(Statement statement, Connection connection) {
        closeStatement(statement);
        closeConnection(connection);
    }

    public static void closeConnection(Connection connection) {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public static void closeStatement(Statement statement) {
        if (statement != null) {
            try {
                statement.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public static void closeResultSet(ResultSet resultSet) {
        if (resultSet != null) {
            try {
                resultSet.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
