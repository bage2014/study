-- 创建数据库
CREATE DATABASE IF NOT EXISTS family_tree CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

-- 创建用户
CREATE USER IF NOT EXISTS 'familytree'@'localhost' IDENTIFIED BY 'familytree123';
CREATE USER IF NOT EXISTS 'familytree'@'%' IDENTIFIED BY 'familytree123';

-- 授予权限
GRANT ALL PRIVILEGES ON family_tree.* TO 'familytree'@'localhost';
GRANT ALL PRIVILEGES ON family_tree.* TO 'familytree'@'%';

-- 刷新权限
FLUSH PRIVILEGES;

USE family_tree;

-- 如果需要手动创建表结构，可以在这里添加DDL语句
-- 注意：应用使用JPA自动创建表，此脚本主要用于初始化数据库和用户
