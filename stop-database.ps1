# Script dừng database
Write-Host "Dừng SQL Server..." -ForegroundColor Yellow
docker-compose -f docker-compose.dev.yml down
Write-Host "✅ Đã dừng!" -ForegroundColor Green
