package util;

import java.sql.Connection;
import java.sql.DriverManager;


public class DBUtil {

    private static final String URL = "jdbc:mysql://localhost:3306/inventory_db";
    private static final String USER = "root";
    private static final String PASSWORD = "Yugam@1234";

    public static Connection getConnection() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");

            Connection con = DriverManager.getConnection(URL, USER, PASSWORD);
            System.out.println("Database Connected Successfully");

            return con;

        } catch (Exception e) {
            System.out.println("Database Connection Failed");
            e.printStackTrace();
            return null;
        }
    }
    
}