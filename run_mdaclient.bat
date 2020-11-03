@echo off

rem the MDA client jar takes a Reel Identifier as an argument
rem it then looks in SQL tables and sends any unsent telemetry records.

rem No dependency on the mdaservice

set /p REEL_IDENTIFIER="Enter the Reel Identifier: "

java -jar mdaclient\target\mdaclient-{VERSION}.jar %REEL_IDENTIFIER%

pause
