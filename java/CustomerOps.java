import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

// File: CustomerOps.java
// CRUD operations for CUSTOMER table
public class CustomerOps {

    // 1. Insert a new customer record
    public static void insert(int id, String fName, String lName, String email, String regDate) {
        Connection con = null;
        PreparedStatement ps = null;
        try {
            con = DBConnection.getConnection();
            if (con == null) return;
            String sql = "INSERT INTO CUSTOMER (Customer_ID, First_Name, Last_Name, Email, Registration_Date) VALUES (?, ?, ?, ?, TO_DATE(?, 'YYYY-MM-DD'))";
            ps = con.prepareStatement(sql);
            ps.setInt(1, id);
            ps.setString(2, fName);
            ps.setString(3, lName);
            ps.setString(4, email);
            ps.setString(5, regDate);

            int rows = ps.executeUpdate();
            con.commit();
            System.out.println("Customer added successfully. Rows affected: " + rows);
        } catch (SQLException e) {
            System.out.println("Insert failed: " + e.getMessage());
        } finally {
            closeResources(null, ps, con);
        }
    }

    // 2. Update existing customer name and email
    public static void update(int id, String fName, String lName, String email) {
        Connection con = null;
        PreparedStatement ps = null;
        try {
            con = DBConnection.getConnection();
            if (con == null) return;
            String sql = "UPDATE CUSTOMER SET First_Name = ?, Last_Name = ?, Email = ? WHERE Customer_ID = ?";
            ps = con.prepareStatement(sql);
            ps.setString(1, fName);
            ps.setString(2, lName);
            ps.setString(3, email);
            ps.setInt(4, id);

            int rows = ps.executeUpdate();
            con.commit();
            if (rows > 0) {
                System.out.println("Customer updated successfully.");
            } else {
                System.out.println("Customer ID not found.");
            }
        } catch (SQLException e) {
            System.out.println("Update failed: " + e.getMessage());
        } finally {
            closeResources(null, ps, con);
        }
    }

    // 3. Delete customer by ID
    public static void delete(int id) {
        Connection con = null;
        PreparedStatement ps = null;
        try {
            con = DBConnection.getConnection();
            if (con == null) return;
            String sql = "DELETE FROM CUSTOMER WHERE Customer_ID = ?";
            ps = con.prepareStatement(sql);
            ps.setInt(1, id);

            int rows = ps.executeUpdate();
            con.commit();
            if (rows > 0) {
                System.out.println("Customer deleted successfully.");
            } else {
                System.out.println("Customer ID not found.");
            }
        } catch (SQLException e) {
            if (e.getErrorCode() == 2292 || e.getMessage().contains("2292")) {
                System.out.println("Cannot delete: record is used in another table");
            } else {
                System.out.println("Delete failed: " + e.getMessage());
            }
        } finally {
            closeResources(null, ps, con);
        }
    }

    // 4. Search customer by ID
    public static void search(int id) {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            con = DBConnection.getConnection();
            if (con == null) return;
            String sql = "SELECT Customer_ID, First_Name, Last_Name, Email, TO_CHAR(Registration_Date, 'YYYY-MM-DD') AS RegDate FROM CUSTOMER WHERE Customer_ID = ?";
            ps = con.prepareStatement(sql);
            ps.setInt(1, id);
            rs = ps.executeQuery();

            if (rs.next()) {
                System.out.println("ID: " + rs.getInt("Customer_ID") + 
                                   " | Name: " + rs.getString("First_Name") + " " + rs.getString("Last_Name") + 
                                   " | Email: " + rs.getString("Email") + 
                                   " | Registered: " + rs.getString("RegDate"));
            } else {
                System.out.println("Customer ID not found.");
            }
        } catch (SQLException e) {
            System.out.println("Search failed: " + e.getMessage());
        } finally {
            closeResources(rs, ps, con);
        }
    }

    // 5. Display all customers
    public static void display() {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            con = DBConnection.getConnection();
            if (con == null) return;
            String sql = "SELECT Customer_ID, First_Name, Last_Name, Email, TO_CHAR(Registration_Date, 'YYYY-MM-DD') AS RegDate FROM CUSTOMER ORDER BY Customer_ID";
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();

            System.out.println("--- All Customers ---");
            while (rs.next()) {
                System.out.println("ID: " + rs.getInt("Customer_ID") + 
                                   " | Name: " + rs.getString("First_Name") + " " + rs.getString("Last_Name") + 
                                   " | Email: " + rs.getString("Email") + 
                                   " | Registered: " + rs.getString("RegDate"));
            }
        } catch (SQLException e) {
            System.out.println("Display failed: " + e.getMessage());
        } finally {
            closeResources(rs, ps, con);
        }
    }

    // Helper method to close JDBC resources
    private static void closeResources(ResultSet rs, PreparedStatement ps, Connection con) {
        try {
            if (rs != null) rs.close();
            if (ps != null) ps.close();
            if (con != null) con.close();
        } catch (SQLException e) {
            System.out.println("Error closing resources: " + e.getMessage());
        }
    }
}
