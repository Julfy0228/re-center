@echo off
setlocal enabledelayedexpansion

set PROFILE=
set SPRING_ARGS=
if "%1"=="--test-data" (
    set PROFILE=-Ptest-data
    set SPRING_ARGS=-Dspring.profiles.active=test-data
    echo 🧪 Запуск в режиме инициализации тестовых данных (in-memory БД)
)

call mvn clean
call mkdir "target"
call mkdir "target/frontend-build"
call mvn package cargo:run -DskipTests !PROFILE! !SPRING_ARGS!