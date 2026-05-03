@echo off
setlocal

REM === SeniorHub Local Runner ===
REM Uses the bundled JDK inside the project folder.
REM No system-wide Java installation required.

set "PROJECT_DIR=%~dp0"
set "JDK_BIN=%PROJECT_DIR%jdk\bin"

REM Verify bundled JDK exists
if not exist "%JDK_BIN%\javac.exe" (
    echo [ERROR] Bundled JDK not found at: %JDK_BIN%
    echo         Make sure the 'jdk' folder is present in the project directory.
    pause
    exit /b 1
)

REM Change into the project directory so relative paths work correctly
cd /d "%PROJECT_DIR%"

echo [1/2] Compiling SeniorHubApp.java with bundled JDK 25...
"%JDK_BIN%\javac.exe" SeniorHubApp.java
if errorlevel 1 (
    echo [ERROR] Compilation failed. Check the output above for details.
    pause
    exit /b 1
)
echo       Compilation successful!

echo [2/2] Launching SeniorHub...
"%JDK_BIN%\javaw.exe" -cp . SeniorHubApp

endlocal