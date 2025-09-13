# Robust Docker Build Script for NestMart Application
# This script validates the WAR file and ensures reliable Docker builds

param(
    [string]$ImageName = "nestmart",
    [string]$Tag = "latest",
    [switch]$NoCache = $false,
    [string]$DockerfilePath = "Dockerfile"
)

Write-Host "🚀 Starting robust Docker build for NestMart..." -ForegroundColor Green

# Step 1: Validate WAR file exists
$warFile = "nestmartappFinal.war"
if (!(Test-Path $warFile)) {
    Write-Error "❌ WAR file '$warFile' not found in current directory!"
    Write-Host "Current directory: $(Get-Location)" -ForegroundColor Yellow
    Write-Host "Available files:" -ForegroundColor Yellow
    Get-ChildItem -Name "*.war"
    exit 1
}

# Step 2: Validate WAR file size and accessibility
$warInfo = Get-Item $warFile
Write-Host "✅ Found WAR file: $($warInfo.Name)" -ForegroundColor Green
Write-Host "   Size: $([math]::Round($warInfo.Length / 1MB, 2)) MB" -ForegroundColor Green
Write-Host "   Last Modified: $($warInfo.LastWriteTime)" -ForegroundColor Green

# Step 3: Check if WAR file is locked by another process
try {
    $fileStream = [System.IO.File]::Open($warFile, "Open", "Read", "ReadWrite")
    $fileStream.Close()
    Write-Host "✅ WAR file is accessible (not locked)" -ForegroundColor Green
} catch {
    Write-Error "❌ WAR file appears to be locked by another process: $($_.Exception.Message)"
    exit 1
}

# Step 4: Validate Dockerfile exists
if (!(Test-Path $DockerfilePath)) {
    Write-Error "❌ Dockerfile not found at: $DockerfilePath"
    exit 1
}

Write-Host "✅ Dockerfile found: $DockerfilePath" -ForegroundColor Green

# Step 5: Clean Docker build cache if requested
if ($NoCache) {
    Write-Host "🧹 Cleaning Docker build cache..." -ForegroundColor Yellow
    docker builder prune -f | Out-Null
}

# Step 6: Build Docker image
$buildArgs = @(
    "build"
    "-t", "$ImageName`:$Tag"
    "-f", $DockerfilePath
)

if ($NoCache) {
    $buildArgs += "--no-cache"
}

$buildArgs += "."

Write-Host "🔨 Building Docker image..." -ForegroundColor Blue
Write-Host "Command: docker $($buildArgs -join ' ')" -ForegroundColor Gray

$buildResult = & docker @buildArgs

if ($LASTEXITCODE -eq 0) {
    Write-Host "✅ Docker image built successfully!" -ForegroundColor Green
    Write-Host "Image: $ImageName`:$Tag" -ForegroundColor Green
    
    # Step 7: Verify the image was created
    $imageCheck = docker images --filter "reference=$ImageName`:$Tag" --format "table {{.Repository}}:{{.Tag}}\t{{.Size}}\t{{.CreatedAt}}"
    Write-Host "`n📋 Image Information:" -ForegroundColor Cyan
    Write-Host $imageCheck -ForegroundColor White
    
} else {
    Write-Error "❌ Docker build failed with exit code: $LASTEXITCODE"
    Write-Host "`n🔍 Troubleshooting tips:" -ForegroundColor Yellow
    Write-Host "1. Ensure the WAR file is not being used by another process" -ForegroundColor Yellow
    Write-Host "2. Try running with -NoCache flag to avoid cache issues" -ForegroundColor Yellow
    Write-Host "3. Check Docker daemon is running and has sufficient resources" -ForegroundColor Yellow
    exit $LASTEXITCODE
}

Write-Host "`n🎉 Build completed successfully!" -ForegroundColor Green
