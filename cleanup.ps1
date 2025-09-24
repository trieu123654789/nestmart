# Quick cleanup for local project folder size
param(
	[switch]$DryRun = $false,
	[int]$Top = 20
)

Write-Host "Scanning for largest directories and files..." -ForegroundColor Cyan

# Top directories by size
Get-ChildItem -Directory -Force | ForEach-Object {
	$size = (Get-ChildItem -Recurse -Force -ErrorAction SilentlyContinue $_.FullName | Measure-Object -Property Length -Sum).Sum
	[PSCustomObject]@{ Name = $_.Name; SizeBytes = ($size | ForEach-Object { if($_){$_} else {0}}) }
} |
	Sort-Object SizeBytes -Descending |
	Select-Object -First $Top |
	ForEach-Object {
		$gb = if($_.SizeBytes){ [math]::Round($_.SizeBytes/1GB,3) } else { 0 }
		"DIR`t{0}`t{1} GB" -f $_.Name, $gb
	}

# Top files by size
Get-ChildItem -File -Force -Recurse -ErrorAction SilentlyContinue |
	Sort-Object Length -Descending |
	Select-Object -First $Top |
	ForEach-Object {
		$gb = [math]::Round($_.Length/1GB,3)
		"FILE`t{0}`t{1} GB" -f $_.FullName, $gb
	}

Write-Host "\nCleanup targets:" -ForegroundColor Yellow
$targets = @(
	"build",
	"dist",
	"out",
	"uploads",
	"*.war",
	"*.zip"
)
$targets | ForEach-Object { Write-Host " - $_" }

if ($DryRun) {
	Write-Host "\nDry-run enabled. No deletions performed." -ForegroundColor Green
	return
}

function Remove-IfExists {
	param([string]$Path)
	if (Test-Path $Path) {
		Write-Host "Deleting $Path ..." -ForegroundColor Red
		Remove-Item -Recurse -Force -ErrorAction SilentlyContinue $Path
	}
}

# Remove directories
Remove-IfExists "build"
Remove-IfExists "dist"
Remove-IfExists "out"
Remove-IfExists "uploads"

# Remove large archives in repo root
Get-ChildItem -Path . -Include *.war,*.zip -Force -File -Recurse -ErrorAction SilentlyContinue |
	ForEach-Object {
		Write-Host ("Deleting file {0}" -f $_.FullName) -ForegroundColor Red
		Remove-Item -Force -ErrorAction SilentlyContinue $_.FullName
	}

Write-Host "\nCleanup complete." -ForegroundColor Green

# Show size again
Write-Host "\nLargest items after cleanup:" -ForegroundColor Cyan
Get-ChildItem -Directory -Force | ForEach-Object {
	$size = (Get-ChildItem -Recurse -Force -ErrorAction SilentlyContinue $_.FullName | Measure-Object -Property Length -Sum).Sum
	[PSCustomObject]@{ Name = $_.Name; SizeBytes = ($size | ForEach-Object { if($_){$_} else {0}}) }
} |
	Sort-Object SizeBytes -Descending |
	Select-Object -First $Top |
	ForEach-Object {
		$gb = if($_.SizeBytes){ [math]::Round($_.SizeBytes/1GB,3) } else { 0 }
		"DIR`t{0}`t{1} GB" -f $_.Name, $gb
	}


