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
- NetBeans IDE (recommended) or VS Code/other external editors

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
   server.ip=127.0.0.1
   client.ip=127.0.0.1
   rmi.port=1040
   ```
   
   Modify these values according to your network setup.

### 3. Build and Run

#### Using NetBeans IDE:

1. Clean and Build the project in NetBeans

2. Run the RMI Registry server first:
   - Right-click on `src\rmi\Registry.java`
   - Select "Run File"
   - Wait for "Server is running" message

3. Run the Client application:
   - Right-click on `src\client\Client.java`
   - Select "Run File"
   - The login screen should appear

#### Using VS Code or External Editor:

1. Ensure the following requirements are met:
   - `/lib` folder contains:
     - AbsoluteLayout.jar
     - mysql-connector-j-9.2.0.jar
   - `/buildvs` folder exists

2. Run the following commands in PowerShell:

```powershell
# Compile all Java files
$files = Get-ChildItem -Recurse -Filter *.java -Path src | ForEach-Object { $_.FullName }
Copy-Item -Recurse -Path src/resources -Destination buildvs
javac -d buildvs -cp "lib/*" $files

# Run the server
java -cp "buildvs;lib/*" rmi.Registry

# Run the client (in a separate terminal)
java -cp "buildvs;lib/*" client.Client
```

### 4. Running Tests

The project includes DAO tests that can be run to verify database operations:

1. Run the test suite:
   ```
   java -cp "buildvs;lib/*" database.test.DAOTestSuite
   ```

### 5. SSL Debugging (Optional)

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

- `src/client/` - Client-side application code and UI components
- `src/config/` - Configuration files and database setup scripts
- `src/database/` - Database access objects and DAO tests
- `src/enums/` - Enumeration types for order status, payment status, etc.
- `src/manager/` - Business logic managers
- `src/model/` - Data models and entity classes
- `src/rmi/` - RMI interfaces and server implementations
- `src/security/` - SSL socket factories for secure communication
- `src/utils/` - Utility classes for configuration, tokens, etc.

## Troubleshooting

- If connection issues occur, verify network settings in NetworkProperties.properties
- For database errors, check DatabaseProperties.properties and ensure MySQL is running
- SSL errors may require regenerating the certificate (server.cert) and truststore (clienttruststore.jks)
- If resources aren't found when running from VS Code, ensure the resources folder is properly copied to the build directory

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