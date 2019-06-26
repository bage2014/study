--------------- H2 ---------------

drop table if exists org_user;
CREATE TABLE org_user(
  id SERIAL,
  name VARCHAR(255),
  sex VARCHAR(8),
  department_id BIGINT
);

drop table if exists org_department;
CREATE TABLE org_department(
  id SERIAL,
  name VARCHAR(255)
);