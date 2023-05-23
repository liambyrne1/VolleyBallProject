:: Substitute XXX for SuperUser Password. 
set PGPASSWORD=XXX

:: psql is a PostgreSQL command.
:: Assumes PostgreSQL bin directory is on the execution path.

:: Assumes code from Git is downloaded to 'C:\projectdir'.
psql -h localhost -U postgres -f C:\projectdir\VolleyBallProject\database\InitVolleyballTest.sql

