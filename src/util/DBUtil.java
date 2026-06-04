package util;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class DBUtil {

    private static final String URL;
    private static final String USER;
    private static final String PASSWORD;

    static {
        Properties props = new Properties();
        try (InputStream in = DBUtil.class.getClassLoader().getResourceAsStream("db.properties")) {
            if (in != null) {
                props.load(in);
            }
        } catch (Exception e) {
            System.out.println("[DBUtil] db.properties not found, using hardcoded defaults.");
        }
        URL      = props.getProperty("db.url",      "jdbc:mysql://localhost:3306/inventory_db");
        USER     = props.getProperty("db.user",     "root");
        PASSWORD = props.getProperty("db.password", "Yugam@1234");
    }

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}
