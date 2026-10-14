-- ====================================================================
-- Voltgrid: Smart EV Charging Station Network
-- File: schema.sql
-- Description: 3NF Relational Database Schema (13 Tables)
-- Style: First Year B.Tech DBMS Lab Manual Standard
-- ====================================================================

-- Drop existing tables to allow clean re-execution of script
-- Child tables dropped first or dropped using CASCADE CONSTRAINTS
DROP TABLE MAINTENANCE CASCADE CONSTRAINTS;
DROP TABLE RESERVED_FOR CASCADE CONSTRAINTS;
DROP TABLE RESERVATION CASCADE CONSTRAINTS;
DROP TABLE PAYMENT CASCADE CONSTRAINTS;
DROP TABLE CHARGING_SESSION CASCADE CONSTRAINTS;
DROP TABLE VEHICLE CASCADE CONSTRAINTS;
DROP TABLE VEHICLE_MODEL CASCADE CONSTRAINTS;
DROP TABLE CUSTOMER_PHONE CASCADE CONSTRAINTS;
DROP TABLE CUSTOMER CASCADE CONSTRAINTS;
DROP TABLE CHARGING_POINT CASCADE CONSTRAINTS;
DROP TABLE STATION CASCADE CONSTRAINTS;
DROP TABLE PINCODE_AREA CASCADE CONSTRAINTS;
DROP TABLE OPERATOR CASCADE CONSTRAINTS;

-- ====================================================================
-- STEP 1: CREATE TABLES WITH PRIMARY KEY CONSTRAINTS
-- ====================================================================

-- Table 1: OPERATOR
-- Stores EV charging network operators (e.g., Tata Power, Zeon, Statiq)
CREATE TABLE OPERATOR (
    Operator_ID NUMBER(5),
    Name VARCHAR2(50) NOT NULL,
    License_No VARCHAR2(30) NOT NULL,
    Contact_Info VARCHAR2(50),
    CONSTRAINT pk_operator PRIMARY KEY (Operator_ID),
    CONSTRAINT uq_operator_license UNIQUE (License_No)
);

-- Table 2: PINCODE_AREA
-- Lookup table mapping postal pincode to city (ensures 3NF normalization)
CREATE TABLE PINCODE_AREA (
    Pincode NUMBER(6),
    City VARCHAR2(50) NOT NULL,
    CONSTRAINT pk_pincode PRIMARY KEY (Pincode)
);

-- Table 3: STATION
-- Physical charging station locations
CREATE TABLE STATION (
    Station_ID NUMBER(5),
    Operator_ID NUMBER(5) NOT NULL,
    Street VARCHAR2(100),
    Pincode NUMBER(6) NOT NULL,
    Contact_No VARCHAR2(15),
    Tariff_Rate NUMBER(6,2) NOT NULL,
    CONSTRAINT pk_station PRIMARY KEY (Station_ID)
);

-- Table 4: CHARGING_POINT
-- Individual charging guns/points installed at a station
CREATE TABLE CHARGING_POINT (
    Point_ID NUMBER(5),
    Station_ID NUMBER(5) NOT NULL,
    Connector_Type VARCHAR2(20) NOT NULL,
    Charging_Mode VARCHAR2(10) NOT NULL,
    Status VARCHAR2(15) NOT NULL,
    Power_Rating NUMBER(5,2) NOT NULL,
    CONSTRAINT pk_point PRIMARY KEY (Point_ID)
);

-- Table 5: CUSTOMER
-- Registered EV owners and drivers
CREATE TABLE CUSTOMER (
    Customer_ID NUMBER(5),
    First_Name VARCHAR2(30) NOT NULL,
    Last_Name VARCHAR2(30),
    Email VARCHAR2(50) NOT NULL,
    Registration_Date DATE NOT NULL,
    CONSTRAINT pk_customer PRIMARY KEY (Customer_ID),
    CONSTRAINT uq_customer_email UNIQUE (Email)
);

-- Table 6: CUSTOMER_PHONE
-- Multi-valued attribute table: customers having multiple phone numbers
CREATE TABLE CUSTOMER_PHONE (
    Customer_ID NUMBER(5),
    Phone_No VARCHAR2(15),
    CONSTRAINT pk_customer_phone PRIMARY KEY (Customer_ID, Phone_No)
);

-- Table 7: VEHICLE_MODEL
-- Reference table for EV model specs and battery capacities
CREATE TABLE VEHICLE_MODEL (
    Model VARCHAR2(30),
    Battery_Capacity NUMBER(5,2) NOT NULL,
    CONSTRAINT pk_vehicle_model PRIMARY KEY (Model)
);

-- Table 8: VEHICLE
-- Specific vehicles owned by customers
CREATE TABLE VEHICLE (
    Vehicle_No VARCHAR2(20),
    Customer_ID NUMBER(5) NOT NULL,
    Model VARCHAR2(30) NOT NULL,
    CONSTRAINT pk_vehicle PRIMARY KEY (Vehicle_No)
);

-- Table 9: CHARGING_SESSION
-- Record of each charging event
CREATE TABLE CHARGING_SESSION (
    Session_ID NUMBER(6),
    Vehicle_No VARCHAR2(20) NOT NULL,
    Point_ID NUMBER(5) NOT NULL,
    Start_Time DATE NOT NULL,
    End_Time DATE,
    Units_Consumed NUMBER(6,2),
    CONSTRAINT pk_session PRIMARY KEY (Session_ID)
);

-- Table 10: PAYMENT
-- Billing and payment record for completed charging sessions
CREATE TABLE PAYMENT (
    Payment_ID NUMBER(6),
    Session_ID NUMBER(6) NOT NULL,
    Payment_Mode VARCHAR2(20) NOT NULL,
    Payment_Date DATE NOT NULL,
    CONSTRAINT pk_payment PRIMARY KEY (Payment_ID),
    CONSTRAINT uq_payment_session UNIQUE (Session_ID)
);

-- Table 11: RESERVATION
-- Slot reservation by customer prior to arrival
CREATE TABLE RESERVATION (
    Reservation_ID NUMBER(6),
    Customer_ID NUMBER(5) NOT NULL,
    Booking_Time DATE NOT NULL,
    Slot_Time DATE NOT NULL,
    CONSTRAINT pk_reservation PRIMARY KEY (Reservation_ID)
);

-- Table 12: RESERVED_FOR
-- Bridge table linking reservation with specific charging point
CREATE TABLE RESERVED_FOR (
    Reservation_ID NUMBER(6),
    Point_ID NUMBER(5),
    CONSTRAINT pk_reserved_for PRIMARY KEY (Reservation_ID, Point_ID)
);

-- Table 13: MAINTENANCE
-- Maintenance history and repair logs for charging points
CREATE TABLE MAINTENANCE (
    Point_ID NUMBER(5),
    Report_No NUMBER(5),
    Issue_Description VARCHAR2(100) NOT NULL,
    Date_Reported DATE NOT NULL,
    Status VARCHAR2(20) NOT NULL,
    CONSTRAINT pk_maintenance PRIMARY KEY (Point_ID, Report_No)
);

-- ====================================================================
-- STEP 2: ADD FOREIGN KEY CONSTRAINTS USING ALTER TABLE
-- ====================================================================

-- Station references Operator and Pincode
ALTER TABLE STATION 
    ADD CONSTRAINT fk_station_operator FOREIGN KEY (Operator_ID) 
    REFERENCES OPERATOR(Operator_ID);

ALTER TABLE STATION 
    ADD CONSTRAINT fk_station_pincode FOREIGN KEY (Pincode) 
    REFERENCES PINCODE_AREA(Pincode);

-- Charging Point references Station
ALTER TABLE CHARGING_POINT 
    ADD CONSTRAINT fk_point_station FOREIGN KEY (Station_ID) 
    REFERENCES STATION(Station_ID);

-- Customer Phone references Customer
ALTER TABLE CUSTOMER_PHONE 
    ADD CONSTRAINT fk_phone_customer FOREIGN KEY (Customer_ID) 
    REFERENCES CUSTOMER(Customer_ID);

-- Vehicle references Customer and Vehicle Model
ALTER TABLE VEHICLE 
    ADD CONSTRAINT fk_vehicle_customer FOREIGN KEY (Customer_ID) 
    REFERENCES CUSTOMER(Customer_ID);

ALTER TABLE VEHICLE 
    ADD CONSTRAINT fk_vehicle_model FOREIGN KEY (Model) 
    REFERENCES VEHICLE_MODEL(Model);

-- Charging Session references Vehicle and Charging Point
ALTER TABLE CHARGING_SESSION 
    ADD CONSTRAINT fk_session_vehicle FOREIGN KEY (Vehicle_No) 
    REFERENCES VEHICLE(Vehicle_No);

ALTER TABLE CHARGING_SESSION 
    ADD CONSTRAINT fk_session_point FOREIGN KEY (Point_ID) 
    REFERENCES CHARGING_POINT(Point_ID);

-- Payment references Charging Session (1-to-1 relationship)
ALTER TABLE PAYMENT 
    ADD CONSTRAINT fk_payment_session FOREIGN KEY (Session_ID) 
    REFERENCES CHARGING_SESSION(Session_ID);

-- Reservation references Customer
ALTER TABLE RESERVATION 
    ADD CONSTRAINT fk_reservation_customer FOREIGN KEY (Customer_ID) 
    REFERENCES CUSTOMER(Customer_ID);

-- Reserved_For references Reservation and Charging Point
ALTER TABLE RESERVED_FOR 
    ADD CONSTRAINT fk_resfor_reservation FOREIGN KEY (Reservation_ID) 
    REFERENCES RESERVATION(Reservation_ID);

ALTER TABLE RESERVED_FOR 
    ADD CONSTRAINT fk_resfor_point FOREIGN KEY (Point_ID) 
    REFERENCES CHARGING_POINT(Point_ID);

-- Maintenance references Charging Point
ALTER TABLE MAINTENANCE 
    ADD CONSTRAINT fk_maint_point FOREIGN KEY (Point_ID) 
    REFERENCES CHARGING_POINT(Point_ID);

-- ====================================================================
-- STEP 3: ADD CHECK CONSTRAINTS USING ALTER TABLE
-- ====================================================================

-- Charging Point status must be valid
ALTER TABLE CHARGING_POINT 
    ADD CONSTRAINT ck_point_status CHECK (Status IN ('Available', 'In-Use', 'Faulty'));

-- Charging Mode must be either AC or DC
ALTER TABLE CHARGING_POINT 
    ADD CONSTRAINT ck_point_mode CHECK (Charging_Mode IN ('AC', 'DC'));

-- Power rating must be positive
ALTER TABLE CHARGING_POINT 
    ADD CONSTRAINT ck_point_power CHECK (Power_Rating > 0);

-- Station tariff rate must be greater than zero
ALTER TABLE STATION 
    ADD CONSTRAINT ck_station_tariff CHECK (Tariff_Rate > 0);

-- Units consumed in a session cannot be negative
ALTER TABLE CHARGING_SESSION 
    ADD CONSTRAINT ck_session_units CHECK (Units_Consumed >= 0);

-- End time must be after start time
ALTER TABLE CHARGING_SESSION 
    ADD CONSTRAINT ck_session_time CHECK (End_Time > Start_Time);
