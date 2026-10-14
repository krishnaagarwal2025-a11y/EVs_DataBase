import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

// File: VoltgridGUI.java
// Desktop GUI Front-End with full CRUD, dropdown foreign keys, row selection, and error handling
public class VoltgridGUI extends JFrame {

    // Connection Status
    private JLabel lblStatus;
    private JTextField txtDbPassword;
    private JButton btnConnect;

    // --- Tab 1: Customer Components ---
    private JTextField txtCustId, txtCustFName, txtCustLName, txtCustEmail, txtCustDate;
    private JTable tblCustomers;
    private DefaultTableModel modelCustomers;

    // --- Tab 2: Station Components ---
    private JTextField txtStationId, txtStreet, txtContact, txtTariff;
    private JComboBox<String> cmbOperator, cmbPincode;
    private JTable tblStations;
    private DefaultTableModel modelStations;

    // --- Tab 3: Charging Point Components ---
    private JTextField txtPointId, txtPower;
    private JComboBox<String> cmbStationId, cmbConnector, cmbMode, cmbPointStatus;
    private JTable tblPoints;
    private DefaultTableModel modelPoints;

    public VoltgridGUI() {
        setTitle("Voltgrid - Smart EV Charging Station Network (JDBC Connected)");
        setSize(1000, 720);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(5, 5));

        // Use native OS look and feel
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignored) {}

        // 1. Top Bar
        add(createTopPanel(), BorderLayout.NORTH);

        // 2. Tabs
        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.setFont(new Font("Segoe UI", Font.BOLD, 13));
        tabbedPane.addTab("Manage Customers", createCustomerPanel());
        tabbedPane.addTab("Manage Charging Stations", createStationPanel());
        tabbedPane.addTab("Manage Charging Points", createPointPanel());
        add(tabbedPane, BorderLayout.CENTER);

        // Verify database connection on startup without auto-loading records
        // Tables remain empty until the user clicks "Show All Records"
        checkConnectionOnly();
    }

    // Top Panel with Live Status and Password Override
    private JPanel createTopPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(24, 43, 73));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));

        JLabel title = new JLabel("⚡ VOLTGRID - EV CHARGING NETWORK DASHBOARD");
        title.setForeground(Color.WHITE);
        title.setFont(new Font("Segoe UI", Font.BOLD, 16));
        panel.add(title, BorderLayout.WEST);

        JPanel connPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        connPanel.setOpaque(false);

        lblStatus = new JLabel("Checking connection...");
        lblStatus.setForeground(Color.YELLOW);
        lblStatus.setFont(new Font("Segoe UI", Font.PLAIN, 12));

        JLabel lblPass = new JLabel("Oracle Password:");
        lblPass.setForeground(Color.WHITE);

        txtDbPassword = new JPasswordField(DBConnection.password, 8);

        btnConnect = new JButton("Connect / Reconnect");
        btnConnect.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                DBConnection.password = txtDbPassword.getText();
                testConnectionAndRefresh();
            }
        });

        connPanel.add(lblStatus);
        connPanel.add(lblPass);
        connPanel.add(txtDbPassword);
        connPanel.add(btnConnect);

        panel.add(connPanel, BorderLayout.EAST);
        return panel;
    }

    // ====================================================================
    // TAB 1: CUSTOMER PANEL
    // ====================================================================
    private JPanel createCustomerPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Form
        JPanel form = new JPanel(new GridLayout(5, 2, 8, 8));
        form.setBorder(BorderFactory.createTitledBorder("Customer Details & Input Formats"));

        form.add(new JLabel("Customer ID (number, e.g. 407):"));
        txtCustId = new JTextField();
        form.add(txtCustId);

        form.add(new JLabel("First Name (text, e.g. Rahul):"));
        txtCustFName = new JTextField();
        form.add(txtCustFName);

        form.add(new JLabel("Last Name (text, e.g. Verma):"));
        txtCustLName = new JTextField();
        form.add(txtCustLName);

        form.add(new JLabel("Email Address (e.g. rahul@gmail.com):"));
        txtCustEmail = new JTextField();
        form.add(txtCustEmail);

        form.add(new JLabel("Registration Date (format: YYYY-MM-DD):"));
        txtCustDate = new JTextField("2024-03-01");
        form.add(txtCustDate);

        // Buttons
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 8, 8));
        JButton btnAdd = new JButton("Add Customer");
        JButton btnUpdate = new JButton("Update Customer");
        JButton btnDelete = new JButton("Delete Customer");
        JButton btnSearch = new JButton("Search by ID");
        JButton btnClear = new JButton("Clear Form");
        JButton btnShowAll = new JButton("Show All Records");

        btnPanel.add(btnAdd);
        btnPanel.add(btnUpdate);
        btnPanel.add(btnDelete);
        btnPanel.add(btnSearch);
        btnPanel.add(btnClear);
        btnPanel.add(btnShowAll);

        JPanel topContainer = new JPanel(new BorderLayout());
        topContainer.add(form, BorderLayout.CENTER);
        topContainer.add(btnPanel, BorderLayout.SOUTH);
        panel.add(topContainer, BorderLayout.NORTH);

        // Table
        modelCustomers = new DefaultTableModel(new String[]{"ID", "First Name", "Last Name", "Email", "Reg Date"}, 0);
        tblCustomers = new JTable(modelCustomers);
        tblCustomers.setRowHeight(22);
        tblCustomers.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                int row = tblCustomers.getSelectedRow();
                if (row >= 0) {
                    txtCustId.setText(modelCustomers.getValueAt(row, 0).toString());
                    txtCustFName.setText(modelCustomers.getValueAt(row, 1).toString());
                    txtCustLName.setText(modelCustomers.getValueAt(row, 2).toString());
                    txtCustEmail.setText(modelCustomers.getValueAt(row, 3).toString());
                    txtCustDate.setText(modelCustomers.getValueAt(row, 4).toString());
                }
            }
        });
        panel.add(new JScrollPane(tblCustomers), BorderLayout.CENTER);

        // Action: Add Customer
        btnAdd.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String idStr = txtCustId.getText().trim();
                String fn = txtCustFName.getText().trim();
                String ln = txtCustLName.getText().trim();
                String em = txtCustEmail.getText().trim();
                String dt = txtCustDate.getText().trim();

                if (idStr.isEmpty() || fn.isEmpty() || em.isEmpty()) {
                    JOptionPane.showMessageDialog(VoltgridGUI.this, "Please enter Customer ID, First Name, and Email.", "Missing Input", JOptionPane.WARNING_MESSAGE);
                    return;
                }

                Connection con = null;
                PreparedStatement ps = null;
                try {
                    int id = Integer.parseInt(idStr);
                    con = DBConnection.getConnection();
                    if (con == null) return;
                    String sql = "INSERT INTO CUSTOMER (Customer_ID, First_Name, Last_Name, Email, Registration_Date) VALUES (?, ?, ?, ?, TO_DATE(?, 'YYYY-MM-DD'))";
                    ps = con.prepareStatement(sql);
                    ps.setInt(1, id);
                    ps.setString(2, fn);
                    ps.setString(3, ln);
                    ps.setString(4, em);
                    ps.setString(5, dt);
                    ps.executeUpdate();
                    con.commit();

                    loadCustomersTable();
                    clearCustomerForm();
                    JOptionPane.showMessageDialog(VoltgridGUI.this, "Customer added successfully! (ID: " + id + ")", "Success", JOptionPane.INFORMATION_MESSAGE);
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(VoltgridGUI.this, "Customer ID must be a numeric integer.", "Input Error", JOptionPane.ERROR_MESSAGE);
                } catch (SQLException ex) {
                    handleSqlException(ex);
                } finally {
                    closeQuietly(null, ps, con);
                }
            }
        });

        // Action: Update Customer
        btnUpdate.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String idStr = txtCustId.getText().trim();
                String fn = txtCustFName.getText().trim();
                String ln = txtCustLName.getText().trim();
                String em = txtCustEmail.getText().trim();

                if (idStr.isEmpty()) {
                    JOptionPane.showMessageDialog(VoltgridGUI.this, "Select a customer from the table or enter Customer ID to update.", "Input Error", JOptionPane.WARNING_MESSAGE);
                    return;
                }

                Connection con = null;
                PreparedStatement ps = null;
                try {
                    int id = Integer.parseInt(idStr);
                    con = DBConnection.getConnection();
                    if (con == null) return;
                    String sql = "UPDATE CUSTOMER SET First_Name = ?, Last_Name = ?, Email = ? WHERE Customer_ID = ?";
                    ps = con.prepareStatement(sql);
                    ps.setString(1, fn);
                    ps.setString(2, ln);
                    ps.setString(3, em);
                    ps.setInt(4, id);

                    int rows = ps.executeUpdate();
                    con.commit();
                    if (rows > 0) {
                        loadCustomersTable();
                        JOptionPane.showMessageDialog(VoltgridGUI.this, "Customer ID " + id + " updated successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                    } else {
                        JOptionPane.showMessageDialog(VoltgridGUI.this, "Customer ID " + id + " not found in database.", "Not Found", JOptionPane.WARNING_MESSAGE);
                    }
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(VoltgridGUI.this, "Customer ID must be numeric.", "Error", JOptionPane.ERROR_MESSAGE);
                } catch (SQLException ex) {
                    handleSqlException(ex);
                } finally {
                    closeQuietly(null, ps, con);
                }
            }
        });

        // Action: Delete Customer
        btnDelete.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String idStr = txtCustId.getText().trim();
                if (idStr.isEmpty()) {
                    JOptionPane.showMessageDialog(VoltgridGUI.this, "Select a customer from the table or enter Customer ID to delete.", "Input Error", JOptionPane.WARNING_MESSAGE);
                    return;
                }

                try {
                    int id = Integer.parseInt(idStr);
                    int confirm = JOptionPane.showConfirmDialog(VoltgridGUI.this, "Are you sure you want to delete Customer ID: " + id + "?", "Confirm Delete", JOptionPane.YES_NO_OPTION);
                    if (confirm == JOptionPane.YES_OPTION) {
                        Connection con = null;
                        PreparedStatement ps = null;
                        try {
                            con = DBConnection.getConnection();
                            if (con == null) return;
                            ps = con.prepareStatement("DELETE FROM CUSTOMER WHERE Customer_ID = ?");
                            ps.setInt(1, id);
                            int rows = ps.executeUpdate();
                            con.commit();

                            if (rows > 0) {
                                loadCustomersTable();
                                clearCustomerForm();
                                JOptionPane.showMessageDialog(VoltgridGUI.this, "Customer ID " + id + " deleted successfully!", "Deleted", JOptionPane.INFORMATION_MESSAGE);
                            } else {
                                JOptionPane.showMessageDialog(VoltgridGUI.this, "Customer ID " + id + " not found in database.", "Not Found", JOptionPane.WARNING_MESSAGE);
                            }
                        } catch (SQLException ex) {
                            if (ex.getErrorCode() == 2292 || ex.getMessage().contains("2292")) {
                                JOptionPane.showMessageDialog(VoltgridGUI.this, 
                                    "Cannot delete Customer ID " + id + ":\n" +
                                    "This customer is linked to existing vehicles, reservations, or sessions (Foreign Key ORA-02292).\n\n" +
                                    "Tip: To demonstrate Delete safely, add a new customer (e.g. ID 999) and delete them.", 
                                    "Integrity Constraint Protected", JOptionPane.WARNING_MESSAGE);
                            } else {
                                handleSqlException(ex);
                            }
                        } finally {
                            closeQuietly(null, ps, con);
                        }
                    }
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(VoltgridGUI.this, "Customer ID must be numeric.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        // Action: Search Customer by ID
        btnSearch.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String idStr = txtCustId.getText().trim();
                if (idStr.isEmpty()) {
                    JOptionPane.showMessageDialog(VoltgridGUI.this, "Please enter Customer ID to search.", "Input Required", JOptionPane.WARNING_MESSAGE);
                    return;
                }

                Connection con = null;
                PreparedStatement ps = null;
                ResultSet rs = null;
                try {
                    int id = Integer.parseInt(idStr);
                    con = DBConnection.getConnection();
                    if (con == null) return;
                    ps = con.prepareStatement("SELECT Customer_ID, First_Name, Last_Name, Email, TO_CHAR(Registration_Date, 'YYYY-MM-DD') AS RegDate FROM CUSTOMER WHERE Customer_ID = ?");
                    ps.setInt(1, id);
                    rs = ps.executeQuery();
                    if (rs.next()) {
                        txtCustFName.setText(rs.getString("First_Name"));
                        txtCustLName.setText(rs.getString("Last_Name"));
                        txtCustEmail.setText(rs.getString("Email"));
                        txtCustDate.setText(rs.getString("RegDate"));

                        // Highlight row in table
                        highlightTableRow(tblCustomers, 0, idStr);
                        JOptionPane.showMessageDialog(VoltgridGUI.this, "Customer Found: " + rs.getString("First_Name") + " " + rs.getString("Last_Name"), "Found", JOptionPane.INFORMATION_MESSAGE);
                    } else {
                        JOptionPane.showMessageDialog(VoltgridGUI.this, "No customer found with ID: " + id, "Not Found", JOptionPane.WARNING_MESSAGE);
                    }
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(VoltgridGUI.this, "Customer ID must be numeric.", "Error", JOptionPane.ERROR_MESSAGE);
                } catch (SQLException ex) {
                    handleSqlException(ex);
                } finally {
                    closeQuietly(rs, ps, con);
                }
            }
        });

        btnClear.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                clearCustomerForm();
            }
        });

        btnShowAll.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                loadCustomersTable();
                clearCustomerForm();
                JOptionPane.showMessageDialog(VoltgridGUI.this, 
                    "Displaying all " + modelCustomers.getRowCount() + " records from table CUSTOMER.", 
                    "All Customer Records", 
                    JOptionPane.INFORMATION_MESSAGE);
            }
        });

        return panel;
    }

    // ====================================================================
    // TAB 2: STATION PANEL
    // ====================================================================
    private JPanel createStationPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Form
        JPanel form = new JPanel(new GridLayout(6, 2, 8, 8));
        form.setBorder(BorderFactory.createTitledBorder("Station Details & Input Formats"));

        form.add(new JLabel("Station ID (number, e.g. 207):"));
        txtStationId = new JTextField();
        form.add(txtStationId);

        form.add(new JLabel("Operator (Select valid network):"));
        cmbOperator = new JComboBox<>(new String[]{
            "101 - Tata Power EV",
            "102 - Zeon Charging",
            "103 - Statiq EV",
            "104 - Ather Grid",
            "105 - Relux Electric",
            "106 - ChargeZone"
        });
        form.add(cmbOperator);

        form.add(new JLabel("Street / Address (e.g. MG Road, Anna Salai):"));
        txtStreet = new JTextField();
        form.add(txtStreet);

        form.add(new JLabel("Pincode (Select supported city):"));
        cmbPincode = new JComboBox<>(new String[]{
            "600001 - Chennai",
            "632014 - Vellore",
            "560001 - Bengaluru",
            "641001 - Coimbatore",
            "625001 - Madurai",
            "500001 - Hyderabad"
        });
        form.add(cmbPincode);

        form.add(new JLabel("Contact Number (10 digits, e.g. 9876543210):"));
        txtContact = new JTextField();
        form.add(txtContact);

        form.add(new JLabel("Tariff Rate (INR/kWh, e.g. 19.50):"));
        txtTariff = new JTextField("19.50");
        form.add(txtTariff);

        // Buttons
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 8, 8));
        JButton btnAdd = new JButton("Add Station");
        JButton btnUpdate = new JButton("Update Station");
        JButton btnDelete = new JButton("Delete Station");
        JButton btnSearch = new JButton("Search by ID");
        JButton btnClear = new JButton("Clear Form");
        JButton btnShowAll = new JButton("Show All Records");

        btnPanel.add(btnAdd);
        btnPanel.add(btnUpdate);
        btnPanel.add(btnDelete);
        btnPanel.add(btnSearch);
        btnPanel.add(btnClear);
        btnPanel.add(btnShowAll);

        JPanel topContainer = new JPanel(new BorderLayout());
        topContainer.add(form, BorderLayout.CENTER);
        topContainer.add(btnPanel, BorderLayout.SOUTH);
        panel.add(topContainer, BorderLayout.NORTH);

        // Table
        modelStations = new DefaultTableModel(new String[]{"Station ID", "Operator ID", "Street", "Pincode", "Contact", "Tariff (INR)"}, 0);
        tblStations = new JTable(modelStations);
        tblStations.setRowHeight(22);
        tblStations.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                int row = tblStations.getSelectedRow();
                if (row >= 0) {
                    txtStationId.setText(modelStations.getValueAt(row, 0).toString());
                    setComboByPrefix(cmbOperator, modelStations.getValueAt(row, 1).toString());
                    txtStreet.setText(modelStations.getValueAt(row, 2).toString());
                    setComboByPrefix(cmbPincode, modelStations.getValueAt(row, 3).toString());
                    txtContact.setText(modelStations.getValueAt(row, 4).toString());
                    txtTariff.setText(modelStations.getValueAt(row, 5).toString());
                }
            }
        });
        panel.add(new JScrollPane(tblStations), BorderLayout.CENTER);

        // Action: Add Station
        btnAdd.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String sidStr = txtStationId.getText().trim();
                String street = txtStreet.getText().trim();
                String contact = txtContact.getText().trim();
                String tariffStr = txtTariff.getText().trim();

                if (sidStr.isEmpty() || street.isEmpty() || tariffStr.isEmpty()) {
                    JOptionPane.showMessageDialog(VoltgridGUI.this, "Please enter Station ID, Street, and Tariff Rate.", "Missing Input", JOptionPane.WARNING_MESSAGE);
                    return;
                }

                Connection con = null;
                PreparedStatement ps = null;
                try {
                    int sid = Integer.parseInt(sidStr);
                    int opId = Integer.parseInt(cmbOperator.getSelectedItem().toString().split(" ")[0]);
                    int pin = Integer.parseInt(cmbPincode.getSelectedItem().toString().split(" ")[0]);
                    double tariff = Double.parseDouble(tariffStr);

                    con = DBConnection.getConnection();
                    if (con == null) return;
                    String sql = "INSERT INTO STATION (Station_ID, Operator_ID, Street, Pincode, Contact_No, Tariff_Rate) VALUES (?, ?, ?, ?, ?, ?)";
                    ps = con.prepareStatement(sql);
                    ps.setInt(1, sid);
                    ps.setInt(2, opId);
                    ps.setString(3, street);
                    ps.setInt(4, pin);
                    ps.setString(5, contact);
                    ps.setDouble(6, tariff);
                    ps.executeUpdate();
                    con.commit();

                    loadStationsTable();
                    refreshStationDropdownForPoints();
                    clearStationForm();
                    JOptionPane.showMessageDialog(VoltgridGUI.this, "Station added successfully! (ID: " + sid + ")", "Success", JOptionPane.INFORMATION_MESSAGE);
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(VoltgridGUI.this, "Station ID and Tariff must be numeric.", "Input Error", JOptionPane.ERROR_MESSAGE);
                } catch (SQLException ex) {
                    handleSqlException(ex);
                } finally {
                    closeQuietly(null, ps, con);
                }
            }
        });

        // Action: Update Station
        btnUpdate.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String sidStr = txtStationId.getText().trim();
                String street = txtStreet.getText().trim();
                String contact = txtContact.getText().trim();
                String tariffStr = txtTariff.getText().trim();

                if (sidStr.isEmpty()) {
                    JOptionPane.showMessageDialog(VoltgridGUI.this, "Select a station from the table or enter Station ID to update.", "Input Error", JOptionPane.WARNING_MESSAGE);
                    return;
                }

                Connection con = null;
                PreparedStatement ps = null;
                try {
                    int sid = Integer.parseInt(sidStr);
                    int opId = Integer.parseInt(cmbOperator.getSelectedItem().toString().split(" ")[0]);
                    int pin = Integer.parseInt(cmbPincode.getSelectedItem().toString().split(" ")[0]);
                    double tariff = Double.parseDouble(tariffStr);

                    con = DBConnection.getConnection();
                    if (con == null) return;
                    String sql = "UPDATE STATION SET Operator_ID = ?, Street = ?, Pincode = ?, Contact_No = ?, Tariff_Rate = ? WHERE Station_ID = ?";
                    ps = con.prepareStatement(sql);
                    ps.setInt(1, opId);
                    ps.setString(2, street);
                    ps.setInt(3, pin);
                    ps.setString(4, contact);
                    ps.setDouble(5, tariff);
                    ps.setInt(6, sid);

                    int rows = ps.executeUpdate();
                    con.commit();
                    if (rows > 0) {
                        loadStationsTable();
                        JOptionPane.showMessageDialog(VoltgridGUI.this, "Station ID " + sid + " updated successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                    } else {
                        JOptionPane.showMessageDialog(VoltgridGUI.this, "Station ID " + sid + " not found.", "Not Found", JOptionPane.WARNING_MESSAGE);
                    }
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(VoltgridGUI.this, "Please enter valid numeric values.", "Error", JOptionPane.ERROR_MESSAGE);
                } catch (SQLException ex) {
                    handleSqlException(ex);
                } finally {
                    closeQuietly(null, ps, con);
                }
            }
        });

        // Action: Delete Station
        btnDelete.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String sidStr = txtStationId.getText().trim();
                if (sidStr.isEmpty()) {
                    JOptionPane.showMessageDialog(VoltgridGUI.this, "Select a station from the table or enter Station ID to delete.", "Input Error", JOptionPane.WARNING_MESSAGE);
                    return;
                }

                try {
                    int sid = Integer.parseInt(sidStr);
                    int confirm = JOptionPane.showConfirmDialog(VoltgridGUI.this, "Are you sure you want to delete Station ID: " + sid + "?", "Confirm Delete", JOptionPane.YES_NO_OPTION);
                    if (confirm == JOptionPane.YES_OPTION) {
                        Connection con = null;
                        PreparedStatement ps = null;
                        try {
                            con = DBConnection.getConnection();
                            if (con == null) return;
                            ps = con.prepareStatement("DELETE FROM STATION WHERE Station_ID = ?");
                            ps.setInt(1, sid);
                            int rows = ps.executeUpdate();
                            con.commit();

                            if (rows > 0) {
                                loadStationsTable();
                                refreshStationDropdownForPoints();
                                clearStationForm();
                                JOptionPane.showMessageDialog(VoltgridGUI.this, "Station ID " + sid + " deleted successfully!", "Deleted", JOptionPane.INFORMATION_MESSAGE);
                            } else {
                                JOptionPane.showMessageDialog(VoltgridGUI.this, "Station ID " + sid + " not found.", "Not Found", JOptionPane.WARNING_MESSAGE);
                            }
                        } catch (SQLException ex) {
                            if (ex.getErrorCode() == 2292 || ex.getMessage().contains("2292")) {
                                JOptionPane.showMessageDialog(VoltgridGUI.this, 
                                    "Cannot delete Station ID " + sid + ":\n" +
                                    "This station has active Charging Points installed (Foreign Key ORA-02292).\n\n" +
                                    "Tip: To demonstrate Delete safely, add a new station (e.g. ID 999) and delete it.", 
                                    "Integrity Constraint Protected", JOptionPane.WARNING_MESSAGE);
                            } else {
                                handleSqlException(ex);
                            }
                        } finally {
                            closeQuietly(null, ps, con);
                        }
                    }
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(VoltgridGUI.this, "Station ID must be numeric.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        // Action: Search Station
        btnSearch.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String sidStr = txtStationId.getText().trim();
                if (sidStr.isEmpty()) {
                    JOptionPane.showMessageDialog(VoltgridGUI.this, "Please enter Station ID to search.", "Input Required", JOptionPane.WARNING_MESSAGE);
                    return;
                }

                Connection con = null;
                PreparedStatement ps = null;
                ResultSet rs = null;
                try {
                    int sid = Integer.parseInt(sidStr);
                    con = DBConnection.getConnection();
                    if (con == null) return;
                    ps = con.prepareStatement("SELECT Station_ID, Operator_ID, Street, Pincode, Contact_No, Tariff_Rate FROM STATION WHERE Station_ID = ?");
                    ps.setInt(1, sid);
                    rs = ps.executeQuery();
                    if (rs.next()) {
                        setComboByPrefix(cmbOperator, String.valueOf(rs.getInt("Operator_ID")));
                        txtStreet.setText(rs.getString("Street"));
                        setComboByPrefix(cmbPincode, String.valueOf(rs.getInt("Pincode")));
                        txtContact.setText(rs.getString("Contact_No"));
                        txtTariff.setText(String.valueOf(rs.getDouble("Tariff_Rate")));

                        highlightTableRow(tblStations, 0, sidStr);
                        JOptionPane.showMessageDialog(VoltgridGUI.this, "Station Found on " + rs.getString("Street"), "Found", JOptionPane.INFORMATION_MESSAGE);
                    } else {
                        JOptionPane.showMessageDialog(VoltgridGUI.this, "Station ID not found: " + sid, "Not Found", JOptionPane.WARNING_MESSAGE);
                    }
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(VoltgridGUI.this, "Station ID must be numeric.", "Error", JOptionPane.ERROR_MESSAGE);
                } catch (SQLException ex) {
                    handleSqlException(ex);
                } finally {
                    closeQuietly(rs, ps, con);
                }
            }
        });

        btnClear.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                clearStationForm();
            }
        });

        btnShowAll.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                loadStationsTable();
                clearStationForm();
                JOptionPane.showMessageDialog(VoltgridGUI.this, 
                    "Displaying all " + modelStations.getRowCount() + " records from table STATION.", 
                    "All Station Records", 
                    JOptionPane.INFORMATION_MESSAGE);
            }
        });

        return panel;
    }

    // ====================================================================
    // TAB 3: CHARGING POINT PANEL
    // ====================================================================
    private JPanel createPointPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Form
        JPanel form = new JPanel(new GridLayout(6, 2, 8, 8));
        form.setBorder(BorderFactory.createTitledBorder("Charging Point (Gun) Details & Input Formats"));

        form.add(new JLabel("Point ID (number, e.g. 308):"));
        txtPointId = new JTextField();
        form.add(txtPointId);

        form.add(new JLabel("Station ID (Select parent station):"));
        cmbStationId = new JComboBox<>(new String[]{
            "201 - Katpadi Main Road (Vellore)",
            "202 - Anna Salai (Chennai)",
            "203 - Indiranagar (Bengaluru)",
            "204 - Avinashi Road (Coimbatore)",
            "205 - KK Nagar (Madurai)",
            "206 - HITEC City (Hyderabad)"
        });
        form.add(cmbStationId);

        form.add(new JLabel("Connector Type:"));
        cmbConnector = new JComboBox<>(new String[]{"CCS2", "Type-2", "GB/T"});
        form.add(cmbConnector);

        form.add(new JLabel("Charging Mode:"));
        cmbMode = new JComboBox<>(new String[]{"DC", "AC"});
        form.add(cmbMode);

        form.add(new JLabel("Status:"));
        cmbPointStatus = new JComboBox<>(new String[]{"Available", "In-Use", "Faulty"});
        form.add(cmbPointStatus);

        form.add(new JLabel("Power Rating (kW, e.g. 60.00):"));
        txtPower = new JTextField("60.00");
        form.add(txtPower);

        // Buttons
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 8, 8));
        JButton btnAdd = new JButton("Add Charging Point");
        JButton btnUpdate = new JButton("Update Point");
        JButton btnDelete = new JButton("Delete Point");
        JButton btnSearch = new JButton("Search by ID");
        JButton btnClear = new JButton("Clear Form");
        JButton btnShowAll = new JButton("Show All Records");

        btnPanel.add(btnAdd);
        btnPanel.add(btnUpdate);
        btnPanel.add(btnDelete);
        btnPanel.add(btnSearch);
        btnPanel.add(btnClear);
        btnPanel.add(btnShowAll);

        JPanel topContainer = new JPanel(new BorderLayout());
        topContainer.add(form, BorderLayout.CENTER);
        topContainer.add(btnPanel, BorderLayout.SOUTH);
        panel.add(topContainer, BorderLayout.NORTH);

        // Table
        modelPoints = new DefaultTableModel(new String[]{"Point ID", "Station ID", "Connector", "Mode", "Status", "Power (kW)"}, 0);
        tblPoints = new JTable(modelPoints);
        tblPoints.setRowHeight(22);
        tblPoints.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                int row = tblPoints.getSelectedRow();
                if (row >= 0) {
                    txtPointId.setText(modelPoints.getValueAt(row, 0).toString());
                    setComboByPrefix(cmbStationId, modelPoints.getValueAt(row, 1).toString());
                    cmbConnector.setSelectedItem(modelPoints.getValueAt(row, 2).toString());
                    cmbMode.setSelectedItem(modelPoints.getValueAt(row, 3).toString());
                    cmbPointStatus.setSelectedItem(modelPoints.getValueAt(row, 4).toString());
                    txtPower.setText(modelPoints.getValueAt(row, 5).toString());
                }
            }
        });
        panel.add(new JScrollPane(tblPoints), BorderLayout.CENTER);

        // Action: Add Point
        btnAdd.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String pidStr = txtPointId.getText().trim();
                String pwrStr = txtPower.getText().trim();

                if (pidStr.isEmpty() || pwrStr.isEmpty()) {
                    JOptionPane.showMessageDialog(VoltgridGUI.this, "Please enter Point ID and Power Rating.", "Missing Input", JOptionPane.WARNING_MESSAGE);
                    return;
                }

                Connection con = null;
                PreparedStatement ps = null;
                try {
                    int pid = Integer.parseInt(pidStr);
                    int sid = Integer.parseInt(cmbStationId.getSelectedItem().toString().split(" ")[0]);
                    String conn = cmbConnector.getSelectedItem().toString();
                    String mode = cmbMode.getSelectedItem().toString();
                    String st = cmbPointStatus.getSelectedItem().toString();
                    double pwr = Double.parseDouble(pwrStr);

                    con = DBConnection.getConnection();
                    if (con == null) return;
                    String sql = "INSERT INTO CHARGING_POINT (Point_ID, Station_ID, Connector_Type, Charging_Mode, Status, Power_Rating) VALUES (?, ?, ?, ?, ?, ?)";
                    ps = con.prepareStatement(sql);
                    ps.setInt(1, pid);
                    ps.setInt(2, sid);
                    ps.setString(3, conn);
                    ps.setString(4, mode);
                    ps.setString(5, st);
                    ps.setDouble(6, pwr);
                    ps.executeUpdate();
                    con.commit();

                    loadPointsTable();
                    clearPointForm();
                    JOptionPane.showMessageDialog(VoltgridGUI.this, "Charging point added successfully! (Point ID: " + pid + ")", "Success", JOptionPane.INFORMATION_MESSAGE);
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(VoltgridGUI.this, "Point ID and Power Rating must be numeric.", "Input Error", JOptionPane.ERROR_MESSAGE);
                } catch (SQLException ex) {
                    handleSqlException(ex);
                } finally {
                    closeQuietly(null, ps, con);
                }
            }
        });

        // Action: Update Point
        btnUpdate.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String pidStr = txtPointId.getText().trim();
                String pwrStr = txtPower.getText().trim();

                if (pidStr.isEmpty()) {
                    JOptionPane.showMessageDialog(VoltgridGUI.this, "Select a charging point from the table or enter Point ID to update.", "Input Error", JOptionPane.WARNING_MESSAGE);
                    return;
                }

                Connection con = null;
                PreparedStatement ps = null;
                try {
                    int pid = Integer.parseInt(pidStr);
                    int sid = Integer.parseInt(cmbStationId.getSelectedItem().toString().split(" ")[0]);
                    String conn = cmbConnector.getSelectedItem().toString();
                    String mode = cmbMode.getSelectedItem().toString();
                    String st = cmbPointStatus.getSelectedItem().toString();
                    double pwr = Double.parseDouble(pwrStr);

                    con = DBConnection.getConnection();
                    if (con == null) return;
                    String sql = "UPDATE CHARGING_POINT SET Station_ID = ?, Connector_Type = ?, Charging_Mode = ?, Status = ?, Power_Rating = ? WHERE Point_ID = ?";
                    ps = con.prepareStatement(sql);
                    ps.setInt(1, sid);
                    ps.setString(2, conn);
                    ps.setString(3, mode);
                    ps.setString(4, st);
                    ps.setDouble(5, pwr);
                    ps.setInt(6, pid);

                    int rows = ps.executeUpdate();
                    con.commit();
                    if (rows > 0) {
                        loadPointsTable();
                        JOptionPane.showMessageDialog(VoltgridGUI.this, "Charging Point " + pid + " updated successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                    } else {
                        JOptionPane.showMessageDialog(VoltgridGUI.this, "Charging Point " + pid + " not found.", "Not Found", JOptionPane.WARNING_MESSAGE);
                    }
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(VoltgridGUI.this, "Please enter valid numeric values.", "Error", JOptionPane.ERROR_MESSAGE);
                } catch (SQLException ex) {
                    handleSqlException(ex);
                } finally {
                    closeQuietly(null, ps, con);
                }
            }
        });

        // Action: Delete Point
        btnDelete.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String pidStr = txtPointId.getText().trim();
                if (pidStr.isEmpty()) {
                    JOptionPane.showMessageDialog(VoltgridGUI.this, "Select a charging point from the table or enter Point ID to delete.", "Input Error", JOptionPane.WARNING_MESSAGE);
                    return;
                }

                try {
                    int pid = Integer.parseInt(pidStr);
                    int confirm = JOptionPane.showConfirmDialog(VoltgridGUI.this, "Are you sure you want to delete Charging Point ID: " + pid + "?", "Confirm Delete", JOptionPane.YES_NO_OPTION);
                    if (confirm == JOptionPane.YES_OPTION) {
                        Connection con = null;
                        PreparedStatement ps = null;
                        try {
                            con = DBConnection.getConnection();
                            if (con == null) return;
                            ps = con.prepareStatement("DELETE FROM CHARGING_POINT WHERE Point_ID = ?");
                            ps.setInt(1, pid);
                            int rows = ps.executeUpdate();
                            con.commit();

                            if (rows > 0) {
                                loadPointsTable();
                                clearPointForm();
                                JOptionPane.showMessageDialog(VoltgridGUI.this, "Charging Point " + pid + " deleted successfully!", "Deleted", JOptionPane.INFORMATION_MESSAGE);
                            } else {
                                JOptionPane.showMessageDialog(VoltgridGUI.this, "Charging Point " + pid + " not found.", "Not Found", JOptionPane.WARNING_MESSAGE);
                            }
                        } catch (SQLException ex) {
                            if (ex.getErrorCode() == 2292 || ex.getMessage().contains("2292")) {
                                JOptionPane.showMessageDialog(VoltgridGUI.this, 
                                    "Cannot delete Charging Point " + pid + ":\n" +
                                    "This point has active charging sessions or maintenance reports (Foreign Key ORA-02292).\n\n" +
                                    "Tip: To demonstrate Delete safely, add a new point (e.g. ID 999) and delete it.", 
                                    "Integrity Constraint Protected", JOptionPane.WARNING_MESSAGE);
                            } else {
                                handleSqlException(ex);
                            }
                        } finally {
                            closeQuietly(null, ps, con);
                        }
                    }
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(VoltgridGUI.this, "Point ID must be numeric.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        // Action: Search Point
        btnSearch.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String pidStr = txtPointId.getText().trim();
                if (pidStr.isEmpty()) {
                    JOptionPane.showMessageDialog(VoltgridGUI.this, "Please enter Point ID to search.", "Input Required", JOptionPane.WARNING_MESSAGE);
                    return;
                }

                Connection con = null;
                PreparedStatement ps = null;
                ResultSet rs = null;
                try {
                    int pid = Integer.parseInt(pidStr);
                    con = DBConnection.getConnection();
                    if (con == null) return;
                    ps = con.prepareStatement("SELECT Point_ID, Station_ID, Connector_Type, Charging_Mode, Status, Power_Rating FROM CHARGING_POINT WHERE Point_ID = ?");
                    ps.setInt(1, pid);
                    rs = ps.executeQuery();
                    if (rs.next()) {
                        setComboByPrefix(cmbStationId, String.valueOf(rs.getInt("Station_ID")));
                        cmbConnector.setSelectedItem(rs.getString("Connector_Type"));
                        cmbMode.setSelectedItem(rs.getString("Charging_Mode"));
                        cmbPointStatus.setSelectedItem(rs.getString("Status"));
                        txtPower.setText(String.valueOf(rs.getDouble("Power_Rating")));

                        highlightTableRow(tblPoints, 0, pidStr);
                        JOptionPane.showMessageDialog(VoltgridGUI.this, "Charging Point Found: Gun " + pid, "Found", JOptionPane.INFORMATION_MESSAGE);
                    } else {
                        JOptionPane.showMessageDialog(VoltgridGUI.this, "Charging Point ID not found: " + pid, "Not Found", JOptionPane.WARNING_MESSAGE);
                    }
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(VoltgridGUI.this, "Point ID must be numeric.", "Error", JOptionPane.ERROR_MESSAGE);
                } catch (SQLException ex) {
                    handleSqlException(ex);
                } finally {
                    closeQuietly(rs, ps, con);
                }
            }
        });

        btnClear.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                clearPointForm();
            }
        });

        btnShowAll.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                loadPointsTable();
                clearPointForm();
                JOptionPane.showMessageDialog(VoltgridGUI.this, 
                    "Displaying all " + modelPoints.getRowCount() + " records from table CHARGING_POINT.", 
                    "All Charging Point Records", 
                    JOptionPane.INFORMATION_MESSAGE);
            }
        });

        return panel;
    }

    // ====================================================================
    // HELPER & DATA LOADING METHODS
    // ====================================================================

    private void checkConnectionOnly() {
        Connection con = DBConnection.getConnection();
        if (con != null) {
            lblStatus.setText("● Connected to Oracle XE (XEPDB1)");
            lblStatus.setForeground(new Color(100, 255, 100));
            try { con.close(); } catch (Exception ignored) {}
        } else {
            lblStatus.setText("● Disconnected (Set Password)");
            lblStatus.setForeground(Color.RED);
        }
        // Tables remain clean and empty until user clicks "Show All Records"
        if (modelCustomers != null) modelCustomers.setRowCount(0);
        if (modelStations != null) modelStations.setRowCount(0);
        if (modelPoints != null) modelPoints.setRowCount(0);
    }

    private void testConnectionAndRefresh() {
        Connection con = DBConnection.getConnection();
        if (con != null) {
            lblStatus.setText("● Connected to Oracle XE (XEPDB1)");
            lblStatus.setForeground(new Color(100, 255, 100));
            try { con.close(); } catch (Exception ignored) {}
            JOptionPane.showMessageDialog(this, "Connected successfully to Oracle Database!\nClick 'Show All Records' on any tab to view the data.", "Connected", JOptionPane.INFORMATION_MESSAGE);
        } else {
            lblStatus.setText("● Connection Failed (Check Password)");
            lblStatus.setForeground(Color.RED);
            JOptionPane.showMessageDialog(this, "Connection Failed!\nPlease verify your Oracle password in the top bar.", "Connection Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void loadCustomersTable() {
        modelCustomers.setRowCount(0);
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            con = DBConnection.getConnection();
            if (con == null) return;
            ps = con.prepareStatement("SELECT Customer_ID, First_Name, Last_Name, Email, TO_CHAR(Registration_Date, 'YYYY-MM-DD') AS RegDate FROM CUSTOMER ORDER BY Customer_ID");
            rs = ps.executeQuery();
            while (rs.next()) {
                modelCustomers.addRow(new Object[]{
                    rs.getInt("Customer_ID"),
                    rs.getString("First_Name"),
                    rs.getString("Last_Name"),
                    rs.getString("Email"),
                    rs.getString("RegDate")
                });
            }
        } catch (SQLException e) {
            System.out.println("Load Customers: " + e.getMessage());
        } finally {
            closeQuietly(rs, ps, con);
        }
    }

    private void loadStationsTable() {
        modelStations.setRowCount(0);
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            con = DBConnection.getConnection();
            if (con == null) return;
            ps = con.prepareStatement("SELECT Station_ID, Operator_ID, Street, Pincode, Contact_No, Tariff_Rate FROM STATION ORDER BY Station_ID");
            rs = ps.executeQuery();
            while (rs.next()) {
                modelStations.addRow(new Object[]{
                    rs.getInt("Station_ID"),
                    rs.getInt("Operator_ID"),
                    rs.getString("Street"),
                    rs.getInt("Pincode"),
                    rs.getString("Contact_No"),
                    rs.getDouble("Tariff_Rate")
                });
            }
        } catch (SQLException e) {
            System.out.println("Load Stations: " + e.getMessage());
        } finally {
            closeQuietly(rs, ps, con);
        }
    }

    private void loadPointsTable() {
        modelPoints.setRowCount(0);
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            con = DBConnection.getConnection();
            if (con == null) return;
            ps = con.prepareStatement("SELECT Point_ID, Station_ID, Connector_Type, Charging_Mode, Status, Power_Rating FROM CHARGING_POINT ORDER BY Point_ID");
            rs = ps.executeQuery();
            while (rs.next()) {
                modelPoints.addRow(new Object[]{
                    rs.getInt("Point_ID"),
                    rs.getInt("Station_ID"),
                    rs.getString("Connector_Type"),
                    rs.getString("Charging_Mode"),
                    rs.getString("Status"),
                    rs.getDouble("Power_Rating")
                });
            }
        } catch (SQLException e) {
            System.out.println("Load Points: " + e.getMessage());
        } finally {
            closeQuietly(rs, ps, con);
        }
    }

    private void refreshStationDropdownForPoints() {
        if (cmbStationId == null) return;
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            con = DBConnection.getConnection();
            if (con == null) return;
            ps = con.prepareStatement("SELECT Station_ID, Street FROM STATION ORDER BY Station_ID");
            rs = ps.executeQuery();
            cmbStationId.removeAllItems();
            while (rs.next()) {
                cmbStationId.addItem(rs.getInt("Station_ID") + " - " + rs.getString("Street"));
            }
        } catch (Exception ignored) {}
        finally {
            closeQuietly(rs, ps, con);
        }
    }

    // Set combo selection by prefix match (e.g. matching "201" to "201 - Katpadi...")
    private void setComboByPrefix(JComboBox<String> combo, String prefix) {
        for (int i = 0; i < combo.getItemCount(); i++) {
            String item = combo.getItemAt(i);
            if (item.startsWith(prefix + " ") || item.equals(prefix)) {
                combo.setSelectedIndex(i);
                return;
            }
        }
    }

    // Find and visually select matching row in JTable
    private void highlightTableRow(JTable table, int colIndex, String targetVal) {
        for (int i = 0; i < table.getRowCount(); i++) {
            Object val = table.getValueAt(i, colIndex);
            if (val != null && val.toString().equals(targetVal)) {
                table.setRowSelectionInterval(i, i);
                table.scrollRectToVisible(table.getCellRect(i, 0, true));
                return;
            }
        }
    }

    // Standardized SQL Exception handler
    private void handleSqlException(SQLException ex) {
        int code = ex.getErrorCode();
        String msg = ex.getMessage();
        if (code == 1 || msg.contains("00001")) {
            JOptionPane.showMessageDialog(this, "Duplicate Error (ORA-00001):\nA record with this Primary Key or unique Email/License already exists.", "Unique Constraint Error", JOptionPane.ERROR_MESSAGE);
        } else if (code == 2290 || msg.contains("02290")) {
            JOptionPane.showMessageDialog(this, "Check Constraint Violation (ORA-02290):\nValues must satisfy business rules (e.g. Tariff > 0, Status in Available/In-Use/Faulty).", "Validation Error", JOptionPane.ERROR_MESSAGE);
        } else if (code == 2291 || msg.contains("02291")) {
            JOptionPane.showMessageDialog(this, "Foreign Key Error (ORA-02291):\nThe referenced parent record (Operator, Pincode, or Station) does not exist.", "Referential Integrity Error", JOptionPane.ERROR_MESSAGE);
        } else if (code == 2292 || msg.contains("02292")) {
            JOptionPane.showMessageDialog(this, "Foreign Key Error (ORA-02292):\nCannot delete: Child records in another table reference this record.", "Referential Integrity Error", JOptionPane.WARNING_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(this, "Database Error (" + code + "):\n" + msg, "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Clear forms
    private void clearCustomerForm() {
        txtCustId.setText("");
        txtCustFName.setText("");
        txtCustLName.setText("");
        txtCustEmail.setText("");
        txtCustDate.setText("2024-03-01");
    }

    private void clearStationForm() {
        txtStationId.setText("");
        txtStreet.setText("");
        txtContact.setText("");
        txtTariff.setText("19.50");
    }

    private void clearPointForm() {
        txtPointId.setText("");
        txtPower.setText("60.00");
    }

    private void closeQuietly(ResultSet rs, PreparedStatement ps, Connection con) {
        try { if (rs != null) rs.close(); } catch (Exception ignored) {}
        try { if (ps != null) ps.close(); } catch (Exception ignored) {}
        try { if (con != null) con.close(); } catch (Exception ignored) {}
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new VoltgridGUI().setVisible(true);
            }
        });
    }
}
