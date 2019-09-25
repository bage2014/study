delete from auth_path_definition;
INSERT INTO auth_path_definition
	(id, ant_path, definition)
VALUES
	(1, '/api1/**', 'authc,roles[admin]');
INSERT INTO auth_path_definition
	(id, ant_path, definition)
VALUES
	(2, '/api2/**', 'authc,roles[org]');
INSERT INTO auth_path_definition
	(id, ant_path, definition)
VALUES
	(3, '/api3/**', 'authc,roles[admin,org]');
INSERT INTO auth_path_definition
	(id, ant_path, definition)
VALUES
	(4, '/api4/**', 'authc,roles[org],perms["write,read"]');
INSERT INTO auth_path_definition
	(id, ant_path, definition)
VALUES
	(5, '/api5/**', 'authc,roles[org],perms["read"]');
