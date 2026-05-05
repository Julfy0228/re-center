@echo off
setlocal enabledelayedexpansion

set PROFILE=
set SPRING_ARGS=
if "%1"=="--jmeter-test" (
    set PROFILE=-Pjmeter-test
    set SPRING_ARGS=-Dspring.profiles.active=jmeter-test
    echo 🧪 Запуск в режиме JMeter тестирования (in-memory БД)
)

call mvn clean
call mkdir "target"
call mkdir "target/frontend-build"
call mvn package cargo:run -DskipTests !PROFILE! !SPRING_ARGS!