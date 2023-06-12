drop table IF EXISTS tb_user;

create TABLE tb_user
(
	id BIGINT NOT NULL comment '主键ID',
	name VARCHAR(30) NULL DEFAULT NULL comment '姓名',
	age INT NULL DEFAULT NULL comment '年龄',
	email VARCHAR(50) NULL DEFAULT NULL comment '邮箱',
	PRIMARY KEY (id)
);

-- ALTER TABLE tb_user alter COLUMN age BIGINT NULL DEFAULT NULL;
