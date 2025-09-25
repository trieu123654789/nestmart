-- Disable password complexity policy
EXEC sp_configure 'show advanced options', 1;
RECONFIGURE;
EXEC sp_configure 'password policy', 0;
RECONFIGURE;

-- Create database if it doesn't exist
IF NOT EXISTS (SELECT name FROM sys.databases WHERE name = 'nestmart')
BEGIN
    CREATE DATABASE nestmart;
END
GO

USE nestmart;
GO

-- Create application user without password
IF NOT EXISTS (SELECT name FROM sys.server_principals WHERE name = 'appuser')
BEGIN
    CREATE LOGIN appuser WITH PASSWORD = '', CHECK_POLICY = OFF;
END
GO

-- Create user in nestmart database
IF NOT EXISTS (SELECT name FROM sys.database_principals WHERE name = 'appuser')
BEGIN
    CREATE USER appuser FOR LOGIN appuser;
    ALTER ROLE db_owner ADD MEMBER appuser;
END
GO

-- Run the main database schema
:r /docker-entrypoint-initdb.d/nestmart.sql
