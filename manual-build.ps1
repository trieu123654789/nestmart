# Manual build script for nestmartappFinal
Write-Host "Building nestmartappFinal manually..."

# Create build directories
New-Item -ItemType Directory -Force -Path "build\web\WEB-INF\classes" | Out-Null
New-Item -ItemType Directory -Force -Path "dist" | Out-Null

# Copy web resources
Write-Host "Copying web resources..."
Copy-Item -Recurse -Force "web\*" "build\web\"

# Create classpath from lib directory
$libPath = "lib\*.jar"
$libFiles = Get-ChildItem -Path $libPath
$classpath = ($libFiles | ForEach-Object { $_.FullName }) -join ";"

Write-Host "Classpath: $classpath"

# Compile Java sources
Write-Host "Compiling Java sources..."
$javaFiles = Get-ChildItem -Recurse -Path "src" -Include "*.java" | ForEach-Object { $_.FullName }

if ($javaFiles) {
    $javaFilesString = $javaFiles -join " "
    $compileCommand = "javac -cp `"$classpath`" -d build\web\WEB-INF\classes $javaFilesString"
    Write-Host "Compile command: $compileCommand"
    
    try {
        Invoke-Expression $compileCommand
        Write-Host "Java compilation completed."
    } catch {
        Write-Host "Java compilation failed: $_"
        exit 1
    }
} else {
    Write-Host "No Java files found in src directory."
}

# Create WAR file
Write-Host "Creating WAR file..."
Set-Location "build\web"
jar -cvf "..\..\dist\nestmartappFinal.war" *
Set-Location "..\..\"

if (Test-Path "dist\nestmartappFinal.war") {
    Write-Host "WAR file created successfully: dist\nestmartappFinal.war"
    Write-Host "File size: $((Get-Item 'dist\nestmartappFinal.war').Length) bytes"
} else {
    Write-Host "Failed to create WAR file."
    exit 1
}

Write-Host "Build completed successfully!"
