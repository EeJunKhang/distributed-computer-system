package database;

import java.sql.*;


public class DBConnection {
     private static final String URL = "jdbc:mysql://localhost:3306/food_ordering_database";
    private static final String USER = "root";
    private static final String PASSWORD = "P@ssw0rd";
    public static Connection getConnection() throws SQLException{
        return (Connection) DriverManager.getConnection(URL, USER, PASSWORD);
    }
}
