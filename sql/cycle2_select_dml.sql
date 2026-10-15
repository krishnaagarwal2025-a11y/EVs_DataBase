SET LINESIZE 200
SET PAGESIZE 50
SPOOL cycle2_output.txt

-- 1. COLUMN ALIASES
-- 1.1 Display customer names and contact email with friendly column headings
SELECT Customer_ID AS "Cust Code", First_Name || ' ' || Last_Name AS "Full Name", Email AS "Email Address" 
FROM CUSTOMER;

-- 1.2 Display station location and rate with renamed columns
SELECT Station_ID AS "Station ID", Street AS "Location", Tariff_Rate AS "Rate Per Unit (INR)" 
FROM STATION;

-- 1.3 Display charging point technical specifications with aliases
SELECT Point_ID AS "Gun ID", Connector_Type AS "Plug Type", Power_Rating AS "Capacity (kW)" 
FROM CHARGING_POINT;


-- 2. DISTINCT CLAUSE
-- 2.1 Display distinct charging modes configured across network
SELECT DISTINCT Charging_Mode FROM CHARGING_POINT;

-- 2.2 Display distinct connector types supported
SELECT DISTINCT Connector_Type FROM CHARGING_POINT;

-- 2.3 Display distinct cities where charging stations are active
SELECT DISTINCT p.City 
FROM PINCODE_AREA p, STATION s 
WHERE p.Pincode = s.Pincode;


-- 3. COMPUTED COLUMNS
-- 3.1 Calculate estimated charging cost for each session based on station tariff rate
SELECT cs.Session_ID, cs.Units_Consumed, st.Tariff_Rate, 
       (cs.Units_Consumed * st.Tariff_Rate) AS Total_Cost_INR
FROM CHARGING_SESSION cs, CHARGING_POINT cp, STATION st
WHERE cs.Point_ID = cp.Point_ID AND cp.Station_ID = st.Station_ID;

-- 3.2 Calculate session charging duration in hours
SELECT Session_ID, Vehicle_No, 
       ROUND((End_Time - Start_Time) * 24, 2) AS Duration_Hours
FROM CHARGING_SESSION;

-- 3.3 Compute battery capacity difference compared to a 100 kWh baseline
SELECT Model, Battery_Capacity, (100.00 - Battery_Capacity) AS "Shortfall From 100kWh" 
FROM VEHICLE_MODEL;


-- 4. FILTERING: LIKE, BETWEEN, IN, NOT, AND/OR
-- 4.1 LIKE: Find customers whose first name begins with letter 'A'
SELECT Customer_ID, First_Name, Email FROM CUSTOMER WHERE First_Name LIKE 'A%';

-- 4.2 BETWEEN: Find charging points with power rating between 20kW and 60kW
SELECT Point_ID, Connector_Type, Power_Rating FROM CHARGING_POINT WHERE Power_Rating BETWEEN 20.00 AND 60.00;

-- 4.3 IN: Find stations located in Vellore (632014) or Bengaluru (560001)
SELECT Station_ID, Street, Pincode FROM STATION WHERE Pincode IN (632014, 560001);

-- 4.4 NOT and AND/OR: Find active DC chargers that are NOT faulty
SELECT Point_ID, Station_ID, Charging_Mode, Status 
FROM CHARGING_POINT 
WHERE Charging_Mode = 'DC' AND NOT (Status = 'Faulty');



-- 5. IS NULL / IS NOT NULL

-- 5.1 Find completed charging sessions where End_Time is recorded (NOT NULL)
SELECT Session_ID, Vehicle_No, Start_Time, End_Time FROM CHARGING_SESSION WHERE End_Time IS NOT NULL;

-- 5.2 Find customers where Last_Name is provided
SELECT Customer_ID, First_Name, Last_Name FROM CUSTOMER WHERE Last_Name IS NOT NULL;

-- 5.3 Find operators with non-null contact info
SELECT Operator_ID, Name, Contact_Info FROM OPERATOR WHERE Contact_Info IS NOT NULL;


-- 6. MULTI-COLUMN UPDATE

-- 6.1 Update both Status and Power_Rating of a charging point in a single statement
UPDATE CHARGING_POINT 
SET Status = 'Available', Power_Rating = 65.00 
WHERE Point_ID = 301;

-- Verify update
SELECT Point_ID, Status, Power_Rating FROM CHARGING_POINT WHERE Point_ID = 301;

-- 6.2 Update Contact_No and Tariff_Rate of station 201
UPDATE STATION 
SET Contact_No = '9444012345', Tariff_Rate = 19.00 
WHERE Station_ID = 201;

-- Verify update
SELECT Station_ID, Contact_No, Tariff_Rate FROM STATION WHERE Station_ID = 201;


-- 7. DELETE WITHOUT BREAKING FOREIGN KEYS

-- 7.1 Insert a temporary test vehicle model and delete it safely
INSERT INTO VEHICLE_MODEL (Model, Battery_Capacity) VALUES ('Temp Test Car', 45.00);
SELECT * FROM VEHICLE_MODEL WHERE Model = 'Temp Test Car';

-- Delete the record (safe because no child row in VEHICLE references it)
DELETE FROM VEHICLE_MODEL WHERE Model = 'Temp Test Car';
SELECT * FROM VEHICLE_MODEL WHERE Model = 'Temp Test Car';

-- 7.2 Insert a standalone customer with no dependent vehicles/reservations and delete
INSERT INTO CUSTOMER (Customer_ID, First_Name, Last_Name, Email, Registration_Date)
VALUES (999, 'Temp', 'User', 'temp.user@test.com', TO_DATE('2024-01-01', 'YYYY-MM-DD'));

DELETE FROM CUSTOMER WHERE Customer_ID = 999;


-- 8. ORDER BY (ASC, DESC, MULTIPLE COLUMNS)

-- 8.1 Display stations sorted by tariff rate in ascending order
SELECT Station_ID, Street, Tariff_Rate FROM STATION ORDER BY Tariff_Rate ASC;

-- 8.2 Display charging sessions sorted by electricity consumed in descending order
SELECT Session_ID, Vehicle_No, Units_Consumed FROM CHARGING_SESSION ORDER BY Units_Consumed DESC;

-- 8.3 Display charging points ordered by Station_ID ascending, then Power_Rating descending
SELECT Point_ID, Station_ID, Connector_Type, Power_Rating 
FROM CHARGING_POINT 
ORDER BY Station_ID ASC, Power_Rating DESC;


-- 9. BUILT-IN ENVIRONMENT & DATE/TIME: SYSDATE, SYSTIMESTAMP, USER

-- 9.1 Display current database server system date
SELECT SYSDATE AS "Current Server Date" FROM DUAL;

-- 9.2 Display high precision current system timestamp
SELECT SYSTIMESTAMP AS "Current Server Timestamp" FROM DUAL;

-- 9.3 Display currently logged in Oracle database user
SELECT USER AS "Active Database User" FROM DUAL;

-- 9.4 List charging sessions recorded on or before the current date
SELECT Session_ID, Vehicle_No, Start_Time FROM CHARGING_SESSION WHERE Start_Time <= SYSDATE;


-- 10. TRANSACTION CONTROL: SAVEPOINT, ROLLBACK TO SAVEPOINT, COMMIT

-- 10.1 First update in transaction
UPDATE STATION SET Tariff_Rate = 25.00 WHERE Station_ID = 202;

-- 10.2 Create a savepoint
SAVEPOINT sp_tariff_update;

-- 10.3 Second update in transaction
UPDATE CHARGING_POINT SET Status = 'Faulty' WHERE Point_ID = 302;

-- 10.4 Verify intermediate state before rollback
SELECT Point_ID, Status FROM CHARGING_POINT WHERE Point_ID = 302;

-- 10.5 Roll back changes made after savepoint
ROLLBACK TO SAVEPOINT sp_tariff_update;

-- 10.6 Verify Point 302 has returned to previous status ('Available')
SELECT Point_ID, Status FROM CHARGING_POINT WHERE Point_ID = 302;

-- 10.7 Verify Station 202 still retains the updated tariff rate of 25.00
SELECT Station_ID, Tariff_Rate FROM STATION WHERE Station_ID = 202;

-- 10.8 Commit the verified changes permanently
COMMIT;

SPOOL OFF
