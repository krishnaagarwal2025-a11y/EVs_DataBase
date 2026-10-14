-- ====================================================================
-- Voltgrid: Smart EV Charging Station Network
-- File: cycle4_joins_views.sql
-- Description: Cycle Sheet IV - Joins, Subqueries, Correlated Subqueries & Views
-- Lab Mapping:
-- BORROWER -> CUSTOMER | LIBRARY_BRANCH -> STATION | PUBLISHER -> OPERATOR
-- BOOK_LOANS -> CHARGING_SESSION | BOOK_COPIES -> CHARGING_POINT | BOOK -> VEHICLE
-- ====================================================================

SET LINESIZE 200
SET PAGESIZE 50
SPOOL cycle4_output.txt

-- ====================================================================
-- 1. INNER JOINS (5 Queries)
-- ====================================================================

-- 1.1 Join CUSTOMER and VEHICLE: Display customer details along with registered vehicle
SELECT c.Customer_ID, c.First_Name, c.Last_Name, v.Vehicle_No, v.Model 
FROM CUSTOMER c
INNER JOIN VEHICLE v ON c.Customer_ID = v.Customer_ID;

-- 1.2 Join STATION and OPERATOR: Display station location along with operator network name
SELECT s.Station_ID, s.Street, o.Name AS Operator_Name, o.License_No, s.Tariff_Rate 
FROM STATION s
INNER JOIN OPERATOR o ON s.Operator_ID = o.Operator_ID;

-- 1.3 Join STATION, PINCODE_AREA, and CHARGING_POINT: Display points installed in each city
SELECT s.Station_ID, p.City, cp.Point_ID, cp.Connector_Type, cp.Power_Rating 
FROM STATION s
INNER JOIN PINCODE_AREA p ON s.Pincode = p.Pincode
INNER JOIN CHARGING_POINT cp ON s.Station_ID = cp.Station_ID;

-- 1.4 Join CHARGING_SESSION, VEHICLE, and CUSTOMER: Display session logs with customer identity
SELECT cs.Session_ID, c.First_Name || ' ' || c.Last_Name AS Customer_Name, 
       cs.Vehicle_No, cs.Units_Consumed, cs.Start_Time 
FROM CHARGING_SESSION cs
INNER JOIN VEHICLE v ON cs.Vehicle_No = v.Vehicle_No
INNER JOIN CUSTOMER c ON v.Customer_ID = c.Customer_ID;

-- 1.5 Join CHARGING_SESSION, PAYMENT, and CHARGING_POINT: Display payment receipts with charger specs
SELECT cs.Session_ID, cp.Charging_Mode, cs.Units_Consumed, 
       pay.Payment_Mode, pay.Payment_Date 
FROM CHARGING_SESSION cs
INNER JOIN PAYMENT pay ON cs.Session_ID = pay.Session_ID
INNER JOIN CHARGING_POINT cp ON cs.Point_ID = cp.Point_ID;


-- ====================================================================
-- 2. OUTER JOINS (4 Queries: Left, Right, Full)
-- ====================================================================

-- 2.1 LEFT OUTER JOIN: List all customers and their vehicles (including customers without a vehicle)
SELECT c.Customer_ID, c.First_Name, c.Last_Name, v.Vehicle_No, v.Model 
FROM CUSTOMER c
LEFT OUTER JOIN VEHICLE v ON c.Customer_ID = v.Customer_ID;

-- 2.2 LEFT OUTER JOIN: List all charging points and their maintenance records (including points never repaired)
SELECT cp.Point_ID, cp.Connector_Type, cp.Status, m.Report_No, m.Issue_Description 
FROM CHARGING_POINT cp
LEFT OUTER JOIN MAINTENANCE m ON cp.Point_ID = m.Point_ID;

-- 2.3 RIGHT OUTER JOIN: List all stations and operators (ensuring all stations are listed)
SELECT o.Operator_ID, o.Name, s.Station_ID, s.Street 
FROM OPERATOR o
RIGHT OUTER JOIN STATION s ON o.Operator_ID = s.Operator_ID;

-- 2.4 FULL OUTER JOIN: Match reservations and charging points (showing unmatched from both sides)
SELECT r.Reservation_ID, r.Customer_ID, cp.Point_ID, cp.Status 
FROM RESERVATION r
FULL OUTER JOIN RESERVED_FOR rf ON r.Reservation_ID = rf.Reservation_ID
FULL OUTER JOIN CHARGING_POINT cp ON rf.Point_ID = cp.Point_ID;


-- ====================================================================
-- 3. SUBQUERIES
-- ====================================================================

-- 3.1 Scalar Subquery in WHERE clause: Find charging points with power rating higher than network average
SELECT Point_ID, Connector_Type, Power_Rating 
FROM CHARGING_POINT 
WHERE Power_Rating > (SELECT AVG(Power_Rating) FROM CHARGING_POINT);

-- 3.2 Subquery with IN: Find customers who have made at least one reservation
SELECT Customer_ID, First_Name, Email 
FROM CUSTOMER 
WHERE Customer_ID IN (SELECT DISTINCT Customer_ID FROM RESERVATION);

-- 3.3 Inline View (Subquery in FROM): Station summary with total active points
SELECT st.Station_ID, st.Street, summary.Total_Points 
FROM STATION st,
     (SELECT Station_ID, COUNT(*) AS Total_Points FROM CHARGING_POINT GROUP BY Station_ID) summary
WHERE st.Station_ID = summary.Station_ID;


-- ====================================================================
-- 4. CORRELATED SUBQUERIES (EXISTS / NOT EXISTS - 3 Queries)
-- ====================================================================

-- 4.1 Correlated Subquery with EXISTS: Find customers who have executed at least one charging session
SELECT c.Customer_ID, c.First_Name, c.Email 
FROM CUSTOMER c
WHERE EXISTS (
    SELECT 1 
    FROM VEHICLE v, CHARGING_SESSION cs 
    WHERE v.Customer_ID = c.Customer_ID 
      AND v.Vehicle_No = cs.Vehicle_No
);

-- 4.2 Correlated Subquery with NOT EXISTS: Find charging points that have NEVER experienced any maintenance issues
SELECT cp.Point_ID, cp.Station_ID, cp.Connector_Type 
FROM CHARGING_POINT cp
WHERE NOT EXISTS (
    SELECT 1 
    FROM MAINTENANCE m 
    WHERE m.Point_ID = cp.Point_ID
);

-- 4.3 Correlated Subquery with EXISTS: Find stations that house at least one high-power DC fast charger (> 50 kW)
SELECT s.Station_ID, s.Street, s.Tariff_Rate 
FROM STATION s
WHERE EXISTS (
    SELECT 1 
    FROM CHARGING_POINT cp 
    WHERE cp.Station_ID = s.Station_ID 
      AND cp.Charging_Mode = 'DC' 
      AND cp.Power_Rating > 50.00
);


-- ====================================================================
-- 5. ANY / ALL OPERATORS
-- ====================================================================

-- 5.1 ANY: Find stations whose tariff rate is greater than ANY tariff rate of stations run by Operator 101
SELECT Station_ID, Operator_ID, Street, Tariff_Rate 
FROM STATION 
WHERE Tariff_Rate > ANY (
    SELECT Tariff_Rate 
    FROM STATION 
    WHERE Operator_ID = 101
);

-- 5.2 ALL: Find DC charging points whose power rating is greater than ALL AC charging points
SELECT Point_ID, Connector_Type, Charging_Mode, Power_Rating 
FROM CHARGING_POINT 
WHERE Charging_Mode = 'DC' 
  AND Power_Rating > ALL (
    SELECT Power_Rating 
    FROM CHARGING_POINT 
    WHERE Charging_Mode = 'AC'
);


-- ====================================================================
-- 6. VIEWS & VIEW UPDATABILITY TEST
-- ====================================================================

-- Drop view if already exists
DROP VIEW vw_available_chargers;

-- 6.1 Create a simple view selecting available charging points
CREATE VIEW vw_available_chargers AS 
SELECT Point_ID, Station_ID, Connector_Type, Charging_Mode, Status, Power_Rating 
FROM CHARGING_POINT 
WHERE Status = 'Available';

-- Query the view
SELECT * FROM vw_available_chargers;

-- 6.2 Check updatability of columns in the view
SELECT column_name, updatable, insertable, deletable 
FROM user_updatable_columns 
WHERE table_name = 'VW_AVAILABLE_CHARGERS';

-- 6.3 Test DML Update through the view: Modify power rating of Point 301
UPDATE vw_available_chargers 
SET Power_Rating = 62.00 
WHERE Point_ID = 301;

-- Verify update reflected in base table CHARGING_POINT
SELECT Point_ID, Power_Rating, Status FROM CHARGING_POINT WHERE Point_ID = 301;

-- Restore original power rating
UPDATE vw_available_chargers 
SET Power_Rating = 60.00 
WHERE Point_ID = 301;

COMMIT;

SPOOL OFF
