# Voltgrid - Smart EV Charging Station Network
**First-Year B.Tech DBMS Lab & Mini Project**

---

## 1. Project Overview
**Voltgrid** is a relational database management system designed to manage a smart electric vehicle (EV) charging network across major Indian metropolitan cities. The system tracks charging network operators, physical charging stations, individual fast/slow charging guns, customer accounts, EV vehicle models, charging sessions, payments, slot reservations, and equipment maintenance.

### Schema Normalization (3NF)
The schema consists of **13 normalized tables** strictly conforming to Third Normal Form (3NF):
1. **1NF Compliance:** Multi-valued phone numbers are factored out into a dedicated table (`CUSTOMER_PHONE`). Composite primary keys are used where appropriate (`RESERVED_FOR`, `CUSTOMER_PHONE`, `MAINTENANCE`).
2. **2NF Compliance:** Attributes depend fully on the complete primary key. Vehicle model details (such as `Battery_Capacity`) are stored in `VEHICLE_MODEL` rather than duplicating them in `VEHICLE`.
3. **3NF Compliance:** Transitive dependencies are removed. Pincode to City mapping is separated into `PINCODE_AREA` so that city does not transitively depend on `Station_ID` via `Pincode`.

---

## 2. Lab Cycle Sheet Mapping (EV Domain)
Adapted according to the standard college DBMS lab manual schema (e.g., Library Management System):

| Standard Lab Entity | Voltgrid EV Entity | Primary Key / Key Attributes |
|---|---|---|
| `BORROWER` | `CUSTOMER` | `Customer_ID`, `First_Name`, `Last_Name`, `Email` |
| `LIBRARY_BRANCH` | `STATION` | `Station_ID`, `Operator_ID`, `Pincode`, `Tariff_Rate` |
| `PUBLISHER` | `OPERATOR` | `Operator_ID`, `Name`, `License_No` |
| `BOOK_LOANS` | `CHARGING_SESSION` | `Session_ID`, `Vehicle_No`, `Point_ID`, `Units_Consumed` |
| `BOOK_COPIES.No_of_copies` | `CHARGING_POINT.Power_Rating` | `Power_Rating` (kW capacity) |
| `BOOK` | `VEHICLE` | `Vehicle_No`, `Customer_ID`, `Model` |

---

## 3. Directory & File Structure

```text
dbms_project/
├── sql/
│   ├── schema.sql                   (13 tables, named PKs, FKs, and CHECK constraints)
│   ├── data.sql                     (Sample Indian records + interactive insert template)
│   ├── cycle1_constraint_tests.sql  (4 valid & 4 invalid integrity constraint tests)
│   ├── cycle2_select_dml.sql        (Aliases, distinct, computed cols, filtering, DML, TCL)
│   ├── cycle3_functions.sql         (DDL ALTERs, character/numeric/date/NULL functions, GROUP BY)
│   ├── cycle4_joins_views.sql       (5 inner joins, 4 outer joins, subqueries, ANY/ALL, views)
│   └── cycle5_plsql.sql             (SELECT INTO, IF-ELSE, CASE, palindrome loop, cursors, procs)
├── java/
│   ├── ConnectOracle.java           (Basic JDBC connectivity verification test)
│   ├── DBConnection.java            (Centralized connection provider)
│   ├── CustomerOps.java             (CRUD operations for CUSTOMER table)
│   ├── StationOps.java              (CRUD operations for STATION table)
│   ├── PointOps.java                (CRUD operations for CHARGING_POINT table)
│   └── Main.java                    (Interactive console menu loop)
└── README.md                        (Setup, execution guide, and viva notes)
```

---

## 4. Part A - SQL*Plus Execution Guide (Windows)

### Step 1: Open Terminal and Navigate to SQL Directory
```cmd
cd c:\projects\dbms_project\sql
```

### Step 2: Connect to Oracle Database via SQL*Plus
- **Local Oracle XE (default service `XEPDB1`):**
  ```cmd
  sqlplus username/password@localhost:1521/XEPDB1
  ```
- **College Lab Server alternative:**
  ```cmd
  sqlplus username/password@HOST:PORT/SERVICE_NAME
  ```
  *(Example: `sqlplus scott/tiger@172.16.0.5:1521/orcl`)*

### Step 3: Run the Scripts in Order
Inside the `SQL>` prompt, execute:
```sql
-- 1. Create all 13 tables and constraints
@schema.sql

-- 2. Populate tables with sample dataset
@data.sql

-- 3. Run Cycle Sheet I (Integrity tests with SPOOL output)
@cycle1_constraint_tests.sql

-- 4. Run Cycle Sheet II (DML, filtering, aliases, transactions)
@cycle2_select_dml.sql

-- 5. Run Cycle Sheet III (Built-in functions, conversions, aggregates)
@cycle3_functions.sql

-- 6. Run Cycle Sheet IV (Joins, subqueries, views)
@cycle4_joins_views.sql

-- 7. Run Cycle Sheet V (PL/SQL blocks, cursors, procedures, functions)
@cycle5_plsql.sql
```

### Spool Output Files
Each cycle script writes its complete terminal output into a text file in `c:\projects\dbms_project\sql\`:
- `cycle1_output.txt`
- `cycle2_output.txt`
- `cycle3_output.txt`
- `cycle4_output.txt`
- `cycle5_output.txt`

---

## 5. Part B - Java JDBC Setup & Execution (Windows)

### Prerequisites
1. Ensure JDK 17 (or compatible JDK) is installed.
2. Place the Oracle JDBC driver file **`ojdbc17.jar`** (or `ojdbc8.jar` / `ojdbc11.jar`) inside the `c:\projects\dbms_project\java\` folder.

### Step 1: Navigate to Java Folder
```cmd
cd c:\projects\dbms_project\java
```

### Step 2: Configure Database Credentials
Open `DBConnection.java` and `ConnectOracle.java` in any text editor and adjust if necessary:
```java
static String url = "jdbc:oracle:thin:@localhost:1521/XEPDB1";
static String username = "system";
static String password = "your_oracle_password";
```

### Step 3: Test Database Connectivity
Compile:
```cmd
javac -cp ojdbc17.jar ConnectOracle.java
```
Run:
```cmd
java -cp ".;ojdbc17.jar" ConnectOracle
```
**Expected Output:**
```text
Database Connected Successfully
```

### Step 4: Compile the Application
Compile all source files together:
```cmd
javac -cp ojdbc17.jar DBConnection.java CustomerOps.java StationOps.java PointOps.java Main.java VoltgridGUI.java
```

### Step 5: Run the Front End Applications

#### Option A: Full Strong Entities Management Portal (Recommended)
```cmd
java -cp ".;ojdbc17.jar" VoltgridPortal
```
* Covers **ALL 7 strong entities** across the EV network:
  1. Customers (`CUSTOMER`)
  2. Network Operators (`OPERATOR`)
  3. Physical Stations (`STATION`)
  4. Charging Points / Guns (`CHARGING_POINT`)
  5. EV Vehicle Models (`VEHICLE_MODEL`)
  6. Registered Vehicles (`VEHICLE`)
  7. Geographic Areas (`PINCODE_AREA`)
* Tables start empty and only display records when you click **"Show All Records"**.
* Full Add, Update, Delete (with FK protection), and Search functions on each entity tab.

#### Option B: Standard 3-Entity GUI
```cmd
java -cp ".;ojdbc17.jar" VoltgridGUI
```
* Focused GUI for Customers, Stations, and Charging Points.

#### Option C: Console Menu Loop
```cmd
java -cp ".;ojdbc17.jar" Main
```

---

## 6. Viva-Voce Quick Reference (Key Questions & Answers)

1. **Why is `Status` constrained using a CHECK constraint?**
   - It enforces *domain integrity* by restricting charging point statuses to only valid states: `'Available'`, `'In-Use'`, or `'Faulty'`.
2. **What happens when an invalid record is deleted?**
   - If a record is referenced by a foreign key in another table (e.g. deleting a Station that has Charging Points), Oracle returns error `ORA-02292` (child record found). The Java code intercepts this and displays: `"Cannot delete: record is used in another table"`.
3. **What is the purpose of `SAVEPOINT` and `ROLLBACK TO SAVEPOINT`?**
   - It allows rolling back parts of a transaction conditionally without aborting the entire transaction.
4. **Why is `user_updatable_columns` queried in Cycle IV?**
   - A view in SQL can only receive `UPDATE`, `INSERT`, or `DELETE` statements if it maps directly to base table columns without `GROUP BY`, `DISTINCT`, or aggregates. Querying `user_updatable_columns` programmatically verifies if the view is updatable.
5. **How does the PL/SQL Palindrome check work?**
   - The loop runs from `LENGTH(string)` down to 1 (`FOR i IN REVERSE 1..len`), extracting characters using `SUBSTR(str, i, 1)` and concatenating them. The original and reversed strings are then compared.
