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

// File: VoltgridPortal.java
// Comprehensive Enterprise Front-End managing ALL Strong Entities in Voltgrid EV System
public class VoltgridPortal extends JFrame {

    // Top Connection Bar
    private JLabel lblStatus;
    private JTextField txtDbPassword;
    private JButton btnConnect;

    // --- Entity 1: CUSTOMER ---
    private JTextField txtCustId, txtCustFName, txtCustLName, txtCustEmail, txtCustDate;
    private JTable tblCustomers;
    private DefaultTableModel modelCustomers;

    // --- Entity 2: OPERATOR ---
    private JTextField txtOpId, txtOpName, txtOpLicense, txtOpContact;
    private JTable tblOperators;
    private DefaultTableModel modelOperators;

    // --- Entity 3: STATION ---
    private JTextField txtStationId, txtStationOpId, txtStationStreet, txtStationPincode, txtStationContact, txtStationTariff;
    private JTable tblStations;
    private DefaultTableModel modelStations;

    // --- Entity 4: CHARGING_POINT ---
    private JTextField txtPointId, txtPointStationId, txtPointPower;
    private JComboBox<String> cmbConnector, cmbMode, cmbPointStatus;
    private JTable tblPoints;
    private DefaultTableModel modelPoints;

    // --- Entity 5: VEHICLE_MODEL ---
    private JTextField txtModelName, txtModelBattery;
    private JTable tblModels;
    private DefaultTableModel modelModels;

    // --- Entity 6: VEHICLE ---
    private JTextField txtVehPlate, txtVehCustId, txtVehModel;
    private JTable tblVehicles;
    private DefaultTableModel modelVehicles;

    // --- Entity 7: PINCODE_AREA ---
    private JTextField txtPinCode, txtPinCity;
    private JTable tblPincodes;
    private DefaultTableModel modelPincodes;

    public VoltgridPortal() {
        setTitle("Voltgrid EV Portal - Strong Entities Management Console (JDBC Connected)");
        setSize(1100, 780);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(5, 5));

        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignored) {}

        // Top Status Header
        add(createTopPanel(), BorderLayout.NORTH);

        // Center Tabbed Pane containing all strong entity functions
        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.setFont(new Font("Segoe UI", Font.BOLD, 12));
        tabbedPane.addTab("1. Customers", createCustomerPanel());
        tabbedPane.addTab("2. Operators", createOperatorPanel());
        tabbedPane.addTab("3. Stations", createStationPanel());
        tabbedPane.addTab("4. Charging Points", createPointPanel());
        tabbedPane.addTab("5. Vehicle Models", createModelPanel());
        tabbedPane.addTab("6. Vehicles", createVehiclePanel());
        tabbedPane.addTab("7. Cities & Pincodes", createPincodePanel());
        add(tabbedPane, BorderLayout.CENTER);

        // Verify connection on launch (tables remain empty until "Show All Records" is clicked)
        checkConnectionOnly();
    }

    private JPanel createTopPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(20, 35, 60));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));

        JLabel title = new JLabel("⚡ VOLTGRID EV SYSTEM - ALL STRONG ENTITIES MANAGEMENT PORTAL");
        title.setForeground(Color.WHITE);
        title.setFont(new Font("Segoe UI", Font.BOLD, 15));
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
                checkConnectionOnly();
                JOptionPane.showMessageDialog(VoltgridPortal.this, "Connection tested with Oracle XE.\nClick 'Show All Records' on any tab to view data.", "Connected", JOptionPane.INFORMATION_MESSAGE);
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
    // 1. ENTITY: CUSTOMER
    // ====================================================================
    private JPanel createCustomerPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel form = new JPanel(new GridLayout(5, 2, 8, 8));
        form.setBorder(BorderFactory.createTitledBorder("Customer Inputs & Formats"));

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

        modelCustomers = new DefaultTableModel(new String[]{"Customer ID", "First Name", "Last Name", "Email", "Reg Date"}, 0);
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

        btnAdd.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    int id = Integer.parseInt(txtCustId.getText().trim());
                    String fn = txtCustFName.getText().trim();
                    String ln = txtCustLName.getText().trim();
                    String em = txtCustEmail.getText().trim();
                    String dt = txtCustDate.getText().trim();
                    executeSqlDml("INSERT INTO CUSTOMER (Customer_ID, First_Name, Last_Name, Email, Registration_Date) VALUES (?, ?, ?, ?, TO_DATE(?, 'YYYY-MM-DD'))",
                            new Object[]{id, fn, ln, em, dt}, "Customer added successfully! (ID: " + id + ")");
                    loadCustomers();
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(VoltgridPortal.this, "Input Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        btnUpdate.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    int id = Integer.parseInt(txtCustId.getText().trim());
                    String fn = txtCustFName.getText().trim();
                    String ln = txtCustLName.getText().trim();
                    String em = txtCustEmail.getText().trim();
                    executeSqlDml("UPDATE CUSTOMER SET First_Name = ?, Last_Name = ?, Email = ? WHERE Customer_ID = ?",
                            new Object[]{fn, ln, em, id}, "Customer ID " + id + " updated successfully!");
                    loadCustomers();
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(VoltgridPortal.this, "Input Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        btnDelete.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    int id = Integer.parseInt(txtCustId.getText().trim());
                    int confirm = JOptionPane.showConfirmDialog(VoltgridPortal.this, "Delete Customer ID: " + id + "?", "Confirm Delete", JOptionPane.YES_NO_OPTION);
                    if (confirm == JOptionPane.YES_OPTION) {
                        executeSqlDml("DELETE FROM CUSTOMER WHERE Customer_ID = ?", new Object[]{id}, "Customer ID " + id + " deleted successfully!");
                        loadCustomers();
                        clearCustomerForm();
                    }
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(VoltgridPortal.this, "Select or enter Customer ID to delete.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        btnSearch.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    int id = Integer.parseInt(txtCustId.getText().trim());
                    Connection con = DBConnection.getConnection();
                    if (con == null) return;
                    PreparedStatement ps = con.prepareStatement("SELECT Customer_ID, First_Name, Last_Name, Email, TO_CHAR(Registration_Date, 'YYYY-MM-DD') AS RegDate FROM CUSTOMER WHERE Customer_ID = ?");
                    ps.setInt(1, id);
                    ResultSet rs = ps.executeQuery();
                    if (rs.next()) {
                        txtCustFName.setText(rs.getString("First_Name"));
                        txtCustLName.setText(rs.getString("Last_Name"));
                        txtCustEmail.setText(rs.getString("Email"));
                        txtCustDate.setText(rs.getString("RegDate"));
                        highlightRow(tblCustomers, 0, String.valueOf(id));
                        JOptionPane.showMessageDialog(VoltgridPortal.this, "Customer Found: " + rs.getString("First_Name") + " " + rs.getString("Last_Name"), "Found", JOptionPane.INFORMATION_MESSAGE);
                    } else {
                        JOptionPane.showMessageDialog(VoltgridPortal.this, "Customer ID not found: " + id, "Not Found", JOptionPane.WARNING_MESSAGE);
                    }
                    rs.close(); ps.close(); con.close();
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(VoltgridPortal.this, "Search error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        btnClear.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) { clearCustomerForm(); }
        });

        btnShowAll.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                loadCustomers();
                JOptionPane.showMessageDialog(VoltgridPortal.this, "Displaying all " + modelCustomers.getRowCount() + " records from table CUSTOMER.", "Customer Records", JOptionPane.INFORMATION_MESSAGE);
            }
        });

        return panel;
    }

    // ====================================================================
    // 2. ENTITY: OPERATOR
    // ====================================================================
    private JPanel createOperatorPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel form = new JPanel(new GridLayout(4, 2, 8, 8));
        form.setBorder(BorderFactory.createTitledBorder("Operator Inputs & Formats"));

        form.add(new JLabel("Operator ID (number, e.g. 107):"));
        txtOpId = new JTextField();
        form.add(txtOpId);

        form.add(new JLabel("Operator Name (e.g. Jio-BP Pulse):"));
        txtOpName = new JTextField();
        form.add(txtOpName);

        form.add(new JLabel("License Number (unique, e.g. LIC-JB-2024-07):"));
        txtOpLicense = new JTextField();
        form.add(txtOpLicense);

        form.add(new JLabel("Contact Info / Email:"));
        txtOpContact = new JTextField("support@operator.com");
        form.add(txtOpContact);

        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 8, 8));
        JButton btnAdd = new JButton("Add Operator");
        JButton btnUpdate = new JButton("Update Operator");
        JButton btnDelete = new JButton("Delete Operator");
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

        modelOperators = new DefaultTableModel(new String[]{"Operator ID", "Name", "License No", "Contact Info"}, 0);
        tblOperators = new JTable(modelOperators);
        tblOperators.setRowHeight(22);
        tblOperators.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                int row = tblOperators.getSelectedRow();
                if (row >= 0) {
                    txtOpId.setText(modelOperators.getValueAt(row, 0).toString());
                    txtOpName.setText(modelOperators.getValueAt(row, 1).toString());
                    txtOpLicense.setText(modelOperators.getValueAt(row, 2).toString());
                    txtOpContact.setText(modelOperators.getValueAt(row, 3).toString());
                }
            }
        });
        panel.add(new JScrollPane(tblOperators), BorderLayout.CENTER);

        btnAdd.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    int id = Integer.parseInt(txtOpId.getText().trim());
                    String name = txtOpName.getText().trim();
                    String lic = txtOpLicense.getText().trim();
                    String contact = txtOpContact.getText().trim();
                    executeSqlDml("INSERT INTO OPERATOR (Operator_ID, Name, License_No, Contact_Info) VALUES (?, ?, ?, ?)",
                            new Object[]{id, name, lic, contact}, "Operator added successfully! (ID: " + id + ")");
                    loadOperators();
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(VoltgridPortal.this, "Input Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        btnUpdate.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    int id = Integer.parseInt(txtOpId.getText().trim());
                    String name = txtOpName.getText().trim();
                    String lic = txtOpLicense.getText().trim();
                    String contact = txtOpContact.getText().trim();
                    executeSqlDml("UPDATE OPERATOR SET Name = ?, License_No = ?, Contact_Info = ? WHERE Operator_ID = ?",
                            new Object[]{name, lic, contact, id}, "Operator ID " + id + " updated successfully!");
                    loadOperators();
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(VoltgridPortal.this, "Input Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        btnDelete.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    int id = Integer.parseInt(txtOpId.getText().trim());
                    int confirm = JOptionPane.showConfirmDialog(VoltgridPortal.this, "Delete Operator ID: " + id + "?", "Confirm Delete", JOptionPane.YES_NO_OPTION);
                    if (confirm == JOptionPane.YES_OPTION) {
                        executeSqlDml("DELETE FROM OPERATOR WHERE Operator_ID = ?", new Object[]{id}, "Operator ID " + id + " deleted successfully!");
                        loadOperators();
                        clearOperatorForm();
                    }
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(VoltgridPortal.this, "Select or enter Operator ID to delete.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        btnSearch.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    int id = Integer.parseInt(txtOpId.getText().trim());
                    Connection con = DBConnection.getConnection();
                    if (con == null) return;
                    PreparedStatement ps = con.prepareStatement("SELECT Operator_ID, Name, License_No, Contact_Info FROM OPERATOR WHERE Operator_ID = ?");
                    ps.setInt(1, id);
                    ResultSet rs = ps.executeQuery();
                    if (rs.next()) {
                        txtOpName.setText(rs.getString("Name"));
                        txtOpLicense.setText(rs.getString("License_No"));
                        txtOpContact.setText(rs.getString("Contact_Info"));
                        highlightRow(tblOperators, 0, String.valueOf(id));
                        JOptionPane.showMessageDialog(VoltgridPortal.this, "Operator Found: " + rs.getString("Name"), "Found", JOptionPane.INFORMATION_MESSAGE);
                    } else {
                        JOptionPane.showMessageDialog(VoltgridPortal.this, "Operator ID not found: " + id, "Not Found", JOptionPane.WARNING_MESSAGE);
                    }
                    rs.close(); ps.close(); con.close();
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(VoltgridPortal.this, "Search error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        btnClear.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) { clearOperatorForm(); }
        });

        btnShowAll.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                loadOperators();
                JOptionPane.showMessageDialog(VoltgridPortal.this, "Displaying all " + modelOperators.getRowCount() + " records from table OPERATOR.", "Operator Records", JOptionPane.INFORMATION_MESSAGE);
            }
        });

        return panel;
    }

    // ====================================================================
    // 3. ENTITY: STATION
    // ====================================================================
    private JPanel createStationPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel form = new JPanel(new GridLayout(6, 2, 8, 8));
        form.setBorder(BorderFactory.createTitledBorder("Station Inputs & Formats"));

        form.add(new JLabel("Station ID (number, e.g. 207):"));
        txtStationId = new JTextField();
        form.add(txtStationId);

        form.add(new JLabel("Operator ID (e.g. 101):"));
        txtStationOpId = new JTextField("101");
        form.add(txtStationOpId);

        form.add(new JLabel("Street Address (e.g. MG Road, Anna Salai):"));
        txtStationStreet = new JTextField();
        form.add(txtStationStreet);

        form.add(new JLabel("Pincode (6 digits, e.g. 600001, 632014):"));
        txtStationPincode = new JTextField("600001");
        form.add(txtStationPincode);

        form.add(new JLabel("Contact Number (10 digits, e.g. 9876543210):"));
        txtStationContact = new JTextField("9876543210");
        form.add(txtStationContact);

        form.add(new JLabel("Tariff Rate (INR/kWh, e.g. 19.50):"));
        txtStationTariff = new JTextField("19.50");
        form.add(txtStationTariff);

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

        modelStations = new DefaultTableModel(new String[]{"Station ID", "Operator ID", "Street", "Pincode", "Contact", "Tariff (INR)"}, 0);
        tblStations = new JTable(modelStations);
        tblStations.setRowHeight(22);
        tblStations.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                int row = tblStations.getSelectedRow();
                if (row >= 0) {
                    txtStationId.setText(modelStations.getValueAt(row, 0).toString());
                    txtStationOpId.setText(modelStations.getValueAt(row, 1).toString());
                    txtStationStreet.setText(modelStations.getValueAt(row, 2).toString());
                    txtStationPincode.setText(modelStations.getValueAt(row, 3).toString());
                    txtStationContact.setText(modelStations.getValueAt(row, 4).toString());
                    txtStationTariff.setText(modelStations.getValueAt(row, 5).toString());
                }
            }
        });
        panel.add(new JScrollPane(tblStations), BorderLayout.CENTER);

        btnAdd.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    int sid = Integer.parseInt(txtStationId.getText().trim());
                    int opId = Integer.parseInt(txtStationOpId.getText().trim());
                    String street = txtStationStreet.getText().trim();
                    int pin = Integer.parseInt(txtStationPincode.getText().trim());
                    String contact = txtStationContact.getText().trim();
                    double tariff = Double.parseDouble(txtStationTariff.getText().trim());

                    executeSqlDml("INSERT INTO STATION (Station_ID, Operator_ID, Street, Pincode, Contact_No, Tariff_Rate) VALUES (?, ?, ?, ?, ?, ?)",
                            new Object[]{sid, opId, street, pin, contact, tariff}, "Station added successfully! (ID: " + sid + ")");
                    loadStations();
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(VoltgridPortal.this, "Input Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        btnUpdate.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    int sid = Integer.parseInt(txtStationId.getText().trim());
                    int opId = Integer.parseInt(txtStationOpId.getText().trim());
                    String street = txtStationStreet.getText().trim();
                    int pin = Integer.parseInt(txtStationPincode.getText().trim());
                    String contact = txtStationContact.getText().trim();
                    double tariff = Double.parseDouble(txtStationTariff.getText().trim());

                    executeSqlDml("UPDATE STATION SET Operator_ID = ?, Street = ?, Pincode = ?, Contact_No = ?, Tariff_Rate = ? WHERE Station_ID = ?",
                            new Object[]{opId, street, pin, contact, tariff, sid}, "Station ID " + sid + " updated successfully!");
                    loadStations();
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(VoltgridPortal.this, "Input Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        btnDelete.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    int sid = Integer.parseInt(txtStationId.getText().trim());
                    int confirm = JOptionPane.showConfirmDialog(VoltgridPortal.this, "Delete Station ID: " + sid + "?", "Confirm Delete", JOptionPane.YES_NO_OPTION);
                    if (confirm == JOptionPane.YES_OPTION) {
                        executeSqlDml("DELETE FROM STATION WHERE Station_ID = ?", new Object[]{sid}, "Station ID " + sid + " deleted successfully!");
                        loadStations();
                        clearStationForm();
                    }
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(VoltgridPortal.this, "Select or enter Station ID to delete.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        btnSearch.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    int sid = Integer.parseInt(txtStationId.getText().trim());
                    Connection con = DBConnection.getConnection();
                    if (con == null) return;
                    PreparedStatement ps = con.prepareStatement("SELECT Station_ID, Operator_ID, Street, Pincode, Contact_No, Tariff_Rate FROM STATION WHERE Station_ID = ?");
                    ps.setInt(1, sid);
                    ResultSet rs = ps.executeQuery();
                    if (rs.next()) {
                        txtStationOpId.setText(String.valueOf(rs.getInt("Operator_ID")));
                        txtStationStreet.setText(rs.getString("Street"));
                        txtStationPincode.setText(String.valueOf(rs.getInt("Pincode")));
                        txtStationContact.setText(rs.getString("Contact_No"));
                        txtStationTariff.setText(String.valueOf(rs.getDouble("Tariff_Rate")));
                        highlightRow(tblStations, 0, String.valueOf(sid));
                        JOptionPane.showMessageDialog(VoltgridPortal.this, "Station Found on " + rs.getString("Street"), "Found", JOptionPane.INFORMATION_MESSAGE);
                    } else {
                        JOptionPane.showMessageDialog(VoltgridPortal.this, "Station ID not found: " + sid, "Not Found", JOptionPane.WARNING_MESSAGE);
                    }
                    rs.close(); ps.close(); con.close();
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(VoltgridPortal.this, "Search error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        btnClear.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) { clearStationForm(); }
        });

        btnShowAll.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                loadStations();
                JOptionPane.showMessageDialog(VoltgridPortal.this, "Displaying all " + modelStations.getRowCount() + " records from table STATION.", "Station Records", JOptionPane.INFORMATION_MESSAGE);
            }
        });

        return panel;
    }

    // ====================================================================
    // 4. ENTITY: CHARGING_POINT
    // ====================================================================
    private JPanel createPointPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel form = new JPanel(new GridLayout(6, 2, 8, 8));
        form.setBorder(BorderFactory.createTitledBorder("Charging Point Inputs & Formats"));

        form.add(new JLabel("Point ID (number, e.g. 308):"));
        txtPointId = new JTextField();
        form.add(txtPointId);

        form.add(new JLabel("Station ID (e.g. 201):"));
        txtPointStationId = new JTextField("201");
        form.add(txtPointStationId);

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
        txtPointPower = new JTextField("60.00");
        form.add(txtPointPower);

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

        modelPoints = new DefaultTableModel(new String[]{"Point ID", "Station ID", "Connector", "Mode", "Status", "Power (kW)"}, 0);
        tblPoints = new JTable(modelPoints);
        tblPoints.setRowHeight(22);
        tblPoints.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                int row = tblPoints.getSelectedRow();
                if (row >= 0) {
                    txtPointId.setText(modelPoints.getValueAt(row, 0).toString());
                    txtPointStationId.setText(modelPoints.getValueAt(row, 1).toString());
                    cmbConnector.setSelectedItem(modelPoints.getValueAt(row, 2).toString());
                    cmbMode.setSelectedItem(modelPoints.getValueAt(row, 3).toString());
                    cmbPointStatus.setSelectedItem(modelPoints.getValueAt(row, 4).toString());
                    txtPointPower.setText(modelPoints.getValueAt(row, 5).toString());
                }
            }
        });
        panel.add(new JScrollPane(tblPoints), BorderLayout.CENTER);

        btnAdd.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    int pid = Integer.parseInt(txtPointId.getText().trim());
                    int sid = Integer.parseInt(txtPointStationId.getText().trim());
                    String conn = cmbConnector.getSelectedItem().toString();
                    String mode = cmbMode.getSelectedItem().toString();
                    String st = cmbPointStatus.getSelectedItem().toString();
                    double pwr = Double.parseDouble(txtPointPower.getText().trim());

                    executeSqlDml("INSERT INTO CHARGING_POINT (Point_ID, Station_ID, Connector_Type, Charging_Mode, Status, Power_Rating) VALUES (?, ?, ?, ?, ?, ?)",
                            new Object[]{pid, sid, conn, mode, st, pwr}, "Charging Point added successfully! (ID: " + pid + ")");
                    loadPoints();
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(VoltgridPortal.this, "Input Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        btnUpdate.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    int pid = Integer.parseInt(txtPointId.getText().trim());
                    int sid = Integer.parseInt(txtPointStationId.getText().trim());
                    String conn = cmbConnector.getSelectedItem().toString();
                    String mode = cmbMode.getSelectedItem().toString();
                    String st = cmbPointStatus.getSelectedItem().toString();
                    double pwr = Double.parseDouble(txtPointPower.getText().trim());

                    executeSqlDml("UPDATE CHARGING_POINT SET Station_ID = ?, Connector_Type = ?, Charging_Mode = ?, Status = ?, Power_Rating = ? WHERE Point_ID = ?",
                            new Object[]{sid, conn, mode, st, pwr, pid}, "Charging Point " + pid + " updated successfully!");
                    loadPoints();
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(VoltgridPortal.this, "Input Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        btnDelete.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    int pid = Integer.parseInt(txtPointId.getText().trim());
                    int confirm = JOptionPane.showConfirmDialog(VoltgridPortal.this, "Delete Charging Point ID: " + pid + "?", "Confirm Delete", JOptionPane.YES_NO_OPTION);
                    if (confirm == JOptionPane.YES_OPTION) {
                        executeSqlDml("DELETE FROM CHARGING_POINT WHERE Point_ID = ?", new Object[]{pid}, "Charging Point " + pid + " deleted successfully!");
                        loadPoints();
                        clearPointForm();
                    }
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(VoltgridPortal.this, "Select or enter Point ID to delete.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        btnSearch.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    int pid = Integer.parseInt(txtPointId.getText().trim());
                    Connection con = DBConnection.getConnection();
                    if (con == null) return;
                    PreparedStatement ps = con.prepareStatement("SELECT Point_ID, Station_ID, Connector_Type, Charging_Mode, Status, Power_Rating FROM CHARGING_POINT WHERE Point_ID = ?");
                    ps.setInt(1, pid);
                    ResultSet rs = ps.executeQuery();
                    if (rs.next()) {
                        txtPointStationId.setText(String.valueOf(rs.getInt("Station_ID")));
                        cmbConnector.setSelectedItem(rs.getString("Connector_Type"));
                        cmbMode.setSelectedItem(rs.getString("Charging_Mode"));
                        cmbPointStatus.setSelectedItem(rs.getString("Status"));
                        txtPointPower.setText(String.valueOf(rs.getDouble("Power_Rating")));
                        highlightRow(tblPoints, 0, String.valueOf(pid));
                        JOptionPane.showMessageDialog(VoltgridPortal.this, "Charging Point Found: Gun " + pid, "Found", JOptionPane.INFORMATION_MESSAGE);
                    } else {
                        JOptionPane.showMessageDialog(VoltgridPortal.this, "Charging Point ID not found: " + pid, "Not Found", JOptionPane.WARNING_MESSAGE);
                    }
                    rs.close(); ps.close(); con.close();
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(VoltgridPortal.this, "Search error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        btnClear.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) { clearPointForm(); }
        });

        btnShowAll.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                loadPoints();
                JOptionPane.showMessageDialog(VoltgridPortal.this, "Displaying all " + modelPoints.getRowCount() + " records from table CHARGING_POINT.", "Point Records", JOptionPane.INFORMATION_MESSAGE);
            }
        });

        return panel;
    }

    // ====================================================================
    // 5. ENTITY: VEHICLE_MODEL
    // ====================================================================
    private JPanel createModelPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel form = new JPanel(new GridLayout(2, 2, 8, 8));
        form.setBorder(BorderFactory.createTitledBorder("Vehicle Model Inputs & Formats"));

        form.add(new JLabel("Model Name (e.g. Tata Tiago EV):"));
        txtModelName = new JTextField();
        form.add(txtModelName);

        form.add(new JLabel("Battery Capacity (in kWh, e.g. 24.00):"));
        txtModelBattery = new JTextField("24.00");
        form.add(txtModelBattery);

        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 8, 8));
        JButton btnAdd = new JButton("Add Model");
        JButton btnUpdate = new JButton("Update Model");
        JButton btnDelete = new JButton("Delete Model");
        JButton btnSearch = new JButton("Search by Name");
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

        modelModels = new DefaultTableModel(new String[]{"Vehicle Model", "Battery Capacity (kWh)"}, 0);
        tblModels = new JTable(modelModels);
        tblModels.setRowHeight(22);
        tblModels.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                int row = tblModels.getSelectedRow();
                if (row >= 0) {
                    txtModelName.setText(modelModels.getValueAt(row, 0).toString());
                    txtModelBattery.setText(modelModels.getValueAt(row, 1).toString());
                }
            }
        });
        panel.add(new JScrollPane(tblModels), BorderLayout.CENTER);

        btnAdd.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    String model = txtModelName.getText().trim();
                    double battery = Double.parseDouble(txtModelBattery.getText().trim());
                    executeSqlDml("INSERT INTO VEHICLE_MODEL (Model, Battery_Capacity) VALUES (?, ?)",
                            new Object[]{model, battery}, "Model added successfully! (" + model + ")");
                    loadModels();
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(VoltgridPortal.this, "Input Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        btnUpdate.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    String model = txtModelName.getText().trim();
                    double battery = Double.parseDouble(txtModelBattery.getText().trim());
                    executeSqlDml("UPDATE VEHICLE_MODEL SET Battery_Capacity = ? WHERE Model = ?",
                            new Object[]{battery, model}, "Model " + model + " updated successfully!");
                    loadModels();
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(VoltgridPortal.this, "Input Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        btnDelete.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    String model = txtModelName.getText().trim();
                    int confirm = JOptionPane.showConfirmDialog(VoltgridPortal.this, "Delete Model: " + model + "?", "Confirm Delete", JOptionPane.YES_NO_OPTION);
                    if (confirm == JOptionPane.YES_OPTION) {
                        executeSqlDml("DELETE FROM VEHICLE_MODEL WHERE Model = ?", new Object[]{model}, "Model " + model + " deleted successfully!");
                        loadModels();
                        clearModelForm();
                    }
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(VoltgridPortal.this, "Select or enter Model to delete.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        btnSearch.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    String model = txtModelName.getText().trim();
                    Connection con = DBConnection.getConnection();
                    if (con == null) return;
                    PreparedStatement ps = con.prepareStatement("SELECT Model, Battery_Capacity FROM VEHICLE_MODEL WHERE UPPER(Model) LIKE ?");
                    ps.setString(1, "%" + model.toUpperCase() + "%");
                    ResultSet rs = ps.executeQuery();
                    if (rs.next()) {
                        txtModelName.setText(rs.getString("Model"));
                        txtModelBattery.setText(String.valueOf(rs.getDouble("Battery_Capacity")));
                        highlightRow(tblModels, 0, rs.getString("Model"));
                        JOptionPane.showMessageDialog(VoltgridPortal.this, "Model Found: " + rs.getString("Model"), "Found", JOptionPane.INFORMATION_MESSAGE);
                    } else {
                        JOptionPane.showMessageDialog(VoltgridPortal.this, "Model not found: " + model, "Not Found", JOptionPane.WARNING_MESSAGE);
                    }
                    rs.close(); ps.close(); con.close();
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(VoltgridPortal.this, "Search error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        btnClear.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) { clearModelForm(); }
        });

        btnShowAll.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                loadModels();
                JOptionPane.showMessageDialog(VoltgridPortal.this, "Displaying all " + modelModels.getRowCount() + " records from table VEHICLE_MODEL.", "Model Records", JOptionPane.INFORMATION_MESSAGE);
            }
        });

        return panel;
    }

    // ====================================================================
    // 6. ENTITY: VEHICLE
    // ====================================================================
    private JPanel createVehiclePanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel form = new JPanel(new GridLayout(3, 2, 8, 8));
        form.setBorder(BorderFactory.createTitledBorder("Vehicle Inputs & Formats"));

        form.add(new JLabel("License Plate (e.g. TN-07-AB-1234):"));
        txtVehPlate = new JTextField();
        form.add(txtVehPlate);

        form.add(new JLabel("Customer Owner ID (e.g. 401):"));
        txtVehCustId = new JTextField("401");
        form.add(txtVehCustId);

        form.add(new JLabel("Model Name (e.g. Tata Nexon EV):"));
        txtVehModel = new JTextField("Tata Nexon EV");
        form.add(txtVehModel);

        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 8, 8));
        JButton btnAdd = new JButton("Add Vehicle");
        JButton btnUpdate = new JButton("Update Vehicle");
        JButton btnDelete = new JButton("Delete Vehicle");
        JButton btnSearch = new JButton("Search by Plate");
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

        modelVehicles = new DefaultTableModel(new String[]{"Vehicle No", "Customer ID", "Model"}, 0);
        tblVehicles = new JTable(modelVehicles);
        tblVehicles.setRowHeight(22);
        tblVehicles.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                int row = tblVehicles.getSelectedRow();
                if (row >= 0) {
                    txtVehPlate.setText(modelVehicles.getValueAt(row, 0).toString());
                    txtVehCustId.setText(modelVehicles.getValueAt(row, 1).toString());
                    txtVehModel.setText(modelVehicles.getValueAt(row, 2).toString());
                }
            }
        });
        panel.add(new JScrollPane(tblVehicles), BorderLayout.CENTER);

        btnAdd.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    String plate = txtVehPlate.getText().trim();
                    int cid = Integer.parseInt(txtVehCustId.getText().trim());
                    String model = txtVehModel.getText().trim();
                    executeSqlDml("INSERT INTO VEHICLE (Vehicle_No, Customer_ID, Model) VALUES (?, ?, ?)",
                            new Object[]{plate, cid, model}, "Vehicle added successfully! (" + plate + ")");
                    loadVehicles();
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(VoltgridPortal.this, "Input Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        btnUpdate.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    String plate = txtVehPlate.getText().trim();
                    int cid = Integer.parseInt(txtVehCustId.getText().trim());
                    String model = txtVehModel.getText().trim();
                    executeSqlDml("UPDATE VEHICLE SET Customer_ID = ?, Model = ? WHERE Vehicle_No = ?",
                            new Object[]{cid, model, plate}, "Vehicle " + plate + " updated successfully!");
                    loadVehicles();
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(VoltgridPortal.this, "Input Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        btnDelete.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    String plate = txtVehPlate.getText().trim();
                    int confirm = JOptionPane.showConfirmDialog(VoltgridPortal.this, "Delete Vehicle: " + plate + "?", "Confirm Delete", JOptionPane.YES_NO_OPTION);
                    if (confirm == JOptionPane.YES_OPTION) {
                        executeSqlDml("DELETE FROM VEHICLE WHERE Vehicle_No = ?", new Object[]{plate}, "Vehicle " + plate + " deleted successfully!");
                        loadVehicles();
                        clearVehicleForm();
                    }
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(VoltgridPortal.this, "Select or enter Vehicle plate to delete.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        btnSearch.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    String plate = txtVehPlate.getText().trim();
                    Connection con = DBConnection.getConnection();
                    if (con == null) return;
                    PreparedStatement ps = con.prepareStatement("SELECT Vehicle_No, Customer_ID, Model FROM VEHICLE WHERE UPPER(Vehicle_No) = ?");
                    ps.setString(1, plate.toUpperCase());
                    ResultSet rs = ps.executeQuery();
                    if (rs.next()) {
                        txtVehCustId.setText(String.valueOf(rs.getInt("Customer_ID")));
                        txtVehModel.setText(rs.getString("Model"));
                        highlightRow(tblVehicles, 0, rs.getString("Vehicle_No"));
                        JOptionPane.showMessageDialog(VoltgridPortal.this, "Vehicle Found: " + plate + " (" + rs.getString("Model") + ")", "Found", JOptionPane.INFORMATION_MESSAGE);
                    } else {
                        JOptionPane.showMessageDialog(VoltgridPortal.this, "Vehicle not found: " + plate, "Not Found", JOptionPane.WARNING_MESSAGE);
                    }
                    rs.close(); ps.close(); con.close();
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(VoltgridPortal.this, "Search error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        btnClear.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) { clearVehicleForm(); }
        });

        btnShowAll.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                loadVehicles();
                JOptionPane.showMessageDialog(VoltgridPortal.this, "Displaying all " + modelVehicles.getRowCount() + " records from table VEHICLE.", "Vehicle Records", JOptionPane.INFORMATION_MESSAGE);
            }
        });

        return panel;
    }

    // ====================================================================
    // 7. ENTITY: PINCODE_AREA
    // ====================================================================
    private JPanel createPincodePanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel form = new JPanel(new GridLayout(2, 2, 8, 8));
        form.setBorder(BorderFactory.createTitledBorder("Pincode Area Inputs & Formats"));

        form.add(new JLabel("Pincode (6 digits, e.g. 110001):"));
        txtPinCode = new JTextField();
        form.add(txtPinCode);

        form.add(new JLabel("City Name (e.g. New Delhi):"));
        txtPinCity = new JTextField();
        form.add(txtPinCity);

        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 8, 8));
        JButton btnAdd = new JButton("Add Pincode");
        JButton btnUpdate = new JButton("Update Pincode");
        JButton btnDelete = new JButton("Delete Pincode");
        JButton btnSearch = new JButton("Search by Pincode");
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

        modelPincodes = new DefaultTableModel(new String[]{"Pincode", "City"}, 0);
        tblPincodes = new JTable(modelPincodes);
        tblPincodes.setRowHeight(22);
        tblPincodes.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                int row = tblPincodes.getSelectedRow();
                if (row >= 0) {
                    txtPinCode.setText(modelPincodes.getValueAt(row, 0).toString());
                    txtPinCity.setText(modelPincodes.getValueAt(row, 1).toString());
                }
            }
        });
        panel.add(new JScrollPane(tblPincodes), BorderLayout.CENTER);

        btnAdd.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    int pin = Integer.parseInt(txtPinCode.getText().trim());
                    String city = txtPinCity.getText().trim();
                    executeSqlDml("INSERT INTO PINCODE_AREA (Pincode, City) VALUES (?, ?)",
                            new Object[]{pin, city}, "Pincode area added successfully! (" + pin + " - " + city + ")");
                    loadPincodes();
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(VoltgridPortal.this, "Input Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        btnUpdate.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    int pin = Integer.parseInt(txtPinCode.getText().trim());
                    String city = txtPinCity.getText().trim();
                    executeSqlDml("UPDATE PINCODE_AREA SET City = ? WHERE Pincode = ?",
                            new Object[]{city, pin}, "Pincode " + pin + " updated to " + city + " successfully!");
                    loadPincodes();
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(VoltgridPortal.this, "Input Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        btnDelete.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    int pin = Integer.parseInt(txtPinCode.getText().trim());
                    int confirm = JOptionPane.showConfirmDialog(VoltgridPortal.this, "Delete Pincode: " + pin + "?", "Confirm Delete", JOptionPane.YES_NO_OPTION);
                    if (confirm == JOptionPane.YES_OPTION) {
                        executeSqlDml("DELETE FROM PINCODE_AREA WHERE Pincode = ?", new Object[]{pin}, "Pincode " + pin + " deleted successfully!");
                        loadPincodes();
                        clearPincodeForm();
                    }
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(VoltgridPortal.this, "Select or enter Pincode to delete.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        btnSearch.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    int pin = Integer.parseInt(txtPinCode.getText().trim());
                    Connection con = DBConnection.getConnection();
                    if (con == null) return;
                    PreparedStatement ps = con.prepareStatement("SELECT Pincode, City FROM PINCODE_AREA WHERE Pincode = ?");
                    ps.setInt(1, pin);
                    ResultSet rs = ps.executeQuery();
                    if (rs.next()) {
                        txtPinCity.setText(rs.getString("City"));
                        highlightRow(tblPincodes, 0, String.valueOf(pin));
                        JOptionPane.showMessageDialog(VoltgridPortal.this, "Pincode Found: " + pin + " -> " + rs.getString("City"), "Found", JOptionPane.INFORMATION_MESSAGE);
                    } else {
                        JOptionPane.showMessageDialog(VoltgridPortal.this, "Pincode not found: " + pin, "Not Found", JOptionPane.WARNING_MESSAGE);
                    }
                    rs.close(); ps.close(); con.close();
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(VoltgridPortal.this, "Search error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        btnClear.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) { clearPincodeForm(); }
        });

        btnShowAll.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                loadPincodes();
                JOptionPane.showMessageDialog(VoltgridPortal.this, "Displaying all " + modelPincodes.getRowCount() + " records from table PINCODE_AREA.", "Pincode Records", JOptionPane.INFORMATION_MESSAGE);
            }
        });

        return panel;
    }

    // ====================================================================
    // GENERAL DATABASE EXECUTION & QUERY METHODS
    // ====================================================================

    private void executeSqlDml(String sql, Object[] params, String successMsg) {
        Connection con = null;
        PreparedStatement ps = null;
        try {
            con = DBConnection.getConnection();
            if (con == null) {
                JOptionPane.showMessageDialog(this, "Database connection not available.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            ps = con.prepareStatement(sql);
            for (int i = 0; i < params.length; i++) {
                ps.setObject(i + 1, params[i]);
            }
            int rows = ps.executeUpdate();
            con.commit();
            if (rows > 0) {
                JOptionPane.showMessageDialog(this, successMsg, "Success", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "No records were modified. Please check the ID/Key.", "Notice", JOptionPane.WARNING_MESSAGE);
            }
        } catch (SQLException ex) {
            handleSqlException(ex);
        } finally {
            closeQuietly(null, ps, con);
        }
    }

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
        // Ensure tables start clean and empty
        if (modelCustomers != null) modelCustomers.setRowCount(0);
        if (modelOperators != null) modelOperators.setRowCount(0);
        if (modelStations != null) modelStations.setRowCount(0);
        if (modelPoints != null) modelPoints.setRowCount(0);
        if (modelModels != null) modelModels.setRowCount(0);
        if (modelVehicles != null) modelVehicles.setRowCount(0);
        if (modelPincodes != null) modelPincodes.setRowCount(0);
    }

    // Loaders
    private void loadCustomers() {
        modelCustomers.setRowCount(0);
        loadTableData("SELECT Customer_ID, First_Name, Last_Name, Email, TO_CHAR(Registration_Date, 'YYYY-MM-DD') AS RegDate FROM CUSTOMER ORDER BY Customer_ID", modelCustomers, 5);
    }

    private void loadOperators() {
        modelOperators.setRowCount(0);
        loadTableData("SELECT Operator_ID, Name, License_No, Contact_Info FROM OPERATOR ORDER BY Operator_ID", modelOperators, 4);
    }

    private void loadStations() {
        modelStations.setRowCount(0);
        loadTableData("SELECT Station_ID, Operator_ID, Street, Pincode, Contact_No, Tariff_Rate FROM STATION ORDER BY Station_ID", modelStations, 6);
    }

    private void loadPoints() {
        modelPoints.setRowCount(0);
        loadTableData("SELECT Point_ID, Station_ID, Connector_Type, Charging_Mode, Status, Power_Rating FROM CHARGING_POINT ORDER BY Point_ID", modelPoints, 6);
    }

    private void loadModels() {
        modelModels.setRowCount(0);
        loadTableData("SELECT Model, Battery_Capacity FROM VEHICLE_MODEL ORDER BY Model", modelModels, 2);
    }

    private void loadVehicles() {
        modelVehicles.setRowCount(0);
        loadTableData("SELECT Vehicle_No, Customer_ID, Model FROM VEHICLE ORDER BY Vehicle_No", modelVehicles, 3);
    }

    private void loadPincodes() {
        modelPincodes.setRowCount(0);
        loadTableData("SELECT Pincode, City FROM PINCODE_AREA ORDER BY Pincode", modelPincodes, 2);
    }

    private void loadTableData(String sql, DefaultTableModel model, int colCount) {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            con = DBConnection.getConnection();
            if (con == null) return;
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next()) {
                Object[] row = new Object[colCount];
                for (int i = 0; i < colCount; i++) {
                    row[i] = rs.getObject(i + 1);
                }
                model.addRow(row);
            }
        } catch (SQLException ex) {
            handleSqlException(ex);
        } finally {
            closeQuietly(rs, ps, con);
        }
    }

    private void highlightRow(JTable table, int colIndex, String targetVal) {
        for (int i = 0; i < table.getRowCount(); i++) {
            Object val = table.getValueAt(i, colIndex);
            if (val != null && val.toString().equalsIgnoreCase(targetVal)) {
                table.setRowSelectionInterval(i, i);
                table.scrollRectToVisible(table.getCellRect(i, 0, true));
                return;
            }
        }
    }

    private void handleSqlException(SQLException ex) {
        int code = ex.getErrorCode();
        String msg = ex.getMessage();
        if (code == 1 || msg.contains("00001")) {
            JOptionPane.showMessageDialog(this, "Duplicate Error (ORA-00001):\nA record with this Primary Key or unique value already exists.", "Unique Constraint Error", JOptionPane.ERROR_MESSAGE);
        } else if (code == 2290 || msg.contains("02290")) {
            JOptionPane.showMessageDialog(this, "Check Constraint Violation (ORA-02290):\nInput violates database domain rules.", "Validation Error", JOptionPane.ERROR_MESSAGE);
        } else if (code == 2291 || msg.contains("02291")) {
            JOptionPane.showMessageDialog(this, "Foreign Key Error (ORA-02291):\nReferenced parent record does not exist in parent table.", "Integrity Error", JOptionPane.ERROR_MESSAGE);
        } else if (code == 2292 || msg.contains("02292")) {
            JOptionPane.showMessageDialog(this, "Foreign Key Protected (ORA-02292):\nCannot delete: Child records in another table reference this record.\n\nTip: To test Delete safely, add a new test record and delete it.", "Integrity Protected", JOptionPane.WARNING_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(this, "Database Error (" + code + "):\n" + msg, "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Clears
    private void clearCustomerForm() {
        txtCustId.setText(""); txtCustFName.setText(""); txtCustLName.setText(""); txtCustEmail.setText(""); txtCustDate.setText("2024-03-01");
    }
    private void clearOperatorForm() {
        txtOpId.setText(""); txtOpName.setText(""); txtOpLicense.setText(""); txtOpContact.setText("support@operator.com");
    }
    private void clearStationForm() {
        txtStationId.setText(""); txtStationOpId.setText("101"); txtStationStreet.setText(""); txtStationPincode.setText("600001"); txtStationContact.setText("9876543210"); txtStationTariff.setText("19.50");
    }
    private void clearPointForm() {
        txtPointId.setText(""); txtPointStationId.setText("201"); txtPointPower.setText("60.00");
    }
    private void clearModelForm() {
        txtModelName.setText(""); txtModelBattery.setText("24.00");
    }
    private void clearVehicleForm() {
        txtVehPlate.setText(""); txtVehCustId.setText("401"); txtVehModel.setText("Tata Nexon EV");
    }
    private void clearPincodeForm() {
        txtPinCode.setText(""); txtPinCity.setText("");
    }

    private void closeQuietly(ResultSet rs, PreparedStatement ps, Connection con) {
        try { if (rs != null) rs.close(); } catch (Exception ignored) {}
        try { if (ps != null) ps.close(); } catch (Exception ignored) {}
        try { if (con != null) con.close(); } catch (Exception ignored) {}
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new VoltgridPortal().setVisible(true);
            }
        });
    }
}
