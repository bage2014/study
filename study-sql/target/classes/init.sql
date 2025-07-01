
-- index
alter table sl_test add key idx_first_name(first_name);
show indexes from sl_test;

-- init data
insert sl_test(id,sex,birth,age,first_name,last_name) values (1,'男','2022-01-01 00:00:00',1,'bage','lu');
insert sl_test(id,sex,birth,age,first_name,last_name) values (2,'女','2020-01-01 00:00:00',3,'bage3','lu');

