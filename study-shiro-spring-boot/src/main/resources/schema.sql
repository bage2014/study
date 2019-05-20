--------------- H2 ---------------
create table if not exists auth_path_definition (
  id int PRIMARY KEY,
  ant_path VARCHAR(255),
  role_names VARCHAR(255),
  filter_names VARCHAR(255)
);