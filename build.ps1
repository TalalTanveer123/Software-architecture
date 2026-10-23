# Depot Manager – Build & Run Script (Windows PowerShell)
# Run from the DepotManager directory

$SRC    = "src"
$TEST   = "test"
$BIN    = "bin"
$LIB    = "lib"
$MAIN   = "app.DepotManagerApp"

# Download JUnit 5 standalone jar if not present
if (-not (Test-Path "$LIB\junit-platform-console-standalone.jar")) {
    Write-Host "Downloading JUnit 5 standalone console runner..."
    New-Item -ItemType Directory -Force -Path $LIB | Out-Null
    Invoke-WebRequest -Uri "https://repo1.maven.org/maven2/org/junit/platform/junit-platform-console-standalone/1.10.2/junit-platform-console-standalone-1.10.2.jar" `
        -OutFile "$LIB\junit-platform-console-standalone.jar"
}

$JUNIT = "$LIB\junit-platform-console-standalone.jar"

# Compile source
New-Item -ItemType Directory -Force -Path $BIN | Out-Null
Write-Host "Compiling sources..."
$srcFiles = Get-ChildItem -Recurse -Path $SRC -Filter "*.java" | ForEach-Object { $_.FullName }
javac -d $BIN -cp $BIN ($srcFiles -join " ")

# Compile tests
Write-Host "Compiling tests..."
$testFiles = Get-ChildItem -Recurse -Path $TEST -Filter "*.java" | ForEach-Object { $_.FullName }
javac -d $BIN -cp "$BIN;$JUNIT" ($testFiles -join " ")

Write-Host ""
Write-Host "=== Running JUnit tests ==="
java -cp "$BIN;$JUNIT" org.junit.platform.console.standalone.ConsoleLauncher --scan-classpath --classpath=$BIN

Write-Host ""
Write-Host "=== Launching Application ==="
java -cp $BIN $MAIN
