# Distributed Food Ordering System

A 3-tier distributed computing system for food ordering, built with Java RMI and MySQL.

## System Architecture

This application implements a 3-tier architecture:
- **Client Tier**: Java Swing GUI application
- **Application Tier**: Java RMI server with business logic
- **Data Tier**: MySQL database

## Features

- User authentication and authorization (Customer/Admin roles)
- Product browsing and management
- Shopping cart functionality
- Order placement and tracking
- Payment processing
- Admin dashboard with reporting

## Prerequisites

- Java Development Kit (JDK) 17 or higher
- MySQL Server 8.0 or higher
- NetBeans IDE (recommended)

## Setup Instructions

### 1. Database Setup

1. Open MySQL and run the SQL script to create the database:
   ```
   src\config\food_ordering_database.sql
   ```

2. Configure database connection in:
   ```
   src\config\DatabaseProperties.properties
   ```
   
   Default settings:
   ```
   jdbc.url=jdbc:mysql://localhost:3306/food_ordering_database
   jdbc.username=root
   jdbc.password=root
   ```

### 2. Network Configuration

1. Configure network settings in:
   ```
   src\config\NetworkProperties.properties
   ```
   
   Default settings:
   ```
   server.ip=192.168.114.114
   client.ip=127.0.0.1
   rmi.port=1040
   ```
   
   Modify these values according to your network setup.

### 3. Build and Run

1. Clean and Build the project in NetBeans

2. Run the RMI Registry server first:
   - Right-click on `src\rmi\Registry.java`
   - Select "Run File"
   - Wait for "Server is running" message

3. Run the Client application:
   - Right-click on `src\client\Client.java`
   - Select "Run File"
   - The login screen should appear

### 4. SSL Debugging (Optional)

To view SSL handshake details, add the following VM option to your run configuration:
```
-Djavax.net.debug=ssl:handshake
```

In NetBeans:
1. Right-click the project
2. Select "Properties"
3. Go to "Run"
4. Add the VM option in the "VM Options" field

## Default Login Credentials

### Admin User
- Username: asd
- Password: asd

### Customer User
- Username: andrew
- Password: asd

## Project Structure

- `src/client/` - Client-side application code
- `src/config/` - Configuration files
- `src/database/` - Database access objects
- `src/enums/` - Enumeration types
- `src/manager/` - Business logic managers
- `src/model/` - Data models
- `src/rmi/` - RMI interfaces and server implementations
- `src/security/` - SSL socket factories
- `src/utils/` - Utility classes

## Troubleshooting

- If connection issues occur, verify network settings in NetworkProperties.properties
- For database errors, check DatabaseProperties.properties and ensure MySQL is running
- SSL errors may require regenerating the certificate (server.cert) and truststore (clienttruststore.jks)

## Security Features

- SSL encryption for RMI communication
- Password hashing with salt
- Token-based authentication

## Logging

Server logs are stored in:
```
src/logs/session_[timestamp]/
```

Log files include:
- application.log - Application events
- stdout.log - Standard output
- stderr.log - Error output
- summary.log - Server startup summary