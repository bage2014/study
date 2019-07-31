
-- DB 1
 DROP TABLE IF EXISTS oauth_client_details;
 CREATE TABLE oauth_client_details1 (
   client_id VARCHAR(255) PRIMARY KEY,
   resource_ids VARCHAR(255),
   client_secret VARCHAR(255),
   scope VARCHAR(255),
   authorized_grant_types VARCHAR(255),
   web_server_redirect_uri VARCHAR(255),
   authorities VARCHAR(255),
   access_token_validity INTEGER,
   refresh_token_validity INTEGER,
   additional_information VARCHAR(4096),
   autoapprove VARCHAR(255)
 );

INSERT INTO oauth_client_details1
	(client_id, client_secret, scope, authorized_grant_types,
	web_server_redirect_uri, authorities, access_token_validity,
	refresh_token_validity, additional_information, autoapprove)
VALUES
	('sampleClientId', '$2a$10$i7bPh8Npg8lsUTxOAwp7suAwMxTw8tNyRkvDJT8/uZGcSzdFocHS6', 'user_info',
	'password,authorization_code,refresh_token,client_credentials', 'http://localhost:8001/client1/login,http://localhost:80011/client11/login', NULL, 36000, 36000, NULL, TRUE);
;


-- DB2
  DROP TABLE IF EXISTS oauth_client_details;
 CREATE TABLE oauth_client_details2 (
   client_id VARCHAR(255) PRIMARY KEY,
   resource_ids VARCHAR(255),
   client_secret VARCHAR(255),
   scope VARCHAR(255),
   authorized_grant_types VARCHAR(255),
   web_server_redirect_uri VARCHAR(255),
   authorities VARCHAR(255),
   access_token_validity INTEGER,
   refresh_token_validity INTEGER,
   additional_information VARCHAR(4096),
   autoapprove VARCHAR(255)
 );


INSERT INTO oauth_client_details2
	(client_id, client_secret, scope, authorized_grant_types,
	web_server_redirect_uri, authorities, access_token_validity,
	refresh_token_validity, additional_information, autoapprove)
VALUES
	('sampleClientId', '$2a$10$i7bPh8Npg8lsUTxOAwp7suAwMxTw8tNyRkvDJT8/uZGcSzdFocHS6', 'user_info',
	'password,authorization_code,refresh_token,client_credentials', 'http://localhost:8002/client2/login,http://localhost:80022/client22/login', NULL, 36000, 36000, NULL, TRUE);
;