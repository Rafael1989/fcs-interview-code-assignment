@echo off
setlocal enabledelayedexpansion
cd /d "C:\Users\rrber\projetos\fcs-interview-code-assignment-main"

REM Check git status
echo ===== GIT STATUS =====
git status

REM Add all changes
echo.
echo ===== GIT ADD =====
git add .

REM Commit with message
echo.
echo ===== GIT COMMIT =====
git commit -m "Add system monitoring dashboard - CPU, Memory, Threads metrics with real-time visualization"

REM Push to remote
echo.
echo ===== GIT PUSH =====
git push

echo.
echo ===== DONE =====

