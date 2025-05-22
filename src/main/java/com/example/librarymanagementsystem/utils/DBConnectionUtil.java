package com.example.librarymanagementsystem.utils;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

// Utility class for managing database connections
public class DBConnectionUtil {
    // Database connection properties
    private static final String URL;
    private static final String USER;
    private static final String PASS;
    // Connection pool properties
    private static final int MIN_CONNECTIONS;
    private static final int MAX_CONNECTIONS;
    private static final int CONNECTION_TIMEOUT;

    // Static initialization block to load properties once when the class is loaded
    static {
        try (InputStream is = DBConnectionUtil.class.getClassLoader().getResourceAsStream("db.properties")) {
            // Check if properties file exists
            if (is == null) {
                throw new RuntimeException("db.properties file not found in classpath");
            }

            // Load properties from db.properties file
            Properties prop = new Properties();
            prop.load(is);

            // Initialize database connection properties
            URL = prop.getProperty("db.url");
            USER = prop.getProperty("db.username");
            PASS = prop.getProperty("db.password");
            String driver = prop.getProperty("db.driver");

            // Initialize connection pool properties with defaults
            MIN_CONNECTIONS = Integer.parseInt(prop.getProperty("db.min_connections", "5"));
            MAX_CONNECTIONS = Integer.parseInt(prop.getProperty("db.max_connections", "20"));
            CONNECTION_TIMEOUT = Integer.parseInt(prop.getProperty("db.connection_timeout", "30000"));

            // Load the JDBC driver
            Class.forName(driver);

            // Log successful loading of properties
            System.out.println("Database connection properties loaded successfully");
        } catch (IOException | ClassNotFoundException | NumberFormatException e) {
            // Log and wrap errors for debugging
            System.err.println("Error loading database properties: " + e.getMessage());
            throw new RuntimeException("Failed to load database properties", e);
        }
    }

    // Establishes and returns a new database connection
    public static Connection getConnection() throws SQLException {
        // Use DriverManager to create a connection with configured properties
        return DriverManager.getConnection(URL, USER, PASS);
    }

    /**
     * Get the minimum number of connections for the connection pool
     *
     * @return Minimum number of connections
     */
    public static int getMinConnections() {
        return MIN_CONNECTIONS;
    }

    /**
     * Get the maximum number of connections for the connection pool
     *
     * @return Maximum number of connections
     */
    public static int getMaxConnections() {
        return MAX_CONNECTIONS;
    }

    /**
     * Get the connection timeout in milliseconds
     *
     * @return Connection timeout
     */
    public static int getConnectionTimeout() {
        return CONNECTION_TIMEOUT;
    }
}