@echo off
chcp 65001 > nul
echo ==========================================================
echo   Compilando e Iniciando Interfaz Grafica de Formula 1...
echo ==========================================================

if not exist "demo\bin" mkdir "demo\bin"

dir /s /b demo\src\main\java\*.java > sources.txt
javac -encoding UTF-8 -d demo\bin @sources.txt
del sources.txt

if %ERRORLEVEL% EQU 0 (
    start javaw -cp demo\bin com.formula1.Main
    echo.
    echo [OK] ¡Interfaz Grafica iniciada correctamente en pantalla!
) else (
    echo.
    echo [ERROR] Hubo un problema al compilar el proyecto.
    pause
)
