@echo off
chcp 65001 > nul
echo ===================================================
echo   Compilando y Ejecutando Pruebas Unitarias...
echo ===================================================

if not exist "demo\bin" mkdir "demo\bin"

dir /s /b demo\src\*.java > sources.txt
javac -encoding UTF-8 -d demo\bin @sources.txt
del sources.txt

if %ERRORLEVEL% EQU 0 (
    java -cp demo\bin com.formula1.SimulacionTest
) else (
    echo.
    echo [ERROR] Hubo un problema al compilar las pruebas.
)
pause
