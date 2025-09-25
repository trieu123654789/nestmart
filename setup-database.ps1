# Script don gian de setup database cho bat ky ai
Write-Host "Khoi dong SQL Server cho NestMart..." -ForegroundColor Green

# Dừng container cũ nếu có
Write-Host "Dừng container cũ..." -ForegroundColor Yellow
docker-compose -f docker-compose.dev.yml down 2>$null

# Khởi động SQL Server
Write-Host "Khởi động SQL Server..." -ForegroundColor Yellow
docker-compose -f docker-compose.dev.yml up -d

# Đợi SQL Server khởi động
Write-Host "Đợi SQL Server khởi động (30 giây)..." -ForegroundColor Yellow
Start-Sleep -Seconds 30

# Tạo database và import schema
Write-Host "Tạo database và import schema..." -ForegroundColor Yellow
docker exec nestmart-sqlserver-dev /opt/mssql-tools18/bin/sqlcmd -S localhost -U sa -P 'sqladmin123!' -C -i /docker-entrypoint-initdb.d/nestmart.sql

Write-Host ""
Write-Host "HOAN THANH!" -ForegroundColor Green
Write-Host "Database: nestmart" -ForegroundColor Cyan
Write-Host "Server: localhost:1433" -ForegroundColor Cyan
Write-Host "Username: sa" -ForegroundColor Cyan
Write-Host "Password: sqladmin123!" -ForegroundColor Cyan
Write-Host ""
Write-Host "Bay gio ban co the chay NetBeans project!" -ForegroundColor Green
