import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

// File: DBConnection.java
// Central database connection helper class
public class DBConnection {

    // Connection parameters
    // College Lab Server URL Format: jdbc:oracle:thin:@HOST:PORT/SERVICE_NAME
    // Example: jdbc:oracle:thin:@172.16.0.5:1521/orcl
    public static String url = "jdbc:oracle:thin:@localhost:1521/XEPDB1";
    public static String username = "system";
    public static String password = "SYS";

    // Returns active database connection with auto-commit disabled for manual commit
    public static Connection getConnection() {
        Connection con = null;
        try {
            Class.forName("oracle.jdbc.driver.OracleDriver");
            con = DriverManager.getConnection(url, username, password);
            if (con != null) {
                con.setAutoCommit(false);
            }
        } catch (ClassNotFoundException e) {
            System.out.println("Oracle JDBC Driver Class Not Found: " + e.getMessage());
        } catch (SQLException e) {
            System.out.println("Database Connection Error: " + e.getMessage());
        }
        return con;
    }
}
