# Database Initialization Script

$MYSQL_HOST = "localhost"
$MYSQL_PORT = "3306"
$MYSQL_USER = "root"
$MYSQL_PASS = "123456"
$DB_NAME = "takeout_system"

Write-Host "============================================"
Write-Host "Takeout System Database Initialization"
Write-Host "============================================"
Write-Host ""

$MYSQL_PATHS = @(
    "C:\Program Files\MySQL\MySQL Server 8.0\bin\mysql.exe",
    "C:\Program Files\MySQL\MySQL Server 8.4\bin\mysql.exe",
    "C:\Program Files\MySQL\MySQL Server 5.7\bin\mysql.exe"
)

$MYSQL_EXE = $null
foreach ($path in $MYSQL_PATHS) {
    if (Test-Path $path) {
        $MYSQL_EXE = $path
        break
    }
}

if ($null -eq $MYSQL_EXE) {
    Write-Host "Error: MySQL executable not found!" -ForegroundColor Red
    Write-Host "Please install MySQL or update path in script."
    pause
    exit 1
}

Write-Host "Found MySQL: $MYSQL_EXE" -ForegroundColor Green
Write-Host "Connecting to MySQL server..." -ForegroundColor Yellow
Write-Host ""

$SCRIPT_DIR = Split-Path -Parent $MyInvocation.MyCommand.Path

Write-Host "[1/2] Creating database and tables..." -ForegroundColor Yellow
$CREATE_SQL = Join-Path $SCRIPT_DIR "01_create_tables.sql"
$argList = @("-h$MYSQL_HOST", "-P$MYSQL_PORT", "-u$MYSQL_USER", "-p$MYSQL_PASS", "--default-character-set=utf8mb4")
$process = Start-Process -FilePath $MYSQL_EXE -ArgumentList $argList -RedirectStandardInput $CREATE_SQL -NoNewWindow -Wait -PassThru

if ($process.ExitCode -ne 0) {
    Write-Host "Error: Failed to execute create table script!" -ForegroundColor Red
    pause
    exit 1
}
Write-Host "Create table script executed successfully!" -ForegroundColor Green
Write-Host ""

Write-Host "[2/2] Inserting initial data..." -ForegroundColor Yellow
$INSERT_SQL = Join-Path $SCRIPT_DIR "02_insert_data.sql"
$argList = @("-h$MYSQL_HOST", "-P$MYSQL_PORT", "-u$MYSQL_USER", "-p$MYSQL_PASS", "--default-character-set=utf8mb4")
$process = Start-Process -FilePath $MYSQL_EXE -ArgumentList $argList -RedirectStandardInput $INSERT_SQL -NoNewWindow -Wait -PassThru

if ($process.ExitCode -ne 0) {
    Write-Host "Error: Failed to execute insert data script!" -ForegroundColor Red
    pause
    exit 1
}
Write-Host "Insert data script executed successfully!" -ForegroundColor Green
Write-Host ""

Write-Host "============================================"
Write-Host "Database initialization completed!" -ForegroundColor Green
Write-Host "============================================"
Write-Host ""
Write-Host "Database name: $DB_NAME"
Write-Host ""
Write-Host "Test accounts:" -ForegroundColor Yellow
Write-Host "- Frontend users: test1/123456, test2/123456, test3/123456"
Write-Host "- Shop admins: shop_admin1/123(KFC), shop_admin2/123(McDonald's), shop_admin3/123(Starbucks), shop_admin4/123(Pizza Hut), shop_admin5/123(Heytea)"
Write-Host "- Platform admin: admin/123456"
Write-Host ""

pause
