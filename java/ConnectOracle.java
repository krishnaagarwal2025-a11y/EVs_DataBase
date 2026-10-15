import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

// File: ConnectOracle.java
// Lab manual connectivity test for Oracle Database
public class ConnectOracle {

    // Database connection parameters
    // Change host, port, service name, username, and password as per lab setup
    static String url = "jdbc:oracle:thin:@localhost:1521/XEPDB1";
    static String username = "system";
    static String password = "SYS";

    public static void main(String[] args) {
        Connection con = null;
        try {
            // Load Oracle JDBC Driver
            Class.forName("oracle.jdbc.driver.OracleDriver");

            // Establish connection
            con = DriverManager.getConnection(url, username, password);

            if (con != null) {
                System.out.println("Database Connected Successfully");
            }
        } catch (ClassNotFoundException e) {
            System.out.println("Oracle JDBC Driver not found: " + e.getMessage());
        } catch (SQLException e) {
            System.out.println("Connection failed: " + e.getMessage());
        } finally {
            try {
                if (con != null) {
                    con.close();
                }
            } catch (SQLException e) {
                System.out.println("Error closing connection: " + e.getMessage());
            }
        }
    }
}
