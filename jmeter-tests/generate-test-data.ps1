# Generate test data SQL from CSV files and send to backend
param([string]$BackendUrl = "http://localhost:8080/re-center", [switch]$Cleanup = $false)
$ErrorActionPreference = "Stop"
Write-Host "Test Data Generator" -ForegroundColor Cyan
Write-Host "===================" -ForegroundColor Cyan
Write-Host "`nChecking backend availability..." -ForegroundColor Yellow
try {
    $response = Invoke-WebRequest -Uri "$BackendUrl/api/test-data/status" -UseBasicParsing -ErrorAction Stop
    Write-Host "Backend is running" -ForegroundColor Green
} catch {
    Write-Host "Backend is not running at $BackendUrl" -ForegroundColor Red
    exit 1
}
if ($Cleanup) {
    Write-Host "`nCleaning up existing test data..." -ForegroundColor Yellow
    try {
        $null = Invoke-WebRequest -Uri "$BackendUrl/api/test-data/cleanup" -Method DELETE -UseBasicParsing -ErrorAction Stop
        Write-Host "Test data cleaned up" -ForegroundColor Green
    } catch {
        Write-Host "Cleanup warning: $($_.Exception.Message)" -ForegroundColor Yellow
    }
}
Write-Host "`nReading CSV files..." -ForegroundColor Yellow
$users = Import-Csv "users.csv"
$adminUsers = Import-Csv "admin-users.csv"
Write-Host "Loaded $($users.Count) regular users" -ForegroundColor Green
Write-Host "Loaded $($adminUsers.Count) admin users" -ForegroundColor Green
$sqlStatements = @()
foreach ($user in $users) {
    $sql = "INSERT INTO Users (email, passwordHash, firstName, lastName, middleName, phoneNumber, role) VALUES ('$($user.email.Trim())', 'computeHash($($user.password.Trim()))', '$($user.firstName.Trim())', '$($user.lastName.Trim())', '$($user.middleName.Trim())', '$($user.phoneNumber.Trim())', 'CLIENT');"
    $sqlStatements += $sql
}
foreach ($user in $adminUsers) {
    $sql = "INSERT INTO Users (email, passwordHash, firstName, lastName, middleName, phoneNumber, role) VALUES ('$($user.email.Trim())', 'computeHash($($user.password.Trim()))', '$($user.firstName.Trim())', '$($user.lastName.Trim())', '$($user.middleName.Trim())', '$($user.phoneNumber.Trim())', 'ADMIN');"
    $sqlStatements += $sql
}
$combinedSql = $sqlStatements -join "`n"
Write-Host "Generated $($sqlStatements.Count) SQL statements" -ForegroundColor Green
Write-Host "`nSending SQL to backend..." -ForegroundColor Yellow
$headers = @{"Content-Type" = "application/json"}
$body = @{sql = $combinedSql} | ConvertTo-Json
try {
    $response = Invoke-WebRequest -Uri "$BackendUrl/api/test-data/execute" -Method POST -Headers $headers -Body $body -UseBasicParsing -ErrorAction Stop
    $result = $response.Content | ConvertFrom-Json
    if ($result.status -eq "success") {
        Write-Host "$($result.message)" -ForegroundColor Green
        Write-Host "`nTest data initialized successfully!" -ForegroundColor Green
    } else {
        Write-Host "$($result.message)" -ForegroundColor Red
        exit 1
    }
} catch {
    Write-Host "Failed to send SQL to backend" -ForegroundColor Red
    exit 1
}
Write-Host "`nAuthenticating as admin..." -ForegroundColor Yellow
$adminEmail = ($adminUsers | Select-Object -First 1).email.Trim()
$adminPassword = ($adminUsers | Select-Object -First 1).password.Trim()
$loginBody = @{
    email = $adminEmail
    password = $adminPassword
} | ConvertTo-Json
try {
    $loginResponse = Invoke-WebRequest -Uri "$BackendUrl/api/auth/login" -Method POST -Headers $headers -Body $loginBody -UseBasicParsing -ErrorAction Stop
    $loginResult = $loginResponse.Content | ConvertFrom-Json
    $token = $loginResult.token
    Write-Host "Authenticated as $adminEmail" -ForegroundColor Green
} catch {
    Write-Host "Failed to authenticate: $($_.Exception.Message)" -ForegroundColor Red
    exit 1
}

$authHeaders = @{
    "Content-Type" = "application/json"
    "Authorization" = "Bearer $token"
}

Write-Host "`nCreating categories..." -ForegroundColor Yellow
$categories = @(
    @{name = "Домики"; description = "Аренда уютных домиков на берегу озера"},
    @{name = "Развлечения"; description = "Активные развлечения и экскурсии"},
    @{name = "Спа и Wellness"; description = "Спа услуги, массажи и оздоровительные процедуры"}
)

foreach ($category in $categories) {
    $categoryBody = @{
        name = $category.name
        description = $category.description
    } | ConvertTo-Json
    
    try {
        $response = Invoke-WebRequest -Uri "$BackendUrl/api/categories" -Method POST -Headers $authHeaders -Body $categoryBody -UseBasicParsing -ErrorAction Stop
        Write-Host "Created category: $($category.name)" -ForegroundColor Green
    } catch {
        Write-Host "Failed to create category $($category.name): $($_.Exception.Message)" -ForegroundColor Red
    }
}

Write-Host "`nCreating services..." -ForegroundColor Yellow
$services = Import-Csv "services.csv"

foreach ($service in $services) {
    $serviceBody = @{
        title = $service.title.Trim()
        description = $service.description.Trim()
        price = [decimal]$service.price.Trim()
        duration = [int]$service.duration.Trim()
        maxPeople = [int]$service.maxCapacity.Trim()
        categoryId = 1
        imageUrl = ""
    } | ConvertTo-Json
    
    try {
        $response = Invoke-WebRequest -Uri "$BackendUrl/api/services" -Method POST -Headers $authHeaders -Body $serviceBody -UseBasicParsing -ErrorAction Stop
        Write-Host "Created service: $($service.title.Trim())" -ForegroundColor Green
    } catch {
        Write-Host "Failed to create service $($service.title.Trim()): $($_.Exception.Message)" -ForegroundColor Red
    }
}
Write-Host "`nAll test data created successfully!" -ForegroundColor Green
