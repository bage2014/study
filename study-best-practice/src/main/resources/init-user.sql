
use mydbpro;

DROP TABLE IF EXISTS tb_user;

CREATE TABLE tb_user
(
	id BIGINT(20) NOT NULL COMMENT '主键ID' AUTO_INCREMENT,
	first_name VARCHAR(32) NULL DEFAULT NULL COMMENT '姓',
	last_name VARCHAR(32) NULL DEFAULT NULL COMMENT '名',
    sex VARCHAR(16) NULL DEFAULT NULL COMMENT '性别，Male 男； Female 女',
	birthday Date NULL DEFAULT NULL COMMENT '年龄',
	address VARCHAR(128) NULL DEFAULT NULL COMMENT '地址',
	email VARCHAR(64) NULL DEFAULT NULL COMMENT '邮箱',
	phone VARCHAR(64) NULL DEFAULT NULL COMMENT '手机号',
	remark VARCHAR(28) NULL DEFAULT NULL COMMENT '备注、描述',
	PRIMARY KEY (id)
);

ALTER TABLE tb_user ADD INDEX idx_phone( phone );


INSERT INTO tb_user (first_name,last_name, sex, birthday, email,phone,remark) VALUES
( 'Jone','si' ,'Male', '1997-02-02', 'test1@baomidou.com','17501500001','hhhhh'),
( 'Jack','hs' ,'Female', '2010-11-02', 'teghjggst1@baomidou.com','17501500003','hhhh'),
( 'Tom','jck' ,'Male', '1992-02-12', 'tesrt1@baomidou.com','17501500022','hh'),
( 'Sandy','hk' ,'Female', '1992-08-02', 'test3@baomidou.com','17501500045','hh'),
( 'Billie','ggd' ,'Male', '1946-02-24', 'te1@baomidou.com','17501500055','jjjjjhhh');



