@echo off
cd C:\Users\rrber\projetos\fcs-interview-code-assignment-main\java-assignment
echo Checking git status...
git status
echo.
echo Adding all files...
git add -A
echo.
echo Committing...
git commit -m "docs: Final status and complete documentation"
echo.
echo Pushing to GitHub...
git push origin main
echo.
echo All done!
pause

