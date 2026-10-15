SET LINESIZE 200
SET PAGESIZE 50
SPOOL cycle3_output.txt


-- 1.1 Modify column width of Last_Name in CUSTOMER to 40 characters
ALTER TABLE CUSTOMER MODIFY (Last_Name VARCHAR2(40));

-- 1.2 Add a new column of type TIMESTAMP to CHARGING_SESSION table
ALTER TABLE CHARGING_SESSION ADD (Session_Logged_At TIMESTAMP);

-- 1.3 Populate the new timestamp column with current system timestamp
UPDATE CHARGING_SESSION SET Session_Logged_At = SYSTIMESTAMP;

-- Verify the added timestamp column
SELECT Session_ID, Vehicle_No, Session_Logged_At FROM CHARGING_SESSION;

-- 1.4 Drop the newly added column from CHARGING_SESSION
ALTER TABLE CHARGING_SESSION DROP COLUMN Session_Logged_At;


-- 2.1 UPPER and LOWER: Display customer first name in uppercase and email in lowercase
SELECT First_Name, UPPER(First_Name) AS Cap_Name, LOWER(Email) AS Small_Email FROM CUSTOMER;

-- 2.2 INITCAP: Format vehicle model name into title case
SELECT Model, INITCAP(Model) AS Title_Case_Model FROM VEHICLE_MODEL;

-- 2.3 SUBSTR: Extract the state code (first 2 characters) from vehicle number
SELECT Vehicle_No, SUBSTR(Vehicle_No, 1, 2) AS State_Code FROM VEHICLE;

-- 2.4 LENGTH: Count number of characters in customer email addresses
SELECT Email, LENGTH(Email) AS Email_Length FROM CUSTOMER;

-- 2.5 REPLACE: Replace hyphen with space in vehicle registration number
SELECT Vehicle_No, REPLACE(Vehicle_No, '-', ' ') AS Formatted_Plate FROM VEHICLE;

-- 2.6 TRIM: Strip leading and trailing whitespace from string
SELECT TRIM('   Voltgrid Network   ') AS Trimmed_String FROM DUAL;

-- 2.7 String Concatenation Operator (||): Combine customer first and last names
SELECT Customer_ID, First_Name || ' ' || Last_Name AS Full_Name FROM CUSTOMER;



-- 3.1 SQRT: Calculate square root of power rating
SELECT Point_ID, Power_Rating, ROUND(SQRT(Power_Rating), 2) AS Sqrt_Power FROM CHARGING_POINT;

-- 3.2 ABS: Find absolute difference between station tariff rate and standard rate of 20.00
SELECT Station_ID, Tariff_Rate, ABS(Tariff_Rate - 20.00) AS Deviation_From_Standard FROM STATION;

-- 3.3 ROUND and TRUNC: Round and truncate consumed units to 1 decimal place and whole number
SELECT Session_ID, Units_Consumed, 
       ROUND(Units_Consumed, 1) AS Rounded_Units, 
       TRUNC(Units_Consumed, 0) AS Truncated_Units 
FROM CHARGING_SESSION;

-- 3.4 MOD: Determine whether Point_ID is even or odd using modulo 2
SELECT Point_ID, MOD(Point_ID, 2) AS Remainder_Mod_2 FROM CHARGING_POINT;

-- 3.5 CEIL and FLOOR: Calculate ceiling and floor values of station tariff rate
SELECT Station_ID, Tariff_Rate, CEIL(Tariff_Rate) AS Ceil_Rate, FLOOR(Tariff_Rate) AS Floor_Rate FROM STATION;


-- 4.1 TO_CHAR: Format session start date and time into readable 12-hour format
SELECT Session_ID, TO_CHAR(Start_Time, 'DD-MON-YYYY HH12:MI:SS AM') AS Formatted_Start FROM CHARGING_SESSION;

-- 4.2 ADD_MONTHS: Calculate customer membership renewal date (6 months after registration)
SELECT Customer_ID, Registration_Date, ADD_MONTHS(Registration_Date, 6) AS Renewal_Date FROM CUSTOMER;

-- 4.3 MONTHS_BETWEEN: Calculate months elapsed since customer registration
SELECT Customer_ID, ROUND(MONTHS_BETWEEN(SYSDATE, Registration_Date), 1) AS Months_Active FROM CUSTOMER;

-- 4.4 NEXT_DAY: Find the date of the next Sunday following the registration date
SELECT Customer_ID, Registration_Date, NEXT_DAY(Registration_Date, 'SUNDAY') AS Next_Sunday FROM CUSTOMER;

-- 4.5 LAST_DAY: Find the last calendar day of the registration month
SELECT Customer_ID, Registration_Date, LAST_DAY(Registration_Date) AS Month_End_Date FROM CUSTOMER;

-- 4.6 EXTRACT: Extract year and month fields from session start date
SELECT Session_ID, EXTRACT(YEAR FROM Start_Time) AS S_Year, EXTRACT(MONTH FROM Start_Time) AS S_Month 
FROM CHARGING_SESSION;

-- 4.7 ROUND and TRUNC on Dates: Round and truncate registration date to the first of the month
SELECT Customer_ID, Registration_Date, 
       ROUND(Registration_Date, 'MONTH') AS Rounded_Month, 
       TRUNC(Registration_Date, 'MONTH') AS Truncated_Month 
FROM CUSTOMER;


-- 5.1 TO_DATE: Convert character string to Oracle date format
SELECT TO_DATE('15-AUG-2024 10:30:00', 'DD-MON-YYYY HH24:MI:SS') AS Converted_Date FROM DUAL;

-- 5.2 TO_NUMBER: Convert string to numeric value and perform arithmetic operation
SELECT TO_NUMBER('150.75') + 10.25 AS Total_Calculated FROM DUAL;



-- 6.1 NVL: Replace NULL last name with 'Not Specified'
SELECT Customer_ID, First_Name, NVL(Last_Name, 'Not Specified') AS Checked_Last_Name FROM CUSTOMER;

-- 6.2 NVL2: Return message depending on whether Contact_Info is present or not
SELECT Operator_ID, Name, NVL2(Contact_Info, 'Contact Available', 'No Contact') AS Status_Contact FROM OPERATOR;

-- 6.3 COALESCE: Pick first non-null value among columns and fallback default
SELECT Customer_ID, COALESCE(Last_Name, First_Name, 'Unknown') AS Customer_Label FROM CUSTOMER;

-- 6.4 NULLIF: Return NULL if Connector_Type equals 'CCS2'
SELECT Point_ID, Connector_Type, NULLIF(Connector_Type, 'CCS2') AS Nullif_Result FROM CHARGING_POINT;


-- 7.1 COUNT: Find total number of registered customers and total charging points
SELECT COUNT(*) AS Total_Customers FROM CUSTOMER;
SELECT COUNT(Point_ID) AS Total_Charging_Points FROM CHARGING_POINT;

-- 7.2 SUM: Calculate grand total electricity units consumed across all sessions
SELECT SUM(Units_Consumed) AS Total_Units_Dispatched FROM CHARGING_SESSION;

-- 7.3 AVG: Calculate average tariff rate across all charging stations
SELECT ROUND(AVG(Tariff_Rate), 2) AS Average_Tariff FROM STATION;

-- 7.4 MAX and MIN: Find highest and lowest battery capacities among EV models
SELECT MAX(Battery_Capacity) AS Max_Battery_kWh, MIN(Battery_Capacity) AS Min_Battery_kWh FROM VEHICLE_MODEL;


-- 8.1 Count total charging points categorized by charging mode (AC vs DC)
SELECT Charging_Mode, COUNT(*) AS Total_Points 
FROM CHARGING_POINT 
GROUP BY Charging_Mode;

-- 8.2 Calculate average power rating for each connector type
SELECT Connector_Type, ROUND(AVG(Power_Rating), 2) AS Avg_Power_kW 
FROM CHARGING_POINT 
GROUP BY Connector_Type;

-- 8.3 Calculate total energy units consumed by each vehicle
SELECT Vehicle_No, SUM(Units_Consumed) AS Total_Units_Charged 
FROM CHARGING_SESSION 
GROUP BY Vehicle_No;


-- 9.1 Filter charging mode groups having more than 2 charging points installed
SELECT Charging_Mode, COUNT(*) AS Point_Count 
FROM CHARGING_POINT 
GROUP BY Charging_Mode 
HAVING COUNT(*) > 2;

-- 9.2 Find stations that have average power rating exceeding 40 kW
SELECT Station_ID, ROUND(AVG(Power_Rating), 2) AS Avg_Station_Power 
FROM CHARGING_POINT 
GROUP BY Station_ID 
HAVING AVG(Power_Rating) > 40.00;

-- 9.3 Find vehicles that have consumed more than 30 total units across all sessions
SELECT Vehicle_No, SUM(Units_Consumed) AS Total_Units 
FROM CHARGING_SESSION 
GROUP BY Vehicle_No 
HAVING SUM(Units_Consumed) > 30.00;

SPOOL OFF
