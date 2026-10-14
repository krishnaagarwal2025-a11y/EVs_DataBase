-- ====================================================================
-- Voltgrid: Smart EV Charging Station Network
-- File: cycle5_plsql.sql
-- Description: Cycle Sheet V - PL/SQL Programming
-- Concepts: SELECT INTO, IF-ELSIF-ELSE, CASE, Loops (Palindrome),
--           Cursors (Explicit & Parameterized), Procedures & Functions
-- ====================================================================

SET LINESIZE 200
SET PAGESIZE 50
SET SERVEROUTPUT ON
SPOOL cycle5_output.txt

-- ====================================================================
-- 1. BASIC PL/SQL BLOCK WITH SELECT INTO
-- ====================================================================
-- Fetch customer details into local PL/SQL variables and print them
DECLARE
    v_cust_id   CUSTOMER.Customer_ID%TYPE := 401;
    v_fname     CUSTOMER.First_Name%TYPE;
    v_email     CUSTOMER.Email%TYPE;
    v_reg_date  CUSTOMER.Registration_Date%TYPE;
BEGIN
    SELECT First_Name, Email, Registration_Date 
    INTO v_fname, v_email, v_reg_date
    FROM CUSTOMER
    WHERE Customer_ID = v_cust_id;

    DBMS_OUTPUT.PUT_LINE('--- Customer Profile ---');
    DBMS_OUTPUT.PUT_LINE('Customer ID : ' || v_cust_id);
    DBMS_OUTPUT.PUT_LINE('Name        : ' || v_fname);
    DBMS_OUTPUT.PUT_LINE('Email       : ' || v_email);
    DBMS_OUTPUT.PUT_LINE('Joined On   : ' || TO_CHAR(v_reg_date, 'DD-MON-YYYY'));
END;
/


-- ====================================================================
-- 2. CONDITIONAL CONTROL: IF - ELSIF - ELSE
-- ====================================================================
-- Classify station based on its per-unit electricity tariff rate
DECLARE
    v_station_id STATION.Station_ID%TYPE := 201;
    v_rate       STATION.Tariff_Rate%TYPE;
    v_category   VARCHAR2(30);
BEGIN
    SELECT Tariff_Rate INTO v_rate 
    FROM STATION 
    WHERE Station_ID = v_station_id;

    IF v_rate < 18.00 THEN
        v_category := 'Economy / Subsidized Station';
    ELSIF v_rate BETWEEN 18.00 AND 21.00 THEN
        v_category := 'Standard Commercial Station';
    ELSE
        v_category := 'Premium High-Speed Station';
    END IF;

    DBMS_OUTPUT.PUT_LINE('Station ID: ' || v_station_id || ' | Rate: Rs.' || v_rate || ' | Category: ' || v_category);
END;
/


-- ====================================================================
-- 3. CONDITIONAL CONTROL: CASE STATEMENT
-- ====================================================================
-- Determine charging speed category using CASE statement
DECLARE
    v_point_id CHARGING_POINT.Point_ID%TYPE := 301;
    v_mode     CHARGING_POINT.Charging_Mode%TYPE;
    v_speed    VARCHAR2(40);
BEGIN
    SELECT Charging_Mode INTO v_mode 
    FROM CHARGING_POINT 
    WHERE Point_ID = v_point_id;

    CASE v_mode
        WHEN 'AC' THEN 
            v_speed := 'Slow / Regular AC Charger';
        WHEN 'DC' THEN 
            v_speed := 'Fast / Rapid DC Charger';
        ELSE 
            v_speed := 'Custom Specification';
    END CASE;

    DBMS_OUTPUT.PUT_LINE('Point ID: ' || v_point_id || ' | Mode: ' || v_mode || ' | Classification: ' || v_speed);
END;
/


-- ====================================================================
-- 4. LOOP STRUCTURE: PALINDROME CHECK
-- ====================================================================
-- Check whether a city name or string is a palindrome using a loop
DECLARE
    v_original VARCHAR2(50);
    v_reversed VARCHAR2(50) := '';
    v_len      NUMBER;
    i          NUMBER;
BEGIN
    -- Test with a sample text string (e.g. 'MADAM' or a city)
    v_original := 'MADAM';
    v_len := LENGTH(v_original);

    -- Loop backwards to construct reversed string
    FOR i IN REVERSE 1..v_len LOOP
        v_reversed := v_reversed || SUBSTR(v_original, i, 1);
    END LOOP;

    DBMS_OUTPUT.PUT_LINE('Original String: ' || v_original);
    DBMS_OUTPUT.PUT_LINE('Reversed String: ' || v_reversed);

    IF UPPER(v_original) = UPPER(v_reversed) THEN
        DBMS_OUTPUT.PUT_LINE('Result: The string IS a palindrome.');
    ELSE
        DBMS_OUTPUT.PUT_LINE('Result: The string is NOT a palindrome.');
    END IF;
END;
/


-- ====================================================================
-- 5. EXPLICIT CURSOR
-- ====================================================================
-- Iterate through all stations using an explicit cursor
DECLARE
    CURSOR cur_stations IS 
        SELECT Station_ID, Street, Tariff_Rate 
        FROM STATION 
        ORDER BY Station_ID;

    v_sid    STATION.Station_ID%TYPE;
    v_street STATION.Street%TYPE;
    v_tariff STATION.Tariff_Rate%TYPE;
BEGIN
    DBMS_OUTPUT.PUT_LINE('--- Stations List (Explicit Cursor) ---');
    OPEN cur_stations;
    LOOP
        FETCH cur_stations INTO v_sid, v_street, v_tariff;
        EXIT WHEN cur_stations%NOTFOUND;
        DBMS_OUTPUT.PUT_LINE('Station ' || v_sid || ' on ' || v_street || ' charges Rs.' || v_tariff || '/unit');
    END LOOP;
    CLOSE cur_stations;
END;
/


-- ====================================================================
-- 6. PARAMETERIZED CURSOR
-- ====================================================================
-- Fetch all charging points belonging to a specific station ID passed as argument
DECLARE
    CURSOR cur_points_by_station (p_sid NUMBER) IS
        SELECT Point_ID, Connector_Type, Charging_Mode, Power_Rating, Status
        FROM CHARGING_POINT
        WHERE Station_ID = p_sid;

    v_rec cur_points_by_station%ROWTYPE;
    v_target_station NUMBER := 201;
BEGIN
    DBMS_OUTPUT.PUT_LINE('--- Charging Points for Station ' || v_target_station || ' ---');
    OPEN cur_points_by_station(v_target_station);
    LOOP
        FETCH cur_points_by_station INTO v_rec;
        EXIT WHEN cur_points_by_station%NOTFOUND;
        DBMS_OUTPUT.PUT_LINE('Gun ' || v_rec.Point_ID || ' | Type: ' || v_rec.Connector_Type || 
                             ' | Mode: ' || v_rec.Charging_Mode || ' | Power: ' || v_rec.Power_Rating || 
                             'kW | Status: ' || v_rec.Status);
    END LOOP;
    CLOSE cur_points_by_station;
END;
/


-- ====================================================================
-- 7. STORED PROCEDURES (2 Procedures)
-- ====================================================================

-- Procedure 1: Fetch customer information by ID using OUT parameters
CREATE OR REPLACE PROCEDURE proc_get_customer_info (
    p_cust_id   IN  NUMBER,
    p_fullname  OUT VARCHAR2,
    p_email     OUT VARCHAR2
) AS
BEGIN
    SELECT First_Name || ' ' || Last_Name, Email
    INTO p_fullname, p_email
    FROM CUSTOMER
    WHERE Customer_ID = p_cust_id;
EXCEPTION
    WHEN NO_DATA_FOUND THEN
        p_fullname := 'Not Found';
        p_email := 'Not Found';
END;
/

-- Procedure 2: Update tariff rate of a station
CREATE OR REPLACE PROCEDURE proc_update_tariff (
    p_station_id IN NUMBER,
    p_new_tariff IN NUMBER
) AS
BEGIN
    UPDATE STATION 
    SET Tariff_Rate = p_new_tariff 
    WHERE Station_ID = p_station_id;

    COMMIT;
    DBMS_OUTPUT.PUT_LINE('Tariff for Station ' || p_station_id || ' updated to Rs.' || p_new_tariff);
END;
/


-- ====================================================================
-- 8. STORED FUNCTIONS (2 Functions)
-- ====================================================================

-- Function 1: Calculate total billing amount for a given session ID
CREATE OR REPLACE FUNCTION fn_calculate_bill (
    p_session_id IN NUMBER
) RETURN NUMBER AS
    v_units  NUMBER;
    v_tariff NUMBER;
    v_total  NUMBER := 0;
BEGIN
    SELECT cs.Units_Consumed, st.Tariff_Rate
    INTO v_units, v_tariff
    FROM CHARGING_SESSION cs, CHARGING_POINT cp, STATION st
    WHERE cs.Point_ID = cp.Point_ID 
      AND cp.Station_ID = st.Station_ID
      AND cs.Session_ID = p_session_id;

    v_total := ROUND(v_units * v_tariff, 2);
    RETURN v_total;
EXCEPTION
    WHEN NO_DATA_FOUND THEN
        RETURN 0;
END;
/

-- Function 2: Count number of charging points at a given station
CREATE OR REPLACE FUNCTION fn_get_total_points (
    p_station_id IN NUMBER
) RETURN NUMBER AS
    v_count NUMBER := 0;
BEGIN
    SELECT COUNT(*) 
    INTO v_count 
    FROM CHARGING_POINT 
    WHERE Station_ID = p_station_id;

    RETURN v_count;
END;
/


-- ====================================================================
-- 9. ANONYMOUS BLOCK CALLING PROCEDURES AND FUNCTIONS
-- ====================================================================
DECLARE
    v_name    VARCHAR2(100);
    v_email   VARCHAR2(50);
    v_bill    NUMBER;
    v_points  NUMBER;
BEGIN
    DBMS_OUTPUT.PUT_LINE('=== Testing Stored Procedures and Functions ===');

    -- 1. Call Procedure 1
    proc_get_customer_info(401, v_name, v_email);
    DBMS_OUTPUT.PUT_LINE('Procedure 1 Result -> Name: ' || v_name || ', Email: ' || v_email);

    -- 2. Call Function 1
    v_bill := fn_calculate_bill(601);
    DBMS_OUTPUT.PUT_LINE('Function 1 Result  -> Total Bill for Session 601: Rs.' || v_bill);

    -- 3. Call Function 2
    v_points := fn_get_total_points(201);
    DBMS_OUTPUT.PUT_LINE('Function 2 Result  -> Total Points at Station 201: ' || v_points);

    -- 4. Call Procedure 2
    proc_update_tariff(201, 19.50);
END;
/

SPOOL OFF
