Create database mydb;
CREATE USER 'bage'@'%' IDENTIFIED BY 'bage';
grant all privileges on mydb.* to 'bage'@'%';
ALTER user 'bage'@'%' IDENTIFIED BY 'bage' PASSWORD EXPIRE NEVER;
ALTER user 'bage'@'%' IDENTIFIED WITH mysql_native_password BY 'bage';
FLUSH PRIVILEGES;


