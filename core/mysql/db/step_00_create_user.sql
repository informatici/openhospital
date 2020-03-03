#login as root

# Substitute '%' or whatever with host name or IP address.

# MySQL does not provide a "DROP USER IF EXIST" statement so
# this is a workaround to create the user and grant 
# him permissions on the 'oh' database.
GRANT ALL ON oh.* TO 'isf'@'localhost' IDENTIFIED BY 'isf123';
GRANT ALL ON oh.* TO 'isf'@'%' IDENTIFIED BY 'isf123';
