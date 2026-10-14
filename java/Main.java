import java.util.Scanner;

// File: Main.java
// Console-based interactive menu for Voltgrid EV Management System
public class Main {

    private static Scanner sc = new Scanner(System.in);

    public static void main(String[] args) {
        while (true) {
            System.out.println("\n==========================================");
            System.out.println("   VOLTGRID - SMART EV CHARGING NETWORK   ");
            System.out.println("==========================================");
            System.out.println("1. Manage Customers");
            System.out.println("2. Manage Charging Stations");
            System.out.println("3. Manage Charging Points (Guns)");
            System.out.println("4. Exit");
            System.out.print("Enter your choice (1-4): ");

            int choice = sc.nextInt();
            sc.nextLine(); // Consume newline

            switch (choice) {
                case 1:
                    customerMenu();
                    break;
                case 2:
                    stationMenu();
                    break;
                case 3:
                    pointMenu();
                    break;
                case 4:
                    System.out.println("Thank you for using Voltgrid EV System. Goodbye!");
                    System.exit(0);
                    break;
                default:
                    System.out.println("Invalid choice! Please enter a number between 1 and 4.");
            }
        }
    }

    // Sub-menu for CUSTOMER operations
    private static void customerMenu() {
        while (true) {
            System.out.println("\n--- Customer Management ---");
            System.out.println("1. Insert New Customer");
            System.out.println("2. Update Customer Details");
            System.out.println("3. Delete Customer");
            System.out.println("4. Search Customer by ID");
            System.out.println("5. Display All Customers");
            System.out.println("6. Back to Main Menu");
            System.out.print("Enter choice (1-6): ");

            int ch = sc.nextInt();
            sc.nextLine();

            switch (ch) {
                case 1:
                    System.out.print("Enter Customer ID (number, e.g. 407): ");
                    int id = sc.nextInt();
                    sc.nextLine();
                    System.out.print("Enter First Name (e.g. Rahul): ");
                    String fname = sc.nextLine();
                    System.out.print("Enter Last Name (e.g. Verma): ");
                    String lname = sc.nextLine();
                    System.out.print("Enter Email (e.g. rahul.verma@gmail.com): ");
                    String email = sc.nextLine();
                    System.out.print("Enter Registration Date (format: YYYY-MM-DD, e.g. 2024-03-15): ");
                    String regDate = sc.nextLine();
                    CustomerOps.insert(id, fname, lname, email, regDate);
                    break;

                case 2:
                    System.out.print("Enter Customer ID to update (e.g. 401): ");
                    int uid = sc.nextInt();
                    sc.nextLine();
                    System.out.print("Enter Updated First Name (e.g. Aarav): ");
                    String ufname = sc.nextLine();
                    System.out.print("Enter Updated Last Name (e.g. Sharma): ");
                    String ulname = sc.nextLine();
                    System.out.print("Enter Updated Email (e.g. aarav.new@gmail.com): ");
                    String uemail = sc.nextLine();
                    CustomerOps.update(uid, ufname, ulname, uemail);
                    break;

                case 3:
                    System.out.print("Enter Customer ID to delete (e.g. 407): ");
                    int did = sc.nextInt();
                    sc.nextLine();
                    CustomerOps.delete(did);
                    break;

                case 4:
                    System.out.print("Enter Customer ID to search (e.g. 401): ");
                    int sid = sc.nextInt();
                    sc.nextLine();
                    CustomerOps.search(sid);
                    break;

                case 5:
                    CustomerOps.display();
                    break;

                case 6:
                    return;

                default:
                    System.out.println("Invalid option! Try again.");
            }
        }
    }

    // Sub-menu for STATION operations
    private static void stationMenu() {
        while (true) {
            System.out.println("\n--- Station Management ---");
            System.out.println("1. Insert New Station");
            System.out.println("2. Update Station Details");
            System.out.println("3. Delete Station");
            System.out.println("4. Search Station by ID");
            System.out.println("5. Display All Stations");
            System.out.println("6. Back to Main Menu");
            System.out.print("Enter choice (1-6): ");

            int ch = sc.nextInt();
            sc.nextLine();

            switch (ch) {
                case 1:
                    System.out.print("Enter Station ID (number, e.g. 207): ");
                    int id = sc.nextInt();
                    System.out.print("Enter Operator ID (e.g. 101 - Tata Power): ");
                    int opId = sc.nextInt();
                    sc.nextLine();
                    System.out.print("Enter Street / Location (e.g. MG Road, Anna Salai): ");
                    String street = sc.nextLine();
                    System.out.print("Enter Pincode (6 digits, e.g. 600001): ");
                    int pin = sc.nextInt();
                    sc.nextLine();
                    System.out.print("Enter Contact Number (10 digits, e.g. 9876543210): ");
                    String contact = sc.nextLine();
                    System.out.print("Enter Tariff Rate (INR per unit, e.g. 19.50): ");
                    double tariff = sc.nextDouble();
                    sc.nextLine();
                    StationOps.insert(id, opId, street, pin, contact, tariff);
                    break;

                case 2:
                    System.out.print("Enter Station ID to update (e.g. 201): ");
                    int uid = sc.nextInt();
                    sc.nextLine();
                    System.out.print("Enter New Contact Number (10 digits, e.g. 9444012345): ");
                    String ucontact = sc.nextLine();
                    System.out.print("Enter New Tariff Rate (INR per unit, e.g. 21.00): ");
                    double utariff = sc.nextDouble();
                    sc.nextLine();
                    StationOps.update(uid, ucontact, utariff);
                    break;

                case 3:
                    System.out.print("Enter Station ID to delete (e.g. 207): ");
                    int did = sc.nextInt();
                    sc.nextLine();
                    StationOps.delete(did);
                    break;

                case 4:
                    System.out.print("Enter Station ID to search (e.g. 201): ");
                    int sid = sc.nextInt();
                    sc.nextLine();
                    StationOps.search(sid);
                    break;

                case 5:
                    StationOps.display();
                    break;

                case 6:
                    return;

                default:
                    System.out.println("Invalid option! Try again.");
            }
        }
    }

    // Sub-menu for CHARGING_POINT operations
    private static void pointMenu() {
        while (true) {
            System.out.println("\n--- Charging Point Management ---");
            System.out.println("1. Insert New Charging Point");
            System.out.println("2. Update Charging Point Details");
            System.out.println("3. Delete Charging Point");
            System.out.println("4. Search Charging Point by ID");
            System.out.println("5. Display All Charging Points");
            System.out.println("6. Back to Main Menu");
            System.out.print("Enter choice (1-6): ");

            int ch = sc.nextInt();
            sc.nextLine();

            switch (ch) {
                case 1:
                    System.out.print("Enter Point ID (number, e.g. 308): ");
                    int id = sc.nextInt();
                    System.out.print("Enter Station ID (e.g. 201): ");
                    int stId = sc.nextInt();
                    sc.nextLine();
                    System.out.print("Enter Connector Type (CCS2 / Type-2 / GB/T): ");
                    String conn = sc.nextLine();
                    System.out.print("Enter Charging Mode (AC / DC): ");
                    String mode = sc.nextLine();
                    System.out.print("Enter Status (Available / In-Use / Faulty): ");
                    String status = sc.nextLine();
                    System.out.print("Enter Power Rating (kW, e.g. 60.00): ");
                    double power = sc.nextDouble();
                    sc.nextLine();
                    PointOps.insert(id, stId, conn, mode, status, power);
                    break;

                case 2:
                    System.out.print("Enter Point ID to update (e.g. 301): ");
                    int uid = sc.nextInt();
                    sc.nextLine();
                    System.out.print("Enter New Status (Available / In-Use / Faulty): ");
                    String ustatus = sc.nextLine();
                    System.out.print("Enter New Power Rating (kW, e.g. 75.00): ");
                    double upower = sc.nextDouble();
                    sc.nextLine();
                    PointOps.update(uid, ustatus, upower);
                    break;

                case 3:
                    System.out.print("Enter Point ID to delete: ");
                    int did = sc.nextInt();
                    sc.nextLine();
                    PointOps.delete(did);
                    break;

                case 4:
                    System.out.print("Enter Point ID to search: ");
                    int sid = sc.nextInt();
                    sc.nextLine();
                    PointOps.search(sid);
                    break;

                case 5:
                    PointOps.display();
                    break;

                case 6:
                    return;

                default:
                    System.out.println("Invalid option! Try again.");
            }
        }
    }
}
