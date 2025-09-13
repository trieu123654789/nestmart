# Post-build script to copy WAR file for Docker builds
# Run this after NetBeans "Clean and Build"

Write-Host "Post-build: Copying WAR file for Docker..." -ForegroundColor Green

# Check if dist WAR exists
$distWar = "dist\nestmartappFinal.war"
$rootWar = "nestmartappFinal.war"

if (Test-Path $distWar) {
    $distInfo = Get-Item $distWar
    Write-Host "Found fresh WAR: $($distInfo.Name) ($([math]::Round($distInfo.Length / 1MB, 2)) MB)" -ForegroundColor Green
    
    # Copy to root for Docker
    Copy-Item $distWar $rootWar -Force
    Write-Host "Copied WAR to root directory for Docker builds" -ForegroundColor Green
    
    # Verify copy
    $rootInfo = Get-Item $rootWar
    Write-Host "Root WAR updated: $($rootInfo.LastWriteTime)" -ForegroundColor Green
    
} else {
    Write-Warning "No WAR file found in dist/ directory. Please run 'Clean and Build' in NetBeans first."
    exit 1
}

Write-Host "Ready for Docker build!" -ForegroundColor Cyan
