SET LINESIZE 200
SET PAGESIZE 50
SPOOL cycle1_output.txt

-- Valid Insert 1: New customer with valid unique ID, email, and proper date
-- Integrity Type: Entity & Domain Integrity (Valid)
INSERT INTO CUSTOMER (Customer_ID, First_Name, Last_Name, Email, Registration_Date)
VALUES (407, 'Rahul', 'Verma', 'rahul.verma@gmail.com', TO_DATE('2023-07-01', 'YYYY-MM-DD'));

-- Valid Insert 2: New vehicle with valid FK referencing Customer 407 and existing model
-- Integrity Type: Referential Integrity (Valid)
INSERT INTO VEHICLE (Vehicle_No, Customer_ID, Model)
VALUES ('DL-01-XY-9999', 407, 'Tata Nexon EV');

-- Valid Insert 3: New charging point with valid status ('Available'), mode ('DC'), power (> 0)
-- Integrity Type: Domain Integrity (Valid CHECK constraints)
INSERT INTO CHARGING_POINT (Point_ID, Station_ID, Connector_Type, Charging_Mode, Status, Power_Rating)
VALUES (308, 201, 'CCS2', 'DC', 'Available', 150.00);

-- Valid Insert 4: Maintenance record with composite PK and valid FK to Point 308
-- Integrity Type: Entity & Referential Integrity (Valid)
INSERT INTO MAINTENANCE (Point_ID, Report_No, Issue_Description, Date_Reported, Status)
VALUES (308, 1, 'Initial calibration check', TO_DATE('2024-03-05', 'YYYY-MM-DD'), 'Completed');


-- Invalid Insert 1: Entity Integrity Violation (Primary Key)
INSERT INTO CUSTOMER (Customer_ID, First_Name, Last_Name, Email, Registration_Date)
VALUES (401, 'Duplicate', 'User', 'duplicate.user@gmail.com', TO_DATE('2023-08-01', 'YYYY-MM-DD'));

-- Invalid Insert 2: Referential Integrity Violation (Foreign Key)
INSERT INTO STATION (Station_ID, Operator_ID, Street, Pincode, Contact_No, Tariff_Rate)
VALUES (207, 999, 'MG Road', 560001, '9999988888', 25.00);

-- Invalid Insert 3: Domain Integrity Violation (CHECK Constraint)
INSERT INTO CHARGING_POINT (Point_ID, Station_ID, Connector_Type, Charging_Mode, Status, Power_Rating)
VALUES (309, 201, 'CCS2', 'DC', 'Broken', 50.00);

-- Invalid Insert 4: Domain / Business Integrity Violation (CHECK Constraint)
INSERT INTO STATION (Station_ID, Operator_ID, Street, Pincode, Contact_No, Tariff_Rate)
VALUES (208, 101, 'Gandhi Road', 632014, '9112233445', -5.00);

-- Roll back test inserts so database remains in clean state for next cycles
ROLLBACK;

SPOOL OFF
