## Overview

Java web application built with Spring MVC (JSP/JSTL), packaged as a WAR. The project supports Ant/PowerShell builds, runs locally on Tomcat or GlassFish, and ships Docker/Docker Compose for containerized deployment. JDBC configuration samples and multiple Dockerfiles are included.

## Tech Stack

- **Language/Runtime**: Java 8 (JRE/JDK 1.8)
- **Frameworks**: Spring 4.3.x (Core, MVC, JDBC), Spring Security 5.7
- **View**: JSP + JSTL
- **App Servers**: Tomcat 9 or GlassFish
- **Build**: Apache Ant (`build.xml`) + PowerShell scripts
- **Packaging**: WAR (`dist/nestmartappFinal.war`)
- **Database**: SQL Server (driver `mssql-jdbc-12.2.0.jre8.jar`)
- **Containers**: Docker, Docker Compose

## Key Features

- CRUD for products, categories, users, and orders (search, pagination, validation)
- Role-based portals (admin, employee, shipper, client) with Spring Security
- Shopping flow: cart, checkout, order processing/history, product image upload
- Excel import/export (Apache POI), file uploads (Commons FileUpload)
- Customer live chat widget (polling, unread counter, session check)
- Password reset via email/SMS (Twilio)

## Project Structure

```text
c:\Projects\nestmart
├─ src/                 # Java sources
├─ web/                 # Static assets and JSP pages
├─ lib/                 # .jar dependencies (Spring, JDBC, JSTL, ...)
├─ build/               # Ant intermediate build output
├─ dist/                # Final WAR (e.g., nestmartappFinal.war)
├─ nbproject/           # NetBeans/Ant configuration
├─ Dockerfile*          # Multiple Dockerfile variants
├─ docker-compose.yml   # Compose services definition
├─ build.xml            # Ant build script
├─ *.ps1                # PowerShell build/deploy helpers
└─ web/WEB-INF/         # web.xml, dispatcher-servlet.xml, Spring configs
```

## Configuration

- `web/WEB-INF/jdbc.properties` or `web/WEB-INF/application.properties`
  - Database connection (URL, username, password, pool, etc.)
- `web/WEB-INF/applicationContext.xml`, `dispatcher-servlet.xml`
  - Beans, datasource, view resolver, component scan, etc.
- Keep secrets out of VCS; prefer environment variables/CI secrets.

## Build (Ant)

From Windows PowerShell (with JDK 8 and Ant installed):

```powershell
# From project root
ant clean war | cat

# WAR will be generated in dist/ (e.g., dist/nestmartappFinal.war)
```

Helper scripts:

```powershell
# Quick build
./manual-build.ps1

# Build and optional deploy per script settings
./build-and-deploy.ps1
```

## Run Locally with Tomcat

1. Install Tomcat 9
2. Copy the WAR from `dist/*.war` to `TOMCAT_HOME/webapps/`
3. Start Tomcat and open: `http://localhost:8080/<context-path>`

Note: the context path defaults to the WAR file name (without `.war`). Check Tomcat logs if needed.

## Run in NetBeans with GlassFish

1. Open the project in NetBeans (8.x/12.x)
2. Add a GlassFish server in NetBeans (Tools → Servers → Add Server)
3. Set GlassFish as the project server (Project → Properties → Run)
4. Configure JDBC if using SQL Server
   - Copy `lib/mssql-jdbc-12.2.0.jre8.jar` to `GLASSFISH_HOME\glassfish\domains\domain1\lib`
   - Or ensure properties in `web/WEB-INF/jdbc.properties` are correct
5. Run the project (F6) and visit: `http://localhost:8080/<context-path>`

## Run with Docker

Example using Tomcat Dockerfile:

```powershell
docker build -t nestmart:tomcat -f Dockerfile.tomcat .
docker run --rm -p 8080:8080 --name nestmart-web nestmart:tomcat
```

Using Docker Compose:

```powershell
docker compose up -d
```

Review `docker-compose.yml` to adjust ports, env vars, and volumes.

---

## 🚀 Usage Guide

### Step 1: Start the Database
```powershell
.\setup-database.ps1
```

### Step 2: Run the Project in NetBeans
- Open NetBeans IDE
- Load the NestMart project
- Run the project (press **F6**)

### Step 3: Access the Application
- Open your browser
- Go to: `http://localhost:8080/nestmart/`

### Database Connection Info
- **Server**: localhost:1433  
- **Database**: nestmart  
- **Username**: sa  
- **Password**: sqladmin123!  

### Stop the Database
```powershell
.\stop-database.ps1
```

### ⚠️ Notes
- Make sure **Docker Desktop** is running
- Ensure port **1433** is free (not used by another app)
- If errors occur, rerun `setup-database.ps1`

### 🔧 Troubleshooting
- **Port already in use**
  ```powershell
  docker-compose -f docker-compose.dev.yml down
  .\setup-database.ps1
  ```
- **Database connection failed**
  - Verify Docker Desktop is running
  - Rerun `setup-database.ps1`
  - Wait ~30 seconds after starting the script

---

## Common Tasks

- Clean and build WAR: `ant clean war`
- Build Docker image: `docker build -t app -f Dockerfile.tomcat .`
- Start via Compose: `docker compose up -d`
- Cleanup build artifacts: `./cleanup.ps1`

## Troubleshooting

- DB connection issues: verify `jdbc.properties` and JDBC driver in `lib/`
- 404/500 at runtime: check server logs and `dispatcher-servlet.xml` mapping
- Ant build errors: ensure JDK 8 and Ant are on PATH
- Docker errors: check env vars, port collisions, file permissions
## 👤 Test Accounts

Use the following test accounts to log in and explore the system:

- **Nguyen Hoang Viet** – Role: **Admin** – Password: `1q2w3e`
- **Ngoc Linh** – Role: **Customer** – Password: `1q2w3e`
- **Nguyen My Binh** – Role: **Employee** – Password: `1q2w3e`
- **Ho Van Kiet** – Role: **Shipper** – Password: `1q2w3e`

> All passwords are stored as BCrypt hashes in the database.

## License

Source code belongs to the project author. Refer to the license (if any) or contact the owner for commercial use.
