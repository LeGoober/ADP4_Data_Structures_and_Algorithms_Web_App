@echo off
rem Launches the ADP470S DSA Visualiser (needs Java 21 or newer).
where java >nul 2>nul
if errorlevel 1 (
  echo Java 21 or newer was not found on this computer.
  echo Use the web version instead: see the link in README.md
  pause
  exit /b 1
)
java -jar "%~dp0DSA-Visualiser.jar"
