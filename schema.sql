-- Security Access System: database setup
-- Run with:  mysql -u root -p < schema.sql

CREATE DATABASE IF NOT EXISTS security_db;
USE security_db;

CREATE TABLE IF NOT EXISTS users (
    user_id  INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50)  NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    role     ENUM('Admin', 'Employee', 'Visitor') NOT NULL
);

-- Demo accounts (one per role) for trying out the app
INSERT IGNORE INTO users (username, password, role) VALUES
    ('admin',    'admin123',    'Admin'),
    ('employee', 'employee123', 'Employee'),
    ('visitor',  'visitor123',  'Visitor');
