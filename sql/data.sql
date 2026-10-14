-- ====================================================================
-- Voltgrid: Smart EV Charging Station Network
-- File: data.sql
-- Description: Sample Data Population (5-8 rows per table)
-- Style: First Year B.Tech DBMS Lab Manual Standard
-- ====================================================================

-- 1. INSERT INTO OPERATOR
INSERT INTO OPERATOR (Operator_ID, Name, License_No, Contact_Info)
VALUES (101, 'Tata Power EV', 'LIC-TP-2023-01', 'support@tatapowerezcharge.com');

INSERT INTO OPERATOR (Operator_ID, Name, License_No, Contact_Info)
VALUES (102, 'Zeon Charging', 'LIC-ZN-2023-02', 'contact@zeoncharging.com');

INSERT INTO OPERATOR (Operator_ID, Name, License_No, Contact_Info)
VALUES (103, 'Statiq EV', 'LIC-ST-2023-03', 'help@statiq.in');

INSERT INTO OPERATOR (Operator_ID, Name, License_No, Contact_Info)
VALUES (104, 'Ather Grid', 'LIC-AG-2023-04', 'grid@atherenergy.com');

INSERT INTO OPERATOR (Operator_ID, Name, License_No, Contact_Info)
VALUES (105, 'Relux Electric', 'LIC-RL-2023-05', 'care@reluxelectric.com');

INSERT INTO OPERATOR (Operator_ID, Name, License_No, Contact_Info)
VALUES (106, 'ChargeZone', 'LIC-CZ-2023-06', 'support@chargezone.com');

-- 2. INSERT INTO PINCODE_AREA
INSERT INTO PINCODE_AREA (Pincode, City) VALUES (632014, 'Vellore');
INSERT INTO PINCODE_AREA (Pincode, City) VALUES (600001, 'Chennai');
INSERT INTO PINCODE_AREA (Pincode, City) VALUES (560001, 'Bengaluru');
INSERT INTO PINCODE_AREA (Pincode, City) VALUES (641001, 'Coimbatore');
INSERT INTO PINCODE_AREA (Pincode, City) VALUES (625001, 'Madurai');
INSERT INTO PINCODE_AREA (Pincode, City) VALUES (500001, 'Hyderabad');

-- 3. INSERT INTO STATION
INSERT INTO STATION (Station_ID, Operator_ID, Street, Pincode, Contact_No, Tariff_Rate)
VALUES (201, 101, 'Katpadi Main Road', 632014, '9876543210', 18.50);

INSERT INTO STATION (Station_ID, Operator_ID, Street, Pincode, Contact_No, Tariff_Rate)
VALUES (202, 101, 'Anna Salai, T Nagar', 600001, '9876543211', 20.00);

INSERT INTO STATION (Station_ID, Operator_ID, Street, Pincode, Contact_No, Tariff_Rate)
VALUES (203, 102, 'Indiranagar 100ft Rd', 560001, '9876543212', 22.00);

INSERT INTO STATION (Station_ID, Operator_ID, Street, Pincode, Contact_No, Tariff_Rate)
VALUES (204, 103, 'Avinashi Road', 641001, '9876543213', 19.00);

INSERT INTO STATION (Station_ID, Operator_ID, Street, Pincode, Contact_No, Tariff_Rate)
VALUES (205, 104, 'KK Nagar Ring Road', 625001, '9876543214', 16.50);

INSERT INTO STATION (Station_ID, Operator_ID, Street, Pincode, Contact_No, Tariff_Rate)
VALUES (206, 105, 'HITEC City Phase 2', 500001, '9876543215', 21.00);

-- 4. INSERT INTO CHARGING_POINT
INSERT INTO CHARGING_POINT (Point_ID, Station_ID, Connector_Type, Charging_Mode, Status, Power_Rating)
VALUES (301, 201, 'CCS2', 'DC', 'Available', 60.00);

INSERT INTO CHARGING_POINT (Point_ID, Station_ID, Connector_Type, Charging_Mode, Status, Power_Rating)
VALUES (302, 201, 'Type-2', 'AC', 'Available', 22.00);

INSERT INTO CHARGING_POINT (Point_ID, Station_ID, Connector_Type, Charging_Mode, Status, Power_Rating)
VALUES (303, 202, 'CCS2', 'DC', 'In-Use', 50.00);

INSERT INTO CHARGING_POINT (Point_ID, Station_ID, Connector_Type, Charging_Mode, Status, Power_Rating)
VALUES (304, 203, 'CCS2', 'DC', 'Available', 120.00);

INSERT INTO CHARGING_POINT (Point_ID, Station_ID, Connector_Type, Charging_Mode, Status, Power_Rating)
VALUES (305, 204, 'Type-2', 'AC', 'Faulty', 11.00);

INSERT INTO CHARGING_POINT (Point_ID, Station_ID, Connector_Type, Charging_Mode, Status, Power_Rating)
VALUES (306, 205, 'GB/T', 'DC', 'Available', 30.00);

INSERT INTO CHARGING_POINT (Point_ID, Station_ID, Connector_Type, Charging_Mode, Status, Power_Rating)
VALUES (307, 206, 'CCS2', 'DC', 'In-Use', 60.00);

-- 5. INSERT INTO CUSTOMER
INSERT INTO CUSTOMER (Customer_ID, First_Name, Last_Name, Email, Registration_Date)
VALUES (401, 'Aarav', 'Sharma', 'aarav.sharma@gmail.com', TO_DATE('2023-01-15', 'YYYY-MM-DD'));

INSERT INTO CUSTOMER (Customer_ID, First_Name, Last_Name, Email, Registration_Date)
VALUES (402, 'Priya', 'Nair', 'priya.nair@gmail.com', TO_DATE('2023-02-20', 'YYYY-MM-DD'));

INSERT INTO CUSTOMER (Customer_ID, First_Name, Last_Name, Email, Registration_Date)
VALUES (403, 'Karthik', 'Rajan', 'karthik.rajan@yahoo.com', TO_DATE('2023-03-10', 'YYYY-MM-DD'));

INSERT INTO CUSTOMER (Customer_ID, First_Name, Last_Name, Email, Registration_Date)
VALUES (404, 'Ananya', 'Reddy', 'ananya.reddy@outlook.com', TO_DATE('2023-04-05', 'YYYY-MM-DD'));

INSERT INTO CUSTOMER (Customer_ID, First_Name, Last_Name, Email, Registration_Date)
VALUES (405, 'Vikram', 'Mehta', 'vikram.mehta@gmail.com', TO_DATE('2023-05-12', 'YYYY-MM-DD'));

INSERT INTO CUSTOMER (Customer_ID, First_Name, Last_Name, Email, Registration_Date)
VALUES (406, 'Sneha', 'Patel', 'sneha.patel@gmail.com', TO_DATE('2023-06-18', 'YYYY-MM-DD'));

-- 6. INSERT INTO CUSTOMER_PHONE
INSERT INTO CUSTOMER_PHONE (Customer_ID, Phone_No) VALUES (401, '9840123456');
INSERT INTO CUSTOMER_PHONE (Customer_ID, Phone_No) VALUES (401, '9840123457');
INSERT INTO CUSTOMER_PHONE (Customer_ID, Phone_No) VALUES (402, '9741234567');
INSERT INTO CUSTOMER_PHONE (Customer_ID, Phone_No) VALUES (403, '9443123456');
INSERT INTO CUSTOMER_PHONE (Customer_ID, Phone_No) VALUES (404, '9988776655');
INSERT INTO CUSTOMER_PHONE (Customer_ID, Phone_No) VALUES (405, '9123456780');
INSERT INTO CUSTOMER_PHONE (Customer_ID, Phone_No) VALUES (406, '9001122334');

-- 7. INSERT INTO VEHICLE_MODEL
INSERT INTO VEHICLE_MODEL (Model, Battery_Capacity) VALUES ('Tata Nexon EV', 40.50);
INSERT INTO VEHICLE_MODEL (Model, Battery_Capacity) VALUES ('Tata Punch EV', 35.00);
INSERT INTO VEHICLE_MODEL (Model, Battery_Capacity) VALUES ('MG ZS EV', 50.30);
INSERT INTO VEHICLE_MODEL (Model, Battery_Capacity) VALUES ('Mahindra XUV400', 39.40);
INSERT INTO VEHICLE_MODEL (Model, Battery_Capacity) VALUES ('Hyundai Ioniq 5', 72.60);
INSERT INTO VEHICLE_MODEL (Model, Battery_Capacity) VALUES ('BYD Atto 3', 60.48);

-- 8. INSERT INTO VEHICLE
INSERT INTO VEHICLE (Vehicle_No, Customer_ID, Model)
VALUES ('TN-23-AB-1234', 401, 'Tata Nexon EV');

INSERT INTO VEHICLE (Vehicle_No, Customer_ID, Model)
VALUES ('TN-01-CD-5678', 402, 'MG ZS EV');

INSERT INTO VEHICLE (Vehicle_No, Customer_ID, Model)
VALUES ('KA-03-EF-9012', 403, 'Hyundai Ioniq 5');

INSERT INTO VEHICLE (Vehicle_No, Customer_ID, Model)
VALUES ('TN-38-GH-3456', 404, 'Tata Punch EV');

INSERT INTO VEHICLE (Vehicle_No, Customer_ID, Model)
VALUES ('TS-09-JK-7890', 405, 'Mahindra XUV400');

INSERT INTO VEHICLE (Vehicle_No, Customer_ID, Model)
VALUES ('TN-59-LM-2345', 406, 'BYD Atto 3');

-- 9. INSERT INTO CHARGING_SESSION
INSERT INTO CHARGING_SESSION (Session_ID, Vehicle_No, Point_ID, Start_Time, End_Time, Units_Consumed)
VALUES (601, 'TN-23-AB-1234', 301, 
        TO_DATE('2024-03-01 09:00:00', 'YYYY-MM-DD HH24:MI:SS'), 
        TO_DATE('2024-03-01 10:15:00', 'YYYY-MM-DD HH24:MI:SS'), 28.50);

INSERT INTO CHARGING_SESSION (Session_ID, Vehicle_No, Point_ID, Start_Time, End_Time, Units_Consumed)
VALUES (602, 'TN-01-CD-5678', 303, 
        TO_DATE('2024-03-01 11:30:00', 'YYYY-MM-DD HH24:MI:SS'), 
        TO_DATE('2024-03-01 12:45:00', 'YYYY-MM-DD HH24:MI:SS'), 32.00);

INSERT INTO CHARGING_SESSION (Session_ID, Vehicle_No, Point_ID, Start_Time, End_Time, Units_Consumed)
VALUES (603, 'KA-03-EF-9012', 304, 
        TO_DATE('2024-03-02 14:00:00', 'YYYY-MM-DD HH24:MI:SS'), 
        TO_DATE('2024-03-02 15:00:00', 'YYYY-MM-DD HH24:MI:SS'), 45.00);

INSERT INTO CHARGING_SESSION (Session_ID, Vehicle_No, Point_ID, Start_Time, End_Time, Units_Consumed)
VALUES (604, 'TN-38-GH-3456', 302, 
        TO_DATE('2024-03-02 16:00:00', 'YYYY-MM-DD HH24:MI:SS'), 
        TO_DATE('2024-03-02 18:30:00', 'YYYY-MM-DD HH24:MI:SS'), 18.20);

INSERT INTO CHARGING_SESSION (Session_ID, Vehicle_No, Point_ID, Start_Time, End_Time, Units_Consumed)
VALUES (605, 'TS-09-JK-7890', 307, 
        TO_DATE('2024-03-03 10:00:00', 'YYYY-MM-DD HH24:MI:SS'), 
        TO_DATE('2024-03-03 11:10:00', 'YYYY-MM-DD HH24:MI:SS'), 25.40);

INSERT INTO CHARGING_SESSION (Session_ID, Vehicle_No, Point_ID, Start_Time, End_Time, Units_Consumed)
VALUES (606, 'TN-59-LM-2345', 301, 
        TO_DATE('2024-03-03 13:00:00', 'YYYY-MM-DD HH24:MI:SS'), 
        TO_DATE('2024-03-03 14:20:00', 'YYYY-MM-DD HH24:MI:SS'), 35.80);

-- 10. INSERT INTO PAYMENT
INSERT INTO PAYMENT (Payment_ID, Session_ID, Payment_Mode, Payment_Date)
VALUES (701, 601, 'UPI', TO_DATE('2024-03-01', 'YYYY-MM-DD'));

INSERT INTO PAYMENT (Payment_ID, Session_ID, Payment_Mode, Payment_Date)
VALUES (702, 602, 'Credit Card', TO_DATE('2024-03-01', 'YYYY-MM-DD'));

INSERT INTO PAYMENT (Payment_ID, Session_ID, Payment_Mode, Payment_Date)
VALUES (703, 603, 'UPI', TO_DATE('2024-03-02', 'YYYY-MM-DD'));

INSERT INTO PAYMENT (Payment_ID, Session_ID, Payment_Mode, Payment_Date)
VALUES (704, 604, 'Debit Card', TO_DATE('2024-03-02', 'YYYY-MM-DD'));

INSERT INTO PAYMENT (Payment_ID, Session_ID, Payment_Mode, Payment_Date)
VALUES (705, 605, 'Net Banking', TO_DATE('2024-03-03', 'YYYY-MM-DD'));

INSERT INTO PAYMENT (Payment_ID, Session_ID, Payment_Mode, Payment_Date)
VALUES (706, 606, 'UPI', TO_DATE('2024-03-03', 'YYYY-MM-DD'));

-- 11. INSERT INTO RESERVATION
INSERT INTO RESERVATION (Reservation_ID, Customer_ID, Booking_Time, Slot_Time)
VALUES (801, 401, 
        TO_DATE('2024-03-01 08:00:00', 'YYYY-MM-DD HH24:MI:SS'), 
        TO_DATE('2024-03-01 09:00:00', 'YYYY-MM-DD HH24:MI:SS'));

INSERT INTO RESERVATION (Reservation_ID, Customer_ID, Booking_Time, Slot_Time)
VALUES (802, 402, 
        TO_DATE('2024-03-01 10:30:00', 'YYYY-MM-DD HH24:MI:SS'), 
        TO_DATE('2024-03-01 11:30:00', 'YYYY-MM-DD HH24:MI:SS'));

INSERT INTO RESERVATION (Reservation_ID, Customer_ID, Booking_Time, Slot_Time)
VALUES (803, 403, 
        TO_DATE('2024-03-02 12:00:00', 'YYYY-MM-DD HH24:MI:SS'), 
        TO_DATE('2024-03-02 14:00:00', 'YYYY-MM-DD HH24:MI:SS'));

INSERT INTO RESERVATION (Reservation_ID, Customer_ID, Booking_Time, Slot_Time)
VALUES (804, 404, 
        TO_DATE('2024-03-02 15:00:00', 'YYYY-MM-DD HH24:MI:SS'), 
        TO_DATE('2024-03-02 16:00:00', 'YYYY-MM-DD HH24:MI:SS'));

INSERT INTO RESERVATION (Reservation_ID, Customer_ID, Booking_Time, Slot_Time)
VALUES (805, 405, 
        TO_DATE('2024-03-03 09:00:00', 'YYYY-MM-DD HH24:MI:SS'), 
        TO_DATE('2024-03-03 10:00:00', 'YYYY-MM-DD HH24:MI:SS'));

INSERT INTO RESERVATION (Reservation_ID, Customer_ID, Booking_Time, Slot_Time)
VALUES (806, 406, 
        TO_DATE('2024-03-03 11:30:00', 'YYYY-MM-DD HH24:MI:SS'), 
        TO_DATE('2024-03-03 13:00:00', 'YYYY-MM-DD HH24:MI:SS'));

-- 12. INSERT INTO RESERVED_FOR
INSERT INTO RESERVED_FOR (Reservation_ID, Point_ID) VALUES (801, 301);
INSERT INTO RESERVED_FOR (Reservation_ID, Point_ID) VALUES (802, 303);
INSERT INTO RESERVED_FOR (Reservation_ID, Point_ID) VALUES (803, 304);
INSERT INTO RESERVED_FOR (Reservation_ID, Point_ID) VALUES (804, 302);
INSERT INTO RESERVED_FOR (Reservation_ID, Point_ID) VALUES (805, 307);
INSERT INTO RESERVED_FOR (Reservation_ID, Point_ID) VALUES (806, 301);

-- 13. INSERT INTO MAINTENANCE
INSERT INTO MAINTENANCE (Point_ID, Report_No, Issue_Description, Date_Reported, Status)
VALUES (305, 1, 'Gun locking mechanism stuck', TO_DATE('2024-02-25', 'YYYY-MM-DD'), 'Pending');

INSERT INTO MAINTENANCE (Point_ID, Report_No, Issue_Description, Date_Reported, Status)
VALUES (305, 2, 'Display screen flickering', TO_DATE('2024-03-01', 'YYYY-MM-DD'), 'In-Progress');

INSERT INTO MAINTENANCE (Point_ID, Report_No, Issue_Description, Date_Reported, Status)
VALUES (301, 1, 'Routine quarterly inspection', TO_DATE('2024-01-10', 'YYYY-MM-DD'), 'Completed');

INSERT INTO MAINTENANCE (Point_ID, Report_No, Issue_Description, Date_Reported, Status)
VALUES (302, 1, 'Firmware update required', TO_DATE('2024-01-15', 'YYYY-MM-DD'), 'Completed');

INSERT INTO MAINTENANCE (Point_ID, Report_No, Issue_Description, Date_Reported, Status)
VALUES (304, 1, 'Cooling fan noisy during DC fast charge', TO_DATE('2024-02-18', 'YYYY-MM-DD'), 'Completed');

INSERT INTO MAINTENANCE (Point_ID, Report_No, Issue_Description, Date_Reported, Status)
VALUES (307, 1, 'Emergency stop button test', TO_DATE('2024-02-20', 'YYYY-MM-DD'), 'Completed');

-- Commit all initial records
COMMIT;

-- ====================================================================
-- INTERACTIVE INSERT SECTION (Cycle Sheet I lab requirement)
-- Note: In SQL*Plus, uncomment and run these to insert interactively
-- using substitution variables (& variable_name).
-- ====================================================================

-- Interactive Insert for CUSTOMER:
-- INSERT INTO CUSTOMER (Customer_ID, First_Name, Last_Name, Email, Registration_Date)
-- VALUES (&Customer_ID, '&First_Name', '&Last_Name', '&Email', TO_DATE('&Registration_Date', 'YYYY-MM-DD'));

-- Interactive Insert for CHARGING_POINT:
-- INSERT INTO CHARGING_POINT (Point_ID, Station_ID, Connector_Type, Charging_Mode, Status, Power_Rating)
-- VALUES (&Point_ID, &Station_ID, '&Connector_Type', '&Charging_Mode', '&Status', &Power_Rating);
