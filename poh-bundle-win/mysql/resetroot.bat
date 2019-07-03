@echo off
echo USE mysql; >resetroot.sql
echo. >>resetroot.sql
echo INSERT IGNORE INTO user VALUES ('localhost','root', '','Y','Y','Y','Y','Y','Y','Y','Y','Y','Y','Y','Y','Y','Y','Y','Y','Y','Y','Y','Y','Y','','','','',0,0,0); >>resetroot.sql
echo REPLACE INTO user VALUES ('localhost','root', '','Y','Y','Y','Y','Y','Y','Y','Y','Y','Y','Y','Y','Y','Y','Y','Y','Y','Y','Y','Y','Y','','','','',0,0,0); >>resetroot.sql
echo INSERT IGNORE INTO user VALUES ('localhost','pma', '','N','N','N','N','N','N','N','Y','N','N','N','N','N','N','N','N','N','N','N','N','N','','','','',0,0,0); >>resetroot.sql
echo REPLACE INTO user VALUES ('localhost','pma', '','N','N','N','N','N','N','N','Y','N','N','N','N','N','N','N','N','N','N','N','N','N','','','','',0,0,0); >>resetroot.sql

bin\mysqld.exe --no-defaults --bind-address=127.0.0.1 --bootstrap --console --skip-grant-tables --skip-innodb --standalone <resetroot.sql >nul
del resetroot.sql
echo.
echo Passwoerter fuer Benutzer "root" und "pma" wurden geloescht.
echo Passwords for user "root" and "pma" were deleted.
echo.
pause
