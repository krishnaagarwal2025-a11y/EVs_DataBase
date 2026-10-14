import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

// File: PointOps.java
// CRUD operations for CHARGING_POINT table
public class PointOps {

    // 1. Insert a new charging point
    public static void insert(int id, int stationId, String connector, String mode, String status, double power) {
        Connection con = null;
        PreparedStatement ps = null;
        try {
            con = DBConnection.getConnection();
            if (con == null) return;
            String sql = "INSERT INTO CHARGING_POINT (Point_ID, Station_ID, Connector_Type, Charging_Mode, Status, Power_Rating) VALUES (?, ?, ?, ?, ?, ?)";
            ps = con.prepareStatement(sql);
            ps.setInt(1, id);
            ps.setInt(2, stationId);
            ps.setString(3, connector);
            ps.setString(4, mode);
            ps.setString(5, status);
            ps.setDouble(6, power);

            int rows = ps.executeUpdate();
            con.commit();
            System.out.println("Charging point added successfully. Rows affected: " + rows);
        } catch (SQLException e) {
            System.out.println("Insert failed: " + e.getMessage());
        } finally {
            closeResources(null, ps, con);
        }
    }

    // 2. Update status and power rating of a charging point
    public static void update(int id, String status, double power) {
        Connection con = null;
        PreparedStatement ps = null;
        try {
            con = DBConnection.getConnection();
            if (con == null) return;
            String sql = "UPDATE CHARGING_POINT SET Status = ?, Power_Rating = ? WHERE Point_ID = ?";
            ps = con.prepareStatement(sql);
            ps.setString(1, status);
            ps.setDouble(2, power);
            ps.setInt(3, id);

            int rows = ps.executeUpdate();
            con.commit();
            if (rows > 0) {
                System.out.println("Charging point updated successfully.");
            } else {
                System.out.println("Charging Point ID not found.");
            }
        } catch (SQLException e) {
            System.out.println("Update failed: " + e.getMessage());
        } finally {
            closeResources(null, ps, con);
        }
    }

    // 3. Delete charging point by ID
    public static void delete(int id) {
        Connection con = null;
        PreparedStatement ps = null;
        try {
            con = DBConnection.getConnection();
            if (con == null) return;
            String sql = "DELETE FROM CHARGING_POINT WHERE Point_ID = ?";
            ps = con.prepareStatement(sql);
            ps.setInt(1, id);

            int rows = ps.executeUpdate();
            con.commit();
            if (rows > 0) {
                System.out.println("Charging point deleted successfully.");
            } else {
                System.out.println("Charging Point ID not found.");
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

    // 4. Search charging point by ID
    public static void search(int id) {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            con = DBConnection.getConnection();
            if (con == null) return;
            String sql = "SELECT Point_ID, Station_ID, Connector_Type, Charging_Mode, Status, Power_Rating FROM CHARGING_POINT WHERE Point_ID = ?";
            ps = con.prepareStatement(sql);
            ps.setInt(1, id);
            rs = ps.executeQuery();

            if (rs.next()) {
                System.out.println("Point ID: " + rs.getInt("Point_ID") + 
                                   " | Station ID: " + rs.getInt("Station_ID") + 
                                   " | Plug: " + rs.getString("Connector_Type") + 
                                   " | Mode: " + rs.getString("Charging_Mode") + 
                                   " | Status: " + rs.getString("Status") + 
                                   " | Power: " + rs.getDouble("Power_Rating") + " kW");
            } else {
                System.out.println("Charging Point ID not found.");
            }
        } catch (SQLException e) {
            System.out.println("Search failed: " + e.getMessage());
        } finally {
            closeResources(rs, ps, con);
        }
    }

    // 5. Display all charging points
    public static void display() {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            con = DBConnection.getConnection();
            if (con == null) return;
            String sql = "SELECT Point_ID, Station_ID, Connector_Type, Charging_Mode, Status, Power_Rating FROM CHARGING_POINT ORDER BY Point_ID";
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();

            System.out.println("--- All Charging Points ---");
            while (rs.next()) {
                System.out.println("Point ID: " + rs.getInt("Point_ID") + 
                                   " | Station ID: " + rs.getInt("Station_ID") + 
                                   " | Plug: " + rs.getString("Connector_Type") + 
                                   " | Mode: " + rs.getString("Charging_Mode") + 
                                   " | Status: " + rs.getString("Status") + 
                                   " | Power: " + rs.getDouble("Power_Rating") + " kW");
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
