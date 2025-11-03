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

-- Note: Password is plain text '12345' as per requirements
-- In production, you should encode passwords using BCrypt
-- Example BCrypt hash for '12345': $2a$10$3qRNX7YqH8qVGlvLJlPz7.fU7bF0K7M8jqzUK5uZ0KqL5bj5Kg5dy

-- Insert initial user
INSERT INTO users (id, username, password, enabled) VALUES (users_seq.NEXTVAL, 'geoffrey', '12345', 1);

-- Insert initial authority
INSERT INTO authorities (id, username, authority) VALUES (authorities_seq.NEXTVAL, 'geoffrey', 'write');

COMMIT;
