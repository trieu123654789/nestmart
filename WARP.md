# WARP.md

This file provides guidance to WARP (warp.dev) when working with code in this repository.

## Project Overview

NestMart is a Java-based e-commerce web application built with Spring MVC framework, using JSP for views, Spring JDBC for data access, and SQL Server as the database. The application supports multiple user roles (Admin, Employee, Shipper, Client) with distinct functionalities for each role.

## Technology Stack

- **Backend**: Java 8, Spring Framework 4.3.29, Spring Security 5.7.3
- **Web Framework**: Spring MVC with JSP views
- **Database**: Microsoft SQL Server with Spring JDBC Template
- **Build Tool**: Apache Ant (NetBeans project)
- **Application Server**: GlassFish 4 EE7
- **Additional Libraries**: Twilio, Apache POI, Commons FileUpload, Lombok

## Development Environment Setup

### Prerequisites
- JDK 8
- SQL Server (with database named "nestmart")
- GlassFish 4 Server
- NetBeans IDE (recommended)

### Database Configuration
Database connection is configured in `web/WEB-INF/jdbc.properties`:
- Server: localhost:1433
- Database: nestmart
- Username: sa
- Password: sqladmin

## Build Commands

This is an Apache Ant-based NetBeans project. Common build commands:

### Build the Project
```bash
ant clean
ant compile
ant dist
```

### Deploy to Server
```bash
ant run-deploy
```

### Clean Build Artifacts
```bash
ant clean
```

### Run Tests
```bash
ant compile-test
ant test
```

## Application Architecture

### Directory Structure
```
nestmartFinal/
├── src/java/com/
│   ├── controllers/     # Spring MVC Controllers
│   ├── models/          # Data models and DAO implementations
│   ├── services/        # Business logic services
│   └── config/          # Configuration classes
├── web/
│   ├── WEB-INF/        # Configuration files and Spring context
│   ├── jsp/            # JSP view templates organized by role
│   └── assets/         # Static resources (CSS, JS, images)
├── lib/                # JAR dependencies
└── build.xml           # Ant build configuration
```

### Key Architectural Patterns

#### MVC Pattern with Spring
- **Controllers**: Handle HTTP requests and routing (e.g., `ProductsController`, `LoginController`)
- **Models**: Data objects with validation annotations (e.g., `Products`, `Accounts`)
- **Views**: JSP templates in `web/jsp/` organized by role (admin, client, employee, shipper)

#### DAO Pattern
- **Interfaces**: Define data access contracts (e.g., `ProductsDAO`, `AccountsDAO`)
- **Implementations**: Spring JDBC-based implementations (e.g., `ProductsDAOImpl`)
- **Dependency Injection**: DAOs are autowired into controllers via Spring

#### Role-Based Access Control
The application implements a 4-tier role system:
1. **Admin** (role = 1): Full system management
2. **Employee** (role = 2): Order and inventory management
3. **Shipper** (role = 3): Delivery management
4. **Client** (role = 4): Customer shopping interface

### Spring Configuration

#### Application Context (`web/WEB-INF/applicationContext.xml`)
- Database configuration with connection pooling
- DAO bean definitions with JDBC template injection
- Mail sender configuration for notifications
- File upload configuration

#### Dispatcher Servlet (`web/WEB-INF/dispatcher-servlet.xml`)
- Component scanning for controllers and services
- View resolver configuration for JSP templates
- URL mapping configuration

### Database Integration
- Uses Spring JDBC Template for database operations
- DAO classes handle CRUD operations with SQL queries
- Transaction management handled at service layer
- Database connection configured via properties file

### Session Management
- HTTP session-based authentication
- Session timeout set to 30 minutes
- Role-based redirection after login
- Session service for centralized session handling

### File Upload Handling
- Multipart file upload support for product images
- Files stored in `web/assets/admin/images/uploads/products/`
- File size limit: 10MB per file
- Image management through ProductImage entity

## Development Workflows

### Adding New Features

1. **Create Model**: Define entity in `src/java/com/models/` with validation annotations
2. **Create DAO**: Interface and implementation for data access
3. **Register DAO**: Add bean definition in `applicationContext.xml`
4. **Create Controller**: Handle HTTP requests in appropriate role package
5. **Create JSP Views**: Add templates in corresponding role directory
6. **Update Navigation**: Modify role-specific navigation JSPs

### Database Changes
1. Update SQL Server database schema
2. Modify DAO implementations for new queries
3. Update model classes if entity structure changes
4. Test data access through DAO methods

### Testing Controllers
```java
// Example of testing controller endpoints
@RequestMapping(value = "/testEndpoint", method = RequestMethod.GET)
public String testMethod(Model model) {
    // Controller logic
    return "viewName";
}
```

### Debugging Database Issues
```bash
# Check database connectivity
ant compile
ant run
# Check logs in server output for JDBC connection errors
```

## Key Components to Understand

### Controllers Organization
- **Admin Controllers**: Product, category, brand, user management
- **Client Controllers**: Shopping, cart, order management
- **Employee Controllers**: Order processing, inventory, support
- **Shipper Controllers**: Delivery and shipping management

### Data Flow Pattern
1. HTTP Request → Controller
2. Controller → Service Layer (if present) → DAO
3. DAO → Database via JDBC Template
4. Response → JSP View with Model attributes
5. JSP → Rendered HTML response

### Security Considerations
- Password stored in plain text (requires improvement)
- Role-based access control implemented in controllers
- Session-based authentication
- CSRF protection needs implementation
- SQL injection prevented through parameterized queries

### File Structure for Views
```
web/jsp/
├── admin/          # Administrative interface
├── client/         # Customer-facing pages
├── employee/       # Employee management interface
└── shipper/        # Shipping and delivery interface
```

## Common Development Tasks

### Run Single Test
```bash
ant compile-single -Djavac.includes=**/*TestClass.java
ant test-single -Dtest.includes=**/*TestClass.java
```

### Deploy Without Full Build
```bash
ant compile
ant run-deploy
```

### Debug Database Queries
Enable SQL logging by adding debug configuration to Spring JDBC template beans in applicationContext.xml.

### Add New User Role
1. Update database schema for new role value
2. Modify LoginController role switch statement
3. Create new JSP directory for role
4. Add role-specific controllers
5. Update RoleUtils class for access control

## Important Configuration Files

- `web/WEB-INF/web.xml`: Servlet and filter configuration
- `web/WEB-INF/applicationContext.xml`: Spring bean definitions
- `web/WEB-INF/dispatcher-servlet.xml`: Spring MVC configuration
- `web/WEB-INF/jdbc.properties`: Database connection properties
- `nbproject/project.properties`: NetBeans project settings
- `build.xml`: Ant build configuration
