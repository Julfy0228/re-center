param(
    [string]$BaseUrl = "http://localhost:8080/re-center/api",
    [string]$BackendPath = "..\..\backend",
    [string]$ResultsDir = ".\results"
)

$colors = @{
    Success = "Green"
    Error = "Red"
    Info = "Cyan"
    Warning = "Yellow"
}

function Write-Log {
    param([string]$Message, [string]$Level = "Info")
    $color = $colors[$Level]
    Write-Host "[$(Get-Date -Format 'HH:mm:ss')] [$Level] $Message" -ForegroundColor $color
}

Write-Log "Проверка доступности бэкенда..." "Info"
$maxRetries = 30
$retryCount = 0
$backendReady = $false

while ($retryCount -lt $maxRetries) {
    try {
        $response = Invoke-RestMethod -Uri "$BaseUrl/services" -Method Get -ErrorAction Stop
        $backendReady = $true
        Write-Log "Бэкенд доступен!" "Success"
        break
    } catch {
        $retryCount++
        if ($retryCount -lt $maxRetries) {
            Write-Log "Попытка $retryCount/$maxRetries. Ожидание бэкенда..." "Warning"
            Start-Sleep -Seconds 2
        }
    }
}

if (-not $backendReady) {
    Write-Log "Бэкенд не запущен!" "Error"
    exit 1
}

Write-Log "Очистка базы данных..." "Info"
$dbPath = Join-Path $BackendPath "data\db*"
try {
    Remove-Item -Path $dbPath -Force -ErrorAction SilentlyContinue
    Write-Log "БД очищена" "Success"
} catch {
    Write-Log "Ошибка при очистке БД: $_" "Warning"
}

Start-Sleep -Seconds 3

Write-Log "Подготовка тестовых данных..." "Info"

Write-Log "Регистрация обычных пользователей..." "Info"
$userRows = Import-Csv -Path ".\users.csv"
foreach ($row in $userRows) {
    $body = @{
        email = $row.email
        password = $row.password
        firstName = $row.firstName
        lastName = $row.lastName
        middleName = $row.middleName
        phoneNumber = $row.phoneNumber
    } | ConvertTo-Json

    try {
        $response = Invoke-RestMethod -Uri "$BaseUrl/auth/register" -Method Post -ContentType "application/json" -Body $body -ErrorAction Stop
        Write-Log "Зарегистрирован: $($row.email)" "Success"
    } catch {
        Write-Log "Ошибка регистрации $($row.email): $($_.Exception.Message)" "Warning"
    }
}

Write-Log "Регистрация администраторов..." "Info"
$adminRows = Import-Csv -Path ".\admin-users.csv"
foreach ($row in $adminRows) {
    $body = @{
        email = $row.email
        password = $row.password
        firstName = $row.firstName
        lastName = $row.lastName
        middleName = $row.middleName
        phoneNumber = $row.phoneNumber
    } | ConvertTo-Json

    try {
        $response = Invoke-RestMethod -Uri "$BaseUrl/auth/register" -Method Post -ContentType "application/json" -Body $body -ErrorAction Stop
        Write-Log "Зарегистрирован админ: $($row.email)" "Success"
    } catch {
        Write-Log "Ошибка регистрации админа $($row.email): $($_.Exception.Message)" "Warning"
    }
}

if (-not (Test-Path $ResultsDir)) {
    New-Item -ItemType Directory -Path $ResultsDir | Out-Null
}

$tests = @(
    @{ Name = "Booking"; File = "booking.jmx" },
    @{ Name = "Admin"; File = "admin.jmx" },
    @{ Name = "Profile"; File = "profile.jmx" }
)

$testResults = @()

foreach ($test in $tests) {
    Write-Log "Запуск теста: $($test.Name)" "Info"
    
    $timestamp = Get-Date -Format "yyyyMMdd_HHmmss"
    $resultFile = Join-Path $ResultsDir "$($test.Name -replace ' ', '_')_$timestamp.jtl"
    $logFile = Join-Path $ResultsDir "$($test.Name -replace ' ', '_')_$timestamp.log"
    
    try {
        $startTime = Get-Date
        
        & jmeter -n -t $test.File -l $resultFile -j $logFile -Jhost=localhost -Jport=8080 -JcontextPath=/re-center
        
        $endTime = Get-Date
        $duration = ($endTime - $startTime).TotalSeconds
        
        $results = @()
        if (Test-Path $resultFile) {
            $results = Import-Csv -Path $resultFile
            $successCount = ($results | Where-Object { $_.success -eq "true" }).Count
            $failCount = ($results | Where-Object { $_.success -eq "false" }).Count
            $totalCount = $results.Count
            
            $testResults += @{
                Name = $test.Name
                Total = $totalCount
                Success = $successCount
                Failed = $failCount
                Duration = $duration
                ResultFile = $resultFile
            }
            
            Write-Log "Тест завершён: $successCount успешно, $failCount ошибок из $totalCount" "Success"
        }
    } catch {
        Write-Log "Ошибка при запуске теста $($test.Name): $_" "Error"
        $testResults += @{
            Name = $test.Name
            Status = "ERROR"
            Error = $_.Exception.Message
        }
    }
}

Write-Log "========== ИТОГИ ТЕСТИРОВАНИЯ ==========" "Info"
foreach ($result in $testResults) {
    if ($result.Status -eq "ERROR") {
        Write-Log "$($result.Name): ОШИБКА - $($result.Error)" "Error"
    } else {
        $successRate = if ($result.Total -gt 0) { [math]::Round(($result.Success / $result.Total) * 100, 2) } else { 0 }
        Write-Log "$($result.Name): $($result.Success)/$($result.Total) успешно ($successRate%) за $([math]::Round($result.Duration, 2))s" "Success"
    }
}

Write-Log "Результаты сохранены в: $ResultsDir" "Info"
Write-Log "========================================" "Info"
