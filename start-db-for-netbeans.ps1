# Script to start SQL Server for NetBeans development
Write-Host "Starting SQL Server for NetBeans development..." -ForegroundColor Green

# Stop any existing containers
Write-Host "Stopping existing containers..." -ForegroundColor Yellow
docker-compose -f docker-compose.dev.yml down

# Start SQL Server
Write-Host "Starting SQL Server..." -ForegroundColor Yellow
docker-compose -f docker-compose.dev.yml up -d

# Wait for SQL Server to be ready
Write-Host "Waiting for SQL Server to be ready..." -ForegroundColor Yellow
Start-Sleep -Seconds 30

# Check if container is running
$containerStatus = docker ps --filter "name=nestmart-sqlserver-dev" --format "table {{.Status}}"
if ($containerStatus -like "*Up*") {
    Write-Host "✅ SQL Server is running successfully!" -ForegroundColor Green
    Write-Host "📊 Database: nestmart" -ForegroundColor Cyan
    Write-Host "🔗 Connection: localhost:1433" -ForegroundColor Cyan
    Write-Host "👤 Username: appuser (no password)" -ForegroundColor Cyan
    Write-Host "👤 Admin: sa / sqladmin123!" -ForegroundColor Cyan
    Write-Host ""
    Write-Host "You can now run your NetBeans project!" -ForegroundColor Green
} else {
    Write-Host "❌ SQL Server failed to start. Check logs with:" -ForegroundColor Red
    Write-Host "docker logs nestmart-sqlserver-dev" -ForegroundColor Yellow
}
