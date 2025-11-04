# Oracle Database Setup

This document explains how to set up and use Oracle Database with the Athenaeum application.

## Overview

The application supports two database configurations:
- **H2 (default)**: In-memory database for development and testing
- **Oracle**: Production database for user authentication

## Starting Oracle Database with Docker

### Using Docker Compose

Start the Oracle database container:

```bash
docker-compose -f docker-compose-oracle.yml up -d
```

This will:
- Pull the Oracle Database 21c Express Edition image
- Start the database on port 1521
- Initialize the database with users, authorities, and Spring Session tables
- Create the default user 'geoffrey' with password '12345'

### Database Connection Details

- **Host**: localhost
- **Port**: 1521
- **SID**: XE
- **System Password**: OraclePassword123
- **JDBC URL**: `jdbc:oracle:thin:@localhost:1521:XE`

## Running the Application with Oracle

To run the application with Oracle database, use the `oracle` Spring profile:

```bash
cd backend
mvn spring-boot:run -Dspring-boot.run.profiles=oracle
```

Or set the environment variable:

```bash
export SPRING_PROFILES_ACTIVE=oracle
mvn spring-boot:run
```

### Environment Variables

You can customize the Oracle connection using environment variables:

- `ORACLE_HOST`: Database host (default: localhost)
- `ORACLE_PORT`: Database port (default: 1521)
- `ORACLE_USERNAME`: Database username (default: system)
- `ORACLE_PASSWORD`: Database password (default: OraclePassword123)

Example:

```bash
export ORACLE_HOST=my-oracle-server
export ORACLE_PORT=1521
export ORACLE_USERNAME=system
export ORACLE_PASSWORD=MySecurePassword
mvn spring-boot:run -Dspring-boot.run.profiles=oracle
```

## Database Schema

The Oracle initialization script creates the following tables:

### Spring Session Tables

Required for session management:

```sql
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

CREATE TABLE SPRING_SESSION_ATTRIBUTES (
    SESSION_PRIMARY_ID CHAR(36) NOT NULL,
    ATTRIBUTE_NAME VARCHAR2(200) NOT NULL,
    ATTRIBUTE_BYTES BLOB NOT NULL,
    CONSTRAINT SPRING_SESSION_ATTRIBUTES_PK PRIMARY KEY (SESSION_PRIMARY_ID, ATTRIBUTE_NAME)
);
```

### Users Table

```sql
CREATE TABLE users (
    id NUMBER(10) NOT NULL,
    username VARCHAR2(45),
    password VARCHAR2(60),
    enabled NUMBER(10) NOT NULL,
    PRIMARY KEY (id)
);
```

### Authorities Table

```sql
CREATE TABLE authorities (
    id NUMBER(10) NOT NULL,
    username VARCHAR2(45),
    authority VARCHAR2(45),
    PRIMARY KEY (id)
);
```

## Initial Data

The database is initialized with:

**User**: geoffrey
- Username: geoffrey
- Password: 12345
- Enabled: 1

**Authority**: write
- Username: geoffrey
- Authority: write

## Security Note

⚠️ **Important**: The default setup uses plain text passwords as specified in the requirements. In a production environment, you should:

1. Encode passwords using BCrypt before storing them in the database
2. Use strong passwords
3. Secure the database connection with TLS/SSL
4. Use proper database credentials management (e.g., secrets management system)

## Stopping the Database

To stop and remove the Oracle container:

```bash
docker-compose -f docker-compose-oracle.yml down
```

To stop and remove the container along with the data volume:

```bash
docker-compose -f docker-compose-oracle.yml down -v
```

## Accessing Oracle SQL*Plus

To connect to the database using SQL*Plus:

```bash
docker exec -it athenaeum-oracle-db sqlplus system/OraclePassword123@XE
```

## Troubleshooting

### Container fails to start

If the Oracle container fails to start, check:
1. You have enough disk space (Oracle requires at least 10GB)
2. Port 1521 is not already in use
3. Check the container logs: `docker logs athenaeum-oracle-db`

### Application cannot connect to database

1. Ensure the Oracle container is running: `docker ps`
2. Check the database is healthy: `docker logs athenaeum-oracle-db`
3. Verify connection settings in application.yml
4. Check firewall rules allow connections to port 1521
