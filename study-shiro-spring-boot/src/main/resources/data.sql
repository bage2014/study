delete from auth_path_definition;
INSERT INTO auth_path_definition
	(id, ant_path, role_names, filter_names)
VALUES
	(1, '/api1/**', 'admin','authc,roles');
INSERT INTO auth_path_definition
	(id, ant_path, role_names, filter_names)
VALUES
	(2, '/api2/**', 'user','authc,roles');
INSERT INTO auth_path_definition
	(id, ant_path, role_names, filter_names)
VALUES
	(3, '/api3/**', 'admin,user','authc,roles');
