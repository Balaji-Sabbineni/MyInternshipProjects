package org.example;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    private static Connection connection;

    public static Connection getConnection() throws SQLException {
        // Database connection details
        String connectionString = "jdbc:mysql://"+configs.dbhost+":"+configs.dbport+"/"+configs.dbname;
        String usernameDB = configs.dbuser;
        String passwordDB = configs.dbpassword;

        if (connection == null || connection.isClosed()) {
            try {
                Class.forName("com.mysql.cj.jdbc.Driver"); // Load the JDBC driver
                connection = DriverManager.getConnection(connectionString, usernameDB, passwordDB);
            } catch (ClassNotFoundException e) {
                throw new SQLException("Error loading JDBC driver", e);
            }
        }
        return connection;
    }
}
