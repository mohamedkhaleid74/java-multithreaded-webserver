@echo off
echo ==============================================
echo       Starting Java Multi-Threaded Server
echo ==============================================
echo.

:: Create a bin directory for the compiled classes if it doesn't exist
if not exist bin mkdir bin

echo [1/2] Compiling Java files...
javac -d bin src\com\webserver\*.java

:: Check if compilation was successful
IF %ERRORLEVEL% NEQ 0 (
    echo.
    echo [ERROR] Compilation failed! Please check your code.
    pause
    exit /b %ERRORLEVEL%
)

echo [2/2] Compilation successful! Starting the server...
echo.
:: Run the Server class
java -cp bin com.webserver.Server

pause
