@echo off
set MYSQL="C:\Program Files\MySQL\MySQL Server 8.0\bin\mysql.exe"
set PASS=Hospital@123

echo Running schema.sql...
%MYSQL% -u root -p%PASS% < database\schema.sql
if %errorlevel% neq 0 (echo Schema failed, but continuing...) else (echo Schema OK!)

echo Running seed.sql...
%MYSQL% -u root -p%PASS% smart_campus < database\seed.sql
if %errorlevel% neq 0 (echo Seed failed!) else (echo Seed OK!)

echo.
echo Verifying data...
%MYSQL% -u root -p%PASS% -e "SELECT COUNT(*) as users FROM smart_campus.users; SELECT COUNT(*) as roles FROM smart_campus.roles;"
echo Done!
pause
