-- Oracle Database Initialization Script for Users and Authorities

-- Create users table
CREATE TABLE users (
    id NUMBER(10) NOT NULL,
    username VARCHAR2(45),
    password VARCHAR2(60),
    enabled NUMBER(10) NOT NULL,
    PRIMARY KEY (id)
);

-- Create sequence for users table
CREATE SEQUENCE users_seq START WITH 1 INCREMENT BY 1;

-- Create authorities table
CREATE TABLE authorities (
    id NUMBER(10) NOT NULL,
    username VARCHAR2(45),
    authority VARCHAR2(45),
    PRIMARY KEY (id)
);

-- Create sequence for authorities table
CREATE SEQUENCE authorities_seq START WITH 1 INCREMENT BY 1;

-- WARNING: Security Notice
-- The password below is stored in plain text as per the original requirements.
-- In a production environment, you MUST:
-- 1. Generate a BCrypt hash for the password using a tool or Spring Security's BCryptPasswordEncoder
-- 2. Replace the plain text password with the BCrypt hash
-- 3. Example BCrypt hash for '12345': $2a$10$XpTqGfobRa8yFX.6SSu7ieLGGfPwTvLKVEqJlkJT3LJVxLvVLKGzu
--
-- To generate a BCrypt hash in Java:
-- String hash = new BCryptPasswordEncoder().encode("your_password");

-- Insert initial user (INSECURE - plain text password, for development only)
INSERT INTO users (id, username, password, enabled) VALUES (users_seq.NEXTVAL, 'geoffrey', '12345', 1);

-- Insert initial authority
INSERT INTO authorities (id, username, authority) VALUES (authorities_seq.NEXTVAL, 'geoffrey', 'write');

COMMIT;
