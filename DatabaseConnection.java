package com.example.demo1;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    private static final String URL = "jdbc:mysql://localhost:3306/javafx_db";
    private static final String USER = "root"; // replace with your MySQL username
    private static final String PASSWORD = "Iqra_19@2004#s"; // replace with your MySQL password

    public Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}
