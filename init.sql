-- Créer la base
CREATE DATABASE IF NOT EXISTS medilabo;
USE medilabo;

-- Table users
CREATE TABLE IF NOT EXISTS users (
    id CHAR(36) PRIMARY KEY,
    role VARCHAR(20),
    email VARCHAR(255) UNIQUE NOT NULL,
    password VARCHAR(255),
    first_name VARCHAR(255),
    last_name VARCHAR(255),
    gender ENUM('F','M'),
    date_of_birth DATE,
    phone VARCHAR(20),
    address VARCHAR(255)
);

-- Table patients
CREATE TABLE IF NOT EXISTS patients (
    id CHAR(36) PRIMARY KEY,
    first_name VARCHAR(255),
    last_name VARCHAR(255),
    email VARCHAR(255) UNIQUE NOT NULL,
    date_of_birth DATE,
    gender ENUM('F','M'),
    address VARCHAR(255),
    phone VARCHAR(20),
    doctor_id CHAR(36) NOT NULL,
    FOREIGN KEY (doctor_id) REFERENCES users(id)
);

-- Données dans users
INSERT INTO users (id, role, email, first_name, last_name, gender, date_of_birth, phone, address, password)
VALUES
('c008d5d3-63ff-4999-ba55-0ea7c24cb6aa', 'USER', 'user1@test.com', 'User', 'One', 'M', '1990-01-01', NULL, '', '$2a$10$RfKMIy3Sz7rGfsvlKUwJSeWx4vpoRRGutwCPRUq9yaHUK7d7Dui3O'),
('5f9fb359-f11f-47af-89f2-38427fbb4263', 'ADMIN', 'user2@test.com', 'User', 'Two', 'F', '1990-01-01', NULL, '', '$2a$10$sYyNX1tHb8nBhWJRtA.7E./56onsPHQjUwjAmkKQcAX8np7H3fnsu');

-- Données dans patients
INSERT INTO patients (id, first_name, last_name, email, date_of_birth, gender, address, phone, doctor_id)
VALUES
('07602259-8b21-4d7c-9b83-c391582176e5', 'Test', 'TestInDanger', 'pthree@email.com', '2004-06-18', 'M', '3 Club Road', '300-444-5555', 'c008d5d3-63ff-4999-ba55-0ea7c24cb6aa'),
('5230f742-542e-4906-b257-70c03e13d9b1', 'Test', 'TestEarlyOnset', 'pfour@email.com', '2002-06-28', 'F', '4 Valley Dr', '400-555-6666', 'c008d5d3-63ff-4999-ba55-0ea7c24cb6aa'),
('5f6df173-4293-4dd6-ba69-8828bfadb27f', 'Test', 'TestBorderline', 'ptwo@email.com', '1945-06-24', 'M', '2 High St', '200-333-4444', 'c008d5d3-63ff-4999-ba55-0ea7c24cb6aa'),
('d20f4060-1462-4c41-ab90-416649a3002f', 'Test', 'TestNone', 'pone@email.com', '1966-12-31', 'F', '1 Brookside St', '100-222-3333', 'c008d5d3-63ff-4999-ba55-0ea7c24cb6aa');
