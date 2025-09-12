# Build and Deploy Script for NestMart Application
Write-Host "Building and Deploying NestMart Application..." -ForegroundColor Green

# Step 1: Build the Java application using Ant
Write-Host "Step 1: Building Java application with Ant..." -ForegroundColor Yellow
if (Test-Path "build.xml") {
    ant clean dist
    if ($LASTEXITCODE -ne 0) {
        Write-Error "Ant build failed!"
        exit 1
    }
    Write-Host "✓ Java application built successfully" -ForegroundColor Green
} else {
    Write-Error "build.xml not found. Please ensure you're in the project root directory."
    exit 1
}

# Step 2: Check if WAR file exists
Write-Host "Step 2: Checking if WAR file exists..." -ForegroundColor Yellow
if (Test-Path "dist/nestmartappFinal.war") {
    Write-Host "✓ WAR file found: dist/nestmartappFinal.war" -ForegroundColor Green
} else {
    Write-Error "WAR file not found. Build may have failed."
    exit 1
}

# Step 3: Build Docker image
Write-Host "Step 3: Building Docker image..." -ForegroundColor Yellow
docker build -t nestmart-app:latest .
if ($LASTEXITCODE -ne 0) {
    Write-Error "Docker build failed!"
    exit 1
}
Write-Host "✓ Docker image built successfully" -ForegroundColor Green

# Step 4: Start the application with Docker Compose
Write-Host "Step 4: Starting application with Docker Compose..." -ForegroundColor Yellow
docker-compose up -d
if ($LASTEXITCODE -ne 0) {
    Write-Error "Docker Compose failed!"
    exit 1
}

Write-Host "✓ Application deployed successfully!" -ForegroundColor Green
Write-Host ""
Write-Host "Access your application at:" -ForegroundColor Cyan
Write-Host "  Application: http://localhost:8080/nestmart" -ForegroundColor White
Write-Host "  GlassFish Admin: http://localhost:4848" -ForegroundColor White
Write-Host "  SQL Server: localhost:1433" -ForegroundColor White
Write-Host ""
Write-Host "To check logs:" -ForegroundColor Cyan
Write-Host "  docker-compose logs -f nestmart-app" -ForegroundColor White
Write-Host ""
Write-Host "To stop the application:" -ForegroundColor Cyan
Write-Host "  docker-compose down" -ForegroundColor White
