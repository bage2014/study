INSERT INTO oauth_client_details
	(client_id, client_secret, scope, authorized_grant_types,
	web_server_redirect_uri, authorities, access_token_validity,
	refresh_token_validity, additional_information, autoapprove)
VALUES
	('sampleClientId', '$2a$10$i7bPh8Npg8lsUTxOAwp7suAwMxTw8tNyRkvDJT8/uZGcSzdFocHS6', 'user_info',
	'password,authorization_code,refresh_token,client_credentials', 'http://localhost:8001/client1/login,http://localhost:8002/client2/login', null, 36000, 36000, null, true);
INSERT INTO oauth_client_details
	(client_id, client_secret, scope, authorized_grant_types,
	web_server_redirect_uri, authorities, access_token_validity,
	refresh_token_validity, additional_information, autoapprove)
VALUES
	('fooClientIdPassword', '$2a$10$i7bPh8Npg8lsUTxOAwp7suAwMxTw8tNyRkvDJT8/uZGcSzdFocHS6', 'read,write,foo,bar',
	'implicit', null, null, 36000, 36000, null, false);
INSERT INTO oauth_client_details
	(client_id, client_secret, scope, authorized_grant_types,
	web_server_redirect_uri, authorities, access_token_validity,
	refresh_token_validity, additional_information, autoapprove)
VALUES
	('barClientIdPassword', '$2a$10$i7bPh8Npg8lsUTxOAwp7suAwMxTw8tNyRkvDJT8/uZGcSzdFocHS6', 'bar,read,write',
	'password,authorization_code,refresh_token', null, null, 36000, 36000, null, true);
