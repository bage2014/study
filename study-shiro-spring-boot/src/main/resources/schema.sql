--------------- H2 ---------------
drop table if exists auth_path_definition;

create table if not exists auth_path_definition (
  id int PRIMARY KEY,
  ant_path VARCHAR(255),
  definition VARCHAR(255)
);