import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

// File: StationOps.java
// CRUD operations for STATION table
public class StationOps {

    // 1. Insert a new station record
    public static void insert(int id, int opId, String street, int pincode, String contact, double tariff) {
        Connection con = null;
        PreparedStatement ps = null;
        try {
            con = DBConnection.getConnection();
            if (con == null) return;
            String sql = "INSERT INTO STATION (Station_ID, Operator_ID, Street, Pincode, Contact_No, Tariff_Rate) VALUES (?, ?, ?, ?, ?, ?)";
            ps = con.prepareStatement(sql);
            ps.setInt(1, id);
            ps.setInt(2, opId);
            ps.setString(3, street);
            ps.setInt(4, pincode);
            ps.setString(5, contact);
            ps.setDouble(6, tariff);

            int rows = ps.executeUpdate();
            con.commit();
            System.out.println("Station added successfully. Rows affected: " + rows);
        } catch (SQLException e) {
            System.out.println("Insert failed: " + e.getMessage());
        } finally {
            closeResources(null, ps, con);
        }
    }

    // 2. Update station contact and tariff rate
    public static void update(int id, String contact, double tariff) {
        Connection con = null;
        PreparedStatement ps = null;
        try {
            con = DBConnection.getConnection();
            if (con == null) return;
            String sql = "UPDATE STATION SET Contact_No = ?, Tariff_Rate = ? WHERE Station_ID = ?";
            ps = con.prepareStatement(sql);
            ps.setString(1, contact);
            ps.setDouble(2, tariff);
            ps.setInt(3, id);

            int rows = ps.executeUpdate();
            con.commit();
            if (rows > 0) {
                System.out.println("Station updated successfully.");
            } else {
                System.out.println("Station ID not found.");
            }
        } catch (SQLException e) {
            System.out.println("Update failed: " + e.getMessage());
        } finally {
            closeResources(null, ps, con);
        }
    }

    // 3. Delete station by ID
    public static void delete(int id) {
        Connection con = null;
        PreparedStatement ps = null;
        try {
            con = DBConnection.getConnection();
            if (con == null) return;
            String sql = "DELETE FROM STATION WHERE Station_ID = ?";
            ps = con.prepareStatement(sql);
            ps.setInt(1, id);

            int rows = ps.executeUpdate();
            con.commit();
            if (rows > 0) {
                System.out.println("Station deleted successfully.");
            } else {
                System.out.println("Station ID not found.");
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

    // 4. Search station by ID
    public static void search(int id) {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            con = DBConnection.getConnection();
            if (con == null) return;
            String sql = "SELECT Station_ID, Operator_ID, Street, Pincode, Contact_No, Tariff_Rate FROM STATION WHERE Station_ID = ?";
            ps = con.prepareStatement(sql);
            ps.setInt(1, id);
            rs = ps.executeQuery();

            if (rs.next()) {
                System.out.println("Station ID: " + rs.getInt("Station_ID") + 
                                   " | Operator: " + rs.getInt("Operator_ID") + 
                                   " | Street: " + rs.getString("Street") + 
                                   " | Pincode: " + rs.getInt("Pincode") + 
                                   " | Phone: " + rs.getString("Contact_No") + 
                                   " | Tariff: Rs." + rs.getDouble("Tariff_Rate"));
            } else {
                System.out.println("Station ID not found.");
            }
        } catch (SQLException e) {
            System.out.println("Search failed: " + e.getMessage());
        } finally {
            closeResources(rs, ps, con);
        }
    }

    // 5. Display all stations
    public static void display() {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            con = DBConnection.getConnection();
            if (con == null) return;
            String sql = "SELECT Station_ID, Operator_ID, Street, Pincode, Contact_No, Tariff_Rate FROM STATION ORDER BY Station_ID";
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();

            System.out.println("--- All Charging Stations ---");
            while (rs.next()) {
                System.out.println("Station ID: " + rs.getInt("Station_ID") + 
                                   " | Operator: " + rs.getInt("Operator_ID") + 
                                   " | Street: " + rs.getString("Street") + 
                                   " | Pincode: " + rs.getInt("Pincode") + 
                                   " | Phone: " + rs.getString("Contact_No") + 
                                   " | Tariff: Rs." + rs.getDouble("Tariff_Rate"));
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
