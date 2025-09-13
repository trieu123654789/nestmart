# Simple Docker Build Script for NestMart Application
param(
    [string]$ImageName = "nestmart",
    [string]$Tag = "latest",
    [switch]$NoCache = $false
)

Write-Host "Starting Docker build for NestMart..." -ForegroundColor Green

# Validate WAR file exists
$warFile = "nestmartappFinal.war"
if (!(Test-Path $warFile)) {
    Write-Error "WAR file '$warFile' not found!"
    exit 1
}

$warInfo = Get-Item $warFile
Write-Host "Found WAR file: $($warInfo.Name) ($([math]::Round($warInfo.Length / 1MB, 2)) MB)" -ForegroundColor Green

# Build Docker image
$buildArgs = @("build", "-t", "$ImageName`:$Tag")
if ($NoCache) {
    $buildArgs += "--no-cache"
}
$buildArgs += "."

Write-Host "Building Docker image..." -ForegroundColor Blue
& docker @buildArgs

if ($LASTEXITCODE -eq 0) {
    Write-Host "Docker image built successfully: $ImageName`:$Tag" -ForegroundColor Green
} else {
    Write-Error "Docker build failed with exit code: $LASTEXITCODE"
    exit $LASTEXITCODE
}
