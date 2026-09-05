package com.library_fine_calculation_system.util;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class DBConnection {

    private static final String URL;
    private static final String USERNAME;
    private static final String PASSWORD;

    static {

        try {

            Properties properties = new Properties();

            InputStream input = DBConnection.class
                    .getClassLoader()
                    .getResourceAsStream("db.properties");

            if (input == null) {
                throw new RuntimeException(
                        "db.properties file not found!"
                );
            }

            properties.load(input);

            URL = properties.getProperty("db.url");
            USERNAME = properties.getProperty("db.username");
            PASSWORD = properties.getProperty("db.password");

        } catch (Exception e) {

            throw new RuntimeException(
                    "Failed to load database configuration!", e
            );
        }
    }


    public static Connection getConnection()
            throws SQLException {

        return DriverManager.getConnection(
                URL,
                USERNAME,
                PASSWORD
        );
    }
}
