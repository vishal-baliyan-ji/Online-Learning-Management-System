package com.lms.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Database Connection Utility
 * File: src/com/lms/util/DBConnection.java
 * Manages database connections for the LMS
 */
public class DBConnection {
    
    // Database credentials
    private static final String URL = "jdbc:mysql://localhost:3306/lms_db?useSSL=false&serverTimezone=UTC";
    private static final String USER = "root";
    private static final String PASSWORD = "YourPassword"; // Change this to your MySQL password
    private static final String DRIVER = "com.mysql.cj.jdbc.Driver";
    
    // Static block to load the JDBC driver
    static {
        try {
            Class.forName(DRIVER);
            System.out.println("MySQL JDBC Driver loaded successfully");
        } catch (ClassNotFoundException e) {
            System.err.println("MySQL JDBC Driver not found");
            e.printStackTrace();
        }
    }
    
    /**
     * Get a database connection
     * @return Connection object or null if connection fails
     */
    public static Connection getConnection() {
        Connection connection = null;
        try {
            connection = DriverManager.getConnection(URL, USER, PASSWORD);
            System.out.println("Database connection established");
        } catch (SQLException e) {
            System.err.println("Failed to establish database connection");
            e.printStackTrace();
        }
        return connection;
    }
    
    /**
     * Close database connection
     * @param connection Connection to close
     */
    public static void closeConnection(Connection connection) {
        if (connection != null) {
            try {
                connection.close();
                System.out.println("Database connection closed");
            } catch (SQLException e) {
                System.err.println("Failed to close database connection");
                e.printStackTrace();
            }
        }
    }
    
    /**
     * Test the database connection
     * @return true if connection successful, false otherwise
     */
    public static boolean testConnection() {
        Connection conn = null;
        try {
            conn = getConnection();
            return conn != null && !conn.isClosed();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            closeConnection(conn);
        }
    }
    
    // Main method for testing
    public static void main(String[] args) {
        if (testConnection()) {
            System.out.println("Database connection test: SUCCESS");
        } else {
            System.out.println("Database connection test: FAILED");
        }
    }
}