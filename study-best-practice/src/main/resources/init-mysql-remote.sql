Create database mydbpro;
CREATE USER 'bage'@'%' IDENTIFIED BY 'bage.1234';
grant all privileges on mydbpro.* to 'bage'@'%';
ALTER user 'bage'@'%' IDENTIFIED BY 'bage.1234' PASSWORD EXPIRE NEVER;
ALTER user 'bage'@'%' IDENTIFIED WITH mysql_native_password BY 'bage';
FLUSH PRIVILEGES;


