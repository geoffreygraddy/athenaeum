-- Oracle Database Initialization Script for Users, Authorities, and Spring Session

-- Create Spring Session tables (required for session management)
CREATE TABLE SPRING_SESSION (
    PRIMARY_ID CHAR(36) NOT NULL,
    SESSION_ID CHAR(36) NOT NULL,
    CREATION_TIME NUMBER(19) NOT NULL,
    LAST_ACCESS_TIME NUMBER(19) NOT NULL,
    MAX_INACTIVE_INTERVAL NUMBER(10) NOT NULL,
    EXPIRY_TIME NUMBER(19) NOT NULL,
    PRINCIPAL_NAME VARCHAR2(100),
    CONSTRAINT SPRING_SESSION_PK PRIMARY KEY (PRIMARY_ID)
);

CREATE UNIQUE INDEX SPRING_SESSION_IX1 ON SPRING_SESSION (SESSION_ID);
CREATE INDEX SPRING_SESSION_IX2 ON SPRING_SESSION (EXPIRY_TIME);
CREATE INDEX SPRING_SESSION_IX3 ON SPRING_SESSION (PRINCIPAL_NAME);

CREATE TABLE SPRING_SESSION_ATTRIBUTES (
    SESSION_PRIMARY_ID CHAR(36) NOT NULL,
    ATTRIBUTE_NAME VARCHAR2(200) NOT NULL,
    ATTRIBUTE_BYTES BLOB NOT NULL,
    CONSTRAINT SPRING_SESSION_ATTRIBUTES_PK PRIMARY KEY (SESSION_PRIMARY_ID, ATTRIBUTE_NAME),
    CONSTRAINT SPRING_SESSION_ATTRIBUTES_FK FOREIGN KEY (SESSION_PRIMARY_ID) REFERENCES SPRING_SESSION(PRIMARY_ID) ON DELETE CASCADE
);

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
