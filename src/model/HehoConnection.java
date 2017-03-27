package model;

import java.sql.*;

public class HehoConnection {
    private static String url;
    private static String username;
    private static String password;

    static {
        url = "jdbc:mysql://localhost:3306/heho_bd";
        username = "root";
        password = "root";
    }

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(url, username, password);
    }
}
