delete from auth_path_definition;
INSERT INTO auth_path_definition
	(id, ant_path, definition)
VALUES
	(1, '/api1/**', 'authc,roles[admin]');
INSERT INTO auth_path_definition
	(id, ant_path, definition)
VALUES
	(2, '/api2/**', 'authc,roles[user]');
INSERT INTO auth_path_definition
	(id, ant_path, definition)
VALUES
	(3, '/api3/**', 'authc,roles[admin,user]');
INSERT INTO auth_path_definition
	(id, ant_path, definition)
VALUES
	(4, '/api4/**', 'authc,roles[user],perms["write,read"]');
INSERT INTO auth_path_definition
	(id, ant_path, definition)
VALUES
	(5, '/api5/**', 'authc,roles[user],perms["read"]');
