@echo off
setlocal enabledelayedexpansion

echo ===============================
echo    PAYNOW QR CODE GENERATOR
echo ===============================

:: Get required parameters
set /p PAYNOWID=Enter PayNow ID (e.g. 196300260DDBS): 
set /p REFERENCE=Enter Payment Reference (e.g. 393214696 / AA658-2000): 
set /p AMOUNT=Enter Amount (e.g. 1826.92): 

:: Optional - Partial Payment
set /p PARTIAL=Allow Partial Payment? (Yes/No) [default: No]: 
if "!PARTIAL!"=="" set PARTIAL=No

:: Optional - Expiry Date
set /p EXPIRY=Enter Expiry Date (YYYYMMDDHHMMSS) [optional]: 

:: Confirm input
echo.
echo Running with:
echo   PayNow ID:         %PAYNOWID%
echo   Reference:         %REFERENCE%
echo   Amount:            %AMOUNT%
echo   Partial Payment:   %PARTIAL%
echo   Expiry Date:       %EXPIRY%
echo.

:: Run the Java JAR
if "!EXPIRY!"=="" (
    java -jar paynowqr-fat.jar "%PAYNOWID%" "%REFERENCE%" "%AMOUNT%" "%PARTIAL%"
) else (
    java -jar paynowqr-fat.jar "%PAYNOWID%" "%REFERENCE%" "%AMOUNT%" "%PARTIAL%" "%EXPIRY%"
)

echo.
pause
