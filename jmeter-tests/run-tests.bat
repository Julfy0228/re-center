@echo off
REM ========================================
REM Lab4 JMeter Tests - Automated Runner
REM ========================================
REM Требует: JMeter в PATH, бэкенд на localhost:8080

setlocal enabledelayedexpansion

cd /d "%~dp0"

echo.
echo ========================================
echo   Lab4 JMeter Automated Test Runner
echo ========================================
echo.

REM Проверка JMeter
echo [*] Проверка JMeter...
jmeter --version >nul 2>&1
if errorlevel 1 (
    echo [ERROR] JMeter не найден в PATH
    echo.
    echo Установите переменную окружения JMETER_HOME:
    echo   [Environment]::SetEnvironmentVariable("JMETER_HOME", "C:\Apache\JMeter", "User")
    echo.
    pause
    exit /b 1
)
echo [OK] JMeter найден
echo.

REM Запуск PowerShell скрипта
echo [*] Запуск тестов...
echo.

powershell -NoProfile -ExecutionPolicy Bypass -File ".\run-tests.ps1"

if errorlevel 1 (
    echo.
    echo [ERROR] Тесты завершились с ошибкой
    pause
    exit /b 1
)

echo.
echo [OK] Все тесты завершены!
echo Результаты в папке: results\
echo.
pause
