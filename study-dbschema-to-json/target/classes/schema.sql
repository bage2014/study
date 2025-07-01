
create database db_example; -- Create the new database

use db_example;

create user 'springuser'@'%' identified by 'ThePassword'; -- Creates the org

grant all on db_example.* to 'springuser'@'%'; -- Gives all the privileges to the new org on the newly created database




use db_example;
drop table if exists oauth_client_details;
create table oauth_client_details (
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

