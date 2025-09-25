# Script to stop SQL Server
Write-Host "Stopping SQL Server..." -ForegroundColor Yellow
docker-compose -f docker-compose.dev.yml down
Write-Host "✅ SQL Server stopped!" -ForegroundColor Green
